package com.member.servletprojectmembermanagement.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

/**
 * 회원 관련 정보를 가지는 도메인 클래스
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberVo extends BaseEntity {
    private String id;
    private String password;
    private String name;
    private String gender;
    private String birth;
    private String mail;
    private String phone;
    private String zipcode;     //우편번호
    private String addr1;
    private String addr2;   //상세주소

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberVo)) return false;
        MemberVo memberVo = (MemberVo) o;
        return Objects.equals(id, memberVo.id) && Objects.equals(password, memberVo.password) && Objects.equals(name, memberVo.name) && Objects.equals(gender, memberVo.gender) && Objects.equals(birth, memberVo.birth) && Objects.equals(mail, memberVo.mail) && Objects.equals(phone, memberVo.phone) && Objects.equals(zipcode, memberVo.zipcode) && Objects.equals(addr1, memberVo.addr1) && Objects.equals(addr2, memberVo.addr2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, password, name, gender, birth, mail, phone, zipcode, addr1, addr2);
    }
}
