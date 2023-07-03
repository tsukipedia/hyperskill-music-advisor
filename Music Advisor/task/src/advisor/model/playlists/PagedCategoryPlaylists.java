package advisor.model.playlists;

import advisor.controller.API;
import advisor.model.PaginatedContent;

import java.io.IOException;

public class PagedCategoryPlaylists implements PaginatedContent {
    private final String categoryName;

    public PagedCategoryPlaylists(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public void fetchPage(String accessToken, Integer currentPageFirstIndex) throws IOException, InterruptedException {
        API.getPlaylistsByCategory(accessToken, this.categoryName, currentPageFirstIndex);
    }
}
