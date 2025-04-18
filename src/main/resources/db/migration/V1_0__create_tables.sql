CREATE TABLE IF NOT EXISTS users
(
    id                  BIGSERIAL PRIMARY KEY,
    full_name           VARCHAR(255) NOT NULL,
    email               VARCHAR(255) NOT NULL UNIQUE,
    password            VARCHAR(255) NOT NULL,
    role                VARCHAR(20)  NOT NULL CHECK (role IN ('FREELANCER', 'EMPLOYER')),
    cv_url              VARCHAR(255),
    profile_picture_url VARCHAR(255),
    created_at          TIMESTAMP    NOT NULL,
    rating              FLOAT CHECK (rating >= 0 AND rating <= 5)
);

CREATE TABLE IF NOT EXISTS categories
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT
);

CREATE TABLE IF NOT EXISTS services
(
    id                 BIGSERIAL PRIMARY KEY,
    freelancer_id      BIGINT         NOT NULL,
    category_id        BIGINT         NOT NULL,
    title              VARCHAR(255)   NOT NULL,
    description        TEXT,
    price              DECIMAL(10, 2) NOT NULL,
    deposit_required   BOOLEAN DEFAULT TRUE,
    deposit_percentage INT     DEFAULT 50,
    created_at         TIMESTAMP      NOT NULL,
    FOREIGN KEY (freelancer_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS jobs
(
    id              BIGSERIAL PRIMARY KEY,
    employer_id     BIGINT         NOT NULL,
    category_id     BIGINT         NOT NULL,
    job_title       VARCHAR(255)   NOT NULL,
    job_description TEXT,
    budget          DECIMAL(10, 2) NOT NULL,
    created_at      TIMESTAMP      NOT NULL,
    deadline        DATE,
    FOREIGN KEY (employer_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS job_bids
(
    id                   BIGSERIAL PRIMARY KEY,
    job_post_id          BIGINT         NOT NULL,
    freelancer_id        BIGINT         NOT NULL,
    bid_amount           DECIMAL(10, 2) NOT NULL,
    message              TEXT,
    created_at           TIMESTAMP      NOT NULL,
    status               VARCHAR(20)    NOT NULL CHECK (status IN ('pending', 'accepted', 'rejected')),
    proposed_start_date  TIMESTAMP,
    proposed_end_date    TIMESTAMP,
    support_availability VARCHAR(255),
    additional_policy    TEXT,
    deposit_amount       DECIMAL(10, 2),
    final_payment_amount DECIMAL(10, 2),
    FOREIGN KEY (job_post_id) REFERENCES jobs (id) ON DELETE CASCADE,
    FOREIGN KEY (freelancer_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS contracts
(
    id                  BIGSERIAL PRIMARY KEY,
    employer_id         BIGINT      NOT NULL,
    freelancer_id       BIGINT      NOT NULL,
    contract_type       VARCHAR(50) NOT NULL, -- "direct" or "bid"
    service_id          BIGINT,
    job_bid_id          BIGINT,
    contract_start_date TIMESTAMP   NOT NULL,
    contract_end_date   TIMESTAMP,
    additional_policy   TEXT,
    created_at          TIMESTAMP   NOT NULL,
    employer_accepted   BOOLEAN,
    freelancer_accepted BOOLEAN,
    status              VARCHAR(20) NOT NULL, -- e.g., "draft", "negotiation", "active", "completed", "cancelled"
    FOREIGN KEY (employer_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (freelancer_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES services (id) ON DELETE SET NULL,
    FOREIGN KEY (job_bid_id) REFERENCES job_bids (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS milestones
(
    id                  BIGSERIAL PRIMARY KEY,
    job_id              BIGINT         NOT NULL,
    title               VARCHAR(255)   NOT NULL,
    description         TEXT,
    start_date          DATE,
    due_date            DATE,
    deliverable_url     VARCHAR(255),
    submission_type     VARCHAR(20) CHECK (submission_type IN ('FILE', 'LINK', 'PREVIEW')),
    review_status       VARCHAR(20) CHECK (review_status IN ('PENDING', 'APPROVED', 'REJECTED', 'IN_PROGRESS')),
    feedback            TEXT,
    hidden              BOOLEAN DEFAULT FALSE,
    fulfillment_comment TEXT,
    checklist           TEXT,
    amount              DECIMAL(10, 2) NOT NULL,
    effort              INT,
    FOREIGN KEY (job_id) REFERENCES jobs (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS wallets
(
    id      BIGSERIAL PRIMARY KEY,
    balance DECIMAL(10, 2) DEFAULT 0,
    user_id BIGINT NOT NULL UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS transactions
(
    id        BIGSERIAL PRIMARY KEY,
    reference VARCHAR(255),
    amount    DECIMAL(10, 2),
    balance   DECIMAL(10, 2),
    type      VARCHAR(20) NOT NULL CHECK (type IN ('DEPOSIT', 'REFUND', 'PAYMENT', 'TRANSFER')),
    wallet_id BIGINT      NOT NULL,
    FOREIGN KEY (wallet_id) REFERENCES wallets (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reviews
(
    id          BIGSERIAL PRIMARY KEY,
    contract_id INTEGER   NOT NULL,
    reviewer_id BIGINT    NOT NULL,
    reviewee_id BIGINT    NOT NULL,
    rating      INTEGER   NOT NULL,
    comment     TEXT,
    created_at  TIMESTAMP NOT NULL,
    FOREIGN KEY (contract_id) REFERENCES contracts (id) ON DELETE CASCADE,
    FOREIGN KEY (reviewer_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (reviewee_id) REFERENCES users (id) ON DELETE CASCADE
);
