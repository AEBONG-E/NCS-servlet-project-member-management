package com.member.servletprojectmembermanagement.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;
import java.util.Optional;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RippleVo extends BaseEntity {
    private long rippleId;
    private MemberVo member;
    private BoardVo board;
    private String content;
    private String ip;
    //로그인 상태가 아닐 때는 '댓글 삭제' 버튼이 생기지 않도록 하기 위한 설정
    private boolean isLogin;

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
        if (!super.equals(o)) return false;
        RippleVo rippleVo = (RippleVo) o;
        return rippleId == rippleVo.rippleId && isLogin == rippleVo.isLogin && Objects.equals(member, rippleVo.member) && Objects.equals(board, rippleVo.board) && Objects.equals(content, rippleVo.content) && Objects.equals(ip, rippleVo.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rippleId, member, board, content, ip, isLogin);
    }
}

