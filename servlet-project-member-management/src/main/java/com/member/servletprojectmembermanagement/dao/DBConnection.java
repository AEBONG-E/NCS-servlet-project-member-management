package com.member.servletprojectmembermanagement.dao;

import lombok.extern.log4j.Log4j2;

import java.sql.*;

/**
 * DB 연결 관련 정보를 가지는 클래스
 * DB 연결 관련 정보를 상수로 지정하고
 * 싱글톤 패턴으로 설계
 */
@Log4j2
public class DBConnection {

    // 싱글톤 객체
    private static DBConnection instance;

    // DB 정보 상수
    private static final String URL = "jdbc:mysql://localhost:3306/servlet_member";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    // private 생성자
    private DBConnection() {
    }

    // 싱글턴 인스턴스 생성
    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    // Connection 가져오기
    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Connection conn;

        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(URL, USER, PASSWORD);

        if (conn != null) {
            log.debug("DB Connection Information");
            log.debug("URL : " + URL);
            log.debug("USER : " + USER);
            log.debug("PASSWORD : " + PASSWORD);
        }

        return conn;
    }

    // PreparedStatement 가져오기
    public PreparedStatement getPreparedStatement(Connection conn, String sql) throws SQLException {
        return conn.prepareStatement(sql);
    }

    // ResultSet 가져오기
    public ResultSet getResultSet(PreparedStatement pstmt) throws SQLException {
        return pstmt.executeQuery();
    }
}

