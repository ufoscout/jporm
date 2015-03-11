-- hello

create table STRANGE_TABLE ();

drop table STRANGE_TABLE;

-- now we try one insert 
insert into TEMP_TABLE (ID, NAME) values (1, 'one');

-- now we try a new insert but now I put an ; simbol here 
insert into TEMP_TABLE (ID, NAME) values (2, 'two');
insert into TEMP_TABLE (ID, NAME) values (3, 'three');

-- and now I try to insert something more difficult:
insert into TEMP_TABLE (ID, NAME) values (4, 'four;');
insert into TEMP_TABLE (ID, NAME) values (5, 'f''ive;');
   
-- and now this is very difficult'; 
-- ;'
--';
insert into TEMP_TABLE (ID, NAME) values (6, 's''ix;');

-- Everything's fine?
insert
into TEMP_TABLE (ID, NAME) values (7, 'seven'';{--ix;');
insert into TEMP_TABLE (ID, NAME) values (8, 'height'';{--ix;');

-- ok let's test the most difficult situation:
insert into TEMP_TABLE (ID, NAME) values (9, 'ni'';ne'';{--ix;');insert into TEMP_TABLE (ID, NAME) values (10, 'ten'';{--ix;'); insert
into TEMP_TABLE (ID, NAME) values (11, 'e''le;{--ven;'); 