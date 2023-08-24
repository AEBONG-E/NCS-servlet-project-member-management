package com.member.servletprojectmembermanagement.dao;

import com.member.servletprojectmembermanagement.vo.BoardVo;
import com.member.servletprojectmembermanagement.vo.MemberVo;
import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("게시판 DB 테스트")
@Log4j2
public class BoardDBTest {

    private DBConnection dbConnection;

    //날짜 포맷 형식 전역 변수 선언
    DateTimeFormatter simpleFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    @BeforeEach
    void setUp() {
        this.dbConnection = DBConnection.getInstance();
    }

    @DisplayName("게시판 DB 테스트 - 게시글 생성 테스트. 생성 되면 true 반환 성공")
    @Test
    void givenBoard_whenSaving_thenReturnsTrue() throws SQLException, ClassNotFoundException {
        //Given
        int count = 0;
        BoardVo board = new BoardVo();

        //when
        String insertSQL = "insert into servlet_member.board values (null, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), null )";
        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(insertSQL);
        pstmt.setString(1, "hooney");
        pstmt.setString(2, "hooney");
        pstmt.setString(3, "Lorem6 Ipsum");
        pstmt.setString(4, "Lorem6 Ipsum is simply dummy text of the printing and typesetting industry.");
        pstmt.setInt(5, 0);
        pstmt.setString(6, "1.1.1.1");
        pstmt.setInt(7, 0);
        pstmt.setString(8, "test6.txt");
        pstmt.setInt(9, 10);
        pstmt.executeUpdate();

        String selectSQL = "select count(*) as cnt from servlet_member.board where num = ?";
        @Cleanup PreparedStatement checkPstmt = conn.prepareStatement(selectSQL);
        checkPstmt.setLong(1, 1);
        @Cleanup ResultSet rs = checkPstmt.executeQuery();

        if (rs.next()) {
            count = rs.getInt("cnt");
        }
        //then
        assertEquals(1, count);
    }

    @DisplayName("게시판 DB 테스트 - 게시판 검색을 위한 게시판 갯수 조회 테스트 개수가 맞으면 true 반환 테스트 성공")
    @Test
    void givenItemsAndText_whenFinding_thenReturnsBoardListCount() throws SQLException, ClassNotFoundException {
        //Given
        String items = null;
        String text = null;
        int count = 0;

        //when
        String sql = "";
        if (items == null && text == null) {
            sql = "SELECT count(*) FROM servlet_member.board";
        } else {
            sql = "SELECT count(*) FROM servlet_member.board WHERE " + items + " LIKE CONCAT('%:" + text + "%')";
        }
        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(sql);
        @Cleanup ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            count = rs.getInt(1);
        }

        //then
        assertEquals(1, count);
    }

    @DisplayName("게시판 DB 테스트 - 게시판 최근 게시물 부터 조회 페이징 테스트.")
    @Test
    void givenPageNumAndLimitAndItemsAndText_whenFindingList_thenReturnsBoardList() throws SQLException, ClassNotFoundException {
        //Given
        int pageNum = 1;
        int limit = 5;
        String items = null;
        String text = null;
        int count = 0;

        //when
        int start = (pageNum - 1) * limit;  //ex. 1페이지면 0, 2페이지면 5, 3페이지면 10
        String sql = "";
        if (items == null && text == null) {    //검색이 없는 경우
            sql = "SELECT * FROM servlet_member.board ORDER BY num DESC LIMIT " + start + ", " + limit;
        } else {
            sql = "SELECT * FROM servlet_member.board WHERE "
                    + items + " LIKE '%" + text + "%' ORDER BY num DESC LIMIT "
                    + start + ", " + limit;
        }

        List<BoardVo> boardList = new ArrayList<>();

        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(sql);
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

        //then
        assertEquals(limit, boardList.size());  // 반환된 게시물 수 확인

                // 정렬 확인: 가장 최근 게시물이 맨 앞에 오는지 확인
                if (!boardList.isEmpty()) {
                    LocalDateTime prevTime = boardList.get(0).getCreatedAt();
                    for (BoardVo board : boardList) {
                        assertTrue(prevTime.compareTo(board.getCreatedAt()) >= 0);
                        prevTime = board.getCreatedAt();
                    }
                }

                // 검색 기능 확인: 만약 items와 text가 설정되어 있다면, 해당 필드에 검색 텍스트가 포함되어 있는지 확인
                if (items != null && text != null) {
                    for (BoardVo board : boardList) {
                        String fieldValue;
                        switch (items) {
                            case "Lorem1 Ipsum":
                                fieldValue = board.getTitle();
                                break;
                            case "Lorem1 Ipsum is simply dummy text of the printing and typesetting industry.":
                                fieldValue = board.getContent();
                                break;
                            default:
                                throw new IllegalArgumentException("잘못된 검색 필드: " + items);
                        }
                        assertTrue(fieldValue.contains(text));
                    }
                }
    }

    @DisplayName("게시판 DB 테스트 - 게시글 조회 시 조회수 증가 테스트. 증가 시 테스트 성공")
    @Test
    void givenBoardNum_whenUpdatingBoardHit_thenReturnsBoardHitCount() throws SQLException, ClassNotFoundException {
        // Given
        long num = 1;
        int originalHit = 0;

        // 먼저 원래 hit 값을 가져옵니다.
        String getOriginalHitSql = "select hit from servlet_member.board where num = ?";
        @Cleanup Connection getConn = dbConnection.getConnection();
        @Cleanup PreparedStatement getStmt = getConn.prepareStatement(getOriginalHitSql);
        getStmt.setLong(1, num);
        @Cleanup ResultSet rs = getStmt.executeQuery();
        if (rs.next()) {
            originalHit = rs.getInt("hit");
        }

        // When
        // hit 값을 증가시킵니다.
        String sql = "update servlet_member.board set hit = hit + 1 where num = ?";
        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setLong(1, num);
        pstmt.executeUpdate();

        // Then
        // 증가된 hit 값을 가져와 비교합니다.
        int updatedHit = 0;
        @Cleanup ResultSet updatedRs = getStmt.executeQuery();
        if (updatedRs.next()) {
            updatedHit = updatedRs.getInt("hit");
        }
        assertEquals(originalHit + 1, updatedHit);
    }

    @DisplayName("게시판 DB 테스트 - 게시글 조회 테스트 게시글 번호와 데이터 일치하면 성공")
    @Test
    void givenBoardNum_whenFindingBoard_thenReturnsBoard() throws SQLException, ClassNotFoundException {
        //Given
        long num = 2;
        BoardVo board = null;
        String expectedTitle = "Lorem1 Ipsum";
        String expectedContent = "Lorem1 Ipsum is simply dummy text of the printing and typesetting industry.";

        //When
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

        //Then
        assertNotNull(board);  // board 객체가 null이 아닌지 확인
        assertEquals(expectedTitle, board.getTitle());  // 제목 확인
        assertEquals(expectedContent, board.getContent());  // 내용 확인

    }

    @DisplayName("게시판 DB 테스트 - 게시글 수정 테스트. 수정 되면 테스트 성공")
    @Test
    void givenBoardNum_whenUpdatingBoard_thenReturnsBoard() throws SQLException, ClassNotFoundException {
        //Given
        long num = 11;
        int count = 0;
        BoardVo board = null;

        //When
        String udateSQL = "update servlet_member.board set member_name = ?, title = ?, content = ?, file_name = ?, file_size = ?, updated_at = now() where num = ?";
        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(udateSQL);
        pstmt.setString(1, "hooney");
        pstmt.setString(2, "Lorem0 Ipsum");
        pstmt.setString(3, "Lorem0 Ipsum is simply dummy text of the printing and typesetting industry.");
        pstmt.setString(4, "test0.txt");
        pstmt.setInt(5, 10);
        pstmt.setLong(6, num);
        pstmt.executeUpdate();

        String selectSQL = "select count(*) as cnt from servlet_member.board where num = ?";
        @Cleanup PreparedStatement checkPstmt = conn.prepareStatement(selectSQL);
        checkPstmt.setLong(1, num);
        @Cleanup ResultSet rs = checkPstmt.executeQuery();

        if (rs.next()) {
            count = rs.getInt("cnt");
        }
        //then
        assertEquals(1, count);

    }

    @DisplayName("게시판 DB 테스트 - 게시글 삭제 테스트. 삭제 되면 테스트 실패")
    @Test
    void givenBoardNum_whenDeletingBoard_thenReturnsTrue() throws SQLException, ClassNotFoundException {
        //Given
        long num = 11;
        int count = 0;

        //When
        String sql = "delete from servlet_member.board where num = ?;";
        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setLong(1, num);
        pstmt.executeUpdate();

        String selectSQL = "select count(*) as cnt from servlet_member.board where num = ?";
        @Cleanup PreparedStatement checkPstmt = conn.prepareStatement(selectSQL);
        checkPstmt.setLong(1, 11);
        @Cleanup ResultSet rs = checkPstmt.executeQuery();

        if (rs.next()) {
            count = rs.getInt("cnt");
        }
        //Then
        assertEquals(0, count);//

    }

}
