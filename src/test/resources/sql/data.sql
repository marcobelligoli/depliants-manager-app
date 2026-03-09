INSERT INTO public.dma_user VALUES (1, 'test1', '$2a$10$H4Y6KcFEjY5ZfiZz1ZaqQuXowi4nkLAt4OizZHFXYnhoCqKm73odi', 'test1@localhost') ON CONFLICT (id) DO NOTHING;
INSERT INTO public.dma_user VALUES (2, 'test2', '$2a$10$Uxy9uzcsGZcbgZrRynhYs.mQyINZ2c/jXegyuH2STDvK6JG6izvKe', 'test2@localhost') ON CONFLICT (id) DO NOTHING;
SELECT setval('dma_user_id_seq', 2, true);

INSERT INTO public.depliant VALUES (2, 2, 'descr', 'fiera', '2', '', '2026-01-05 20:11:42.114519', '2026-03-05 18:50:57.634135', 1) ON CONFLICT (id) DO NOTHING;
INSERT INTO public.depliant VALUES (4, 1, 'Cronaca movimento terra Fiat FR12', 'test', '', '', '2026-01-27 16:00:33.397301', NULL, 2) ON CONFLICT (id) DO NOTHING;
INSERT INTO public.depliant VALUES (5, 2, 'Cronaca movimento terra Fiat FL12', '', '', '', '2026-01-27 16:01:38.346419', NULL, 2) ON CONFLICT (id) DO NOTHING;
INSERT INTO public.depliant VALUES (6, 3, 'Cronaca movimento terra Fiat FL4', '', '', '', '2026-01-27 16:02:19.498551', NULL, 2) ON CONFLICT (id) DO NOTHING;
INSERT INTO public.depliant VALUES (7, 4, 'Da cancellare', '', '', '', '2026-01-27 16:02:19.498551', NULL, 1) ON CONFLICT (id) DO NOTHING;
SELECT setval('depliant_id_seq', 7, true);