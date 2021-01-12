package com.javaex.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.UserDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.UserVo;


@WebServlet("/user")
public class UserController extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("user controller");
		
		
		String action = request.getParameter("action");
		System.out.println("action=" + action);
		
		//joinForm 포워드
		if("joinForm".equals(action)) {
			System.out.println("회원가입폼");
			WebUtil.forward(request, response, "/WEB-INF/views/user/joinForm.jsp");
			
		}else if("join".equals(action)) {
			System.out.println("화원가입");
			
			//파라미터 값 꺼내기
			String id = request.getParameter("id");
			String password = request.getParameter("pw");
			String name = request.getParameter("name");
			String gender = request.getParameter("gender");
			
			//Vo로 묶기
			UserVo userVo = new UserVo(id, password, name, gender);
			System.out.println(userVo.toString());

			
			//dao insert(Vo) 사용 > 저장 > 회원가입
			UserDao userDao = new UserDao();
			userDao.insert(userVo);
			
			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/user/joinOk.jsp");
			
		}else if("loginForm".equals(action)) {
			System.out.println("로그인 폼");
			
			WebUtil.forward(request, response, "/WEB-INF/views/user/loginForm.jsp");
			
		}else if("login".equals(action)) {
			System.out.println("로그인");
			//파라미터 id pw
			String id = request.getParameter("id");
			String pw = request.getParameter("pw");
			
			//dao > getUSer();
			UserDao userDao = new UserDao();
			UserVo authVo = userDao.getUser(id, pw);
			
			//System.out.println(authVo.toString());
			
			if(authVo == null) {
				System.out.println("로그인 실패");
				
				//리다이렉트 --> 로그인 폼
				WebUtil.redirect(request, response, "/mysite2/user?action=loginForm");
				
				
			} else { //성공일때
				System.out.println("로그인 성공");
				
				//세션영역에 필요한 값(vo) 넣어준다.
				HttpSession session = request.getSession();
				session.setAttribute("authUser", authVo );
				
				WebUtil.redirect(request, response, "/mysite2/main");
			}
		}else if ("logout".equals(action)) {
			System.out.println("로그아웃");
			
			//세션영역에 있는 vo를 삭제해야함
			HttpSession session = request.getSession();
			session.removeAttribute("authUser");
			session.invalidate();
			
			WebUtil.redirect(request, response, "/mysite2/main");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}
