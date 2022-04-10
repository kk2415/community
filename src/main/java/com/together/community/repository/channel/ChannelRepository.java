package com.together.community.repository.channel;

import com.together.community.domain.channel.Channel;
import java.util.List;

public interface ChannelRepository {

    public void save(Channel channel);
    public Channel findById(Long id);
    public List<Channel> findByMemberId(Long memberId);
    public List<Channel> findByName(String name);
    public List<Channel> findAll();
    public void delete(Long id);

}
