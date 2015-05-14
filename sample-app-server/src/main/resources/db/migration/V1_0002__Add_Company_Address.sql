alter table company
add address varchar(255);

update company
set address='200 Adelaide W, Toronto, ON M5H 1W7'
where name = 'MARVAME';

update company
set address='80 Wellington Street, Ottawa, ON K1A 0A2'
where name = 'BOILICON';