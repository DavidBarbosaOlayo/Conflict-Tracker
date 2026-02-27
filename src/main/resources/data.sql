INSERT INTO countries (id, name, code) VALUES
(1, 'Ukraine', 'UKR'),
(2, 'Russia', 'RUS'),
(3, 'Syria', 'SYR'),
(4, 'United States', 'USA'),
(5, 'France', 'FRA');

INSERT INTO conflicts (id, name, start_date, status, description) VALUES
(1, 'Russo-Ukrainian War', DATE '2014-02-20', 'ACTIVE', 'Conflict between Russia and Ukraine with major escalation since 2022.'),
(2, 'Syrian Civil War', DATE '2011-03-15', 'FROZEN', 'Multi-sided conflict involving internal and external actors.'),
(3, 'Gulf War', DATE '1990-08-02', 'ENDED', 'International conflict triggered by the Iraqi invasion of Kuwait.');

INSERT INTO conflict_countries (conflict_id, country_id) VALUES
(1, 1),
(1, 2),
(2, 3),
(2, 4),
(3, 4),
(3, 5);

INSERT INTO factions (id, name, conflict_id) VALUES
(1, 'Ukrainian Armed Forces', 1),
(2, 'Russian Armed Forces', 1),
(3, 'Syrian Government Forces', 2);

INSERT INTO faction_countries (faction_id, country_id) VALUES
(1, 1),
(1, 4),
(2, 2),
(3, 3),
(3, 2);

INSERT INTO events (id, event_date, location, description, conflict_id) VALUES
(1, DATE '2022-02-24', 'Kyiv', 'Large-scale invasion begins.', 1),
(2, DATE '2016-12-22', 'Aleppo', 'Government forces retake most of eastern Aleppo.', 2),
(3, DATE '1991-02-27', 'Kuwait City', 'Coalition forces liberate Kuwait City.', 3);

ALTER TABLE countries ALTER COLUMN id RESTART WITH 100;
ALTER TABLE conflicts ALTER COLUMN id RESTART WITH 100;
ALTER TABLE factions ALTER COLUMN id RESTART WITH 100;
ALTER TABLE events ALTER COLUMN id RESTART WITH 100;
