package com.member.servletprojectmembermanagement.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 게시판 관련 정보를 가지는 도메인 클래스
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardVo {
    private long num;
    private MemberVo member;
    private String title;
    private String content;
    private Integer hit;
    private String ip;
    private Integer rippleCnt;  //게시판에 등록된 댓글의 갯수
    private String fileName;    //파일명
    private Long fileSize;    //파일크기
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getMemberId() {
        return member != null ? member.getId() : null;
    }

    public String getMemberName() {
        return member != null ? member.getName() : null;
    }

    public BoardVo(long num) {
        this.num = num;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardVo)) return false;
        BoardVo boardVo = (BoardVo) o;
        return num == boardVo.num && Objects.equals(member, boardVo.member) && Objects.equals(title, boardVo.title) && Objects.equals(content, boardVo.content) && Objects.equals(hit, boardVo.hit) && Objects.equals(ip, boardVo.ip) && Objects.equals(rippleCnt, boardVo.rippleCnt) && Objects.equals(fileName, boardVo.fileName) && Objects.equals(fileSize, boardVo.fileSize) && Objects.equals(createdAt, boardVo.createdAt) && Objects.equals(updatedAt, boardVo.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(num, member, title, content, hit, ip, rippleCnt, fileName, fileSize, createdAt, updatedAt);
    }
}
