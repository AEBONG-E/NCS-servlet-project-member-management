package com.member.servletprojectmembermanagement.dao;

import com.member.servletprojectmembermanagement.vo.BoardVo;
import com.member.servletprojectmembermanagement.vo.MemberVo;
import com.member.servletprojectmembermanagement.vo.RippleVo;
import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class RippleDao {
    private static RippleDao instance;
    private final DBConnection dbConnection;

    //날짜 포맷 형식 전역 변수 선언
    DateTimeFormatter simpleFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public RippleDao() {
        this.dbConnection = DBConnection.getInstance();
    }

    public static RippleDao getInstance() {
        if (instance == null) {
            instance = new RippleDao();
        }
        return instance;
    }

    /*
     * 댓글 저장
     */
    public boolean save(RippleVo ripple) throws SQLException, ClassNotFoundException {
        String insertSQL = "insert into servlet_member.ripple values (null, ?, ?, ?, ?, ?, now())";
        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(insertSQL);
        pstmt.setLong(1, ripple.getRippleId());
        pstmt.setString(2, ripple.getMember().getId());
        pstmt.setString(3, ripple.getMember().getName());
        pstmt.setString(4, ripple.getContent());
        pstmt.setString(5, ripple.getIp());
        pstmt.executeUpdate();
        log.info("RippleDao : save() savedRipple - {}", ripple);

        if (pstmt != null) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * 게시글 번호로 댓글 목록 조회
     */
    public List<RippleVo> findRipplesByBoardNum(long boardNum) throws SQLException, ClassNotFoundException {
        List<RippleVo> rippleList = new ArrayList<>();
        String sql = "select * from servlet_member.ripple where board_num = ?;";
        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setLong(1, boardNum);
        @Cleanup ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            LocalDateTime createdAt = LocalDateTime.parse(rs.getString("created_at"), simpleFormatter);

            MemberVo member = new MemberVo(
                    rs.getString("member_id"),
                    rs.getString("member_name")
            );

            BoardVo board = new BoardVo(
                    boardNum
            );

            RippleVo ripple = new RippleVo(
                    rs.getLong("rippleId"),
                    board,
                    member,
                    rs.getString("content"),
                    rs.getString("ip"),
                    createdAt
            );
            rippleList.add(ripple);
        }
        log.info("RippleDao: findRipplesByBoardNum() findedRipples - {}", rippleList);
        return rippleList;
    }

    /*
     * 댓글 고유 번호 기준으로 게시글 조회
     */
    public long findBoardByRippleId(long rippleId) throws SQLException, ClassNotFoundException {
        log.info("RippleDao : findBoardByRippleId()");
        String sql = "select board_num from servlet_member.ripple where rippleId = ?";
        @Cleanup Connection getConn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = getConn.prepareStatement(sql);
        pstmt.setLong(1, rippleId);
        @Cleanup ResultSet rs = pstmt.executeQuery();
        rs.next();
        return rs.getLong(1);
    }

    /*
     * 게시판 번호를 기준으로 저장된 댓글 개수 조회
     */
    public int findRippleCountByBoardNum(long boardNum) throws SQLException, ClassNotFoundException {
        log.info("RippleDao : findRippleCountByBoardNum()");
        String sql = "select count(*) from servlet_member.ripple where board_num = ?";
        @Cleanup Connection getConn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = getConn.prepareStatement(sql);
        pstmt.setLong(1, boardNum);
        @Cleanup ResultSet rs = pstmt.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    /*
     * 댓글 고유 번호로 댓글 삭제
     */
    public boolean deleteRippleByRippleId(long rippleId) throws SQLException, ClassNotFoundException {
        log.info("RippleDao : deleteRippleByRippleId()");
        String sql = "delete from servlet_member.ripple where rippleId = ?";
        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setLong(1, rippleId);
        pstmt.executeUpdate();

        if (pstmt != null) {
            return true;
        } else {
            return false;
        }
    }

}
