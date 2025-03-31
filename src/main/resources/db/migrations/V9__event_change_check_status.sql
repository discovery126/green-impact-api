ALTER TABLE public.events ALTER COLUMN status SET DEFAULT 'SCHEDULED'::character varying;

ALTER TABLE public.events
    DROP CONSTRAINT events_status_check;

ALTER TABLE public.events
    ADD CONSTRAINT events_status_check CHECK (((status)::text = ANY ((ARRAY['SCHEDULED'::character varying,'ACTIVE'::character varying, 'COMPLETED'::character varying, 'CANCELED'::character varying])::text[]))) NOT VALID;
