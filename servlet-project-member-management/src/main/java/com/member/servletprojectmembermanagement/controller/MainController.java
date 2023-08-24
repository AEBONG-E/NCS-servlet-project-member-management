package com.member.servletprojectmembermanagement.controller;

import com.member.servletprojectmembermanagement.service.MemberServiceImpl;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
                req.getRequestDispatcher("/main/welcome.jsp").forward(req, resp);
                break;
        }
    }
}
