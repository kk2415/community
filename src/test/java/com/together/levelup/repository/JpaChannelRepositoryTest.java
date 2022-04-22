package com.together.levelup.repository;

import com.together.levelup.domain.channel.Channel;
import com.together.levelup.domain.channel.ChannelMember;
import com.together.levelup.domain.member.Gender;
import com.together.levelup.domain.member.Member;
import com.together.levelup.repository.channel.ChannelRepository;
import com.together.levelup.repository.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@SpringBootTest
@Transactional
class JpaChannelRepositoryTest {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void save() {
        Member member1 = Member.createMember("test0", "0000", "김경희",
                Gender.MALE, "970927", "010-2354-9960", null);
        memberRepository.save(member1);

        Channel channel = Channel.createChannel(member1, "모두모두 모여라 요리왕", 20L, "요리 친목도모");
        channelRepository.save(channel);
        Channel findChannel = channelRepository.findById(channel.getId());
        Assertions.assertThat(findChannel).isEqualTo(channel);
    }

    @Test
    void findByMemberId() {
        Member member1 = Member.createMember("test0",
                "0000", "김경희", Gender.MALE, "970927", "010-2354-9960", null);
        Member member2 = Member.createMember("test1",
                "0000", "이예지", Gender.FEMALE, "020509", "010-5874-3699", null);
        memberRepository.save(member1);
        memberRepository.save(member2);

        Channel channel = Channel.createChannel(member1,"모두모두 모여라 요리왕", 20L, "요리 친목도모");
        channelRepository.save(channel);

        ChannelMember channelMember1 = ChannelMember.createChannelMember(member1);
        ChannelMember channelMember2 = ChannelMember.createChannelMember(member2);
        channel.addMember(channelMember1, channelMember2);

        List<Channel> findChannels = channelRepository.findByMemberId(member1.getId());
        Assertions.assertThat(findChannels.size()).isEqualTo(1);
    }

    @Test
    void delete() {
        Member member1 = Member.createMember("test0",
                "0000", "김경희", Gender.MALE, "970927", "010-2354-9960", null);
        Member member2 = Member.createMember("test1",
                "0000", "이예지", Gender.FEMALE, "020509", "010-5874-3699", null);
        memberRepository.save(member1);
        memberRepository.save(member2);

        Channel channel = Channel.createChannel(member1, "모두모두 모여라 요리왕", 20L, "요리 친목도모");
        channelRepository.save(channel);

        channelRepository.delete(channel.getId());
        List<Channel> findChannels = channelRepository.findAll();
        Assertions.assertThat(findChannels.size()).isEqualTo(0);
    }

}