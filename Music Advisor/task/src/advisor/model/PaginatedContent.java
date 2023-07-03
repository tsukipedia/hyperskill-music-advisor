package advisor.model;

import java.io.IOException;

public interface PaginatedContent {
    void fetchPage(String accessToken, Integer currentPageFirstIndex) throws IOException, InterruptedException;
    Integer totalElements = 0;
}
