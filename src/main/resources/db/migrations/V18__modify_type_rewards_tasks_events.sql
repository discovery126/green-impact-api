ALTER TABLE rewards ALTER COLUMN description TYPE character varying(255);
ALTER TABLE rewards ALTER COLUMN title TYPE character varying(100);
ALTER TABLE tasks ALTER COLUMN title TYPE character varying(100);
ALTER TABLE events
    RENAME COLUMN name to title;