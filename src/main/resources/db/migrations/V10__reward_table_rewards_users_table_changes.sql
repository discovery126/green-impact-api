ALTER TABLE public.rewards DROP COLUMN promo_code;
ALTER TABLE public.rewards ADD COLUMN type character varying(50) NOT NULL DEFAULT 'DIGITAL';
ALTER TABLE public.rewards ADD COLUMN created_at timestamp NOT NULL DEFAULT now();

ALTER TABLE public.rewards
    ADD CONSTRAINT rewards_type_check CHECK (((type)::text = ANY ((ARRAY['DIGITAL'::character varying,'PHYSICAL'::character varying])::text[]))) NOT VALID;

ALTER TABLE public.users_rewards ADD COLUMN promo_code character varying(255) NULL;
ALTER TABLE public.users_rewards ADD COLUMN status character varying(50) NOT NULL DEFAULT 'REDEEMED';
ALTER TABLE public.users_rewards ADD COLUMN issued_at timestamp NOT NULL DEFAULT now();

ALTER TABLE public.users_rewards
    ADD CONSTRAINT users_rewards_status CHECK (((users_rewards.status)::text = ANY ((ARRAY['PENDING'::character varying,'SHIPED'::character varying,'REDEEMED'::character varying])::text[]))) NOT VALID;
