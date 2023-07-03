package advisor.model.playlists;

import advisor.controller.API;
import advisor.model.PaginatedContent;

import java.io.IOException;

public class PagedFeaturedPlaylists implements PaginatedContent {
    @Override
    public void fetchPage(String accessToken, Integer currentPageFirstIndex) throws IOException, InterruptedException {
        API.getFeaturedPlaylists(accessToken, currentPageFirstIndex);
    }
}
