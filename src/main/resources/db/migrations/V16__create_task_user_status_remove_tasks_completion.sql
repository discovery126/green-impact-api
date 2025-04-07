ALTER TABLE public.taken_tasks RENAME TO tasks_users_status;

ALTER TABLE public.tasks_users_status
    ADD COLUMN completed_at timestamp NULL;

ALTER TABLE public.tasks_users_status
    ALTER COLUMN taken_at TYPE timestamp;

ALTER TABLE public.tasks_users_status
    ADD COLUMN description character varying(128) NULL;

ALTER TABLE public.tasks_users_status
    ADD COLUMN status character varying(30) NULL;

ALTER TABLE public.tasks_users_status
    ADD CONSTRAINT tasks_users_status CHECK (((status)::text = ANY (ARRAY[('PENDING'::character varying)::text, ('CONFIRMED'::character varying)::text, ('REJECTED'::character varying)::text]))) NOT VALID;

ALTER TABLE public.tasks_users_status
    ADD COLUMN verified_at timestamp NULL;

ALTER TABLE public.tasks_users_status
    ADD COLUMN verified_by bigint NULL;

ALTER TABLE public.tasks_users_status
    ADD CONSTRAINT verified_by_foreign FOREIGN KEY (verified_by) REFERENCES public.users (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE;

ALTER TABLE task_proofs
    DROP COLUMN task_completion_id;

DROP TABLE tasks_completion;

ALTER TABLE task_proofs
    ADD COLUMN task_completion_id bigint;

ALTER TABLE public.task_proofs
    ADD CONSTRAINT task_completion_id_foreign FOREIGN KEY (task_completion_id) REFERENCES public.tasks_users_status (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE;
