package com.member.servletprojectmembermanagement.dao;

import com.member.servletprojectmembermanagement.vo.BoardVo;
import com.member.servletprojectmembermanagement.vo.MemberVo;
import com.member.servletprojectmembermanagement.vo.RippleVo;
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

@DisplayName("댓글 DB 테스트")
@Log4j2
class RippleDBTest {

    private DBConnection dbConnection;

    //날짜 포맷 형식 전역 변수 선언
    DateTimeFormatter simpleFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    @BeforeEach
    void setUp() {
        this.dbConnection = DBConnection.getInstance();
    }

    @DisplayName("댓글 DB 테스트 - 댓글 생성 테스트. 생성 되면 True 반환 성공")
    @Test
    void givenRipple_whenSaving_thenReturnsTrue() throws SQLException, ClassNotFoundException {
        //Given
        int count = 0;
        RippleVo ripple = new RippleVo();

        //When
        String insertSQL = "insert into servlet_member.ripple values (null, ?, ?, ?, ?, ?, now())";
        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(insertSQL);
        pstmt.setLong(1, 1);
        pstmt.setString(2, "hooney");
        pstmt.setString(3, "hooney");
        pstmt.setString(4, "Lorem2 Ipsum2 has been the industry's standard dummy text ever since the 1500s");
        pstmt.setString(5, "0.0.0.0");
        pstmt.executeUpdate();

        String selectSQL = "select count(*) as cnt from servlet_member.ripple where rippleId = ?";
        @Cleanup PreparedStatement checkPstmt = conn.prepareStatement(selectSQL);
        checkPstmt.setLong(1, 1);
        @Cleanup ResultSet rs = checkPstmt.executeQuery();

        if (rs.next()) {
            count = rs.getInt("cnt");
        }

        //Then
        assertEquals(1, count);
    }

    @DisplayName("댓글 DB 테스트 - 게시글 번호로 댓글 목록 조회 테스트. 댓글 목록이 비어있지 않으면 False 반환 성공")
    @Test
    void givenBoardNum_whenFindingRipple_thenReturnsFalse() throws SQLException, ClassNotFoundException {
        //Given
        long boardNum = 1;
        List<RippleVo> rippleVoList = new ArrayList<>();

        //When
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
            rippleVoList.add(ripple);
        }
        //Then
        assertFalse(rippleVoList.isEmpty());
    }

    @DisplayName("댓글 DB 테스트 - 댓글 고유 번호로 게시글 번호를 찾아 게시글 조회 테스트. 조회 되면 True 반환 성공")
    @Test
    void givenRippleId_whenFindingBoardNum_thenReturnsTrue() throws SQLException, ClassNotFoundException {
        // Given
        long rippleId = 1;
        long expectedBoardNum = 2;  // 예상되는 게시글 번호
        long actualBoardNum = -1;  // 실제로 쿼리로 가져올 게시글 번호, 초기값을 -1 등으로 설정

        // When
        String sql = "select board_num from servlet_member.ripple where rippleId = ?";
        @Cleanup Connection getConn = dbConnection.getConnection();
        @Cleanup PreparedStatement getStmt = getConn.prepareStatement(sql);
        getStmt.setLong(1, rippleId);
        @Cleanup ResultSet rs = getStmt.executeQuery();

        // 조회된 결과가 있다면 actualBoardNum을 업데이트
        if (rs.next()) {
            actualBoardNum = rs.getLong("board_num");
        }

        // Then
//        assertNotEquals(-1, actualBoardNum, "Board number should have been updated from -1"); // 게시글 번호가 -1에서 업데이트되어야 함
        assertEquals(expectedBoardNum, actualBoardNum, "Board numbers should match"); // 예상 게시글 번호와 실제 게시글 번호가 일치해야 함
    }


    @DisplayName("댓글 DB 테스트 - 게시글 번호를 기준으로 저장된 댓글 개수 조회 테스트. 개수가 맞으면 True 반환 성공")
    @Test
    void givenBoardNum_whenCountingRipples_thenReturnsTrue() throws SQLException, ClassNotFoundException {
        // Given
        long boardNum = 1;  // 테스트할 게시글 번호
        int expectedCount = 2;  // 예상되는 댓글 개수 (실제 데이터에 따라 변경해야 할 수도 있습니다.)
        int actualCount = 0;  // 실제로 쿼리로 가져올 댓글 개수

        // When
        String sql = "select count(*) from servlet_member.ripple where board_num = ?";
        @Cleanup Connection getConn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = getConn.prepareStatement(sql);
        pstmt.setLong(1, boardNum);
        @Cleanup ResultSet rs = pstmt.executeQuery();

        // 조회된 결과가 있다면 actualCount를 업데이트
        if (rs.next()) {
            actualCount = rs.getInt(1);  // count(*) 결과는 첫 번째 컬럼에 위치합니다.
        }

        // Then
        assertEquals(expectedCount, actualCount, "The number of ripples should match the expected value");  // 예상 댓글 개수와 실제 댓글 개수가 일치해야 함
    }


    @DisplayName("댓글 DB 테스트 - 댓글 고유 번호로 댓글 삭제 테스트. 이전 댓글 null 이면 True 반환 성공")
    @Test
    void givenRippleId_whenDeletingRipple_thenReturnsTrue() throws SQLException, ClassNotFoundException {
        // Given
        long rippleId = 2; // 삭제할 댓글의 고유 번호

        // 댓글이 존재하는지 먼저 확인
        String checkSql = "select * from servlet_member.ripple where rippleId = ?";
        @Cleanup Connection checkConn = dbConnection.getConnection();
        @Cleanup PreparedStatement checkPstmt = checkConn.prepareStatement(checkSql);
        checkPstmt.setLong(1, rippleId);
        @Cleanup ResultSet checkRs = checkPstmt.executeQuery();
        boolean existsBeforeDelete = checkRs.next();

        // When
        String sql = "delete from servlet_member.ripple where rippleId = ?";
        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setLong(1, rippleId);
        int deletedRows = pstmt.executeUpdate(); // 성공적으로 삭제되면 삭제된 행의 수가 반환됩니다.

        // 댓글이 삭제된 후에도 존재하는지 확인
        @Cleanup ResultSet checkRsAfter = checkPstmt.executeQuery();
        boolean existsAfterDelete = checkRsAfter.next();

        // Then
//        assertTrue(existsBeforeDelete, "Ripple should exist before deletion"); // 삭제 전에는 댓글이 존재해야 합니다.
        assertEquals(1, deletedRows, "One row should be deleted"); // 정확히 하나의 댓글만 삭제되어야 합니다.
        assertFalse(existsAfterDelete, "Ripple should not exist after deletion"); // 삭제 후에는 댓글이 존재하지 않아야 합니다.
    }

}