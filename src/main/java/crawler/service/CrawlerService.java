package crawler.service;

import java.io.IOException;
import java.util.List;

/**
 * Created by dibyendu on 10/11/15.
 */
public interface CrawlerService {

    List<String> getAllUrl(String baseUrl, String searchString) throws IOException;
    Boolean download(List<String> urlsList, String downloadDirectory) throws  Exception;
}
