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
				query += " to_char(sysdate,'YY-MM-DD HH24:MI'), ?) ";
				
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
					query += " select  no, ";
					query += "         title, ";
					query += "         content, ";
					query += "         hit, ";
					query += "         to_char(reg_date, 'YY-MM-DD HH24:MI') ";
					query += " from board";
					
					pstmt = conn.prepareStatement(query);
					
					
					rs = pstmt.executeQuery();

					// 4.결과처리
					while (rs.next()) {
						int no = rs.getInt("no");
						String title = rs.getString("title");
						String content = rs.getString("content");
						int hit = rs.getInt("hit");
						String reg_date = rs.getString("reg_date");

						BoardVo BoardVo = new BoardVo(no, title, content, hit, reg_date);
						boardList.add(BoardVo);
					}

				} catch (SQLException e) {
					System.out.println("error:" + e);
				}

				close();

				return boardList;

		}
}