package com.javaex.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.BoardDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;


@WebServlet("/board")
public class BoardController extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("board controller");
		
		
		
		

		String action = request.getParameter("action");
		System.out.println("action=" + action);
		
		//write Form
		if("writeForm".equals(action)) {
			System.out.println("게시글 작성");
			WebUtil.forward(request, response, "/WEB-INF/views/board/writeForm.jsp");
			
		}else if("write".equals(action)){
			System.out.println("게시글 등록");
			
			//파라미터 값 꺼내기
			String title = request.getParameter("title");
			String content = request.getParameter("content");

			//세션에 있는 no
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			int no = authUser.getNo();
			
			//vo로 묶기
			BoardVo boardVo = new BoardVo(title, content, no);
			
			System.out.println(boardVo);
			
			//Dao insert(vo)사용
			BoardDao boardDao = new BoardDao();
			boardDao.insertB(boardVo);
			
			//포워드
			WebUtil.forward(request, response, "/mysite2/board");
			
		}else {
			//메인 게시판 화면 포워드
			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
			
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
