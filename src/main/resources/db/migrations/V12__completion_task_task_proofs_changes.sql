ALTER TABLE public.tasks_completion DROP COLUMN proof_url;

ALTER TABLE public.tasks_completion ALTER COLUMN status SET DEFAULT 'PENDING'::character varying;
ALTER TABLE public.tasks_completion ALTER COLUMN verified_by DROP NOT NULL;
ALTER TABLE public.tasks_completion ADD COLUMN verified_at timestamp NULL;

ALTER TABLE public.tasks_completion
    DROP CONSTRAINT tasks_completion_status;

ALTER TABLE public.tasks_completion
    ADD CONSTRAINT tasks_completion_status CHECK (((status)::text = ANY (ARRAY[('PENDING'::character varying)::text, ('CONFIRMED'::character varying)::text, ('REJECTED'::character varying)::text]))) NOT VALID;


ALTER TABLE task_proofs
    ALTER COLUMN image_url TYPE character varying(255);

