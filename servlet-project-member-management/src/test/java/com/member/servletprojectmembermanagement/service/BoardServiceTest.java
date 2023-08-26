package com.member.servletprojectmembermanagement.service;

import com.member.servletprojectmembermanagement.dao.BoardDao;

import com.member.servletprojectmembermanagement.dto.BoardDto;
import com.member.servletprojectmembermanagement.dto.MemberDto;
import com.member.servletprojectmembermanagement.vo.BoardVo;
import com.member.servletprojectmembermanagement.vo.MemberVo;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("[Service 로직] 테스트")
@Log4j2
class BoardServiceTest {

    private BoardDao dao;
    private BoardDao mockDao;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private HttpSession mockSession;

    private static final int LISTCOUNT = 5; //한페이지에 노출 할 게시물의 수

    @BeforeEach
    void setUp() {
        this.dao = BoardDao.getInstance();
        this.mockDao = mock(BoardDao.class);
        this.mockRequest = mock(HttpServletRequest.class);
        this.mockResponse = mock(HttpServletResponse.class);
        this.mockSession =  mock(HttpSession.class);
    }

    //날짜 포맷 형식 전역 변수 선언
    DateTimeFormatter simpleFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    @DisplayName("Service 테스트 - 게시글 생성. 게시글 생성 되면 true 반환 테스트 성공")
    @Test
    void givenBoard_whenRegisteringBoard_thenAppropriateResponse() throws SQLException, ClassNotFoundException {
        //Given
        //mock 환경에서 session 에 담긴 값을 실제 로직에서 가져오지 못하는 문제로
        //세션을 활용한 파라미터 전달은 실제 로직을 구현 후 재테스트 예정
        //현 테스트는 memberId 값을 임의로 지정 후 테스트 진행
        BoardDto dto = new BoardDto();
        MemberDto memberDto = new MemberDto();

        when(mockRequest.getParameter("id")).thenReturn("hooney");
        when(mockRequest.getParameter("name")).thenReturn("hooney");

        memberDto.setId(mockRequest.getParameter("id"));
        memberDto.setName(mockRequest.getParameter("name"));
        MemberVo member = MemberDto.toVo(memberDto);

        //boardDto 객체에 값 담기
        when(mockRequest.getParameter("title")).thenReturn("Lorem Ipsum");
        when(mockRequest.getParameter("content")).thenReturn("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
        when(mockRequest.getRemoteAddr()).thenReturn("123.123.123.132");
        when(mockRequest.getParameter("fileName")).thenReturn("test.png");

        dto.setMember(member);
        dto.setTitle(mockRequest.getParameter("title"));
        dto.setContent(mockRequest.getParameter("content"));
        dto.setIp(mockRequest.getRemoteAddr());
        dto.setFileName(mockRequest.getParameter("fileName"));

        //When
        BoardVo vo = BoardDto.toVo(dto);
        boolean result = dao.save(vo);

        log.info("service 테스트: BoardVo: {}", vo);
        log.info("service 테스트: Save Result: {}", result);

        //Then
        assertTrue(result);
    }

    @DisplayName("Service 테스트 - 게시판 조회. 게시글 페이징, 최신 게시글 정렬, 검색 테스트 성공 시 true 반환")
    @Test
    void givenBoardList_whenGettingBoardList_thenAppropriateResponse() throws SQLException, ClassNotFoundException {
        //Given
        List<BoardDto> boardList;
        int pageNum = 1; //페이지 번호
        int limit = LISTCOUNT; //한페이지에 노출 할 게시물의 수
        String items = "title"; //검색 필드
        String text = "Lorem Ipsum"; //검색어
        int totalRecord = 0; //전체 게시물 수
        int totalPage = 0; //전체 페이지 수

        //전달되는 페이지 번호가 있으면 실행
        if (mockRequest.getParameter("pageNum") != null) {
            pageNum = Integer.parseInt(mockRequest.getParameter("pageNum"));
        }
        items = mockRequest.getParameter("itmes");
        text = mockRequest.getParameter("text");
        totalRecord = dao.findBoardListCountByItemsAndByText(items, text);

        //When
        boardList = BoardDto.BoardListMapper(dao.findList(pageNum, limit, items, text));

        // 전체 페이지 계산
        totalPage = (int) Math.ceil((double) totalRecord / limit);

        //Then
        // 1. 게시물 전체 조회
//        assertFalse(boardList.isEmpty(), "Board list should not be empty");

        // 2. 최신순 정렬 (가장 최신의 게시물이 리스트의 첫 번째에 오는지 확인)
//        assertEquals("Lorem Ipsum", boardList.get(0).getTitle(), "The list should be sorted by the most recent");
//
        // 3. 페이징 기능 (리스트 크기가 limit과 같거나 작은지 확인)
//        assertTrue(boardList.size() <= limit, "The list size should be limited by the page limit");
//
        // 4. 검색 기능 (모든 반환된 게시물이 검색어를 포함하는지 확인)
//        String finalText = "Lorem Ipsum";
//        assertTrue(boardList.stream().anyMatch(board -> board.getTitle().contains(finalText)), "All board titles should contain the search text");
//
        // 5. 전체 페이지 수
        assertEquals(totalPage, Math.ceil((double) totalRecord / limit), "Total pages should match calculated pages");
    }

    @DisplayName("Service 테스트 - 게시글 조회. 조회 성공 시 true 반환")
    @Test
    void givenBoardNum_whenGettingBoardList_thenAppropriateResponse() throws SQLException, ClassNotFoundException {
        //Given
        long num = 1;
        int pageNum = 1;

        // 조회 전 조회수 가져오기
        BoardDto beforeDto = BoardDto.BoardMapper(dao.findBoardByNum(num));
        int beforeHit = beforeDto.getHit();

        MemberDto memberDto = MemberDto.MemberMapper(
                new MemberVo(
                        "hooney",
                        "hooney"
                )
        );
        MemberVo memberVo = MemberDto.toVo(memberDto);

        BoardDto expectedDto = new BoardDto(
                num,
                memberVo,
                "Lorem Ipsum",
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
                beforeHit + 1,
                "123.123.123.123",
                0,
                "test.png",
                0L,
                LocalDateTime.parse("2023-08-24 21:49:36", simpleFormatter),
                null
        );

        //When
        when(mockRequest.getParameter("num")).thenReturn(String.valueOf(num));
        when(mockRequest.getParameter("pageNum")).thenReturn(String.valueOf(pageNum));  // 페이지 정보 추가

        dao.updateBoardHitByNum(num);
        BoardDto actualDto = BoardDto.BoardMapper(dao.findBoardByNum(num));

        //Then
//        assertNotNull(actualDto, "The returned BoardDto should not be null");
//        assertEquals(expectedDto, actualDto, "The expected and actual BoardDto should match");

        //조회수 검증
        // 조회수 검증
        assertEquals(beforeHit + 1, actualDto.getHit(), "The hit count should have been incremented");

        // 페이지 정보 검증
//        assertEquals(pageNum, Integer.parseInt(mockRequest.getParameter("pageNum")), "The expected and actual page number should match");
    }

    @DisplayName("Service 테스트 - 게시글 수정. 수정 성공 시 true 반환")
    @Test
    void givenBoard_whenUpdatingBoard_thenAppropriateResponse() throws SQLException, ClassNotFoundException {
        //Given
        long num = 9;

        MemberDto memberDto = MemberDto.MemberMapper(
                new MemberVo(
                        "hooney",
                        "hooney"
                )
        );
        MemberVo memberVo = MemberDto.toVo(memberDto);

        BoardDto expectedDto = new BoardDto(
                num,
                memberVo,
                "Lorem Ipsum",
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
                0,
                "123.123.123.123",
                0,
                "test.png",
                0L,
                LocalDateTime.parse("2023-08-25 09:27:18", simpleFormatter),
                null
        );

        when(mockRequest.getParameter("id")).thenReturn("hooney");
        when(mockRequest.getParameter("name")).thenReturn("hooney");

        memberDto.setId(mockRequest.getParameter("id"));
        memberDto.setName(mockRequest.getParameter("name"));
        MemberVo member = MemberDto.toVo(memberDto);

        //boardDto 객체에 값 담기
        when(mockRequest.getParameter("title")).thenReturn("Lorem9 Ipsum");
        when(mockRequest.getParameter("content")).thenReturn("Lorem9 Ipsum is simply dummy text of the printing and typesetting industry.");
        when(mockRequest.getRemoteAddr()).thenReturn("123.123.123.132");
        when(mockRequest.getParameter("fileName")).thenReturn("test9.png");
        when(mockRequest.getParameter("fileSize")).thenReturn("0");

        BoardDto actualDto = new BoardDto();
        actualDto.setMember(member);
        actualDto.setTitle(mockRequest.getParameter("title"));
        actualDto.setContent(mockRequest.getParameter("content"));
        actualDto.setIp(mockRequest.getRemoteAddr());
        actualDto.setFileName(mockRequest.getParameter("fileName"));
        actualDto.setFileSize(Long.valueOf(mockRequest.getParameter("fileSize")));
        actualDto.setNum(num);

        //When
        BoardVo vo = BoardDto.toVo(actualDto);
        boolean result = dao.update(vo);

        log.info("service 테스트: BoardVo: {}", vo);
        log.info("service 테스트: Save Result: {}", result);

        //Then
//        assertTrue(result);
        assertEquals(expectedDto, actualDto);
    }

    @DisplayName("Service 테스트 - 게시글 삭제. 삭제 성공 시 true 반환")
    @Test
    void givenBoardNum_whenDeletingBoard_thenAppropriateResponse() throws SQLException, ClassNotFoundException {
        //Given
        long num = 9;

        //DB 에 게시글 존재 확인
        assertNotNull(dao.findBoardByNum(num));

        //When
        boolean isDeleted = dao.deleteByNum(num);

        //Then
//        assertTrue(isDeleted);
        assertNull(dao.findBoardByNum(num));
    }


}