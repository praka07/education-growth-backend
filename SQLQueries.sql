create table users(autoId int identity(1,1) primary key,
userName varchar(40) not null,
password varchar(10),
role int not null,
active bit default(0),
createdDate smalldatetime default(getdate()),
createdBy int,
loginFlag bit default(0)
)