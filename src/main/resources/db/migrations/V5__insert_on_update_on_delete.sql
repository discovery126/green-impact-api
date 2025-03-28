ALTER TABLE public.credentials
    DROP CONSTRAINT credentials_user_id_foreign;

ALTER TABLE public.credentials
    ADD CONSTRAINT credentials_user_id_foreign FOREIGN KEY (user_id) REFERENCES public.users(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

ALTER TABLE public.events
    DROP CONSTRAINT events_city_id_foreign;

ALTER TABLE public.events
    ADD CONSTRAINT events_city_id_foreign FOREIGN KEY (city_id) REFERENCES public.cities(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

ALTER TABLE public.events
    DROP CONSTRAINT events_organiser_id_foreign;

ALTER TABLE public.events
    ADD CONSTRAINT events_organiser_id_foreign FOREIGN KEY (organiser_id) REFERENCES public.users(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

ALTER TABLE public.rewards
    DROP CONSTRAINT rewards_category_id_foreign;

ALTER TABLE public.rewards
    ADD CONSTRAINT rewards_category_id_foreign FOREIGN KEY (category_id) REFERENCES public.reward_categories(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

ALTER TABLE public.roles_users
    DROP CONSTRAINT roles_users_role_id_foreign;

ALTER TABLE public.roles_users
    ADD CONSTRAINT roles_users_role_id_foreign FOREIGN KEY (role_id) REFERENCES public.roles(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

ALTER TABLE public.roles_users
    DROP CONSTRAINT roles_users_user_id_foreign;

ALTER TABLE public.roles_users
    ADD CONSTRAINT roles_users_user_id_foreign FOREIGN KEY (user_id) REFERENCES public.users(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;


ALTER TABLE public.taken_tasks
    DROP CONSTRAINT taken_tasks_task_id_foreign;

ALTER TABLE public.taken_tasks
    ADD CONSTRAINT taken_tasks_task_id_foreign FOREIGN KEY (task_id) REFERENCES public.tasks(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

ALTER TABLE public.taken_tasks
    DROP CONSTRAINT taken_tasks_user_id_foreign;

ALTER TABLE public.taken_tasks
    ADD CONSTRAINT taken_tasks_user_id_foreign FOREIGN KEY (user_id) REFERENCES public.users(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

ALTER TABLE public.task_proofs
    DROP CONSTRAINT task_proofs_task_completion_id_foreign;

ALTER TABLE public.task_proofs
    ADD CONSTRAINT task_proofs_task_completion_id_foreign FOREIGN KEY (task_completion_id) REFERENCES public.tasks_completion(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

ALTER TABLE public.tasks
    DROP CONSTRAINT tasks_category_id_foreign;

ALTER TABLE public.tasks
    ADD CONSTRAINT tasks_category_id_foreign FOREIGN KEY (category_id) REFERENCES public.task_categories(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

ALTER TABLE public.tasks_completion
    DROP CONSTRAINT tasks_completion_taken_task_id_foreign;

ALTER TABLE public.tasks_completion
    ADD CONSTRAINT tasks_completion_taken_task_id_foreign FOREIGN KEY (taken_task_id) REFERENCES public.taken_tasks(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

ALTER TABLE public.tasks_completion
    DROP CONSTRAINT tasks_completion_verified_by_foreign;

ALTER TABLE public.tasks_completion
    ADD CONSTRAINT tasks_completion_verified_by_foreign FOREIGN KEY (verified_by) REFERENCES public.users(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

ALTER TABLE public.users
    DROP CONSTRAINT users_city_id_foreign;

ALTER TABLE public.users
    ADD CONSTRAINT users_city_id_foreign FOREIGN KEY (city_id) REFERENCES public.cities(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

ALTER TABLE public.users_events
    DROP CONSTRAINT users_events_event_id_foreign;

ALTER TABLE public.users_events
    ADD CONSTRAINT users_events_event_id_foreign FOREIGN KEY (event_id) REFERENCES public.events(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

ALTER TABLE public.users_events
    DROP CONSTRAINT users_events_user_id_foreign;

ALTER TABLE public.users_events
    ADD CONSTRAINT users_events_user_id_foreign FOREIGN KEY (user_id) REFERENCES public.users(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

ALTER TABLE public.users_rewards
    DROP CONSTRAINT users_rewards_reward_id_foreign;

ALTER TABLE public.users_rewards
    ADD CONSTRAINT users_rewards_reward_id_foreign FOREIGN KEY (reward_id) REFERENCES public.rewards(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

ALTER TABLE public.users_rewards
    DROP CONSTRAINT users_rewards_user_id_foreign;

ALTER TABLE public.users_rewards
    ADD CONSTRAINT users_rewards_user_id_foreign FOREIGN KEY (user_id) REFERENCES public.users(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;