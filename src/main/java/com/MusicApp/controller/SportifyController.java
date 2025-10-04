package com.MusicApp.controller;

import org.apache.hc.core5.http.ParseException;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.miscellaneous.AudioAnalysis;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetAudioAnalysisForTrackRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import java.io.IOException;

import static com.MusicApp.controller.AuthController.spotifyApi;


@RestController
@RequestMapping("/api")
public class SportifyController {

    @GetMapping(value = "user-top-artists")
    public Artist[] getUserTopArtists() {

        final GetUsersTopArtistsRequest getUsersTopArtistsRequest = spotifyApi.getUsersTopArtists()
                .time_range("medium_term")
                .limit(10)
                .offset(5)
                .build();

        try {
            final Paging<Artist> artistPaging = getUsersTopArtistsRequest.execute();

            // return top artists as JSON
            return artistPaging.getItems();
        } catch (Exception e) {
            System.out.println("Something went wrong!\n" + e.getMessage());
        }
        return new Artist[0];
    }

    @GetMapping(value = "user-top-songs")
    public Track[] getUserTopTracks() {

        final GetUsersTopTracksRequest getUsersTopTracksRequest = spotifyApi.getUsersTopTracks()
                .time_range("medium_term")
                .limit(10)
                .offset(5)
                .build();

        try {

            final Paging<Track> trackPaging = getUsersTopTracksRequest.execute();

            // return top Tracks as JSON
            return trackPaging.getItems();
        } catch (Exception e) {
            System.out.println("Something went wrong!\n" + e.getMessage());
        }
        return new Track[0];
    }

//
//    @GetMapping(value = "user-playlists")
//    public Track[] getUserPlaylists() {
//
//        final GetListOfUsersPlaylistsRequestRequest getListOfUsersPlaylistsRequestRequest = spotifyApi.getListOfUsersPlaylists();
//
//        try {
//
//            final Paging<Track> trackPaging = getListOfUsersPlaylistsRequestRequest.execute();
//
//            // return Playlists as JSON
//            return playlistPaging.getItems();
//        } catch (Exception e) {
//            System.out.println("Something went wrong!\n" + e.getMessage());
//        }
//        return new Playlist[0];
//    }


    public static void getAudioAnalysisForTrack(String id) {

        final GetAudioAnalysisForTrackRequest getAudioAnalysisForTrackRequest = spotifyApi
                .getAudioAnalysisForTrack(id)
                .build();
        try {
            final AudioAnalysis audioAnalysis = getAudioAnalysisForTrackRequest.execute();

            System.out.println("Track duration: " + audioAnalysis.getTrack().getDuration());
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void getCurrentUsersProfile_Sync() {
        final GetCurrentUsersProfileRequest getCurrentUsersProfileRequest = spotifyApi.getCurrentUsersProfile()
                .build();
        try {
            final User user = getCurrentUsersProfileRequest.execute();

            System.out.println("Display name: " + user.getDisplayName());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
