package com.member.servletprojectmembermanagement.controller;

import com.member.servletprojectmembermanagement.dto.RippleDto;
import com.member.servletprojectmembermanagement.service.RippleServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Log4j2
@WebServlet("/ripple/*")
public class RippleController extends HttpServlet {

    private RippleServiceImpl rippleService;

    public RippleController() {}

    @Override
    public void init() throws ServletException {
        rippleService = new RippleServiceImpl();
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
        String viewPath = "/WEB-INF/template/ripple/";

        log.info("command : " + command);
        resp.setContentType("text/html; charset=UTF-8");
        req.setCharacterEncoding("UTF-8");

        switch (command) {
            case "/ripple/process_ripple_register":
                try {
                    JSONObject jsonObject = new JSONObject();
                    if (rippleService.registerRipple(req)) {
                        jsonObject.put("result", "true");
                    } else {
                        jsonObject.put("result", "false");
                    }
                    resp.getWriter().print(jsonObject.toJSONString());
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "/ripple/ripple_list":
                try {
                    List<RippleDto> rippleList = rippleService.getRipplesByBoardNum(req);
                    JSONArray jsonArray = new JSONArray();
                    for (RippleDto ripple : rippleList) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("rippleId", ripple.getRippleId());
                        jsonObject.put("board_num", ripple.getBoard().getNum());
                        jsonObject.put("member_id", ripple.getMember().getId());
                        jsonObject.put("member_name", ripple.getMember().getName());
                        jsonObject.put("content", ripple.getContent());
                        jsonObject.put("isLogin", ripple.isLogin());
                        jsonArray.add(jsonObject);
                    }
                    resp.getWriter().print(jsonArray.toJSONString());
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "/ripple/process_ripple_delete":
                try {
                    JSONObject jsonObject = new JSONObject();
                    if (rippleService.removeRippleByRippleId(req)) {
                        jsonObject.put("result", "true");
                    } else {
                        jsonObject.put("result", "false");
                    }
                    resp.getWriter().print(jsonObject.toJSONString());
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }
}
