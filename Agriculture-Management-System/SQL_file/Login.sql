create database Login;
use Login;
Create table Logininfo(username varchar(20) primary key, password varchar(20));
insert into Logininfo values("kumar1705",12345);
select * from Logininfo;