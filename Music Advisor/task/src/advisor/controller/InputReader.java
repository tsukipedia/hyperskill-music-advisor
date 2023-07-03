package advisor.controller;

import advisor.Authentication;
import advisor.model.playlists.PagedFeaturedPlaylists;
import advisor.model.albums.PagedReleases;
import advisor.model.PaginatedContent;
import advisor.model.User;
import advisor.model.PagedCategories;
import advisor.model.playlists.PagedCategoryPlaylists;
import advisor.view.Printer;

import java.io.IOException;

public class InputReader {
    private static User user = new User();
    private static String accessToken = "";
    private static PaginatedContent currentContent;
    private static Integer currentPageFirstIndex = 0;

    public static void evaluateUserInput(String input) throws IOException, InterruptedException {

        if (!input.equals("auth") && !user.isAuthenticated()) {
            Printer.printAuthError();
            return;
        }

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
                currentContent = new PagedFeaturedPlaylists();
                currentPageFirstIndex = 0;
                currentContent.fetchPage(accessToken, currentPageFirstIndex);
            }
            case "new" -> {
                currentContent = new PagedReleases();
                currentPageFirstIndex = 0;
                currentContent.fetchPage(accessToken, currentPageFirstIndex);
            }
            case "categories" -> {
                currentContent = new PagedCategories();
                currentPageFirstIndex = 0;
                currentContent.fetchPage(accessToken, currentPageFirstIndex);
            }
            case "prev" -> {
                currentPageFirstIndex -= API.getPageSize();
                if (currentPageFirstIndex < 0) {
                    currentPageFirstIndex = 0;
                    System.out.println("No more pages.");
                } else {
                    currentContent.fetchPage(accessToken, currentPageFirstIndex);
                }
            }
            case "next" -> {
                if (API.getCurrentPage(currentPageFirstIndex + API.getPageSize()) > API.getTotalPages()) {
                    System.out.println("No more pages.");
                } else {
                    currentPageFirstIndex += API.getPageSize();
                    currentContent.fetchPage(accessToken, currentPageFirstIndex);
                }
            }
            default -> {
                if (input.contains("playlists")) {
                    String categoryName = input.replace("playlists ", "");
                    currentContent = new PagedCategoryPlaylists(categoryName);
                    currentPageFirstIndex = 0;
                    currentContent.fetchPage(accessToken, currentPageFirstIndex);
                } else {
                    System.out.println("Error: Invalid command.");
                }
            }
        }
    }

    public static void setCurrentPageFirstIndex(Integer currentPageFirstIndex) {
        InputReader.currentPageFirstIndex = currentPageFirstIndex;
    }
}
