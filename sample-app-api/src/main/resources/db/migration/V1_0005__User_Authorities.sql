insert into user (username, password, name, email) 
			values ('Company', '$2a$10$uq5uv8n/UXq7BjdYMTJuvuqOIC6ABD6vO0fYCGArtlKXlEUV1m2va',
				 	'Company Auth', 'company.auth@email.com');

insert into user_role(user_id, role_id)
select user.id as user_id, role.id as role_id
from user, role
where user.username = 'Company' and role.role_name = 'COMPANY';

insert into user (username, password, name, email) 
			values ('CompanyEdit', '$2a$10$uq5uv8n/UXq7BjdYMTJuvuqOIC6ABD6vO0fYCGArtlKXlEUV1m2va',
					'Company Edit Auth', 'company.edit.auth@email.com');

insert into user_role(user_id, role_id)
select user.id as user_id, role.id as role_id
from user, role
where user.username = 'CompanyEdit' and role.role_name = 'COMPANY.EDIT';

insert into user (username, password, name, email)
			values ('CompanyCreate', '$2a$10$uq5uv8n/UXq7BjdYMTJuvuqOIC6ABD6vO0fYCGArtlKXlEUV1m2va',
					'Company Create Auth', 'company.create.auth@email.com');

insert into user_role(user_id, role_id)
select user.id as user_id, role.id as role_id
from user, role
where user.username = 'CompanyCreate' and role.role_name = 'COMPANY.CREATE';

insert into user (username, password, name, email) 
			values ('User', '$2a$10$uq5uv8n/UXq7BjdYMTJuvuqOIC6ABD6vO0fYCGArtlKXlEUV1m2va',
					'User Auth', 'user.auth@email.com');

insert into user_role(user_id, role_id)
select user.id as user_id, role.id as role_id
from user, role
where user.username = 'User' and role.role_name = 'USER';

insert into user (username, password, name, email)
			values ('UserEdit', '$2a$10$uq5uv8n/UXq7BjdYMTJuvuqOIC6ABD6vO0fYCGArtlKXlEUV1m2va',
					'User Edit Auth', 'user.edit.auth@email.com');

insert into user_role(user_id, role_id)
select user.id as user_id, role.id as role_id
from user, role
where user.username = 'UserEdit' and role.role_name = 'USER.EDIT';

insert into user (username, password, name, email)
			values ('UserCreate', '$2a$10$uq5uv8n/UXq7BjdYMTJuvuqOIC6ABD6vO0fYCGArtlKXlEUV1m2va',
					'User Create Auth', 'user.create.auth@email.com');

insert into user_role(user_id, role_id)
select user.id as user_id, role.id as role_id
from user, role
where user.username = 'UserCreate' and role.role_name = 'USER.CREATE';

