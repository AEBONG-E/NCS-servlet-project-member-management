package com.member.servletprojectmembermanagement.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class BaseEntity {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
