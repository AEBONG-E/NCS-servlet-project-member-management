package com.member.servletprojectmembermanagement.dao;

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

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("[DAO 로직] 테스트")
@Log4j2
public class MemberDaoTest {

    private MemberDao memberDao;
    private DBConnection dbConnection;

    @BeforeEach
    void setUp() {
        this.memberDao = MemberDao.getInstance();
        this.dbConnection = DBConnection.getInstance();
    }

    @DisplayName("DAO 테스트 - ID 중복 있으면 true 반환 테스트 성공")
    @Test
    void givenMemberId_whenIdExists_thenReturnsTrue() throws SQLException, ClassNotFoundException {
        //given
        int count = 0;
        String memberId = "hooney";

        //when
        String sql = "select count(*) as cnt from servlet_member.member where id = ?";
        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, memberId);
        @Cleanup ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            count = rs.getInt("cnt");
        }

        //then
        assertTrue(count >= 1);

    }

    @DisplayName("DAO 테스트 - ID 중복 없으면 true 반환 테스트 실패")
    @Test
    void givenMemberId_whenIdExists_thenReturnsFalse() throws SQLException, ClassNotFoundException {
        //given
        int count = 0;
        String memberId = "hooney";

        //when
        String sql = "select count(*) as cnt from servlet_member.member where id = ?";
        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, memberId);
        @Cleanup ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            count = rs.getInt("cnt");
        }

        //then
        assertFalse(count >= 1);

    }

    @DisplayName("DAO 테스트 - 회원 생성 테스트.")
    @Test
    void givenMember_whenSavingMember_thenReturnsTrue() throws SQLException, ClassNotFoundException {
        //given
        MemberVo member = new MemberVo();
        //when
        String insertSQL = "insert into servlet_member.member values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), null)";
        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(insertSQL);
        pstmt.setString(1, "test2");
        pstmt.setString(2, "test1234");
        pstmt.setString(3, "나길녀");
        pstmt.setString(4, "야");
        pstmt.setString(5, "19900103");
        pstmt.setString(6, "test2@gmail.com");
        pstmt.setString(7, "01012342222");
        pstmt.setString(8, "34005");
        pstmt.setString(9, "대전광역시 유성구 대덕대로1111번길 1-8");
        pstmt.setString(10, "가나타운 1동 2호");
        pstmt.executeUpdate();

        String selectSQL = "select count(*) as cnt from servlet_member.member where id = ?";
        @Cleanup PreparedStatement checkPstmt = conn.prepareStatement(selectSQL);
        checkPstmt.setString(1, "test");
        @Cleanup ResultSet rs = checkPstmt.executeQuery();

        int count = 0;
        /*
         회원이 정상적으로 생성 되었다면 count 값은 1로
         true 가 반환되며 테스트 성공
         */
        if (rs.next()) {
            count = rs.getInt("cnt");
        }

        //then
        assertEquals(1, count);
    }

    @DisplayName("DAO 테스트 - 로그인 테스트. ID, PW 모두 같다면 테스트 성공")
    @Test
    void givenMemberIdAndPassword_whenFindingMember_thenReturnsMember() throws SQLException, ClassNotFoundException {
        //given
        String memberId = "hooney2";
        String password = "hooney1108";
        MemberVo member = null;

        //when
        String sql = "select * from servlet_member.member where id = ? and password = ?";
        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, memberId);
        pstmt.setString(2, password);
        @Cleanup ResultSet rs = pstmt.executeQuery();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (rs.next()) {
            LocalDateTime createdAt = LocalDateTime.parse(rs.getString("created_at"), formatter);

            member = new MemberVo(
                    rs.getString("id"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("gender"),
                    rs.getString("birth"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("zipcode"),
                    rs.getString("addr1"),
                    rs.getString("addr2"),
                    createdAt,
                    null
            );
        }

        //then
        assertNotNull(member);
        assertEquals(memberId, member.getId());
        assertEquals(password, member.getPassword());
    }

    @DisplayName("DAO 테스트 - 회원 조회 테스트. ID 가 같다면 해당 회원 정보를 리턴")
    @Test
    void givenMemberId_whenFindingMember_thenReturnsMember() throws SQLException, ClassNotFoundException {
        //given
        String memberId = "test";
        MemberVo member = null;

        //when
        String sql = "select * from servlet_member.member where id = ?";
        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, memberId);
        @Cleanup ResultSet rs = pstmt.executeQuery();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (rs.next()) {
            LocalDateTime createdAt = LocalDateTime.parse(rs.getString("created_at"), formatter);

            member = new MemberVo(
                    rs.getString("id"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("gender"),
                    rs.getString("birth"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("zipcode"),
                    rs.getString("addr1"),
                    rs.getString("addr2"),
                    createdAt,
                    null
            );
        }

        //then
        assertNotNull(member);
        assertEquals(memberId, member.getId());
    }

    @DisplayName("DAO 테스트 - 회원 정보 수정 테스트.")
    @Test
    void givenMember_whenUpdatingMember_thenReturnsTrue() throws SQLException, ClassNotFoundException {
        //given
        MemberVo member = new MemberVo();
        //when
        String insertSQL = "update servlet_member.member set password = ?, name = ?, gender = ?, birth = ?, email = ?, " +
                "phone = ?, zipcode = ?, addr1 = ?, addr2 = ?, updated_at = now() where id = ?";
        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(insertSQL);
        pstmt.setString(1, "test1111");
        pstmt.setString(2, "가길동");
        pstmt.setString(3, "남");
        pstmt.setString(4, "19900102");
        pstmt.setString(5, "test@gmail.com");
        pstmt.setString(6, "01012341111");
        pstmt.setString(7, "34005");
        pstmt.setString(8, "대전광역시 유성구 대덕대로1111번길 1-8");
        pstmt.setString(9, "가나타운 1동 1호");
        pstmt.setString(10, "test");
        pstmt.executeUpdate();

        String selectSQL = "select count(*) as cnt from servlet_member.member where id = ?";
        @Cleanup PreparedStatement checkPstmt = conn.prepareStatement(selectSQL);
        checkPstmt.setString(1, "test");
        @Cleanup ResultSet rs = checkPstmt.executeQuery();

        int count = 0;
        /*
         회원이 정상적으로 수정 되었다면 count 값은 1로
         true 가 반환되며 테스트 성공
         */
        if (rs.next()) {
            count = rs.getInt("cnt");
        }

        //then
        assertEquals(1, count);
    }

    @DisplayName("DAO 테스트 - 회원 삭제 테스트.")
    @Test
    void givenMember_whenDeletingMember_thenReturnsTrue() throws SQLException, ClassNotFoundException {
        //given
        String memberId = "test2";

        //when
        String sql = "delete from servlet_member.member where id = ?";
        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, memberId);
        pstmt.executeUpdate();

        String selectSQL = "select count(*) as cnt from servlet_member.member where id = ?";
        @Cleanup PreparedStatement checkPstmt = conn.prepareStatement(selectSQL);
        checkPstmt.setString(1, "test2");
        @Cleanup ResultSet rs = checkPstmt.executeQuery();

        int count = 0;
        /*
         회원이 정상적으로 삭제 되었다면 count 값은 0 으로
         true 가 반환되며 테스트 성공
         */
        if (rs.next()) {
            count = rs.getInt("cnt");
        }

        //then
        assertEquals(0, count); // Expecting zero because the member should be deleted
    }


}

