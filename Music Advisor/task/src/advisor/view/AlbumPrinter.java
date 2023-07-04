package advisor.view;

import advisor.Pagination;
import advisor.model.albums.Album;

import java.util.List;

public class AlbumPrinter implements Printer{
    @Override
    public void printContent(List content) {
        System.out.println("---NEW RELEASES---");
        for (Album album : (List<Album>) content) {
            System.out.println(album.getName());
            System.out.println(album.getArtists().toString());
            System.out.println(album.getLink() + "\n");
        }
        System.out.println("---PAGE " + Pagination.getCurrentPage() + " OF " + Pagination.getLastPage() + "---");
    }
}
