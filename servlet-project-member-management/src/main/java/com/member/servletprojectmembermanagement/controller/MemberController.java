package com.member.servletprojectmembermanagement.controller;
import com.member.servletprojectmembermanagement.dto.MemberDto;
import com.member.servletprojectmembermanagement.service.MemberServiceImpl;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * controller 에서는 복잡한 로직을 처리 하지 않고
 * 페이지 노출 또는 post action 작업 처리 및 예외 처리만 진행
 * 복잡한 로직 처리는 service 에서 처리해야 유지보수가 쉬우며
 * 에러 발생 시 명확하게 구분이 가능
 */
@Log4j2
@WebServlet("/member/*")
public class MemberController extends HttpServlet {

    private MemberServiceImpl memberService;

    public MemberController() {}

    @Override
    public void init() throws ServletException {
        memberService = new MemberServiceImpl();
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
            case "/member/member_register":   //회원 가입 페이지 노출
                log.info("MemberController: member_register");
                req.getRequestDispatcher("/WEB-INF/template/member/member_register.jsp").forward(req, resp);
                break;
            case "/member/ajax_id_check": //아이디 중복 체크
                log.info("MemberController: ajax_id_check...");
                try {
                    memberService.isExistId(req, resp);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "/member/process_member_register": //회원 가입 처리
                log.info("MemberController: process_member_register...");
                try {
                    if (memberService.registerMember(req)) {
                        resp.sendRedirect("member_result?msg=1");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case "/member/login":   //로그인 페이지 노출
                log.info("MemberController: login");
                req.getRequestDispatcher("/WEB-INF/template/member/login.jsp").forward(req, resp);
                break;
            case "/member/process_login":  //로그인 처리
                log.info("MemberController: process_login...");
                try {
                    if (memberService.login(req)) {
                        resp.sendRedirect("member_result?msg=2");
                    } else {
                        resp.sendRedirect("login?error=1");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case "/member/member_result":    //결과 페이지
                log.info("MemberController: member_result");
                String param = req.getParameter("msg");
                req.getRequestDispatcher("/WEB-INF/template/member/member_result.jsp?msg=" + param).forward(req, resp);
                break;
            case "/member/process_logout": //로그아웃 처리
                log.info("MemberController: process_logout...");
                memberService.logout(req);
                resp.sendRedirect("/member/login");
                break;
            case "/member/member_update":    //회원정보 수정 페이지 노출
                log.info("MemberController: member_update");
                try {
                    MemberDto member = memberService.getMemberById(req);
                    log.info("MemberController - memberDto {}: ", member);
                    req.setAttribute("member", member);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                req.getRequestDispatcher("/WEB-INF/template/member/member_update.jsp").forward(req, resp);
                break;
            case "/member/process_member_update": //회원 가입 처리
                log.info("MemberController: process_member_update...");
                try {
                    if (memberService.updateMember(req)) {
                        resp.sendRedirect("member_result?msg=0");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case "/member/process_member_delete": //회원 삭제 처리
                log.info("MemberController: process_member_delete...");
                try {
                    if (memberService.deleteMember(req)) {
                        req.getSession().invalidate();
                        resp.sendRedirect("/");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
        }

    }
}
