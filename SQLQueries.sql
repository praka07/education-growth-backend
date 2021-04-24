
create table batch(
batchId int identity(1,1) primary key,
fromDate int,
toDate int
)

create table users (
autoId int identity(1,1) primary key,
rollNumber varchar(10),
emailId varchar(40) not null,
password varchar(10) default('abc'),
firstName varchar(15),
lastName varchar(15),
contactNumber varchar(15),
role int not null,
batch int,
active bit default(1),
createdDate smalldatetime default(getdate()),
createdBy int
)

insert into users(rollnumber,emailid,password,firstname,lastname,contactnumber,role,batch,createdby) values
(0,'ibee@gmail.com','123','ibee','mouhaa','866788',1,0,1)

create Procedure createUser
@firstName varchar(15),
@lastName varchar(15),
@emailId varchar(50),
@contactNumber varchar(15),
@Batch int,
@role int,
@createdBy int
As
Begin
 Set NoCount On
	Declare @userId int
	
	insert into users(firstName,lastName,contactNumber,batch,emailId,createdBy,role)
	values(@firstName,@lastName,@contactNumber,@batch,@emailId,@createdBy,@role)
	set @userId = @@IDENTITY

	Declare @rollNumber varchar(10)
	set @rollNumber = ''
	if @role = 3 begin
		Select @rollNumber = cast(fromDate as varchar) + '-MCA-' + cast(@userId as varchar) from batch where batchId = @Batch
	end
	update users set rollNumber = @rollNumber where autoId = @userId

 Set NoCount Off
 End 