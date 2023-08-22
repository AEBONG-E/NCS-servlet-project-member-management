package com.member.servletprojectmembermanagement.dao;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DB 연결 테스트")
@Log4j2
class DBConnectionTest {

    @DisplayName("[DB 테스트] - DB 연결 정상 테스트1")
    @Test
    void testDBConnection() {
        // Given
        DBConnection dbConnection = DBConnection.getInstance();

        // When
        Connection conn = null;
        try {
            conn = dbConnection.getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        // Then
        assertNotNull(conn, "DB connection success");
    }

    /**
     * SQLException 이 발생하지 않아서 테스트 실패 발생되면
     * DB 정상 연결됨
     */
    @DisplayName("[DB 테스트] - DB 연결 정상 테스트2")
    @Test
    public void testSQLException() {
        // Given
        DBConnection dbConnection = DBConnection.getInstance();

        // When & Then
        assertThrows(SQLException.class, dbConnection::getConnection);
    }
}