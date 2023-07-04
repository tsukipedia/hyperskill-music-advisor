package advisor.model;

import advisor.Pagination;

import java.io.IOException;
import java.util.List;

public abstract class PaginatedContent {

    private Pagination pageInfo = new Pagination();

    public Integer getCurrentPageFirstIndex() {
        return pageInfo.getCurrentPageFirstIndex();
    }

    public void changePage(String direction) {
        switch (direction) {
            case "prev" -> this.pageInfo.prevPage();
            case "next" -> this.pageInfo.nextPage();
            default -> throw new RuntimeException("Unexpected error changing page");
        }
    }

    public abstract List fetchPage(String accessToken) throws IOException, InterruptedException;

}
