DELETE FROM contacts;
ALTER TABLE contacts DROP COLUMN id;
CREATE EXTENSION "uuid-ossp" IF NOT EXISTS;
ALTER TABLE contacts ADD COLUMN id uuid DEFAULT uuid_generate_v4() PRIMARY KEY;
ALTER TABLE contacts ALTER COLUMN image DROP NOT NULL;
ALTER TABLE contacts ALTER COLUMN description DROP NOT NULL;