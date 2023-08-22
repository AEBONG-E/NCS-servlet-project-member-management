package com.member.servletprojectmembermanagement.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 회원 관련 정보를 가지는 도메인 클래스
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberVo {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberVo)) return false;
        MemberVo memberVo = (MemberVo) o;
        return Objects.equals(id, memberVo.id) && Objects.equals(password, memberVo.password) && Objects.equals(name, memberVo.name) && Objects.equals(gender, memberVo.gender) && Objects.equals(birth, memberVo.birth) && Objects.equals(email, memberVo.email) && Objects.equals(phone, memberVo.phone) && Objects.equals(zipcode, memberVo.zipcode) && Objects.equals(addr1, memberVo.addr1) && Objects.equals(addr2, memberVo.addr2) && Objects.equals(createdAt, memberVo.createdAt) && Objects.equals(updatedAt, memberVo.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, password, name, gender, birth, email, phone, zipcode, addr1, addr2, createdAt, updatedAt);
    }
}
