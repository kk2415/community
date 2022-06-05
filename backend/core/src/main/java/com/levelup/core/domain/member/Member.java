package com.levelup.core.domain.member;

import com.levelup.core.domain.channel.Channel;
import com.levelup.core.domain.channel.ChannelMember;
import com.levelup.core.domain.comment.Comment;
import com.levelup.core.domain.file.UploadFile;
import com.levelup.core.domain.notice.Notice;
import com.levelup.core.domain.post.Post;
import com.levelup.core.domain.qna.Qna;
import com.levelup.core.domain.vote.Vote;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "email")
    private String email;

    private String password;
    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String birthday;
    private String phone;

    @Embedded
    private UploadFile uploadFile;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @Enumerated(EnumType.STRING)
    private Authority authority;

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

    @OneToMany(mappedBy = "waitingMember")
    private List<ChannelMember> channelWaitingMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Channel> channels = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Notice> notices = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Qna> qna = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Vote> votes;

    //==생성 메서드==//
    public static Member createMember(String email, String password, String name,
                                      Gender gender, String birthday, String phone, UploadFile uploadFile) {
        Member member = new Member();
        member.setEmail(email);
        member.setPassword(password);
        member.setName(name);
        member.setGender(gender);
        member.setBirthday(birthday);
        member.setPhone(phone);
        member.setAuthority(Authority.NORMAL);
        member.setDateCreated(LocalDateTime.now());
        member.setUploadFile(uploadFile);
        return member;
    }

    public static Member createMember(Authority authority, String email, String password, String name,
                                      Gender gender, String birthday, String phone, UploadFile uploadFile) {
        Member member = new Member();
        member.setEmail(email);
        member.setPassword(password);
        member.setName(name);
        member.setGender(gender);
        member.setBirthday(birthday);
        member.setPhone(phone);
        member.setAuthority(authority);
        member.setDateCreated(LocalDateTime.now());
        member.setUploadFile(uploadFile);
        return member;
    }
}