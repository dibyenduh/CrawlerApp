package crawler.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dibyendu on 10/11/15.
 */
public class CrawlerServiceImplementation implements CrawlerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerServiceImplementation.class);

    public List<String> getAllUrl(String baseUrl, String searchString) throws IOException {
        LOGGER.info("Executing getAllUrl().......");
        Document htmlDoc = (Jsoup.connect(baseUrl)).get();
        Elements hrefElements = htmlDoc.select("a");
        hrefElements = filterAllHrefBasedOnSearchString(hrefElements, searchString);
        List<String> absUrls = new ArrayList<String>();
        for (Element e : hrefElements){
            String absUrl = e.attr("abs:href");
            Document newHtmlDoc = (Jsoup.connect(absUrl)).get();
            Elements newHrefElement = newHtmlDoc.select("a");
            List<Element> relativeUrls = filterRelativeUrlsOnSearchString(newHrefElement, searchString);

            for(Element element : relativeUrls){
                absUrls.add(element.attr("abs:href"));
            }
        }

        return absUrls;
    }

    public Boolean download(List<String> urlsList, String downloadDir) throws Exception {
        try {
            int number = 1;
            for (String url : urlsList) {
                File dir = new File(downloadDir);
                if (!dir.exists()){
                    dir.mkdir();
                }

                File file = new File(dir, "Email_" + (number++)
                        + ".txt");
                saveToFile(url, file);
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            LOGGER.info("Exception at the time of Downloading", e);
            return Boolean.FALSE;
        }
    }

    private void saveToFile(String url, File file) throws IOException {
        URL emailUrl;
        HttpURLConnection connection = null;
        BufferedReader input = null;
        OutputStream writer = new FileOutputStream(file);
        try {
            emailUrl = new URL(url);
            LOGGER.info("Opening HTTP connection for Downloading");
            connection = (HttpURLConnection)emailUrl.openConnection();
            input = new BufferedReader( new InputStreamReader( connection.getInputStream()));
            int c;
            while( ( c = input.read() ) != -1 ) {
                writer.write((char) c);
            }
            LOGGER.info("Download Completed");

        } catch (Exception e) {
            LOGGER.info("Exception at the time of saving to file", e);
        } finally {
            input.close();
            writer.close();
            connection.disconnect();
        }
    }

    private List<Element> filterRelativeUrlsOnSearchString(Elements newHrefElement, String searchString){
        List<Element> relativeUrls = new ArrayList<Element>();
        for (Element e : newHrefElement){
            if(e.attr("abs:href").indexOf(searchString) > 0 && e.attr("abs:href").indexOf("%") > 0){
                relativeUrls.add(e);
            }
        }
        return relativeUrls;
    }

    private Elements filterAllHrefBasedOnSearchString(Elements hrefElements, String searchString){
        Iterator it = hrefElements.iterator();
        while(it.hasNext()){
            Element e = (Element)it.next();
            if(e.attr("abs:href").indexOf(searchString) > 0 && e.attr("abs:href").indexOf("thread") > 0){
                continue;
            }
            it.remove();
        }
        return hrefElements;
    }
}
