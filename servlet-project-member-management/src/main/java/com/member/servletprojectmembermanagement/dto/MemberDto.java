package com.member.servletprojectmembermanagement.dto;

import com.member.servletprojectmembermanagement.vo.MemberVo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@ToString
@NoArgsConstructor
public class MemberDto {
    private String id;
    private String password;
    private String name;
    private String gender;
    private String birth;
    private String email;
    private String phone;
    private String zipcode;     //우편번호
    private String addr1;
    private String addr2;   //상세주소
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MemberDto MemberMapper(MemberVo vo) {
        MemberDto dto = new MemberDto();
        dto.setId(vo.getId());
        dto.setPassword(vo.getPassword());
        dto.setName(vo.getName());
        dto.setGender(vo.getGender());
        dto.setBirth(vo.getBirth());
        dto.setEmail(vo.getEmail());
        dto.setPhone(vo.getPhone());
        dto.setZipcode(vo.getZipcode());
        dto.setAddr1(vo.getAddr1());
        dto.setAddr2(vo.getAddr2());
        dto.setCreatedAt(vo.getCreatedAt());
        dto.setUpdatedAt(vo.getUpdatedAt());
        return dto;
    }

    public static MemberVo toVo(MemberDto dto) {
        return new MemberVo(
                dto.getId(),
                dto.getPassword(),
                dto.getName(),
                dto.getGender(),
                dto.getBirth(),
                dto.getEmail(),
                dto.getPhone(),
                dto.getZipcode(),
                dto.getAddr1(),
                dto.getAddr2(),
                dto.getCreatedAt(),
                dto.getUpdatedAt()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberDto)) return false;
        MemberDto memberDto = (MemberDto) o;
        return Objects.equals(id, memberDto.id) && Objects.equals(password, memberDto.password) && Objects.equals(name, memberDto.name) && Objects.equals(gender, memberDto.gender) && Objects.equals(birth, memberDto.birth) && Objects.equals(email, memberDto.email) && Objects.equals(phone, memberDto.phone) && Objects.equals(zipcode, memberDto.zipcode) && Objects.equals(addr1, memberDto.addr1) && Objects.equals(addr2, memberDto.addr2) && Objects.equals(createdAt, memberDto.createdAt) && Objects.equals(updatedAt, memberDto.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, password, name, gender, birth, email, phone, zipcode, addr1, addr2, createdAt, updatedAt);
    }
}
