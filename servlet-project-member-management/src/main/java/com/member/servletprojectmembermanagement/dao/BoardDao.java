package com.member.servletprojectmembermanagement.dao;

import com.member.servletprojectmembermanagement.vo.BoardVo;
import com.member.servletprojectmembermanagement.vo.MemberVo;
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

/**
 * DAO 에서는 복잡한 로직을 처리 하지 않고
 * query 와 관련된 작업처리 와 query 작업 처리시
 * 최소한의 파라미터 만 활용하는 것이 좋음.
 * 복잡한 로직 처리는 service 에서 처리해야 유지보수가 쉬우며
 * 에러 발생 시 명확하게 구분이 가능
 */
@Log4j2
public class BoardDao {

    /*
     * 메모리의 부하를 줄이도록 싱글톤 패턴으로 구현
     * 한번만 생성되면 계속 사용할 수 있음.
     */
    private static BoardDao instance;
    private final DBConnection dbConnection;

    //날짜 포맷 형식 전역 변수 선언
    DateTimeFormatter simpleFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public BoardDao() {
        this.dbConnection = DBConnection.getInstance();
    }

    public static BoardDao getInstance() {
        if (instance == null) {
            instance = new BoardDao();
        }
        return instance;
    }

    /*
     * 게시글 생성
     */
    public boolean save(BoardVo board) throws SQLException, ClassNotFoundException {
        String sql = "insert into servlet_member.board values (null, ?, ?, ?, ?, 0, ?, 0, ?, ?, now(), null )";
        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, board.getMemberId());
        pstmt.setString(2, board.getMemberName());
        pstmt.setString(3, board.getTitle());
        pstmt.setString(4, board.getContent());
        pstmt.setString(5, board.getIp());
        pstmt.setString(6, board.getFileName());
        pstmt.setLong(7, board.getFileSize());
        pstmt.executeUpdate();
        log.info("BoardDao: save() - {}", board);

        if (pstmt != null) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * 등록된 게시판 갯수 조회
     */
    public int findBoardListCountByItemsAndByText(String items, String text) throws SQLException, ClassNotFoundException {
        String sql = "";
        PreparedStatement pstmt = null;
        @Cleanup Connection conn = dbConnection.getConnection();

        if (items == null && text == null) {
            sql = "SELECT count(*) FROM servlet_member.board";
            pstmt = conn.prepareStatement(sql);
        } else {
            sql = "SELECT count(*) FROM servlet_member.board WHERE " + items + " LIKE CONCAT('%', ?, '%')";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, text);
        }

        @Cleanup ResultSet rs = pstmt.executeQuery();

        rs.next();
        return rs.getInt(1);
    }


    /*
     * 게시판 전체 조회
     * 최근 게시물 부터 조회
     * 검색 기능 및 페이징 처리
     */
    public List<BoardVo> findList(
            int pageNum, int limit, String items, String text
    ) throws SQLException, ClassNotFoundException {

        List<BoardVo> boardList = new ArrayList<>();
        int start = (pageNum - 1) * limit;  //ex. 1페이지면 0, 2페이지면 5, 3페이지면 10

        String sql = "";
        PreparedStatement pstmt = null;
        @Cleanup Connection conn = dbConnection.getConnection();

        if (items == null && text == null) {
            sql = "SELECT * FROM servlet_member.board ORDER BY num DESC LIMIT ?, ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, start);
            pstmt.setInt(2, limit);
        } else {
            sql = "SELECT * FROM servlet_member.board WHERE "
                    + items + " LIKE ? ORDER BY num DESC LIMIT ?, ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + text + "%");
            pstmt.setInt(2, start);
            pstmt.setInt(3, limit);
        }

        @Cleanup ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            String createdAtStr = rs.getString("created_at");
            String updatedAtStr = rs.getString("updated_at");

            LocalDateTime createdAt = null;
            LocalDateTime updatedAt = null;

            if (createdAtStr != null) {
                createdAt = LocalDateTime.parse(createdAtStr, simpleFormatter);
            }

            if (updatedAtStr != null) {
                updatedAt = LocalDateTime.parse(updatedAtStr, simpleFormatter);
            }

            MemberVo member = new MemberVo(
                    rs.getString("member_id"),
                    rs.getString("member_name")
            );
            BoardVo board = new BoardVo(
                    rs.getLong("num"),
                    member,
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getInt("hit"),
                    rs.getString("ip"),
                    rs.getInt("ripple_count"),
                    rs.getString("file_name"),
                    rs.getLong("file_size"),
                    createdAt,
                    updatedAt
            );
            boardList.add(board);
        }
        log.debug("BoardDao: findList() boardList - {}", boardList);
        return boardList;
    }

    /*
     * 게시글 조회 수 증가 처리
     */
    public void updateBoardHitByNum(long num) throws SQLException, ClassNotFoundException {
        log.info("BoardDao: updateBoardHitByNum()");
        String sql = "update servlet_member.board set hit = hit + 1 where num = ?";
        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setLong(1, num);
        pstmt.executeUpdate();
    }


    /*
     * 게시글 조회
     */
    public BoardVo findBoardByNum(long num) throws SQLException, ClassNotFoundException {
        log.info("BoardDao: findBoardByNum()");
        BoardVo board = null;

        String sql = "select * from servlet_member.board where num = ?;";
        @Cleanup Connection getConn = dbConnection.getConnection();
        @Cleanup PreparedStatement getStmt = getConn.prepareStatement(sql);
        getStmt.setLong(1, num);
        @Cleanup ResultSet rs = getStmt.executeQuery();

        if (rs.next()) {
            String createdAtStr = rs.getString("created_at");
            String updatedAtStr = rs.getString("updated_at");

            LocalDateTime createdAt = null;
            LocalDateTime updatedAt = null;

            if (createdAtStr != null) {
                createdAt = LocalDateTime.parse(createdAtStr, simpleFormatter);
            }

            if (updatedAtStr != null) {
                updatedAt = LocalDateTime.parse(updatedAtStr, simpleFormatter);
            }

            MemberVo member = new MemberVo(
                    rs.getString("member_id"),
                    rs.getString("member_name")
            );
            board = new BoardVo(
                    rs.getLong("num"),
                    member,
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getInt("hit"),
                    rs.getString("ip"),
                    rs.getInt("ripple_count"),
                    rs.getString("file_name"),
                    rs.getLong("file_size"),
                    createdAt,
                    updatedAt
            );
        }
        return board;
    }

    /*
     * 게시글 수정
     */
    public boolean update(BoardVo board) throws SQLException, ClassNotFoundException {

        String udateSQL = "update servlet_member.board set member_name = ?, title = ?, content = ?, file_name = ?, file_size = ?, updated_at = now() where num = ?";
        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(udateSQL);
        pstmt.setString(1, board.getMemberName());
        pstmt.setString(2, board.getTitle());
        pstmt.setString(3, board.getContent());
        pstmt.setString(4, board.getFileName());
        pstmt.setLong(5, board.getFileSize());
        pstmt.setLong(6, board.getNum());
        pstmt.executeUpdate();
        log.info("BoardDao: update() - {}", board);

        if (pstmt != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteByNum(long num) throws SQLException, ClassNotFoundException {
        log.info("BoardDao: deleteByNum()");
        String sql = "delete from servlet_member.board where num = ?;";
        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setLong(1, num);
        pstmt.executeUpdate();

        if (pstmt != null) {
            return true;
        } else {
            return false;
        }
    }

}
