--테이블 삭제
drop table board;


--시퀀스 삭제
drop sequence seq_board_no;

--테이블 생성
create table board(
    no number,
    title varchar2(500) not null,
    content varchar2(4000),
    hit number,
    reg_date date not null,
    user_no number not null,
    primary key(no)
    );

--시퀀스 생성
create sequence seq_board_no
INCREMENT by 1
start with 1;

--리스트
select no,
       title,
       content,
       hit,
       reg_date,
       user_no
from board;

commit;