package com.member.servletprojectmembermanagement.service;

import com.member.servletprojectmembermanagement.dao.MemberDao;
import com.member.servletprojectmembermanagement.dto.MemberDto;
import com.member.servletprojectmembermanagement.vo.MemberVo;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@Log4j2
class MemberServiceImplTest {

    private MemberDao dao;
    private MemberDao mockDao;
    private MemberServiceImpl service;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private StringWriter stringWriter;
    private PrintWriter writer;
    private HttpSession mockSession;

    @BeforeEach
    void setUp() {
        this.dao = MemberDao.getInstance();
        this.mockDao = mock(MemberDao.class);
        this.service = MemberServiceImpl.getInstance(dao);
        this.mockRequest = mock(HttpServletRequest.class);
        this.mockResponse = mock(HttpServletResponse.class);
        this.stringWriter = new StringWriter();
        this.writer = new PrintWriter(stringWriter);
        this.mockSession = mock(HttpSession.class);

    }

    //날짜 포맷 형식 전역 변수 선언
    DateTimeFormatter simpleFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    @DisplayName("Service 테스트 - 회원 중복 검증. ID가 같으면 true 반환 테스트 성공")
    @Test
    void givenId_whenCheckingExistence_thenAppropriateResponse() throws IOException, SQLException, ClassNotFoundException {
        // Given
        String memberId = "hooney";
        when(mockRequest.getParameter("id")).thenReturn(memberId);
        when(mockResponse.getWriter()).thenReturn(writer);

        // When
        service.isExistId(mockRequest, mockResponse);

        // Then
        writer.flush();
        String responseString = stringWriter.toString();
        assertTrue(responseString.contains("\"result\":\"true\""));

    }

    @DisplayName("Service 테스트 - 회원 생성. 회원이 생성 되면 true 반환")
    @Test
    void givenMember_whenRegisteringExistence_thenAppropriateResponse() throws Exception {
        // Given
        MemberDto dto = new MemberDto();
        // 월 데이터를 나타내는 문자열 배열
        String[] months = new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

        // 이메일 도메인을 나타내는 문자열 배열
        String[] emailDomains = new String[] {"naver.com", "daum.net", "gmail.com", "nate.com"};

        // Request 설정
        when(mockRequest.getParameter("id")).thenReturn("hooney2");
        when(mockRequest.getParameter("password")).thenReturn("hooney1108");
        when(mockRequest.getParameter("name")).thenReturn("가길동");
        when(mockRequest.getParameter("gender")).thenReturn("남");
        when(mockRequest.getParameter("birthyy")).thenReturn("1990");
        when(mockRequest.getParameterValues("birthmm")).thenReturn(months);
        when(mockRequest.getParameter("birthdd")).thenReturn("02");
        when(mockRequest.getParameter("mail1")).thenReturn("hooney2");
        when(mockRequest.getParameterValues("mail2")).thenReturn(emailDomains);
        when(mockRequest.getParameter("phone")).thenReturn("01012341112");
        when(mockRequest.getParameter("zipcode")).thenReturn("34005");
        when(mockRequest.getParameter("addr1")).thenReturn("대전광역시 유성구 대덕대로1111번길 1-8");
        when(mockRequest.getParameter("addr2")).thenReturn("가나타운 1동 2호");
        when(mockRequest.getParameter("createdAt")).thenReturn(String.valueOf(LocalDateTime.now()));

        dto.setId(mockRequest.getParameter("id"));
        dto.setPassword(mockRequest.getParameter("password"));
        dto.setName(mockRequest.getParameter("name"));
        dto.setGender(mockRequest.getParameter("gender"));
        String year = mockRequest.getParameter("birthyy");
        String month = mockRequest.getParameterValues("birthmm")[0];
        String day = mockRequest.getParameter("birthdd");
        dto.setBirth(year + "/" + month + "/" + day);
        String mail1 = mockRequest.getParameter("mail1");
        String mail2 = mockRequest.getParameterValues("mail2")[0];
        dto.setEmail(mail1 + "@" + mail2);
        dto.setPhone(mockRequest.getParameter("phone"));
        dto.setZipcode(mockRequest.getParameter("zipcode"));
        dto.setAddr1(mockRequest.getParameter("addr1"));
        dto.setAddr2(mockRequest.getParameter("addr2"));
        LocalDateTime createdAt = LocalDateTime.parse(mockRequest.getParameter("createdAt"), formatter);
        dto.setCreatedAt(createdAt);

        // When
        MemberVo vo = MemberDto.toVo(dto); // dto to vo 변환
        boolean result = service.registerMember(mockRequest);

        log.info("MemberVo: {}", vo);
        log.info("Save Result: {}", result);

        // Then
        assertTrue(result);

    }

    @DisplayName("Service 테스트 - 로그인 성공하면 true 반환")
    @Test
    void givenMemberIdAndPassword_whenLogin_thenAppropriateResponse() throws IOException, SQLException, ClassNotFoundException {
        // Given
        String memberId = "hooney";
        String password = "hooney1108";
        String expectedName = "hooney";

        when(mockDao.findMemberByIdAndPassword(memberId, password)).thenReturn(true);
        when(mockDao.findMemberNameById(memberId)).thenReturn(expectedName);
        when(mockRequest.getParameter("id")).thenReturn(memberId);
        when(mockRequest.getParameter("password")).thenReturn(password);

        mockSession = mock(HttpSession.class);
        when(mockRequest.getSession()).thenReturn(mockSession);

        // When
        boolean result = service.login(mockRequest);

        // Then
        assertTrue(result);
        verify(mockSession).setAttribute("sessionMemberId", memberId);
        verify(mockSession).setAttribute("sessionMemberName", expectedName);
    }

    @DisplayName("Service 테스트 - 입력한 ID와 실제 회원 정보 조회 성공하면 true 반환")
    @Test
    void givenMemberId_whenGettingMember_thenAppropriateResponse() throws IOException, SQLException, ClassNotFoundException {
        // Given
        String memberId = "hooney";
        MemberDto expectedDto = new MemberDto(
                memberId,
                "hooney1108",
                "hooney",
                "남",
                "1990/01/01",
                "hooney@gmail.com",
                "01012341234",
                "34005",
                "대전광역시 유성구 대덕대로1111번길 1-8",
                "가나타운 1동 1호",
                LocalDateTime.parse("2023-08-23 15:01:09", simpleFormatter),
                null
        );

        MemberVo memberVo = MemberDto.toVo(expectedDto);

        when(mockDao.findMemberById(memberId)).thenReturn(memberVo);

        mockSession = mock(HttpSession.class);
        when(mockRequest.getSession()).thenReturn(mockSession);
        when(mockSession.getAttribute("sessionMemberId")).thenReturn(memberId);

        // When
        MemberDto actualDto = service.getMemberById(mockRequest);

        // Then
        assertNotNull(actualDto);
        assertEquals(expectedDto, actualDto);
    }

    @DisplayName("Service 테스트 - 회원 정보 수정 성공하면 true 반환 성공")
    @Test
    void givenMember_whenUpdatingMember_thenAppropriateResponse() throws IOException, SQLException, ClassNotFoundException {
        // Given
        String memberId = "hooney2";
        when(mockRequest.getSession()).thenReturn(mockSession);
        when(mockSession.getAttribute("sessionMemberId")).thenReturn(memberId);

        MemberDto expectedDto = new MemberDto(
                memberId,
                "hooney1108",
                "가길동",
                "남",
                "1990/01/02",
                "hooney2@naver.com",
                "01012341112",
                "34005",
                "대전광역시 유성구 대덕대로1111번길 1-8",
                "가나타운 1동 2호",
                LocalDateTime.parse("2023-08-23 17:18:02", simpleFormatter),
                null
        );

        // 월 데이터를 나타내는 문자열 배열
        String[] months = new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

        // 이메일 도메인을 나타내는 문자열 배열
        String[] emailDomains = new String[] {"naver.com", "daum.net", "gmail.com", "nate.com"};

        when(mockRequest.getParameter("password")).thenReturn("hooney1108");
        when(mockRequest.getParameter("name")).thenReturn("hooney2");
        when(mockRequest.getParameter("gender")).thenReturn("남");
        when(mockRequest.getParameter("birthyy")).thenReturn("1990");
        when(mockRequest.getParameterValues("birthmm")).thenReturn(months);
        when(mockRequest.getParameter("birthdd")).thenReturn("02");
        when(mockRequest.getParameter("mail1")).thenReturn("hooney2");
        when(mockRequest.getParameterValues("mail2")).thenReturn(emailDomains);
        when(mockRequest.getParameter("phone")).thenReturn("01012341112");
        when(mockRequest.getParameter("zipcode")).thenReturn("34005");
        when(mockRequest.getParameter("addr1")).thenReturn("대전광역시 유성구 대덕대로1111번길 1-8");
        when(mockRequest.getParameter("addr2")).thenReturn("가나타운 1동 2호");
        when(mockRequest.getParameter("createdAt")).thenReturn("2023-08-23 17:18:02");
        when(mockRequest.getParameter("updatedAt")).thenReturn(String.valueOf(LocalDateTime.now()));

        MemberVo memberVo = MemberDto.toVo(expectedDto);
        when(mockDao.update(memberVo)).thenReturn(true);

        // When
        boolean result = service.updateMember(mockRequest);

        // Then
        assertTrue(result);
    }


    @DisplayName("Service 테스트 - 회원 삭제 성공하면 true 반환 되고 테스트 성공")
    @Test
    void givenMember_whenDeletingMember_thenAppropriateResponse() throws IOException, SQLException, ClassNotFoundException {
        //Given
        String memberId = "hooney2";
        when(mockRequest.getSession()).thenReturn(mockSession);
        when(mockSession.getAttribute("sessionMemberId")).thenReturn(memberId);

        //When
        boolean result = service.deleteMember(mockRequest);

        //Then
        assertTrue(result);
    }
}