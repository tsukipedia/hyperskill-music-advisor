package advisor.model.playlists;

import advisor.controller.API;
import advisor.model.PaginatedContent;

import java.io.IOException;
import java.util.List;

public class PagedFeaturedPlaylists extends PaginatedContent {
    @Override
    public List<Playlist> fetchPage(String accessToken) throws IOException, InterruptedException {
        return API.getFeaturedPlaylists(accessToken, super.getCurrentPageFirstIndex());
    }

}
