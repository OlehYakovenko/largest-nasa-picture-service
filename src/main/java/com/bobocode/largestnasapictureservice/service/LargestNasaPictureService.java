package com.bobocode.largestnasapictureservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.websocket.server.ServerEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Comparator;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class LargestNasaPictureService {
    private final RestTemplate restTemplate;
    @Value("${nasa.base.url}")
    private String nasaBaseUrl;
    @Value("${nasa.api.key}")
    private String nasaApiKey;

    //build url
    public String getLargestNasaPictureUrl(int sol) {
        var url = UriComponentsBuilder.fromHttpUrl(nasaBaseUrl)
                .queryParam("sol", sol)
                .queryParam("api_key", nasaApiKey)
                .toUriString();
        var jsonResponse = restTemplate.getForObject(url, JsonNode.class);
        var imageUrls = StreamSupport.stream(jsonResponse.get("photos").spliterator(), false)
                .map(p -> p.get("img_src"))
                .map(JsonNode::asText)
                .toList();

        return imageUrls.parallelStream()
                .map(pictureUrl -> {
                    var initialHeader = restTemplate.headForHeaders(pictureUrl);
                    var redirectHeaders = restTemplate.headForHeaders(initialHeader.getLocation());
                    var imageSize = redirectHeaders.getContentLength();
                    return new Picture(pictureUrl, imageSize);
                })
                .max(Comparator.comparing(Picture::size))
                .map(Picture::url)
                .orElseThrow();
    }

    record Picture(String url, Long size) {
    }
}
