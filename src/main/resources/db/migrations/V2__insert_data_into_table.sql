insert into cities(name_city) VALUES ('Таганрог');

insert into roles(name_role) VALUES ('USER');
insert into roles(name_role) VALUES ('ADMIN');

insert into users(name, username, points, created_at, city_id)
VALUES ('Денис','denis222',0,NOW(),1);

insert into credentials( email, user_id, password_hash)
VALUES ('example@gmail.com',1,'$2a$10$Xl7ZGn0F9pdiwROSQLk4zuvhI71AMPobsgw5pKzQril6IWiidOIAa')
--email = example@gmail.com password = pass111