package advisor.model;

import advisor.controller.API;

import java.io.IOException;
import java.util.List;

public class PagedCategories extends PaginatedContent {

    @Override
    public List<String> fetchPage(String accessToken) throws IOException, InterruptedException {
        return API.getCategories(accessToken, super.getCurrentPageFirstIndex());
    }

}
