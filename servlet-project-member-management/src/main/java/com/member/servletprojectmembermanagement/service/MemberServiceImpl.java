package com.member.servletprojectmembermanagement.service;

import com.member.servletprojectmembermanagement.dao.MemberDao;
import com.member.servletprojectmembermanagement.dto.MemberDto;
import com.member.servletprojectmembermanagement.vo.MemberVo;

import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Log4j2
public class MemberServiceImpl implements MemberService {

    private static MemberServiceImpl instance;
    MemberDao memberDao = MemberDao.getInstance();

    //날짜 포맷 형식 전역 변수 선언
    DateTimeFormatter simpleFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public MemberServiceImpl() {}
    private MemberServiceImpl(MemberDao dao) {
        this.memberDao = dao;
    }

    public static MemberServiceImpl getInstance(MemberDao dao) {
        if (instance == null) {
            instance = new MemberServiceImpl(dao);
        }
        return instance;
    }


    @Override
    public void isExistId(HttpServletRequest request, HttpServletResponse response) throws SQLException, ClassNotFoundException, IOException {
        log.info("MemberService: isExistId()");
        String id = request.getParameter("id");
        response.getWriter().print("{\"result\":\"");

        if (memberDao.isExistId(id)) {
            response.getWriter().print("true");
        }
        else {
            response.getWriter().print("false");
        }
        response.getWriter().print("\"}");
    }

    @Override
    public boolean registerMember(HttpServletRequest request) throws Exception {
        log.info("MemberService: registerMember()");

        MemberDto memberDto = new MemberDto();

        memberDto.setId(request.getParameter("id"));
        memberDto.setPassword(request.getParameter("password"));
        memberDto.setName(request.getParameter("name"));
        memberDto.setGender(request.getParameter("gender"));

        String year = request.getParameter("birthyy");
        String month = request.getParameterValues("birthmm")[0];
        String day = request.getParameter("birthdd");
        memberDto.setBirth(year + "/" + month + "/" + day);

        String mail1 = request.getParameter("mail1");
        String mail2 = request.getParameterValues("mail2")[0];
        memberDto.setEmail(mail1 + "@" + mail2);

        memberDto.setPhone(request.getParameter("phone"));
        memberDto.setZipcode(request.getParameter("zipcode"));
        memberDto.setAddr1(request.getParameter("addr1"));
        memberDto.setAddr2(request.getParameter("addr2"));

        LocalDateTime createdAt = LocalDateTime.now();
        memberDto.setCreatedAt(createdAt);

        if (!memberDao.isExistId(memberDto.getId())) {
            MemberVo vo = MemberDto.toVo(memberDto);
            return memberDao.save(vo);
        }
        return false;
    }

    @Override
    public boolean login(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        log.info("MemberService: login()");
        String id = request.getParameter("id");
        String password = request.getParameter("password");
        String name;

        if (memberDao.findMemberByIdAndPassword(id, password)) {
            name = memberDao.findMemberNameById(id);   //DB에서 name 가져와서 session 에 전달하는 역할
            if ((String) request.getSession().getAttribute("sessionMemberId") == null) {
                request.getSession().setAttribute("sessionMemberId", id);
                request.getSession().setAttribute("sessionMemberName", name);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public void logout(HttpServletRequest request) {
        if ((String) request.getSession().getAttribute("sessionMemberId") != null) {
            request.getSession().invalidate();
        }
    }

    @Override
    public MemberDto getMemberById(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        log.info("MemberService: getMemberById()");
        String id = (String) request.getSession().getAttribute("sessionMemberId");
        return MemberDto.MemberMapper(memberDao.findMemberById(id));
    }

    @Override
    public boolean updateMember(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        log.info("MemberService: updateMember()");

        MemberDto dto = new MemberDto();
        if ((String) request.getSession().getAttribute("sessionMemberId") != null) {
            String id = (String) request.getSession().getAttribute("sessionMemberId");

            dto.setId(id);
            dto.setPassword(request.getParameter("password"));
            dto.setName(request.getParameter("name"));
            dto.setGender(request.getParameter("gender"));

            String year = request.getParameter("birthyy");
            String month = request.getParameterValues("birthmm")[0];
            String day = request.getParameter("birthdd");
            dto.setBirth(year + "/" + month + "/" + day);

            String mail1 = request.getParameter("mail1");
            String mail2 = request.getParameterValues("mail2")[0];
            dto.setEmail(mail1 + "@" + mail2);

            dto.setPhone(request.getParameter("phone"));
            dto.setZipcode(request.getParameter("zipcode"));
            dto.setAddr1(request.getParameter("addr1"));
            dto.setAddr2(request.getParameter("addr2"));

            dto.setCreatedAt(LocalDateTime.parse(request.getParameter("createdAt"), simpleFormatter));
            LocalDateTime updatedAt = LocalDateTime.now();
            dto.setCreatedAt((updatedAt));

            MemberVo vo = MemberDto.toVo(dto);
            return memberDao.update(vo);
        }
        return false;
    }

    @Override
    public boolean deleteMember(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        log.info("MemberService: deleteMember()");

        if ((String) request.getSession().getAttribute("sessionMemberId") != null) {
            String id = (String) request.getSession().getAttribute("sessionMemberId");
            return memberDao.deleteByMemberId(id);
        }
        return false;
    }
}
