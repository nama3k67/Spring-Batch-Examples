drop table information if exists;

create table information (
	student_id bigint identity primary key, 
	name varchar(20) not null,
	class varchar(20),
	avg_mark double not null, 
	classification varchar(30) not null
)