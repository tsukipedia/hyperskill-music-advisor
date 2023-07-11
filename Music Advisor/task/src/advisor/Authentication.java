package advisor;

import advisor.view.Printer;
import advisor.model.User;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.CountDownLatch;

public class Authentication {

    private static String baseUrl = "https://accounts.spotify.com";

    private static final String clientId = "";

    private static final String clientSecret = "";

    public String getAccess(User user) throws IOException, InterruptedException {
        System.out.println(Authentication.getBaseUrl()
                + "/authorize?client_id="
                + Authentication.getClientId()
                + "&redirect_uri=http://localhost:8080&response_type=code");
        CountDownLatch actionManager = new CountDownLatch(1);
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8080), 0);
        server.createContext("/",
                new HttpHandler() {
                    public void handle(HttpExchange exchange) throws IOException {
                        System.out.println("waiting for code...");
                        String query = exchange.getRequestURI().getQuery();
                        String response;
                        int statusCode = 0;
                        if (query != null && query.contains("code")) {
                            System.out.println("code received");
                            user.setCode(query.replace("code=",""));
                            response = "Got the code. Return back to your program.";
                            statusCode = 200;
                            actionManager.countDown();
                        } else {
                            response = "Authorization code not found. Try again.";
                            statusCode = 404;
                        }
                        exchange.sendResponseHeaders(statusCode, response.length());
                        exchange.getResponseBody().write(response.getBytes());
                        exchange.getResponseBody().close();
                    }
                }
        );
        server.start();
        actionManager.await();
        server.stop(1);

        return user.getCode();
    }

    public static String getAccessToken(User user) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        String uri = "/api/token";
        String body = "grant_type=authorization_code" +
                "&code=" + URLEncoder.encode(user.getCode(), StandardCharsets.UTF_8) +
                "&redirect_uri=" + URLEncoder.encode("http://localhost:8080", StandardCharsets.UTF_8);
        String credentials = clientId + ":" + clientSecret;
        String basicAuth = "Basic " + Base64.getEncoder()
                .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", basicAuth)
                .uri(URI.create(baseUrl + uri))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        System.out.println("making http request for access_token...");

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();
        System.out.println(responseBody);

        int startIndex = responseBody.indexOf("\"access_token\":\"") + "\"access_token\":\"".length();
        int endIndex = responseBody.indexOf("\"", startIndex);
        return responseBody.substring(startIndex, endIndex);
    }

    public static void setBaseUrl(String url) {
        baseUrl = url;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static String getClientId() {
        return clientId;
    }

}
