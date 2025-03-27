ALTER TABLE cities ADD COLUMN latitude numeric(9,6) NULL;
ALTER TABLE cities ADD COLUMN longitude numeric(9,6) NULL;
ALTER TABLE tasks_completion ADD COLUMN description character varying(100) NOT NULL default '';
ALTER TABLE rewards ADD COLUMN promo_code character varying(255) NOT NULL default '';
ALTER TABLE rewards ADD COLUMN amount int NULL;

UPDATE cities
SET latitude = 47.2362, longitude = 38.8969
WHERE name_city = 'Таганрог';