-- SYSADMIN
INSERT INTO USERS (USERNAME, PASSWORD, EMAIL, ROLE)
VALUES ('SYSADMIN', '$2a$10$ZmC7ffSP/L3PLUjJJ1UMNemmhOLKPZHVJKqO7k0XmItSGjFliqVo2', 'admin@admin.com', 'ROLE_ADMIN');

-- Opera nationala + EVENIMENTE
INSERT INTO marker (latitude, longitude, popup)
VALUES (44.435726, 26.07958, 'Opera Nationala');

INSERT INTO event (title, description, marker_id, posted_user_id)
VALUES ('Aida - 10 Iunie', 'Libretto de Antonio Ghislanzoni Breaks da
Language Italiană Subtitles DA Premiere 12 septembrie 1996 World premiere 24 decembrie 1871, Teatrul de Operă din Cairo', 1, 1);

INSERT INTO event (title, description, marker_id, posted_user_id)
VALUES ('Nabucco - 11 Iunie', 'Giuseppe Verdi Libretto Temistocle Solera Duration 3h Breaks 2 Language italian Subtitles Romanian Premiere 31st of October 1987 World premiere 9th March 1842, Teatro alla Scala Milano', 1, 1);