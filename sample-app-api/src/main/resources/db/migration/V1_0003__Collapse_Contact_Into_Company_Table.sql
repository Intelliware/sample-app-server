alter table company
add contact_email varchar(255);

alter table company
add contact_first_name varchar(255);

alter table company
add contact_last_name varchar(255);

alter table company
drop contact_id;

drop table contact;

update company
set contact_email ='karyn.porter@stelaecor.com', contact_first_name = 'Karyn', contact_last_name = 'Porter'
where name = 'MARVAME';

update company
set contact_email ='page.williamson@stelaecor.com', contact_first_name = 'Page', contact_last_name = 'Williamson'
where name = 'BOILICON';




