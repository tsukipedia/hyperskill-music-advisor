package advisor.model.albums;

import advisor.controller.API;
import advisor.model.PaginatedContent;

import java.io.IOException;

public class PagedReleases implements PaginatedContent {
    @Override
    public void fetchPage(String accessToken, Integer currentPageFirstIndex) throws IOException, InterruptedException {
        API.getNewReleases(accessToken, currentPageFirstIndex);
    }

}
