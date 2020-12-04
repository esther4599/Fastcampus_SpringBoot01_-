
insert into person(`id`, `name`, `age`, `blood_type`, `year_of_birthday`, `month_of_birthday`, `day_of_birthday`) values (1, '홍길동', 10, 'A', 1991, 8, 15);
insert into person(`id`, `name`, `age`, `blood_type`, `year_of_birthday`, `month_of_birthday`, `day_of_birthday`) values (2, '허준', 9, 'AB', 1992, 7, 21);
insert into person(`id`, `name`, `age`, `blood_type`, `year_of_birthday`, `month_of_birthday`, `day_of_birthday`) values (3, '허균', 8, 'A', 1993, 10, 15);
insert into person(`id`, `name`, `age`, `blood_type`, `year_of_birthday`, `month_of_birthday`, `day_of_birthday`) values (4, '황진이', 7, 'B', 1994, 8, 31);
insert into person(`id`, `name`, `age`, `blood_type`, `year_of_birthday`, `month_of_birthday`, `day_of_birthday`) values (5, '홍길동', 6, 'O', 1995, 12, 23);

insert into block(`id`, `name`) values (1, '홍길동');
insert into block(`id`, `name`) values (2, '허균');

--아래는 block과 person을 연결해주는 코드
update person set block_id = 1 where id = 1;
update person set block_id = 2 where id = 3;
