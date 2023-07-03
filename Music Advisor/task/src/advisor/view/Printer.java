package advisor.view;

import advisor.model.albums.Album;
import advisor.model.playlists.Playlist;

import java.util.List;

public class Printer {
    public static void printNewReleases(List<Album> newReleases, Integer currentPage, Integer totalPages) {
        if (currentPage > totalPages) {
            System.out.println("No more pages.");
            return;
        }
        System.out.println("---NEW RELEASES---");
        for (Album album : newReleases) {
            System.out.println(album.getName());
            System.out.println(album.getArtists().toString());
            System.out.println(album.getLink() + "\n");
        }
        System.out.println("---PAGE " + currentPage + " OF " + totalPages + "---");
    }

    public static void printPlaylists(List<Playlist> playlists, Integer currentPage, Integer totalPages) {
        if (playlists == null) return;
        if (currentPage > totalPages) {
            System.out.println("No more pages.");
            return;
        }
        System.out.println("---FEATURED---");
        for (Playlist playlist : playlists) {
            System.out.println(playlist.getName());
            System.out.println(playlist.getLink() + "\n");
        }
        System.out.println("---PAGE " + currentPage + " OF " + totalPages + "---");
    }

    public static void printCategories(List<String> categories, Integer currentPage, Integer totalPages) {
        if (currentPage > totalPages) {
            System.out.println("No more pages.");
            return;
        }
        System.out.println("---CATEGORIES---");
        categories.forEach(System.out::println);
        System.out.println("---PAGE " + currentPage + " OF " + totalPages + "---");
    }

    public static void printAuth(String baseUrl) {
        System.out.println(baseUrl + "/authorize?client_id=6623a7ef869f41fc9bbd028c510bfe14&redirect_uri=http://localhost:8080&response_type=code");
    }

    public static void printAuthError() {
        System.out.println("Please, provide access for application.");
    }
}
