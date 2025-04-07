ALTER TABLE public.tasks
    DROP COLUMN start_date;

ALTER TABLE public.tasks
    RENAME end_date TO expired_date;