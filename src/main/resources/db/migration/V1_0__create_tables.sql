CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     full_name VARCHAR(255) NOT NULL,
                                     email VARCHAR(255) NOT NULL UNIQUE,
                                     password VARCHAR(255) NOT NULL,
                                     role VARCHAR(20) NOT NULL CHECK (role IN ('FREELANCER', 'EMPLOYER')),
                                     cv_url VARCHAR(255),
                                     created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS categories (
                                          id BIGSERIAL PRIMARY KEY,
                                          name VARCHAR(255) NOT NULL,
                                          description TEXT
);

CREATE TABLE IF NOT EXISTS services (
                                        id BIGSERIAL PRIMARY KEY,
                                        freelancer_id BIGINT NOT NULL,
                                        category_id BIGINT NOT NULL,
                                        title VARCHAR(255) NOT NULL,
                                        description TEXT,
                                        price DECIMAL(10,2) NOT NULL,
                                        deposit_required BOOLEAN DEFAULT TRUE,
                                        deposit_percentage INT DEFAULT 50,
                                        created_at TIMESTAMP NOT NULL,
                                        FOREIGN KEY (freelancer_id) REFERENCES users(id) ON DELETE CASCADE,
                                        FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS jobs (
                                    id BIGSERIAL PRIMARY KEY,
                                    employer_id BIGINT NOT NULL,
                                    job_title VARCHAR(255) NOT NULL,
                                    job_description TEXT,
                                    budget DECIMAL(10,2) NOT NULL,
                                    created_at TIMESTAMP NOT NULL,
                                    deadline DATE,
                                    FOREIGN KEY (employer_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS job_bids (
                                        id SERIAL PRIMARY KEY,
                                        job_post_id BIGINT NOT NULL,
                                        freelancer_id BIGINT NOT NULL,
                                        bid_amount DECIMAL(10,2) NOT NULL,
                                        commitment_days INT NOT NULL,
                                        message TEXT,
                                        created_at TIMESTAMP NOT NULL,
                                        status VARCHAR(20) NOT NULL CHECK (status IN ('pending', 'accepted', 'rejected')),
                                        FOREIGN KEY (job_post_id) REFERENCES jobs(id) ON DELETE CASCADE,
                                        FOREIGN KEY (freelancer_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS contracts (
                                         id SERIAL PRIMARY KEY,
                                         employer_id BIGINT NOT NULL,
                                         freelancer_id BIGINT NOT NULL,
                                         contract_type VARCHAR(50) NOT NULL, -- "direct" or "bid"
                                         service_id BIGINT,
                                         job_bid_id INT,
                                         contract_start_date TIMESTAMP NOT NULL,
                                         contract_end_date TIMESTAMP,
                                         commitment_period INT,
                                         support_availability VARCHAR(255),
                                         additional_policy TEXT,
                                         deposit_amount DECIMAL(10,2) NOT NULL,
                                         final_payment_amount DECIMAL(10,2) NOT NULL,
                                         deposit_status VARCHAR(20) NOT NULL CHECK (deposit_status IN ('pending', 'paid')),
                                         final_payment_status VARCHAR(20) NOT NULL CHECK (final_payment_status IN ('pending', 'paid')),
                                         created_at TIMESTAMP NOT NULL,
                                         FOREIGN KEY (employer_id) REFERENCES users(id) ON DELETE CASCADE,
                                         FOREIGN KEY (freelancer_id) REFERENCES users(id) ON DELETE CASCADE,
                                         FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE SET NULL,
                                         FOREIGN KEY (job_bid_id) REFERENCES job_bids(id) ON DELETE SET NULL
);
