ALTER TABLE public.events DROP COLUMN organiser_id;

ALTER TABLE public.events ADD COLUMN description character varying(255) NOT NULL DEFAULT '';
ALTER TABLE public.events ADD COLUMN organiser_name character varying(100) NOT NULL DEFAULT '';
ALTER TABLE public.events ADD COLUMN organiser_phone character varying(15) NOT NULL DEFAULT '';
ALTER TABLE public.events ADD COLUMN event_points int NOT NULL DEFAULT 10;
ALTER TABLE public.events ADD COLUMN event_start_at timestamp NOT NULL DEFAULT NOW() ;
ALTER TABLE public.events RENAME event_date_at TO event_end_at;

ALTER TABLE public.events
    DROP CONSTRAINT events_status_check;

ALTER TABLE public.events
    ADD CONSTRAINT events_status_check CHECK (((status)::text = ANY ((ARRAY['scheduled'::character varying,'active'::character varying, 'completed'::character varying, 'canceled'::character varying])::text[]))) NOT VALID;


ALTER TABLE public.events
    ALTER COLUMN street TYPE character varying(100);

ALTER TABLE public.events
    ALTER COLUMN house_number DROP NOT NULL;