#drop table if exists student;

create table course (
  id integer primary key not null,
  numbers varchar(14) not null,
  time varchar(10)
);

