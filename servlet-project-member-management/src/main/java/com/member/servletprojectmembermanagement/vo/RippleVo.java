package com.member.servletprojectmembermanagement.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * 댓글 관련 정보를 가지는 도메인 클래스
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RippleVo {
    private long rippleId;
    private BoardVo board;
    private MemberVo member;
    private String content;
    private String ip;
    private LocalDateTime createdAt;

    //로그인 상태가 아닐 때는 '댓글 삭제' 버튼이 생기지 않도록 하기 위한 설정
    private boolean isLogin;

    public RippleVo(long rippleId, BoardVo board, MemberVo member, String content, String ip, LocalDateTime createdAt) {
        this.rippleId = rippleId;
        this.board = board;
        this.member = member;
        this.content = content;
        this.ip = ip;
        this.createdAt = createdAt;
    }

    public String getMemberId() {
        return member != null ? member.getId() : null;
    }

    public String getMemberName() {
        return member != null ? member.getName() : null;
    }

    public Optional<Long> getBoardNum() {
        return Optional.ofNullable(board).map(BoardVo::getNum);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RippleVo)) return false;
        RippleVo rippleVo = (RippleVo) o;
        return rippleId == rippleVo.rippleId && Objects.equals(member, rippleVo.member) && Objects.equals(board, rippleVo.board) && Objects.equals(content, rippleVo.content) && Objects.equals(ip, rippleVo.ip) && Objects.equals(createdAt, rippleVo.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rippleId, member, board, content, ip, createdAt);
    }
}

