package com.MusicApp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;


@RestController
@RequestMapping("/spotify")
public class SportifyController {

    private final WebClient webClient = WebClient.create("https://api.spotify.com/v1");

    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUser(
            @RegisteredOAuth2AuthorizedClient("spotify") OAuth2AuthorizedClient authorizedClient,
            @AuthenticationPrincipal OAuth2User oauthUser) {

        String token = authorizedClient.getAccessToken().getTokenValue();
        String response = webClient.get()
                .uri("/me")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/search")
    public ResponseEntity<String> search(
            @RegisteredOAuth2AuthorizedClient("spotify") OAuth2AuthorizedClient authorizedClient,
            @RequestParam String query,
            @RequestParam(defaultValue = "track") String type) {

        String token = authorizedClient.getAccessToken().getTokenValue();
        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("q", query)
                        .queryParam("type", type)
                        .build())
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/playlists")
    public ResponseEntity<String> getPlaylists(
            @RegisteredOAuth2AuthorizedClient("spotify") OAuth2AuthorizedClient authorizedClient) {

        String token = authorizedClient.getAccessToken().getTokenValue();
        String response = webClient.get()
                .uri("/me/playlists")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/like")
    public ResponseEntity<String> saveTrack(
            @RegisteredOAuth2AuthorizedClient("spotify") OAuth2AuthorizedClient authorizedClient,
            @RequestParam String trackId) {

        String token = authorizedClient.getAccessToken().getTokenValue();
        String response = webClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/me/tracks")
                        .queryParam("ids", trackId)
                        .build())
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return ResponseEntity.ok("Track saved: " + trackId);
    }
}
