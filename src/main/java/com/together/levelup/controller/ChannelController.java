package com.together.levelup.controller;

import com.together.levelup.api.SessionName;
import com.together.levelup.domain.member.Member;
import com.together.levelup.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/channel")
public class ChannelController {

    private final ChannelService channelService;

    @GetMapping("/project/create")
    public String createProject() {
        return "html/channel/createProjectChannel";
    }

    @GetMapping("/study/create")
    public String createStudy() {
        return "html/channel/createStudyChannel";
    }

    @GetMapping("/study/edit/{channelId}")
    public String editStudy() {
        return "html/channel/updateStudyChannel";
    }

    @GetMapping("/project/edit/{channelId}")
    public String editProejct() {
        return "html/channel/updateProjectChannel";
    }

    @GetMapping("/detail/{channelId}")
    public String detail(@PathVariable Long channelId,
                         @RequestParam(required = false, defaultValue = "1") Long page,
                         @RequestParam(required = false) String field,
                         @RequestParam(required = false) String query,
                         HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session.getAttribute(SessionName.SESSION_NAME) == null) {
            return "html/channel/detailChannel";
        }

        Member manager = channelService.findOne(channelId).getMember();

        Member findMember = (Member)session.getAttribute(SessionName.SESSION_NAME);
        if (findMember.getId().equals(manager.getId())) {
            return "html/channel/managerDetailChannel";
        }

        return "html/channel/detailChannel";
    }

    @GetMapping("/detail-description/{channelId}")
    public String detailDescription() {
        return "html/channel/detailChannelDescription";
    }

    @PostMapping("/create")
    public String createPost() {
        return "redirect:/";
    }

}
