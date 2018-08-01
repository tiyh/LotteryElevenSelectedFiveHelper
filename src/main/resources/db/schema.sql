#drop table if exists student;

create table lottery (
  id integer primary key not null,
  numbers varchar(14) not null,
  time varchar(10)
);

