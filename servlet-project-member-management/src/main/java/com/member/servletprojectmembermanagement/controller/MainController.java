package com.member.servletprojectmembermanagement.controller;

import com.member.servletprojectmembermanagement.service.MemberServiceImpl;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Log4j2
@WebServlet("/")
public class MainController extends HttpServlet {

    public MainController() {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String RequestURI = req.getRequestURI();
        String contextPath = req.getContextPath();
        String command = RequestURI.substring(contextPath.length());

        log.info("MemberController - command: {}", command);
        resp.setContentType("text/html; charset=utf-8");
        req.setCharacterEncoding("utf-8");

        switch (command) {
            case "/":
                log.info("MemberController: welcome");
                HttpSession session = req.getSession(false); // false를 전달하여 세션이 없으면 null을 반환하도록 합니다.

                // 아이디와 이름 세션 값을 임시 변수에 백업
                Object sessionMemberId = null;
                Object sessionMemberName = null;
                if (session != null) {
                    sessionMemberId = session.getAttribute("sessionMemberId");
                    sessionMemberName = session.getAttribute("sessionMemberName");
                }

                // 모든 세션을 초기화
                if (session != null) {
                    session.invalidate();
                }

                // 새 세션을 만들고 아이디와 이름 세션만 복원
                session = req.getSession(true);
                if (sessionMemberId != null) {
                    session.setAttribute("sessionMemberId", sessionMemberId);
                }
                if (sessionMemberName != null) {
                    session.setAttribute("sessionMemberName", sessionMemberName);
                }

                req.getRequestDispatcher("/main/welcome.jsp").forward(req, resp);
                break;
        }
    }
}

