package com.levelup.recruit.crawler.scraper;

import com.levelup.recruit.crawler.connetion.JsoupConnectionMaker;
import com.levelup.recruit.domain.domain.Job;
import com.levelup.recruit.domain.domain.KakaoJob;
import com.levelup.recruit.domain.entity.enumeration.Company;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class KakaoScraper {

    private JsoupTemplate jsoupTemplate;

    public KakaoScraper(@Qualifier("KakaoConnectionMaker") JsoupConnectionMaker connectionMaker) {
        jsoupTemplate = JsoupTemplate.from(connectionMaker);
    }

    public List<Job> findJobs() {
        Elements jobList = jsoupTemplate.select("ul.list_jobs li");

        return jobList.stream().map(job -> {
            final String title = jsoupTemplate.selectSub(job, "a.link_jobs > h4.tit_jobs").text();
            final String url = Company.KAKAO.getUrl() + jsoupTemplate.selectSub(job, "a.link_jobs").attr("href");
            final String noticeEndDate = jsoupTemplate.selectSub(job, "dl.list_info > dt:contains(영입마감일) + dd").text();

            return KakaoJob.of(title, url, noticeEndDate);
        }).collect(Collectors.toUnmodifiableList());
    }
}