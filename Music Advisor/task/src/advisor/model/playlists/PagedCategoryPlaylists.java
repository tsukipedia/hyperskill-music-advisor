package advisor.model.playlists;

import advisor.controller.API;
import advisor.model.PaginatedContent;

import java.io.IOException;
import java.util.List;

public class PagedCategoryPlaylists extends PaginatedContent {
    private final String categoryName;

    public PagedCategoryPlaylists(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public List<Playlist> fetchPage(String accessToken) throws IOException, InterruptedException {
        return API.getPlaylistsByCategory(accessToken, this.categoryName, super.getCurrentPageFirstIndex());
    }
}
