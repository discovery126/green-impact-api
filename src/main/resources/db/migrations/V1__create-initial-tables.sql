CREATE TABLE public.cities (
                               id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
                               name_city character varying(50) DEFAULT 'Таганрог'::character varying NOT NULL
);

CREATE TABLE public.credentials (
                                    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
                                    user_id bigint NOT NULL,
                                    email character varying(100) NOT NULL,
                                    password_hash character varying(255) NOT NULL
);

CREATE TABLE public.events (
                               id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
                               name character varying(100) NOT NULL,
                               event_date_at timestamp(0) without time zone NOT NULL,
                               organiser_id bigint NOT NULL,
                               city_id integer NOT NULL,
                               street integer NOT NULL,
                               house_number integer NOT NULL,
                               status character varying(20) DEFAULT 'scheduled'::character varying NOT NULL,
                               event_code character varying(50) NOT NULL,
                               latitude numeric(9,6) NOT NULL,
                               longitude numeric(9,6) NOT NULL,
                               created_at timestamp(0) without time zone NOT NULL
);

CREATE TABLE public.reward_categories (
                                          id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
                                          name_category character varying(50) NOT NULL
);

CREATE TABLE public.rewards (
                                id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
                                title character varying(50) NOT NULL,
                                description character varying(100) NOT NULL,
                                cost_points integer NOT NULL,
                                category_id integer NOT NULL
);

CREATE TABLE public.roles (
                              id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
                              name_role character varying(20) NOT NULL
);

CREATE TABLE public.roles_users (
                                    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
                                    user_id bigint NOT NULL,
                                    role_id integer NOT NULL
);

CREATE TABLE public.taken_tasks (
                                    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
                                    user_id bigint NOT NULL,
                                    task_id bigint NOT NULL,
                                    taken_at timestamp(0) without time zone NOT NULL
);


CREATE TABLE public.task_categories (
                                        id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
                                        name_category character varying(50) NOT NULL
);

CREATE TABLE public.task_proofs (
                                    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
                                    task_completion_id bigint NOT NULL,
                                    image_url text NOT NULL
);

CREATE TABLE public.tasks (
                              id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
                              title character varying(50) NOT NULL,
                              description character varying(100) NOT NULL,
                              type character varying(20) DEFAULT 'limited'::character varying NOT NULL,
                              unit character varying(20) NOT NULL,
                              amount integer DEFAULT 1 NOT NULL,
                              category_id integer NOT NULL,
                              start_date timestamp(0) without time zone NOT NULL,
                              end_date timestamp(0) without time zone NOT NULL
);

CREATE TABLE public.tasks_completion (
                                         id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
                                         taken_task_id bigint NOT NULL,
                                         completed_at timestamp(0) without time zone NOT NULL,
                                         proof_url character varying(255) NOT NULL,
                                         status character varying(30) NOT NULL,
                                         points integer NOT NULL,
                                         verified_by bigint NOT NULL
);

CREATE TABLE public.users (
                              id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
                              name character varying(50),
                              username character varying(100),
                              points integer,
                              created_at timestamp(0) without time zone NOT NULL,
                              city_id integer
);

CREATE TABLE public.users_events (
                                     id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
                                     user_id bigint NOT NULL,
                                     event_id bigint NOT NULL,
                                     registered_at timestamp(0) without time zone NOT NULL,
                                     confirmed_at timestamp(0) without time zone NOT NULL
);

CREATE TABLE public.users_rewards (
                                      id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
                                      user_id bigint NOT NULL,
                                      reward_id bigint NOT NULL
);

ALTER TABLE ONLY public.cities
    ADD CONSTRAINT cities_name_city_unique UNIQUE (name_city);

ALTER TABLE ONLY public.cities
    ADD CONSTRAINT cities_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.credentials
    ADD CONSTRAINT credentials_email_unique UNIQUE (email);

ALTER TABLE ONLY public.credentials
    ADD CONSTRAINT credentials_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.events
    ADD CONSTRAINT events_pkey PRIMARY KEY (id);

ALTER TABLE public.events
    ADD CONSTRAINT events_status_check CHECK (((status)::text = ANY ((ARRAY['scheduled'::character varying, 'completed'::character varying, 'canceled'::character varying])::text[]))) NOT VALID;

ALTER TABLE ONLY public.reward_categories
    ADD CONSTRAINT reward_categories_name_category_unique UNIQUE (name_category);

ALTER TABLE ONLY public.reward_categories
    ADD CONSTRAINT reward_categories_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.rewards
    ADD CONSTRAINT rewards_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_name_role_unique UNIQUE (name_role);

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.taken_tasks
    ADD CONSTRAINT taken_tasks_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.task_categories
    ADD CONSTRAINT task_categories_name_category_unique UNIQUE (name_category);

ALTER TABLE ONLY public.task_categories
    ADD CONSTRAINT task_categories_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.task_proofs
    ADD CONSTRAINT task_proofs_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.tasks_completion
    ADD CONSTRAINT tasks_completion_pkey PRIMARY KEY (id);

ALTER TABLE public.tasks_completion
    ADD CONSTRAINT tasks_completion_status CHECK (((status)::text = ANY ((ARRAY['pending'::character varying, 'confirmed'::character varying, 'rejected'::character varying])::text[]))) NOT VALID;

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT tasks_pkey PRIMARY KEY (id);

ALTER TABLE public.tasks
    ADD CONSTRAINT tasks_type_check CHECK (((type)::text = ANY ((ARRAY['limited'::character varying, 'daily'::character varying])::text[]))) NOT VALID;

ALTER TABLE public.tasks
    ADD CONSTRAINT tasks_unit_check CHECK (((unit)::text = ANY ((ARRAY['шт'::character varying, 'кг'::character varying])::text[]))) NOT VALID;

ALTER TABLE ONLY public.users_events
    ADD CONSTRAINT users_events_event_id_user_id_unique UNIQUE (event_id, user_id);

ALTER TABLE ONLY public.users_events
    ADD CONSTRAINT users_events_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_unique UNIQUE (username);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.users_rewards
    ADD CONSTRAINT users_rewards_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.credentials
    ADD CONSTRAINT credentials_user_id_foreign FOREIGN KEY (user_id) REFERENCES public.users(id);

ALTER TABLE ONLY public.events
    ADD CONSTRAINT events_city_id_foreign FOREIGN KEY (city_id) REFERENCES public.cities(id);

ALTER TABLE ONLY public.events
    ADD CONSTRAINT events_organiser_id_foreign FOREIGN KEY (organiser_id) REFERENCES public.users(id);

ALTER TABLE ONLY public.rewards
    ADD CONSTRAINT rewards_category_id_foreign FOREIGN KEY (category_id) REFERENCES public.reward_categories(id);

ALTER TABLE ONLY public.roles_users
    ADD CONSTRAINT roles_users_role_id_foreign FOREIGN KEY (role_id) REFERENCES public.roles(id);

ALTER TABLE ONLY public.roles_users
    ADD CONSTRAINT roles_users_user_id_foreign FOREIGN KEY (user_id) REFERENCES public.users(id);

ALTER TABLE ONLY public.taken_tasks
    ADD CONSTRAINT taken_tasks_task_id_foreign FOREIGN KEY (task_id) REFERENCES public.tasks(id);

ALTER TABLE ONLY public.taken_tasks
    ADD CONSTRAINT taken_tasks_user_id_foreign FOREIGN KEY (user_id) REFERENCES public.users(id);

ALTER TABLE ONLY public.task_proofs
    ADD CONSTRAINT task_proofs_task_completion_id_foreign FOREIGN KEY (task_completion_id) REFERENCES public.tasks_completion(id);

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT tasks_category_id_foreign FOREIGN KEY (category_id) REFERENCES public.task_categories(id);

ALTER TABLE ONLY public.tasks_completion
    ADD CONSTRAINT tasks_completion_taken_task_id_foreign FOREIGN KEY (taken_task_id) REFERENCES public.taken_tasks(id);

ALTER TABLE ONLY public.tasks_completion
    ADD CONSTRAINT tasks_completion_verified_by_foreign FOREIGN KEY (verified_by) REFERENCES public.users(id);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_city_id_foreign FOREIGN KEY (city_id) REFERENCES public.cities(id);

ALTER TABLE ONLY public.users_events
    ADD CONSTRAINT users_events_event_id_foreign FOREIGN KEY (event_id) REFERENCES public.events(id);

ALTER TABLE ONLY public.users_events
    ADD CONSTRAINT users_events_user_id_foreign FOREIGN KEY (user_id) REFERENCES public.users(id);

ALTER TABLE ONLY public.users_rewards
    ADD CONSTRAINT users_rewards_reward_id_foreign FOREIGN KEY (reward_id) REFERENCES public.rewards(id);

ALTER TABLE ONLY public.users_rewards
    ADD CONSTRAINT users_rewards_user_id_foreign FOREIGN KEY (user_id) REFERENCES public.users(id);