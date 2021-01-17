package com.javaex.controller;

import java.io.IOException;
import java.util.List;

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
			
			//리다이렉트
			WebUtil.redirect(request, response, "/mysite2/board");
			
		}else if("delete".equals(action)){
			System.out.println("게시글 삭제");
			
			//파라미터 값 꺼내기
			int no = Integer.parseInt(request.getParameter("no"));
			
			//dao deleteB
			BoardDao boardDao = new BoardDao();
			boardDao.deleteB(no);

			//리다이렉트
			WebUtil.redirect(request, response, "/mysite2/board");
			
		}else if("read".equals(action)) {
			System.out.println("게시글 읽기");
			
			
			//파라미터값
			int no = Integer.parseInt(request.getParameter("no"));
			System.out.println(no);
			
			//dao 여기도 수정예정
			BoardDao boardDao = new BoardDao();
			
			//조회수 업데이트...수정예정
			boardDao.hitB(no);
			
			//dao getB
			BoardVo boardVo = boardDao.getB(no);
			
						
			//어트리뷰트
			request.setAttribute("boardVo", boardVo);
			
			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/board/read.jsp");
		
		
		}else if("modifyForm".equals(action)) {
			System.out.println("게시글 수정 폼");
			
			int no = Integer.parseInt(request.getParameter("no"));
			
			BoardDao boardDao = new BoardDao();
			BoardVo boardVo = boardDao.getB(no);
			
			request.setAttribute("boardVo", boardVo);
			
			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/board/modifyForm.jsp");
			
		}else if ("modify".equals(action)) {
			System.out.println("게시글 수정");
			
			//파라미터 값 가져오기
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int no = Integer.parseInt(request.getParameter("no"));
			
			//BoardVo
			BoardVo boardVo = new BoardVo(no, title, content);
			
			//Dao updateB
			BoardDao boardDao = new BoardDao();
			boardDao.updateB(boardVo);
			
			//redirect
			WebUtil.redirect(request, response, "/mysite2/Board");
			
		}else {
			System.out.println("게시판 메인 리스트");
			
			BoardDao boardDao = new BoardDao();
			List<BoardVo> boardList = boardDao.getBoardList();
			
			request.setAttribute("boardList", boardList);
			
			
			//메인 게시판 화면 포워드
			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
			
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
