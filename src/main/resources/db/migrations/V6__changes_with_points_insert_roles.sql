ALTER TABLE tasks ADD COLUMN points int NOT NULL default 0;
ALTER TABLE tasks_completion DROP COLUMN points;

insert into roles_users(user_id, role_id)
VALUES (1,1);

insert into roles_users(user_id, role_id)
VALUES (1,2);