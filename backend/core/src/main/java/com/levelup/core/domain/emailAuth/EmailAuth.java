package com.levelup.core.domain.emailAuth;

import com.levelup.core.domain.base.BaseTimeEntity;
import com.levelup.core.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Builder
@AllArgsConstructor
@Table(name = "email_auth")
@Entity
public class EmailAuth extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "email_auth_id")
    private Long id;

    @Column(nullable = false, name = "email_auth_type")
    @Enumerated(EnumType.STRING)
    private EmailAuthType authType;

    @Column(nullable = false)
    private String email;

    @Setter
    @Column(nullable = false)
    private String securityCode;

    @Setter
    @Column(nullable = false)
    private Boolean isAuthenticated;

    @Setter
    @Column(nullable = false)
    private LocalDateTime receivedDate;

    @Column(nullable = false)
    private LocalDateTime expireDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    protected EmailAuth() {}

    public void setMember(Member member) {
        this.member = member;
    }

    public static String createSecurityCode() {
        Random rand = new Random();
        StringBuilder securityCode = new StringBuilder();

        int idx = 0;
        while (idx < 6) {
            String randomInt = Integer.toString(rand.nextInt(10));
            if (!securityCode.toString().contains(randomInt)) {
                securityCode.append(randomInt);
                idx++;
            }
        }

        return securityCode.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof EmailAuth)) return false;
        return id != null && id.equals(((EmailAuth) obj).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
