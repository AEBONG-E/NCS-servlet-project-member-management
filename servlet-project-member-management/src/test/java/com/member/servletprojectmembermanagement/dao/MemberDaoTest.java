package com.member.servletprojectmembermanagement.dao;

import com.member.servletprojectmembermanagement.vo.MemberVo;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;

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
        String memberId = "hooney";

        //when
        boolean exists = memberDao.isExistId(memberId);

        //then
        assertTrue(exists);

    }

    @DisplayName("DAO 테스트 - ID 중복 없으면 true 반환 테스트 실패")
    @Test
    void givenMemberId_whenIdExists_thenReturnsFalse() throws SQLException, ClassNotFoundException {
        //given
        String memberId = "hooney";

        //when
        boolean exists = memberDao.isExistId(memberId);

        //then
        assertFalse(exists);

    }

    @DisplayName("DAO 테스트 - 회원 생성 테스트.")
    @Test
    void givenMember_whenSavingMember_thenReturnsTrue() throws SQLException, ClassNotFoundException {
        //given
        MemberVo member = new MemberVo(
                "hooney3",
                "hooney1108",
                "hooney3",
                "남",
                "19900102",
                "hooney@gmail.com",
                "01012341113",
                "34005",
                "대전광역시 유성구 대덕대로1111번길 1-8",
                "가나타운 1동 3호",
                LocalDateTime.now(),
                null
        );

        //when
        boolean savedMember = memberDao.save(member);

        //then
        assertTrue(savedMember);
    }

    @DisplayName("DAO 테스트 - 로그인 테스트. ID, PW 모두 같다면 테스트 성공")
    @Test
    void givenMemberIdAndPassword_whenFindingMember_thenReturnsMember() throws SQLException, ClassNotFoundException {
        //given
        String memberId = "hooney2";
        String password = "hooney1108";
        MemberVo member = null;

        //when
        boolean exists = memberDao.findMemberByIdAndPassword(memberId, password);

        //then
        assertTrue(exists);
    }

    @DisplayName("DAO 테스트 - 회원 조회 테스트. ID 가 같다면 해당 회원 정보를 리턴")
    @Test
    void givenMemberId_whenFindingMember_thenReturnsMember() throws SQLException, ClassNotFoundException {
        //given
        String memberId = "hooney";

        //when
        MemberVo findedMember = memberDao.findMemberById(memberId);

        //then
        assertNotNull(findedMember);
        assertEquals(memberId, findedMember.getId());
    }

    @DisplayName("DAO 테스트 - 회원 정보 수정 테스트.")
    @Test
    void givenMember_whenUpdatingMember_thenReturnsTrue() throws SQLException, ClassNotFoundException {
        //given
        MemberVo member = new MemberVo(
                "hooney",
                "hooney1108",
                "hooney",
                "남",
                "19900101",
                "hooney@gmail.com",
                "01012341234",
                "34005",
                "대전광역시 유성구 대덕대로1111번길 1-8",
                "가나타운 2동 1호",
                null,
                LocalDateTime.now()
        );

        //when
        boolean updatedMember = memberDao.update(member);

        //then
        assertTrue(updatedMember);
    }

    @DisplayName("DAO 테스트 - 회원 삭제 테스트.")
    @Test
    void givenMember_whenDeletingMember_thenReturnsTrue() throws SQLException, ClassNotFoundException {
        //given
        String memberId = "hooney3";

        //when
        boolean deletedMember = memberDao.deleteByMemberId(memberId);

        //then
        assertTrue(deletedMember);
    }


}

