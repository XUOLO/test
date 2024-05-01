package com.example.API.Service;

import com.example.API.Entity.Song;
import com.example.API.Image;
import com.example.API.Repository.SongRepository;
import org.springframework.stereotype.Service;

@Service
public class SongService {
    private final SongRepository songRepository;

    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }
    public Song viewById(long id) {
        return songRepository.findById(id).get();
    }
    public void addSong(Song song) {
        songRepository.save(song);
    }
}
