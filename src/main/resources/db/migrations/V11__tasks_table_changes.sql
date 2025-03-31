ALTER TABLE public.tasks DROP COLUMN amount;
ALTER TABLE public.tasks DROP COLUMN unit;
ALTER TABLE public.tasks ALTER COLUMN start_date DROP NOT NULL;
ALTER TABLE public.tasks ALTER COLUMN end_date DROP NOT NULL;
ALTER TABLE public.tasks ALTER COLUMN type SET DEFAULT 'LIMITED'::character varying;

ALTER TABLE public.tasks
    DROP CONSTRAINT tasks_type_check;

ALTER TABLE public.tasks
    ADD CONSTRAINT tasks_type_check CHECK (((type)::text = ANY ((ARRAY['LIMITED'::character varying, 'DAILY'::character varying])::text[]))) NOT VALID;
