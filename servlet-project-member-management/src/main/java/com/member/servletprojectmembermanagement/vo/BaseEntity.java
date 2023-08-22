package com.member.servletprojectmembermanagement.vo;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 데이터 생성일자, 수정일자 등 공통 관련 정보를 가지는 도메인 클래스
 * (추후 생성자, 수정자 등 도 포함)
 */
@Data
@ToString
public class BaseEntity {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
