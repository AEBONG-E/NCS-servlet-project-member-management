package com.member.servletprojectmembermanagement.controller;

import com.member.servletprojectmembermanagement.service.BoardServiceImpl;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@WebServlet("/board/*")
public class BoardController extends HttpServlet {

    private BoardServiceImpl boardService;

    public BoardController() {
    }

    @Override
    public void init() throws ServletException {
        boardService = new BoardServiceImpl();
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
        String viewPath = "/WEB-INF/template/board/";

        log.info("BoardController - command: {}", command);
        resp.setContentType("text/html; charset=utf-8");
        req.setCharacterEncoding("utf-8");

        switch (command) {
            case "/board/board_register": //게시글 작성 페이지 노출
                log.info("BoardController: board_register");
                req.getRequestDispatcher(viewPath + "board_register.jsp").forward(req, resp);
                break;
            case "/board/process_board_register": //작성된 게시글 저장 처리
                log.info("BoardController: process_board_register...");
                try {
                    if (boardService.registerBoard(req)) {
                        resp.sendRedirect("board_list");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case "/board/board_list": //게시판 목록 페이지 노출
                log.info("BoardController: board_list");
                try {
                    boardService.getBoardList(req);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                req.getRequestDispatcher(viewPath + "board_list.jsp").forward(req, resp);
                break;
            case "/board/board_article": //게시글 노출
                log.info("BoardController: board_article");
                try {
                    boardService.getBoard(req);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                req.getRequestDispatcher(viewPath + "board_article.jsp").forward(req,resp);
                break;
            case "/board/board_update": //게시글 수정 폼페이지 노출
                log.info("BoardController: board_update");
                try {
                    boardService.getBoard(req);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                req.getRequestDispatcher(viewPath + "board_update.jsp").forward(req,resp);
                break;
            case "/board/process_board_update": //게시글 수정 처리
                log.info("BoardController: process_board_update...");
                try {
                    if (boardService.updateBoard(req)) {
                        resp.sendRedirect("board_list");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case "/board/process_board_delete": //게시글 삭제 처리
                log.info("BoardController: process_board_delete...");
                try {
                    if (boardService.removeBoard(req)) {
                        resp.sendRedirect("board_list");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;

            default:
                req.getRequestDispatcher(viewPath + "error.jsp").forward(req, resp);
                break;
        }
    }
}
