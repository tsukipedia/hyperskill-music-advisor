package advisor.model.albums;

import advisor.controller.API;
import advisor.model.PaginatedContent;

import java.io.IOException;
import java.util.List;

public class PagedReleases extends PaginatedContent {
    @Override
    public List<Album> fetchPage(String accessToken) throws IOException, InterruptedException {
        return API.getNewReleases(accessToken, super.getCurrentPageFirstIndex());
    }

}
