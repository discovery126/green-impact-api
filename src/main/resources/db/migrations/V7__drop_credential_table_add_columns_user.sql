DROP TABLE credentials;

ALTER TABLE public.users ADD COLUMN email character varying(100) NOT NULL DEFAULT '';
ALTER TABLE public.users ADD COLUMN password_hash character varying(255) NOT NULL DEFAULT '';
ALTER TABLE public.users RENAME COLUMN username TO display_name;
ALTER TABLE public.users DROP COLUMN name;

UPDATE users
    SET email='example@gmail.com',
        password_hash='$2a$10$Xl7ZGn0F9pdiwROSQLk4zuvhI71AMPobsgw5pKzQril6IWiidOIAa'
    WHERE users.id=1