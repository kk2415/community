package com.levelup.api.dto.request.member;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.levelup.api.dto.business.member.MemberDto;
import com.levelup.core.domain.file.UploadFile;
import com.levelup.core.domain.member.Gender;
import com.levelup.core.domain.member.Member;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;

@Getter
public class MemberRequest {

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String password;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String nickname;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthday;

    @NotNull
    private String phone;

    @NotNull
    private UploadFile uploadFile;

    protected MemberRequest() {}

    private MemberRequest(String email,
                          String password,
                          String name,
                          String nickname,
                          Gender gender,
                          LocalDate birthday,
                          String phone,
                          UploadFile uploadFile) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.gender = gender;
        this.birthday = birthday;
        this.phone = phone;
        this.uploadFile = uploadFile;
    }

    static public MemberRequest of(
            String email,
            String password,
            String name,
            String nickname,
            Gender gender,
            LocalDate birthday,
            String phone,
            UploadFile uploadFile
    ) {
        return new MemberRequest(email, password, name, nickname, gender, birthday, phone, uploadFile);
    }

    public MemberDto toDto() {
        return MemberDto.builder()
                .memberId(null)
                .email(email)
                .password(password)
                .name(name)
                .nickname(nickname)
                .gender(gender)
                .birthday(birthday)
                .phone(phone)
                .profileImage(uploadFile)
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .nickname(nickname)
                .gender(gender)
                .birthday(birthday)
                .phone(phone)
                .profileImage(uploadFile)
                .articles(new ArrayList<>())
                .comments(new ArrayList<>())
                .channelMembers(new ArrayList<>())
                .roles(new ArrayList<>())
                .build();
    }
}