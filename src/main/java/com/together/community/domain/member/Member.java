package com.together.community.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.together.community.domain.Comment;
import com.together.community.domain.Post;
import com.together.community.domain.channel.Channel;
import com.together.community.domain.channel.ChannelMember;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "email_domain")
    private String emailDomain;

    private String password;
    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String birthday;
    private String phone;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    /*
    * cascade = CascadeType.ALL : member를 persist()하면 member랑 맵핑된 post도 같이 영속화된다.
    * 하지만 postRepository를 따로 만들어서 em.persist를 할꺼라서 여기서 post를 persist 안해도 된다.
    * */
    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<ChannelMember> channelMembers = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
    private Channel channel;

    //==생성 메서드==//
    public static Member createMember(String emailId, String emailDomain, String password, String name,
                                      Gender gender, String birthday, String phone) {
        Member member = new Member();
        member.setEmailId(emailId);
        member.setEmailDomain(emailDomain);
        member.setPassword(password);
        member.setName(name);
        member.setGender(gender);
        member.setBirthday(birthday);
        member.setPhone(phone);
        member.setAuthority(Authority.NORMAL);
        member.setDateCreated(LocalDateTime.now());
        return member;
    }

}
