package com.member.servletprojectmembermanagement.service;

import com.member.servletprojectmembermanagement.dto.RippleDto;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

public interface RippleService {

    /**
     * 댓글 생성 로직
     * (댓글 생성 시 게시글 의 댓글 갯수 카운트 반영 메소드 동작)
     * @param request
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    boolean registerRipple(HttpServletRequest request) throws Exception;

    /**
     * 게시글 기준으로 댓글 목록 가져오는 로직
     * @param request
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    List<RippleDto> getRipplesByBoardNum(HttpServletRequest request) throws SQLException, ClassNotFoundException;

    /**
     * 댓글 고유 번호를 기준으로 댓글 삭제 로직
     * @param request
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    boolean removeRippleByRippleId(HttpServletRequest request) throws SQLException, ClassNotFoundException;
}
