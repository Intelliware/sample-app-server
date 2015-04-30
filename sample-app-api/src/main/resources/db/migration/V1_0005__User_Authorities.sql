insert into user (username, password) values ('Company', '$2a$10$uq5uv8n/UXq7BjdYMTJuvuqOIC6ABD6vO0fYCGArtlKXlEUV1m2va');

insert into user_role(user_id, role_id)
select user.id as user_id, role.id as role_id
from user, role
where user.username = 'Company' and role.role_name = 'COMPANY';

insert into user (username, password) values ('CompanyEdit', '$2a$10$uq5uv8n/UXq7BjdYMTJuvuqOIC6ABD6vO0fYCGArtlKXlEUV1m2va');

insert into user_role(user_id, role_id)
select user.id as user_id, role.id as role_id
from user, role
where user.username = 'CompanyEdit' and role.role_name = 'COMPANY.EDIT';

insert into user (username, password) values ('CompanyCreate', '$2a$10$uq5uv8n/UXq7BjdYMTJuvuqOIC6ABD6vO0fYCGArtlKXlEUV1m2va');

insert into user_role(user_id, role_id)
select user.id as user_id, role.id as role_id
from user, role
where user.username = 'CompanyCreate' and role.role_name = 'COMPANY.CREATE';

insert into user (username, password) values ('User', '$2a$10$uq5uv8n/UXq7BjdYMTJuvuqOIC6ABD6vO0fYCGArtlKXlEUV1m2va');

insert into user_role(user_id, role_id)
select user.id as user_id, role.id as role_id
from user, role
where user.username = 'User' and role.role_name = 'USER';

insert into user (username, password) values ('UserEdit', '$2a$10$uq5uv8n/UXq7BjdYMTJuvuqOIC6ABD6vO0fYCGArtlKXlEUV1m2va');

insert into user_role(user_id, role_id)
select user.id as user_id, role.id as role_id
from user, role
where user.username = 'UserEdit' and role.role_name = 'USER.EDIT';

insert into user (username, password) values ('UserCreate', '$2a$10$uq5uv8n/UXq7BjdYMTJuvuqOIC6ABD6vO0fYCGArtlKXlEUV1m2va');

insert into user_role(user_id, role_id)
select user.id as user_id, role.id as role_id
from user, role
where user.username = 'UserCreate' and role.role_name = 'USER.CREATE';

