package com.member.servletprojectmembermanagement.dto;

import com.member.servletprojectmembermanagement.vo.BoardVo;
import com.member.servletprojectmembermanagement.vo.MemberVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {
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

    public static BoardDto BoardMapper(BoardVo vo) {
        if (vo == null) {
            return null;
        }
        BoardDto dto = new BoardDto();
        dto.setNum(vo.getNum());
        dto.setMember(vo.getMember());
        dto.setTitle(vo.getTitle());
        dto.setContent(vo.getContent());
        dto.setHit(vo.getHit());
        dto.setIp(vo.getIp());
        dto.setRippleCnt(vo.getRippleCnt());
        dto.setFileName(vo.getFileName());
        dto.setFileSize(vo.getFileSize());
        dto.setCreatedAt(vo.getCreatedAt());
        dto.setUpdatedAt(vo.getUpdatedAt());

        return dto;
    }

    public static List<BoardDto> BoardListMapper(List<BoardVo> voList) {
        return voList.stream().map(BoardDto::BoardMapper).collect(Collectors.toList());
    }

    public static BoardVo toVo(BoardDto dto) {
        return new BoardVo(
                dto.getNum(),
                dto.getMember(),
                dto.getTitle(),
                dto.getContent(),
                dto.getHit(),
                dto.getIp(),
                dto.getRippleCnt(),
                dto.getFileName(),
                dto.getFileSize(),
                dto.getCreatedAt(),
                dto.getUpdatedAt()
        );
    }

    public static List<BoardVo> toVoList(List<BoardDto> dtoList) {
        return dtoList.stream().map(BoardDto::toVo).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardDto)) return false;
        BoardDto boardDto = (BoardDto) o;
        return num == boardDto.num && Objects.equals(member, boardDto.member) && Objects.equals(title, boardDto.title) && Objects.equals(content, boardDto.content) && Objects.equals(hit, boardDto.hit) && Objects.equals(ip, boardDto.ip) && Objects.equals(rippleCnt, boardDto.rippleCnt) && Objects.equals(fileName, boardDto.fileName) && Objects.equals(fileSize, boardDto.fileSize) && Objects.equals(createdAt, boardDto.createdAt) && Objects.equals(updatedAt, boardDto.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(num, member, title, content, hit, ip, rippleCnt, fileName, fileSize, createdAt, updatedAt);
    }
}
