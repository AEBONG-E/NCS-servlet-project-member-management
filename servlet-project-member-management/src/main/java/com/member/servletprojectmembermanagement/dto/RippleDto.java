package com.member.servletprojectmembermanagement.dto;

import com.member.servletprojectmembermanagement.vo.BoardVo;
import com.member.servletprojectmembermanagement.vo.MemberVo;
import com.member.servletprojectmembermanagement.vo.RippleVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RippleDto {
    private long rippleId;
    private BoardVo board;
    private MemberVo member;
    private String content;
    private String ip;
    private LocalDateTime createdAt;

    //로그인 상태가 아닐 때는 '댓글 삭제' 버튼이 생기지 않도록 하기 위한 설정
    private boolean isLogin;

    public static RippleDto RippleMapper(RippleVo vo) {
        if (vo == null) {
            return null;
        }
        RippleDto dto = new RippleDto();
        dto.setRippleId(vo.getRippleId());
        dto.setBoard(vo.getBoard());
        dto.setMember(vo.getMember());
        dto.setContent(vo.getContent());
        dto.setIp(vo.getIp());
        dto.setCreatedAt(vo.getCreatedAt());
        return dto;
    }

    public static List<RippleDto> RippleListMapper(List<RippleVo> voList) {
        return voList.stream().map(RippleDto::RippleMapper).collect(Collectors.toList());
    }

    public static RippleVo toVo(RippleDto dto) {
        return new RippleVo(
                dto.getRippleId(),
                dto.getBoard(),
                dto.getMember(),
                dto.getContent(),
                dto.getIp(),
                dto.getCreatedAt()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RippleDto)) return false;
        RippleDto rippleDto = (RippleDto) o;
        return rippleId == rippleDto.rippleId && Objects.equals(board, rippleDto.board) && Objects.equals(member, rippleDto.member) && Objects.equals(content, rippleDto.content) && Objects.equals(ip, rippleDto.ip) && Objects.equals(createdAt, rippleDto.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rippleId, board, member, content, ip, createdAt);
    }
}
