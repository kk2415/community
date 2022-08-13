package com.levelup.core.repository.member;


import com.levelup.core.domain.member.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

    @EntityGraph(attributePaths = {"emailAuth", "roles"})
    Optional<Member> findById(Long id);

    @EntityGraph(attributePaths = "emailAuth")
    Optional<Member> findByNickname(String nickname);

    @EntityGraph(attributePaths = "emailAuth")
    @Query("select cm.member from ChannelMember cm join cm.channel c where c.id = :channelId")
    List<Member> findByChannelId(@Param("channelId") Long channelId);
}