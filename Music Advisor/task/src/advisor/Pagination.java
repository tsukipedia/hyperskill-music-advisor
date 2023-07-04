package advisor;

public class Pagination {

    private static Integer pageSize;
    private static Integer currentPage;
    private static Integer lastPage;
    private Integer currentPageFirstIndex;

    public Pagination() {
        currentPage = 1;
        currentPageFirstIndex = 0;
        lastPage = 1;
    }

    public void nextPage() {
        if ((currentPage + 1) > lastPage) throw new IndexOutOfBoundsException("No more pages");
        currentPage++;
        currentPageFirstIndex += pageSize;
    }

    public void prevPage() {
        if((currentPage - 1) < 1) throw new IndexOutOfBoundsException("No more pages");
        currentPage--;
        currentPageFirstIndex -= pageSize;
    }

    public static void setLastPage(Integer totalElements) {
        lastPage = (int) Math.ceil((double) totalElements / pageSize);
    }

    public Integer getCurrentPageFirstIndex() {
        return currentPageFirstIndex;
    }

    protected static void setPageSize(String page) {
        pageSize = Integer.parseInt(page);
    }

    public static Integer getPageSize() {
        return pageSize;
    }

    public static Integer getCurrentPage() {
        return currentPage;
    }

    public static Integer getLastPage() {
        return lastPage;
    }
}
