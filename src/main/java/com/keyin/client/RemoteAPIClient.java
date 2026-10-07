package com.keyin.client;

import com.google.gson.Gson;
import com.keyin.trail.Trail;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RemoteAPIClient {
    private static final String TRAILS_PATH = "/trails";

    private final URI baseUri;
    private final HttpClient httpClient;
    private final Gson gson;

    public RemoteAPIClient(String baseUrl) {
        this(baseUrl, HttpClient.newHttpClient(), new Gson());
    }

    public RemoteAPIClient(String baseUrl, HttpClient httpClient, Gson gson) {
        Objects.requireNonNull(baseUrl, "baseUrl must not be null");
        String normalizedBaseUrl = baseUrl.endsWith("/")
                ? baseUrl.substring(0, baseUrl.length() - 1)
                : baseUrl;
        this.baseUri = URI.create(normalizedBaseUrl);
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient must not be null");
        this.gson = Objects.requireNonNull(gson, "gson must not be null");
    }

    public List<Trail> getTrails() throws IOException, InterruptedException {
        HttpResponse<String> response = send(
                HttpRequest.newBuilder(uri(TRAILS_PATH))
                        .header("Accept", "application/json")
                        .GET()
                        .build());
        Trail[] trails = parseBody(response, Trail[].class);
        return new ArrayList<>(Arrays.asList(trails));
    }

    public Trail getTrail(Long id) throws IOException, InterruptedException {
        Objects.requireNonNull(id, "id must not be null");
        HttpResponse<String> response = send(
                HttpRequest.newBuilder(uri(TRAILS_PATH + "/" + id))
                        .header("Accept", "application/json")
                        .GET()
                        .build());
        return parseBody(response, Trail.class);
    }

    public Trail createTrail(Trail trail) throws IOException, InterruptedException {
        Objects.requireNonNull(trail, "trail must not be null");
        HttpResponse<String> response = send(
                HttpRequest.newBuilder(uri(TRAILS_PATH))
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(trail)))
                        .build());
        return parseBody(response, Trail.class);
    }

    public Trail updateTrail(Long id, Trail trail) throws IOException, InterruptedException {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(trail, "trail must not be null");
        HttpResponse<String> response = send(
                HttpRequest.newBuilder(uri(TRAILS_PATH + "/" + id))
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(trail)))
                        .build());
        return parseBody(response, Trail.class);
    }

    public void deleteTrail(Long id) throws IOException, InterruptedException {
        Objects.requireNonNull(id, "id must not be null");
        send(HttpRequest.newBuilder(uri(TRAILS_PATH + "/" + id))
                .DELETE()
                .build());
    }

    private HttpResponse<String> send(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = response.statusCode();
        if (statusCode < 200 || statusCode >= 300) {
            throw new IOException("Remote API returned HTTP " + statusCode + ": " + response.body());
        }
        return response;
    }

    private <T> T parseBody(HttpResponse<String> response, Class<T> responseType) throws IOException {
        if (response.body() == null || response.body().isBlank()) {
            throw new IOException("Remote API returned an empty response body");
        }
        try {
            T result = gson.fromJson(response.body(), responseType);
            if (result == null) {
                throw new IOException("Remote API returned an empty JSON response");
            }
            return result;
        } catch (com.google.gson.JsonParseException exception) {
            throw new IOException("Remote API returned invalid JSON", exception);
        }
    }

    private URI uri(String path) {
        return URI.create(baseUri.toString() + path);
    }
}
