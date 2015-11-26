package com.crawler.app;

import crawler.service.CrawlerService;
import crawler.service.CrawlerServiceImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Hello world!
 */
public class CrawlerApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerApp.class);
    private static String URL = "http://mail-archives.apache.org/mod_mbox/maven-users/";
    private static String DOWNLOAD_DIRECTORY = "/Users/dibyendu/source_code/DownloadedMail";
    private static String searchString = "2014";

    public static void main(String[] args) throws Exception {
        LOGGER.info("Starting Crawling App........");

        CrawlerService crawlerService = new CrawlerServiceImplementation();
        LOGGER.info("Collecting all Email Urls.................");
        List<String> urls = crawlerService.getAllUrl(URL, searchString);
        LOGGER.info("Start Downloading..........");
        crawlerService.download(urls, DOWNLOAD_DIRECTORY);

    }
}
