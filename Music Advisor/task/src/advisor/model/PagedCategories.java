package advisor.model;

import advisor.controller.API;

import java.io.IOException;

public class PagedCategories implements PaginatedContent {

    @Override
    public void fetchPage(String accessToken, Integer currentPageFirstIndex) throws IOException, InterruptedException {
        API.getCategories(accessToken, currentPageFirstIndex);
    }
}
