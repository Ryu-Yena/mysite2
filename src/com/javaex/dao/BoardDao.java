package com.javaex.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.vo.BoardVo;
import com.javaex.vo.GuestVo;


public class BoardDao {
	
	// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		private String driver = "oracle.jdbc.driver.OracleDriver";
		private String url = "jdbc:oracle:thin:@localhost:1521:xe";
		private String id = "webdb";
		private String pw = "webdb";

		private void getConnection() {
			try {
				// 1. JDBC 드라이버 (Oracle) 로딩
				Class.forName(driver);

				// 2. Connection 얻어오기
				conn = DriverManager.getConnection(url, id, pw);
				// System.out.println("접속성공");

			} catch (ClassNotFoundException e) {
				System.out.println("error: 드라이버 로딩 실패 - " + e);
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}
		}

		public void close() {
			// 5. 자원정리
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}
		}
		
		//게시글 등록
		public int insertB(BoardVo boardVo) {
			int count = 0;
			getConnection();

			try {

				// 3. SQL문 준비 / 바인딩 / 실행
				String query = ""; // 쿼리문 문자열만들기, ? 주의
				query += " insert into board ";
				query += " values (seq_board_no.nextval, ?, ?, null, ";
				query += " sysdate, ?) ";
				
				//System.out.println(query);

				pstmt = conn.prepareStatement(query); // 쿼리로 만들기

				pstmt.setString(1, boardVo.getTitle()); 
				pstmt.setString(2, boardVo.getContent());
				pstmt.setInt(3, boardVo.getUser_no());

				count = pstmt.executeUpdate(); // 쿼리문 실행

				// 4.결과처리
				System.out.println(count + "건 저장");

			} catch (SQLException e) {
				System.out.println("error:" + e);
			}
			close();
			return count;
		}
		
		
		//게시글 리스트
		public List<BoardVo> getBoardList(){
			List<BoardVo> boardList = new ArrayList<BoardVo>();
			
			getConnection();
			
			try {
					// 3. SQL문 준비 / 바인딩 / 실행 --> 완성된 sql문을 가져와서 작성할것
					String query = "";
					query += " select  b.no, ";
					query += "         b.title, ";
					query += " 		   u.name, ";
					query += "         b.hit, ";
					query += "         to_char(b.reg_date,'yyyy-mm-dd hh24:mi') reg_date, ";
					query += "         u.no user_no ";
					query += " from board b, users u ";
					query += " where b.user_no = u.no ";
					query += " order by b.no desc ";
					
					pstmt = conn.prepareStatement(query);
					
					
					rs = pstmt.executeQuery();

					// 4.결과처리
					while (rs.next()) {
						int no = rs.getInt("no");
						String title = rs.getString("title");
						String name = rs.getString("name");
						int hit = rs.getInt("hit");
						String reg_date = rs.getString("reg_date");
						int user_no = rs.getInt("user_no");

						BoardVo BoardVo = new BoardVo(no, title, name, hit, reg_date, user_no);
						boardList.add(BoardVo);
					}

				} catch (SQLException e) {
					System.out.println("error:" + e);
				}

				close();

				return boardList;

		}
		
		//조회수 증가
		public int hitB(int num) {
			int count = 0;
			getConnection();

			try {
				// 3. SQL문 준비 / 바인딩 / 실행
				String query = ""; // 쿼리문 문자열만들기, ? 주의
				query += " update board ";
				query += " set hit = hit+1 ";
				query += " where no = ? ";
				
				pstmt = conn.prepareStatement(query); // 쿼리로 만들기
				pstmt.setInt(1, num);


				count = pstmt.executeUpdate(); // 쿼리문 실행

				// 4.결과처리
				System.out.println("조회수 증가");

			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

			close();
			return count;
		}
		
		
		//게시글 삭제
		public int deleteB(int no) {
			int count = 0;
			getConnection();

			try {
				// 3. SQL문 준비 / 바인딩 / 실행
				String query = ""; // 쿼리문 문자열만들기, ? 주의
				query += " delete from board ";
				query += " where no = ? ";
				
				pstmt = conn.prepareStatement(query); // 쿼리로 만들기
				pstmt.setInt(1, no);


				count = pstmt.executeUpdate(); // 쿼리문 실행

				// 4.결과처리
				System.out.println(count + "건 삭제");

			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

			close();
			return count;
		}
		
		//게시글 읽어오기
		public BoardVo getB(int bno) {
			BoardVo boardVo = null;
			getConnection();

			try {
				// 3. SQL문 준비 / 바인딩 / 실행
				String query = ""; 
				query += " select b.no, ";
				query += "        u.name, ";
				query += "  	  b.hit, ";
				query += "  	  to_char(b.reg_date,'yyyy-mm-dd') reg_date, ";
				query += "  	  b.title, ";
				query += "  	  b.content, ";
				query += "  	  u.no ";
				query += " from board b, users u ";
				query += " where b.user_no = u.no ";
				query += " and b.no = ? ";
				
				pstmt = conn.prepareStatement(query); // 쿼리로 만들기
				pstmt.setInt(1, bno);

				rs = pstmt.executeQuery();

				// 4.결과처리
				while (rs.next()) {
					String name = rs.getString("name");
					int hit = rs.getInt("hit");
					String reg_date = rs.getString("reg_date");
					String title = rs.getString("title");
					String content = rs.getString("content");
					int user_no = rs.getInt("user_no");
					int no = rs.getInt("no");
					boardVo = new BoardVo(no, title, name, content, hit, reg_date, user_no);
					
				}


			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

			close();
			return boardVo;
		}
		
		//게시글 수정하기
		public int updateB(BoardVo boardVo) {
			int count = 0;
			getConnection();
			

			try {
				// 3. SQL문 준비 / 바인딩 / 실행
				String query = "";
				query += " update board ";
				query += " set title = ?, ";
				query += " content = ? ";
				query += " where no = ? ";

				System.out.println(query);

				// 쿼리문 만들기
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, boardVo.getTitle());
				pstmt.setString(2, boardVo.getContent());
				pstmt.setInt(3, boardVo.getNo());
				
				count = pstmt.executeUpdate();

				// 4.결과처리
				System.out.println(count + " 건 수정");

				} catch (SQLException e) {
							System.out.println("error:" + e);
						}

			close();
			return count;
		}
		
		
		
}