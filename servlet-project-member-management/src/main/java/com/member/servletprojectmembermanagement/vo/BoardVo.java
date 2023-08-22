package com.member.servletprojectmembermanagement.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardVo extends BaseEntity {
    private long num;
    private MemberVo member;
    private String title;
    private String content;
    private Integer hit;
    private String ip;
    private Integer rippleCnt;  //게시판에 등록된 댓글의 갯수
    private String fileName;    //파일명
    private Integer fileSize;    //파일크기

    public String getMemberId() {
        return member != null ? member.getId() : null;
    }

    public String getMemberName() {
        return member != null ? member.getName() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardVo)) return false;
        BoardVo boardVo = (BoardVo) o;
        return num == boardVo.num && Objects.equals(member, boardVo.member) && Objects.equals(title, boardVo.title) && Objects.equals(content, boardVo.content) && Objects.equals(hit, boardVo.hit) && Objects.equals(ip, boardVo.ip) && Objects.equals(rippleCnt, boardVo.rippleCnt) && Objects.equals(fileName, boardVo.fileName) && Objects.equals(fileSize, boardVo.fileSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(num, member, title, content, hit, ip, rippleCnt, fileName, fileSize);
    }
}
