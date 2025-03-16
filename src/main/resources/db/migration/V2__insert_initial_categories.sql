INSERT INTO categories (name, description)
SELECT
    'Web Development', 'Jobs related to building websites and web applications'
    WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Web Development')
UNION ALL
SELECT
    'Graphic Design', 'Jobs related to designing graphics, logos, and visual content'
    WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Graphic Design')
UNION ALL
SELECT
    'Content Writing', 'Jobs related to writing articles, blogs, and other content'
    WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Content Writing')
UNION ALL
SELECT
    'Digital Marketing', 'Jobs related to SEO, social media, and online advertising'
    WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Digital Marketing')
UNION ALL
SELECT
    'Mobile App Development', 'Jobs related to developing Android and iOS applications'
    WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Mobile App Development')
UNION ALL
SELECT
    'Data Science & AI', 'Jobs related to data analysis, machine learning, and AI development'
    WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Data Science & AI')
UNION ALL
SELECT
    'Cybersecurity', 'Jobs related to securing systems and protecting data from threats'
    WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Cybersecurity');
