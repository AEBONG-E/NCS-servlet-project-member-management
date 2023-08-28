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

@DisplayName("[DAO 로직] 테스트")
@Log4j2
class RippleDaoTest {
    private RippleDao rippleDao;
    private DBConnection dbConnection;

    //날짜 포맷 형식 전역 변수 선언
    DateTimeFormatter simpleFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    @BeforeEach
    void setUp() {
        this.rippleDao = RippleDao.getInstance();
        this.dbConnection = DBConnection.getInstance();
    }

    @DisplayName("댓글 DB 테스트 - 댓글 생성 테스트. 생성 되면 True 반환 성공")
    @Test
    void givenRipple_whenSaving_thenReturnsTrue() throws SQLException, ClassNotFoundException {
        //Given
        MemberVo member = new MemberVo(
                "hooney",
                "hooney"
        );
        BoardVo board = new BoardVo(
                1
        );
        RippleVo ripple = new RippleVo(
                1,
                board,
                member,
                "Lorem2 Ipsum2 has been the industry's standard dummy text ever since the 1500s",
                "0.0.0.0",
                LocalDateTime.now()
        );

        //When
        boolean savedRipple = rippleDao.save(ripple);

        //Then
        assertTrue(savedRipple);
    }

    @DisplayName("댓글 DB 테스트 - 게시글 번호로 댓글 목록 조회 테스트. 댓글 목록이 비어있지 않으면 False 반환 성공")
    @Test
    void givenBoardNum_whenFindingRipple_thenReturnsFalse() throws SQLException, ClassNotFoundException {
        //Given
        long boardNum = 1;
        List<RippleVo> rippleList = new ArrayList<>();

        //When
        rippleList = rippleDao.findRipplesByBoardNum(1);
        //Then
        assertFalse(rippleList.isEmpty());
    }

    @DisplayName("댓글 DB 테스트 - 댓글 고유 번호로 게시글 번호를 찾아 게시글 조회 테스트. 조회 되면 True 반환 성공")
    @Test
    void givenRippleId_whenFindingBoardNum_thenReturnsTrue() throws SQLException, ClassNotFoundException {
        // Given
        long rippleId = 3;
        long expectedBoardNum = 1; // 예상되는 게시글 번호

        // When
        long actualBoardNum = rippleDao.findBoardByRippleId(rippleId); // 실제로 조회된 게시글 번호

        // Then
        assertEquals(expectedBoardNum, actualBoardNum, "The board number should match the expected value"); // 조회된 게시글 번호와 예상되는 게시글 번호가 일치해야 함
    }



    @DisplayName("댓글 DB 테스트 - 게시글 번호를 기준으로 저장된 댓글 개수 조회 테스트. 개수가 맞으면 True 반환 성공")
    @Test
    void givenBoardNum_whenCountingRipples_thenReturnsTrue() throws SQLException, ClassNotFoundException {
        // Given
        long boardNum = 1;  // 테스트할 게시글 번호
        int expectedCount = 2;  // 예상되는 댓글 개수

        // When
        int actualCount = rippleDao.findRippleCountByBoardNum(boardNum);  // 실제로 조회된 댓글 개수

        // Then
        assertEquals(expectedCount, actualCount, "The count of ripples should match the expected value");
    }


    @DisplayName("댓글 DB 테스트 - 댓글 고유 번호로 댓글 삭제 테스트. 이전 댓글 null 이면 True 반환 성공")
    @Test
    void givenRippleId_whenDeletingRipple_thenReturnsTrue() throws SQLException, ClassNotFoundException {
        // Given
        long rippleId = 3; // 삭제할 댓글의 고유 번호

        // 댓글이 존재하는지 먼저 확인
        List<RippleVo> rippleList = rippleDao.findRipplesByBoardNum(rippleId);
//        assertNotNull(rippleList, "Ripple should exist before deletion");

        // When
        boolean isDeleted = rippleDao.deleteRippleByRippleId(rippleId);

        // Then
        assertTrue(isDeleted, "Ripple should be successfully deleted");

        // 다시 댓글을 조회해 본다.
        List<RippleVo> deletedRippleList = rippleDao.findRipplesByBoardNum(rippleId);
//        assertNull(deletedRippleList, "Ripple should be null after deletion");
    }
}