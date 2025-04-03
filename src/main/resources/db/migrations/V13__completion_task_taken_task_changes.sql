ALTER TABLE public.taken_tasks
    ALTER COLUMN taken_at TYPE DATE;

ALTER TABLE public.taken_tasks
    ADD CONSTRAINT taken_tasks_daily_unique UNIQUE (user_id, taken_at, task_id);


ALTER TABLE public.tasks_completion
    DROP COLUMN taken_task_id;

ALTER TABLE  public.tasks_completion
    ADD COLUMN user_id bigint NOT NULL DEFAULT 1;

ALTER TABLE public.tasks_completion
    ADD CONSTRAINT tasks_completion_user_id_foreign FOREIGN KEY (user_id) REFERENCES public.users (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE;

ALTER TABLE public.tasks_completion
    ADD COLUMN task_id bigint NOT NULL DEFAULT 1;

ALTER TABLE public.tasks_completion
    ADD CONSTRAINT tasks_completion_task_id_foreign FOREIGN KEY (task_id)
        REFERENCES public.tasks (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE;


ALTER TABLE public.tasks_completion
    DROP CONSTRAINT tasks_completion_verified_by_foreign;

ALTER TABLE public.tasks_completion
    ADD CONSTRAINT tasks_completion_verified_by_foreign FOREIGN KEY (verified_by) REFERENCES public.users(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE;

ALTER TABLE public.tasks_completion
    ALTER COLUMN description DROP NOT NULL;