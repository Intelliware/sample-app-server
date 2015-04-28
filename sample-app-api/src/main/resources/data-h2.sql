insert into contact (email, first_name, last_name) values ('karyn.porter@stelaecor.com', 'Karyn', 'Porter');
select @contact_id := scope_identity();
insert into company (name, phone, contact_id) values ('MARVAME', '+1 (828) 533-2655', @contact_id);
insert into company (name, phone, contact_id) values ('BOILICON', '+1 (893) 432-3827', @contact_id);
