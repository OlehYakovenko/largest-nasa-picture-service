package com.bobocode.largestnasapictureservice.controller;

import com.bobocode.largestnasapictureservice.service.LargestNasaPictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/pictures")
@RequiredArgsConstructor
public class LargestPictureController {
    private final LargestNasaPictureService pictureService;

    @GetMapping("/{sol}/largest")
    public ResponseEntity<?> getLargestNasaPicture(@PathVariable Integer sol){
        var url = pictureService.getLargestNasaPictureUrl(sol);
        return ResponseEntity
                .status(HttpStatus.PERMANENT_REDIRECT)
                .location(URI.create(url))
                .build();
    }
}
