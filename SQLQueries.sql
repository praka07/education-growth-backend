
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
('','ibee@gmail.com','123','ibee','mouhaa','866788',1,0,1)

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
 
 
 Create table elective(
electiveId int identity(1,1) primary key,
electiveName varchar(100),
electiveType varchar(10))

Create table subject(
subjectId int identity(1,1) primary key,
subjectCode varchar(5),
subjectName varchar(100),
subjectType varchar(10),
semester int,
active bit default(1),
credit decimal(18,2),
createdBy int,
createdDate smalldatetime default(getdate()),
batch int
)

Create table subjectMapping(
mappingId int identity(1,1) primary key,
studentId int,
semester int,
subjectId int,
electiveId int,
createdBy int,
createdDate smalldatetime default(getdate()),
foreign key(studentId) references users(autoId),
foreign key(subjectId) references subject(subjectId),
foreign key(electiveId) references elective(electiveId)
)

Create table markDetails(
autoId int identity(1,1) primary key,
studentId int,
semester int,
subjectId int,
internalMarks decimal(18,2),
externalMarks decimal(18,2),
totalMarks decimal(18,2),
creditPoints decimal(18,2),
createdBy int,
createdDate smalldatetime default(getdate()),
foreign key(studentId) references users(autoId),
foreign key(subjectId) references subject(subjectId)
)
--------------------------------------
Create Proc getSubjectsListbyStudent
@autoId int,
@semester int,
@createdBy int
as
begin
set nocount on
	
	if not exists(select '' from markDetails where semester = @semester and studentId = @autoId) begin
		insert into markDetails(studentId,semester,subjectId,internalMarks,externalMarks,
		totalMarks,creditPoints,createdBy,createdDate)
		select u.autoId, a.semester, a.subjectId, 0, 0, 0, 0, @createdBy, GETDATE()
		from subject as a
		inner join users as u on u.batch = a.batch
		where a.semester = @semester and u.autoId = @autoId 
	end
	
	select b.autoId,a.semester, a.subjectId, a.subjectCode, a.subjectType, credit, 
	case when a.subjectType in (2,3) then a.subjectName + '-' + electiveName else a.subjectName end as subjectName
	from markDetails as b
	inner join subject as a on a.subjectId = b.subjectId
	left join subjectMapping as d on d.subjectId = b.subjectId and d.semester = b.semester
	left join elective as c on c.electiveId = d.electiveId
	where b.semester = @semester and b.studentId = @autoId
	
set nocount off
end
