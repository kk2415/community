package com.levelup.api.dto.member;

import com.levelup.core.DateFormat;
import com.levelup.core.domain.member.Gender;
import com.levelup.core.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
public class CreateMemberResponse {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private Gender gender;
    private String birthday;
    private String phone;

    private CreateMemberResponse(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.name = member.getName();
        this.nickname = member.getNickname();
        this.gender = member.getGender();
        this.birthday = member.getBirthday().format(DateTimeFormatter.ofPattern(DateFormat.DATE_FORMAT));
        this.phone = member.getPhone();
    }

    public static CreateMemberResponse from(Member member) {
        return new CreateMemberResponse(member);
    }
}
