package com.member.servletprojectmembermanagement.service;

import com.member.servletprojectmembermanagement.dto.MemberDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public interface MemberService {

    /**
     * 회원 중복 검증 로직
     * @param request
     * @param response
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public void isExistId(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException;

    /**
     * 회원 가입 로직
     * @param request
     * @return
     * @throws Exception
     */
    public boolean registerMember(HttpServletRequest request) throws Exception;

    /**
     * 로그인 로직
     * @param request
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public boolean login(HttpServletRequest request) throws SQLException, ClassNotFoundException;

    /**
     * 로그아웃 로직
     * @param request
     */
    public void logout(HttpServletRequest request);

    /**
     * 회원 정보 조회 로직
     * @param request
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public MemberDto getMemberById(HttpServletRequest request) throws SQLException, ClassNotFoundException;

    /**
     * 회원 수정 로직
     * @param request
     * @return
     */
    public boolean updateMember(HttpServletRequest request) throws SQLException, ClassNotFoundException;

    /**
     * 회원 삭제 로직
     * @param request
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public boolean deleteMember(HttpServletRequest request) throws SQLException, ClassNotFoundException;

}
