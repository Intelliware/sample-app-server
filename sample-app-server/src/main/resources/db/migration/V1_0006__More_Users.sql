insert into user (username, password, name, email) 
			values ('Mary 1', '$2a$10$uq5uv8n/UXq7BjdYMTJuvuqOIC6ABD6vO0fYCGArtlKXlEUV1m2va',
				 	'Mary the First', 'mary1@email.com');
				 	
insert into user (username, password, name, email) 
			values ('Mary 2', '$2a$10$uq5uv8n/UXq7BjdYMTJuvuqOIC6ABD6vO0fYCGArtlKXlEUV1m2va',
				 	'Mary the Second', 'mary2@email.com');
				 	
insert into user (username, password, name, email) 
			values ('Mary 3', '$2a$10$uq5uv8n/UXq7BjdYMTJuvuqOIC6ABD6vO0fYCGArtlKXlEUV1m2va',
				 	'Mary the Third', 'mary3@email.com');
				 	
insert into user (username, password, name, email) 
			values ('Mary 4', '$2a$10$uq5uv8n/UXq7BjdYMTJuvuqOIC6ABD6vO0fYCGArtlKXlEUV1m2va',
				 	'Mary the Fourth', 'mary4@email.com');

insert into user (username, password, name, email) 
			values ('Mary 5', '$2a$10$uq5uv8n/UXq7BjdYMTJuvuqOIC6ABD6vO0fYCGArtlKXlEUV1m2va',
				 	'Mary the Fifth', 'mary5@email.com');
				 	
insert into user (username, password, name, email) 
			values ('Mary 6', '$2a$10$uq5uv8n/UXq7BjdYMTJuvuqOIC6ABD6vO0fYCGArtlKXlEUV1m2va',
				 	'Mary the Sixth', 'mary6@email.com');

insert into user (username, password, name, email) 
			values ('Mary 7', '$2a$10$uq5uv8n/UXq7BjdYMTJuvuqOIC6ABD6vO0fYCGArtlKXlEUV1m2va',
				 	'Mary the Seventh', 'mary7@email.com');	
				 	
insert into user (username, password, name, email) 
			values ('Mary 8', '$2a$10$uq5uv8n/UXq7BjdYMTJuvuqOIC6ABD6vO0fYCGArtlKXlEUV1m2va',
				 	'Mary the Eighth', 'mary8@email.com');
				 	
insert into user (username, password, name, email) 
			values ('Mary 9', '$2a$10$uq5uv8n/UXq7BjdYMTJuvuqOIC6ABD6vO0fYCGArtlKXlEUV1m2va',
				 	'Mary the Ninth', 'mary9@email.com');
				 	
insert into user (username, password, name, email) 
			values ('Mary 10', '$2a$10$uq5uv8n/UXq7BjdYMTJuvuqOIC6ABD6vO0fYCGArtlKXlEUV1m2va',
				 	'Mary the Tenth', 'mary10@email.com');
				 	
insert into user (username, password, name, email) 
			values ('Mary 11', '$2a$10$uq5uv8n/UXq7BjdYMTJuvuqOIC6ABD6vO0fYCGArtlKXlEUV1m2va',
				 	'Mary the Eleventh', 'mary11@email.com');
				 	
insert into user (username, password, name, email) 
			values ('Mary 12', '$2a$10$uq5uv8n/UXq7BjdYMTJuvuqOIC6ABD6vO0fYCGArtlKXlEUV1m2va',
				 	'Mary the Twelfth', 'mary12@email.com');		
				 	
insert into user_role(user_id, role_id)
	select user.id as user_id, role.id as role_id
	from user, role
	where user.name like 'Mary%';