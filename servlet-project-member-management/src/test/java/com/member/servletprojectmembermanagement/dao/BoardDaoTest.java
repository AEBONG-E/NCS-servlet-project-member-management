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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("[DAO 로직] 테스트")
@Log4j2
class BoardDaoTest {

    private BoardDao boardDao;
    private DBConnection dbConnection;

    //날짜 포맷 형식 전역 변수 선언
    DateTimeFormatter simpleFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    @BeforeEach
    void setUp() {
        this.boardDao = BoardDao.getInstance();
        this.dbConnection = DBConnection.getInstance();
    }

    @DisplayName("게시판 DAO 테스트 - 게시글 생성 테스트. 생성 되면 true 반환 성공")
    @Test
    void givenBoard_whenSaving_thenReturnsTrue() throws SQLException, ClassNotFoundException {
        //Given
        MemberVo member = new MemberVo(
                "hooney",
                "hooney"
        );
        BoardVo board = new BoardVo(
                0L,
                member,
                "Lorem Ipsum6",
                "Lorem Ipsum6 is simply dummy text of the printing and typesetting industry.",
                0,
                "123.123.123.123",
                0,
                "test6.png",
                0L,
                LocalDateTime.now(),
                null
        );
        //when
        boolean savedBoard = boardDao.save(board);

        //then
        assertTrue(savedBoard);
    }

    @DisplayName("게시판 DAO 테스트 - 게시판 검색을 위한 게시판 갯수 조회 테스트 개수가 맞으면 true 반환 테스트 성공")
    @Test
    void givenItemsAndText_whenFinding_thenReturnsBoardListCount() throws SQLException, ClassNotFoundException {
        //Given
        String items = null;
        String text = null;
        int count = 0;

        //when
        int expectedCount = 5;
        count = boardDao.findBoardListCountByItemsAndByText(items, text);

        //then
        assertEquals(expectedCount, count);
    }

    @DisplayName("게시판 DAO 테스트 - 게시판 최근 게시물 부터 조회 페이징 테스트.")
    @Test
    void givenPageNumAndLimitAndItemsAndText_whenFindingList_thenReturnsBoardList() throws SQLException, ClassNotFoundException {
        //Given
        int pageNum = 1;
        int limit = 5;
        String items = "title";
        String text = "Lorem Ipsum5";

        //when
        List<BoardVo> boardList = boardDao.findList(pageNum, limit, items, text);

        //then
//        assertEquals(limit, boardList.size());  // 반환된 게시물 수 확인

//        // 정렬 확인: 가장 최근 게시물이 맨 앞에 오는지 확인
//        if (!boardList.isEmpty()) {
//            LocalDateTime prevTime = boardList.get(0).getCreatedAt();
//            for (BoardVo board : boardList) {
//                assertTrue(prevTime.compareTo(board.getCreatedAt()) >= 0);
//                prevTime = board.getCreatedAt();
//            }
//        }

        // 검색 기능 확인: 만약 items와 text가 설정되어 있다면, 해당 필드에 검색 텍스트가 포함되어 있는지 확인
        if (items != null && text != null) {
            for (BoardVo board : boardList) {
                String fieldValue;
                switch (items) {
                    case "title":
                        fieldValue = board.getTitle();
                        break;
                    case "content":
                        fieldValue = board.getContent();
                        break;
                    default:
                        throw new IllegalArgumentException("잘못된 검색 필드: " + items);
                }
                assertTrue(fieldValue.contains(text));
            }
        }
    }

    @DisplayName("게시판 DAO 테스트 - 게시글 조회 시 조회수 증가 테스트. 증가 시 테스트 성공")
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
        boardDao.updateBoardHitByNum(num);

        // Then
        // 증가된 hit 값을 가져와 비교
        int updatedHit = 0;
        @Cleanup PreparedStatement getUpdatedStmt = getConn.prepareStatement(getOriginalHitSql);
        getUpdatedStmt.setLong(1, num);
        @Cleanup ResultSet updatedRs = getUpdatedStmt.executeQuery();
        if (updatedRs.next()) {
            updatedHit = updatedRs.getInt("hit");
        }
        assertEquals(originalHit + 1, updatedHit);
    }

    @DisplayName("게시판 DAO 테스트 - 게시글 조회 테스트 게시글 번호와 데이터 일치하면 성공")
    @Test
    void givenBoardNum_whenFindingBoard_thenReturnsBoard() throws SQLException, ClassNotFoundException {
        //Given
        long num = 1;
        BoardVo board = null;
        String expectedTitle = "Lorem Ipsum";
        String expectedContent = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.";

        //When
        board = boardDao.findBoardByNum(num);

        //Then
        assertNotNull(board);  // board 객체가 null이 아닌지 확인
        assertEquals(expectedTitle, board.getTitle());  // 제목 확인
        assertEquals(expectedContent, board.getContent());  // 내용 확인

    }

    @DisplayName("게시판 DAO 테스트 - 게시글 수정 테스트. 수정 되면 테스트 성공")
    @Test
    void givenBoardNum_whenUpdatingBoard_thenReturnsBoard() throws SQLException, ClassNotFoundException {
        //Given
        long num = 6;
        int count = 0;
        MemberVo member = new MemberVo(
                "hooney",
                "hooney"
        );
        BoardVo board = new BoardVo(
                6L,
                member,
                "Lorem Ipsum1",
                "Lorem Ipsum1 is simply dummy text of the printing and typesetting industry.",
                0,
                "123.123.123.123",
                0,
                "test1.png",
                0L,
                null,
                LocalDateTime.now()
        );

        //when
        boolean updated = boardDao.update(board);

        //then
        assertTrue(updated);

    }

    @DisplayName("게시판 DAO 테스트 - 게시글 삭제 테스트. 삭제 되면 테스트 실패")
    @Test
    void givenBoardNum_whenDeletingBoard_thenReturnsTrue() throws SQLException, ClassNotFoundException {
        //Given
        long num = 6;
        int count = 0;

        //When
        boolean deletedBoard = boardDao.deleteByNum(num);

        //Then
        assertTrue(deletedBoard);

    }

}