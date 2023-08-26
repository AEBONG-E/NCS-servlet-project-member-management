package com.member.servletprojectmembermanagement.service;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

public interface BoardService {

    /**
     * 게시글 생성 로직
     * @param request
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    boolean registerBoard(HttpServletRequest request) throws SQLException, ClassNotFoundException, Exception;

    /**
     * 전체 게시판 목록 조회 로직
     * 최근 게시글 순 정렬, 페이징 기능, 검색 기능
     * @param request
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    void getBoardList(HttpServletRequest request) throws SQLException, ClassNotFoundException;

    /**
     * 게시글 조회 로직
     * 조회할 때 마다 조회 수 증가
     * @param request
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    void getBoard(HttpServletRequest request) throws SQLException, ClassNotFoundException;

    /**
     * 게시글 업데이트 로직
     * 파일 업로드 기능
     * @param request
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    boolean updateBoard(HttpServletRequest request) throws SQLException, ClassNotFoundException, Exception;

    /**
     * 게시글 삭제 로직
     * @param request
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    boolean removeBoard(HttpServletRequest request) throws SQLException, ClassNotFoundException;

}
