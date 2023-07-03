package advisor.controller;

import advisor.model.playlists.Playlist;
import advisor.model.albums.Album;

import advisor.view.Printer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class API {
    private static String baseUrl = "https://accounts.spotify.com";
    private static final HttpClient client = HttpClient.newBuilder().build();;

    private static HashMap<String, String> categories = new HashMap<>();

    private static Integer pageSize = 5;

    private static Integer totalElements;

    public static void setBaseUrl(String url) {
        baseUrl = url;
    }

    public static void setPageSize(String page) {
        pageSize = Integer.parseInt(page);
    }

    public static Integer getPageSize() {
        return pageSize;
    }

    private static void updateCategories(String accessToken) throws IOException, InterruptedException {
        categories = new HashMap<>();
        final String url = baseUrl + "/v1/browse/categories";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject responseJSON = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonObject categoriesObject = responseJSON.getAsJsonObject("categories");
        JsonArray categoriesArray = categoriesObject.getAsJsonArray("items");

        for (JsonElement item : categoriesArray) {
            JsonObject categoryObject = item.getAsJsonObject();
            String id = categoryObject.get("id").getAsString();
            String name = categoryObject.get("name").getAsString();

            categories.put(name, id);
        }
    }

    public static void getCategories(String accessToken, Integer currentPageFirstIndex) throws IOException, InterruptedException {
        List<String> categories = new ArrayList<>();
        /*
        Weird pagination handling because of Hyperskill's outdated knowledge on spotify API
        hyperskill hint: don't waste time for implementation pagination of Spotify API because test used own logic or
        ignore in answers on ?limit=XX&offset=YY
         */
//        final String url = baseUrl + "/v1/browse/categories?limit=" + pageSize + "&offset=" + currentPageFirstIndex;
        final String url = baseUrl + "/v1/browse/categories";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject responseJSON = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonObject categoriesObject = responseJSON.getAsJsonObject("categories");
        totalElements = categoriesObject.get("total").getAsInt();
        JsonArray categoriesArray = categoriesObject.getAsJsonArray("items");

        for (JsonElement item : categoriesArray) {
            JsonObject categoryObject = item.getAsJsonObject();
            String name = categoryObject.get("name").getAsString();

            categories.add(name);
        }

//        Printer.printCategories(categories, getCurrentPage(currentPageFirstIndex), getTotalPages(totalElements));
        Integer currentPageLastIndex = Math.min((currentPageFirstIndex + pageSize), totalElements);
        Printer.printCategories(categories.subList(currentPageFirstIndex, currentPageLastIndex), getCurrentPage(currentPageFirstIndex), getTotalPages());
    }

    public static void getFeaturedPlaylists(String accessToken, Integer currentPageFirstIndex) throws IOException, InterruptedException {
        List<Playlist> featured = new ArrayList<>();

//        final String url = baseUrl + "/v1/browse/featured-playlists?limit=" + pageSize + "&offset=" + currentPageFirstIndex;
        final String url = baseUrl + "/v1/browse/featured-playlists";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject responseJSON = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonObject playlistsObject = responseJSON.getAsJsonObject("playlists");
        totalElements = playlistsObject.get("total").getAsInt();
        JsonArray playlistsArray = playlistsObject.getAsJsonArray("items");

        for (JsonElement item : playlistsArray) {
            JsonObject playlistObject = item.getAsJsonObject();
            String id = playlistObject.get("id").getAsString();
            String name = playlistObject.get("name").getAsString();
            String link = playlistObject.getAsJsonObject("external_urls").get("spotify").getAsString();
            featured.add(new Playlist(id, name, link));
        }

//        Printer.printPlaylists(featured, getCurrentPage(currentPageFirstIndex), getTotalPages(totalElements));
        Integer currentPageLastIndex = Math.min((currentPageFirstIndex + pageSize), totalElements);
        Printer.printPlaylists(featured.subList(currentPageFirstIndex, currentPageLastIndex), getCurrentPage(currentPageFirstIndex), getTotalPages());
    }

    public static void getNewReleases(String accessToken, Integer currentPageFirstIndex) throws IOException, InterruptedException {
        List<Album> newReleases = new ArrayList<>();
//        final String url = baseUrl + "/v1/browse/new-releases?limit=" + pageSize + "&offset=" + currentPageFirstIndex;
        final String url = baseUrl + "/v1/browse/new-releases";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject responseJSON = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonObject releasesObject = responseJSON.getAsJsonObject("albums");
        totalElements = releasesObject.get("total").getAsInt();
        JsonArray releasesArray = releasesObject.getAsJsonArray("items");

        for (JsonElement item : releasesArray) {
            List<String> artists = new ArrayList<>();
            JsonObject albumObject = item.getAsJsonObject();

            String id = albumObject.get("id").getAsString();
            String name = albumObject.get("name").getAsString();
            String link = albumObject.getAsJsonObject("external_urls").get("spotify").getAsString();

            JsonArray artistsArray =  albumObject.getAsJsonArray("artists");
            for (JsonElement artist : artistsArray) {
                JsonObject artistObject = artist.getAsJsonObject();
                String artistName = artistObject.get("name").getAsString();
                artists.add(artistName);
            }
            newReleases.add(new Album(id, name, artists, link));
        }

//        Printer.printNewReleases(newReleases, getCurrentPage(currentPageFirstIndex), getTotalPages(totalElements));
        Integer currentPageLastIndex = Math.min((currentPageFirstIndex + pageSize), totalElements);
        Printer.printNewReleases(newReleases.subList(currentPageFirstIndex, currentPageLastIndex), getCurrentPage(currentPageFirstIndex), getTotalPages());
    }

    public static void getPlaylistsByCategory(String accessToken, String categoryName, Integer currentPageFirstIndex) throws IOException, InterruptedException {
        List<Playlist> playlists = new ArrayList<>();
        updateCategories(accessToken);
        if (!categories.containsKey(categoryName)) {
            System.out.println("Unknown category name.");
            return;
        }
        String categoryId = categories.get(categoryName);

//        final String url = baseUrl + "/v1/browse/categories/" + categoryId + "/playlists?limit=" + pageSize + "&offset=" + currentPageFirstIndex;
        final String url = baseUrl + "/v1/browse/categories/" + categoryId + "/playlists";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        try {
            JsonObject responseJSON = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject playlistsObject = responseJSON.getAsJsonObject("playlists");
            totalElements = playlistsObject.get("total").getAsInt();
            JsonArray playlistsArray = playlistsObject.getAsJsonArray("items");

            for (JsonElement item : playlistsArray) {
                JsonObject playlistObject = item.getAsJsonObject();
                String id = playlistObject.get("id").getAsString();
                String name = playlistObject.get("name").getAsString();
                String link = playlistObject.getAsJsonObject("external_urls").get("spotify").getAsString();
                playlists.add(new Playlist(id, name, link));
            }

//            Printer.printPlaylists(playlists, getCurrentPage(currentPageFirstIndex), getTotalPages(totalElements));
            Integer currentPageLastIndex = Math.min((currentPageFirstIndex + pageSize), totalElements);
            Printer.printPlaylists(playlists.subList(currentPageFirstIndex, currentPageLastIndex), getCurrentPage(currentPageFirstIndex), getTotalPages());
        } catch (NullPointerException npe) {
            System.out.println("Error in server response. " + response.body());
        }
    }

    public static Integer getCurrentPage(Integer currentPageFirstIndex) {
        Integer page = 1;
        Integer lastIndex = pageSize - 1;
        while(lastIndex < currentPageFirstIndex) {
            lastIndex += pageSize;
            page++;
        }
        return page;
    }

    public static Integer getTotalPages() {
        return (int) Math.ceil((double) totalElements / pageSize);
    }

}
