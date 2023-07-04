package advisor.view;

import advisor.Pagination;
import advisor.model.playlists.Playlist;

import java.util.List;

public class PlaylistPrinter implements Printer {
    @Override
    public void printContent(List content) {
        if (content == null) return;

        System.out.println("---FEATURED---");
        for (Playlist playlist : (List<Playlist>) content) {
            System.out.println(playlist.getName());
            System.out.println(playlist.getLink() + "\n");
        }
        System.out.println("---PAGE " + Pagination.getCurrentPage() + " OF " + Pagination.getLastPage() + "---");
    }
}
