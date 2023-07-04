package advisor.controller;

import advisor.Authentication;
import advisor.model.playlists.PagedFeaturedPlaylists;
import advisor.model.albums.PagedReleases;
import advisor.model.PaginatedContent;
import advisor.model.User;
import advisor.model.PagedCategories;
import advisor.model.playlists.PagedCategoryPlaylists;
import advisor.view.AlbumPrinter;
import advisor.view.CategoriesPrinter;
import advisor.view.PlaylistPrinter;
import advisor.view.Printer;

import java.io.IOException;
import java.util.List;

public class InputReader {
    private static User user = new User();
    private static String accessToken = "";
    private static PaginatedContent apiCaller;
    private static Printer printer;

    public static void evaluateUserInput(String input) throws IOException, InterruptedException {

        if (!input.equals("auth") && !user.isAuthenticated()) throw new RuntimeException("Please, provide access for application.");

        List content;
        switch (input) {
            case "auth" -> {
                Authentication auth = new Authentication();
                String authCode = auth.getAccess(user);
                user.setCode(authCode);
                user.setIsAuthenticated(true);
                accessToken = Authentication.getAccessToken(user);
                System.out.println("---SUCCESS---");
            }
            case "featured" -> {
                apiCaller = new PagedFeaturedPlaylists();
                printer = new PlaylistPrinter();
                content = apiCaller.fetchPage(accessToken);
                printer.printContent(content);
            }
            case "new" -> {
                apiCaller = new PagedReleases();
                printer = new AlbumPrinter();
                content = apiCaller.fetchPage(accessToken);
                printer.printContent(content);
            }
            case "categories" -> {
                apiCaller = new PagedCategories();
                printer = new CategoriesPrinter();
                content = apiCaller.fetchPage(accessToken);
                printer.printContent(content);
            }
            case "prev" -> {
                apiCaller.changePage("prev");
                content = apiCaller.fetchPage(accessToken);
                printer.printContent(content);
            }
            case "next" -> {
                apiCaller.changePage("next");
                content = apiCaller.fetchPage(accessToken);
                printer.printContent(content);
            }
            default -> {
                if (input.contains("playlists")) {
                    String categoryName = input.replace("playlists ", "");
                    apiCaller = new PagedCategoryPlaylists(categoryName);
                    printer = new PlaylistPrinter();
                    content = apiCaller.fetchPage(accessToken);
                    printer.printContent(content);
                } else {
                    System.out.println("Error: Invalid command.");
                }
            }
        }
    }
}
