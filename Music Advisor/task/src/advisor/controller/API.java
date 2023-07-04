package advisor.controller;

import advisor.Pagination;
import advisor.model.playlists.Playlist;
import advisor.model.albums.Album;

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

    public static List<String> getCategories(String accessToken, Integer currentPageFirstIndex) throws IOException, InterruptedException {
        List<String> pagedCategories = new ArrayList<>();

        final String url = baseUrl + "/v1/browse/categories";
//        final String url = baseUrl + "/v1/browse/categories?limit=" + Pagination.getPageSize() + "&offset=" + currentPageFirstIndex;
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
            String name = categoryObject.get("name").getAsString();

            pagedCategories.add(name);
        }

        int totalElements = categoriesObject.get("total").getAsInt();
        Pagination.setLastPage(totalElements);
        int currentPageLastIndex = Math.min(currentPageFirstIndex + Pagination.getPageSize(), totalElements);
        return pagedCategories.subList(currentPageFirstIndex, currentPageLastIndex);
//        return pagedCategories;
    }

    public static List<Playlist> getFeaturedPlaylists(String accessToken, Integer currentPageFirstIndex) throws IOException, InterruptedException {
        List<Playlist> featured = new ArrayList<>();

        final String url = baseUrl + "/v1/browse/featured-playlists";
//        final String url = baseUrl + "/v1/browse/featured-playlists?limit=" + Pagination.getPageSize() + "&offset=" + currentPageFirstIndex;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject responseJSON = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonObject playlistsObject = responseJSON.getAsJsonObject("playlists");
        JsonArray playlistsArray = playlistsObject.getAsJsonArray("items");

        for (JsonElement item : playlistsArray) {
            JsonObject playlistObject = item.getAsJsonObject();
            String id = playlistObject.get("id").getAsString();
            String name = playlistObject.get("name").getAsString();
            String link = playlistObject.getAsJsonObject("external_urls").get("spotify").getAsString();
            featured.add(new Playlist(id, name, link));
        }

        int totalElements = playlistsObject.get("total").getAsInt();
        Pagination.setLastPage(totalElements);
        int currentPageLastIndex = Math.min(currentPageFirstIndex + Pagination.getPageSize(), totalElements);
        return featured.subList(currentPageFirstIndex, currentPageLastIndex);
//        return featured
    }

    public static List<Album> getNewReleases(String accessToken, Integer currentPageFirstIndex) throws IOException, InterruptedException {
        List<Album> newReleases = new ArrayList<>();
        final String url = baseUrl + "/v1/browse/new-releases";
//        final String url = baseUrl + "/v1/browse/new-releases?limit=" + Pagination.getPageSize() + "&offset=" + currentPageFirstIndex;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject responseJSON = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonObject releasesObject = responseJSON.getAsJsonObject("albums");
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

        int totalElements = releasesObject.get("total").getAsInt();
        Pagination.setLastPage(totalElements);
        int currentPageLastIndex = Math.min(currentPageFirstIndex + Pagination.getPageSize(), totalElements);
        return newReleases.subList(currentPageFirstIndex, currentPageLastIndex);
//        return newReleases;
    }

    public static List<Playlist> getPlaylistsByCategory(String accessToken, String categoryName, Integer currentPageFirstIndex) throws IOException, InterruptedException {
        List<Playlist> playlists = new ArrayList<>();

        updateCategories(accessToken);
        if (!categories.containsKey(categoryName)) throw new IndexOutOfBoundsException("Unknown category name");
        String categoryId = categories.get(categoryName);

        final String url = baseUrl + "/v1/browse/categories/" + categoryId + "/playlists";
//        final String url = baseUrl + "/v1/browse/categories/" + categoryId + "/playlists?limit=" + Pagination.getPageSize() + "&offset=" + currentPageFirstIndex;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        try {
            JsonObject responseJSON = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject playlistsObject = responseJSON.getAsJsonObject("playlists");
            JsonArray playlistsArray = playlistsObject.getAsJsonArray("items");

            for (JsonElement item : playlistsArray) {
                JsonObject playlistObject = item.getAsJsonObject();
                String id = playlistObject.get("id").getAsString();
                String name = playlistObject.get("name").getAsString();
                String link = playlistObject.getAsJsonObject("external_urls").get("spotify").getAsString();
                playlists.add(new Playlist(id, name, link));
            }

            int totalElements = playlistsObject.get("total").getAsInt();
            Pagination.setLastPage(totalElements);
            int currentPageLastIndex = Math.min(currentPageFirstIndex + Pagination.getPageSize(), totalElements);
            return playlists.subList(currentPageFirstIndex, currentPageLastIndex);
//            return playlists;
        } catch (NullPointerException npe) {
            System.out.println("Error in server response. " + response.body());
            return null;
        }
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

    public static void setBaseUrl(String url) {
        baseUrl = url;
    }

}
