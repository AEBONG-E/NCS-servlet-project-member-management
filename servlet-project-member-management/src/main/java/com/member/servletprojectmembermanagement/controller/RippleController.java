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
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Log4j2
@WebServlet("/ripple/*")
public class RippleController extends HttpServlet {

    private RippleServiceImpl rippleService;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public RippleController() {}

    @Override
    public void init() throws ServletException {
        rippleService = new RippleServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String RequestURI = req.getRequestURI();
        String contextPath = req.getContextPath();
        String command = RequestURI.substring(contextPath.length());

        log.info("command : " + command);
        resp.setContentType("text/html; charset=UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        req.setCharacterEncoding("UTF-8");

        switch (command) {
            case "/ripple/ripple_list":
                try {
                    List<RippleDto> rippleList = rippleService.getRipplesByBoardNum(req);
                    JSONArray jsonArray = new JSONArray();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"); // 포맷터 선언

                    for (RippleDto ripple : rippleList) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("rippleId", ripple.getRippleId());
                        jsonObject.put("num", ripple.getBoard().getNum());
                        jsonObject.put("id", ripple.getMember().getId());
                        jsonObject.put("name", ripple.getMember().getName());
                        jsonObject.put("content", ripple.getContent());

                        String formattedDateTime = ripple.getCreatedAt().format(formatter); // 날짜 형식 변경
                        jsonObject.put("createdAt", formattedDateTime);

                        jsonObject.put("isLogin", ripple.isLogin());
                        jsonArray.add(jsonObject);
                    }

                    String jsonOutput = jsonArray.toJSONString();
                    log.info("Generated JSON: " + jsonOutput);
                    resp.getWriter().print(jsonOutput);  // 한 번만 출력하기

                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String RequestURI = req.getRequestURI();
        String contextPath = req.getContextPath();
        String command = RequestURI.substring(contextPath.length());

        log.info("command : " + command);
        resp.setContentType("application/json; charset=UTF-8");
        req.setCharacterEncoding("UTF-8");

        switch (command) {
            case "/ripple/process_ripple_register":
                try {
                    JSONObject jsonObject = new JSONObject();
                    if (rippleService.registerRipple(req)) {
                        jsonObject.put("result", "true");
                        jsonObject.put("message", "등록에 성공했습니다.");
                    } else {
                        jsonObject.put("result", "false");
                    }
                    resp.getWriter().print(jsonObject.toJSONString());
                } catch (Exception e) {
                    e.printStackTrace();
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
