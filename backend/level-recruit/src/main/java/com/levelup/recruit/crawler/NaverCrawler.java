package com.levelup.recruit.crawler;

import com.levelup.recruit.crawler.scraper.NaverScraper;
import com.levelup.recruit.domain.domain.Job;
import com.levelup.recruit.domain.enumeration.Company;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component("NaverCrawler")
public class NaverCrawler implements Crawler {

    private final NaverScraper naverScraper;

    @Override
    public Company getCompany() {
        return Company.NAVER;
    }

    @Override
    public List<Job> crawling() {
        return naverScraper.findJobs();
    }
}
