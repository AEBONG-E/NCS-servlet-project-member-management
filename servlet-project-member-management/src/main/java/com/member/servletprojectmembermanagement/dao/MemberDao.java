package com.member.servletprojectmembermanagement.dao;

import com.member.servletprojectmembermanagement.vo.MemberVo;
import com.sun.xml.internal.bind.v2.TODO;
import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * DAO 에서는 복잡한 로직을 처리 하지 않고
 * query 와 관련된 작업처리 와 query 작업 처리시
 * 최소한의 파라미터 만 활용하는 것이 좋음.
 * 복잡한 로직 처리는 service 에서 처리해야 유지보수가 쉬우며
 * 에러 발생 시 명확하게 구분이 가능
 */
@Log4j2
public class MemberDao {

    /*
     * 메모리의 부하를 줄이도록 싱글톤 패턴으로 구현
     * 한번만 생성되면 계속 사용할 수 있음.
     */
    private static MemberDao instance;
    private final DBConnection dbConnection;

    public MemberDao() {
        this.dbConnection = DBConnection.getInstance();
    }

    public static MemberDao getInstance() {
        if (instance == null) {
            instance = new MemberDao();
        }
        return instance;
    }

    /*
     * @Cleanup 어노테이션을 활용.
     * try~catch 와 close() 작업 불필요.
     * 코드가 간결 해짐.
     * 회원 중복 체크
     * DB에 id 값이 존재 하는 경우 true.
     * 존재하지 않는 경우 false.
     */
    public boolean isExistId(String memberId) throws SQLException, ClassNotFoundException {
        log.debug("MemberDao: isExistId()");

        String sql = "select count(*) as cnt from servlet_member.member where id = ?";
        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, memberId);
        @Cleanup ResultSet rs = pstmt.executeQuery();

        int count = 1;
        if (rs.next()) {
            if (count == rs.getInt("cnt")) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /*
     * 회원 생성
     */
    public boolean save(MemberVo member) throws SQLException, ClassNotFoundException {
        log.debug("MemberDao: save()");
        String sql = "insert into servlet_member.member values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), null)";
        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, member.getId());
        pstmt.setString(2, member.getPassword());
        pstmt.setString(3, member.getName());
        pstmt.setString(4, member.getGender());
        pstmt.setString(5, member.getBirth());
        pstmt.setString(6, member.getEmail());
        pstmt.setString(7, member.getPhone());
        pstmt.setString(8, member.getZipcode());
        pstmt.setString(9, member.getAddr1());
        pstmt.setString(10, member.getAddr2());
        pstmt.executeUpdate();

        if (pstmt != null) {
            return true;
        } else
            return false;
    }

    /*
     * 로그인 처리
     */
    public boolean findMemberByIdAndPassword(String memberId, String password) throws SQLException, ClassNotFoundException {
        log.debug("MemberDao: findMemberByIdAndPassword()");
        String sql = "select * from servlet_member.member where id = ? and password = ?";
        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, memberId);
        pstmt.setString(2, password);
        @Cleanup ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * 회원 정보 조회
     */
    public MemberVo findMemberById(String memberId) throws SQLException, ClassNotFoundException {
        log.debug("MemberDao: findMemberById()");
        log.info("id: {}", memberId);
        MemberVo member = null;
        String sql = "select * from servlet_member.member where id = ?";
        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, memberId);
        @Cleanup ResultSet rs = pstmt.executeQuery();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (rs.next()) {
            LocalDateTime createdAt = LocalDateTime.parse(rs.getString("created_at"), formatter);
            member = new MemberVo(
                    memberId,
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
        assert member != null;
        log.info("MemberDao: {}", member.toString());
        return member;
    }

    /*
     * 회원 정보 업데이트
     */
    public boolean update(MemberVo member) throws SQLException, ClassNotFoundException {
        log.debug("MemberDao: update()");
        String sql = "update servlet_member.member set password = ?, name = ?, gender = ?, birth = ?, email = ?, " +
                "phone = ?, zipcode = ?, addr1 = ?, addr2 = ?, updated_at = now() where id = ?";
        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, member.getPassword());
        pstmt.setString(2, member.getName());
        pstmt.setString(3, member.getGender());
        pstmt.setString(4, member.getBirth());
        pstmt.setString(5, member.getEmail());
        pstmt.setString(6, member.getPhone());
        pstmt.setString(7, member.getZipcode());
        pstmt.setString(8, member.getAddr1());
        pstmt.setString(9, member.getAddr2());
        pstmt.setString(10, member.getId());
        pstmt.executeUpdate();

        if (pstmt != null) {
            return true;
        } else
            return false;
    }

    public boolean deleteByMemberId(String memberId) throws SQLException, ClassNotFoundException {
        log.debug("MemberDao: deleteByMemberId()");
        String sql = "delete from servlet_member.member where id = ?";
        @Cleanup Connection conn = dbConnection.getConnection();
        @Cleanup PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, memberId);
        pstmt.executeUpdate();

        if (pstmt != null) {
            return true;
        } else
            return false;
    }
}

