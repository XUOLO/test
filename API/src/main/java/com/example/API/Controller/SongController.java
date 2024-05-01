package com.example.API.Controller;

import com.example.API.Entity.Song;
import com.example.API.Image;
import com.example.API.Repository.SongRepository;
import com.example.API.Service.SongService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

@Controller
public class SongController {
    private final SongService songService;
    @Autowired
    private SongRepository songRepository;

    public SongController(SongService songService) {
        this.songService = songService;
    }
    @Transactional
    @GetMapping("/displaySong")
    public ResponseEntity<byte[]> displayImage(@RequestParam("id") long id) throws IOException, SQLException
    {
        Song song = songService.viewById(id);
        byte [] imageBytes = null;
        imageBytes = song.getAudio().getBytes(1,(int) song.getAudio().length());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    }
    @Transactional
    @PostMapping("/songs")
    public String addSong(@RequestParam("title") String title,
                          @RequestParam("artist") String artist,
                          @RequestParam("audioFile") MultipartFile audioFile) throws IOException, SerialException, SQLException{
        if (!audioFile.isEmpty()) {
            try {
                byte[] audioData = audioFile.getBytes();

                Song song = new Song();
                song.setTitle(title);
                song.setArtist(artist);

                byte[] bytes = audioFile.getBytes();
                Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);

                song.setAudio(blob);

                songService.addSong(song);

                // Xử lý thành công
            } catch (IOException e) {
                // Xử lý lỗi
            }
        } else {
            // Xử lý lỗi: không có tệp nhạc được chọn
        }

        return "redirect:/songs";
    }
    @GetMapping("/songs")
    public String listSong(Model model){

        model.addAttribute("listSong",songRepository.findAll());
        return "listSong";
    }

    @GetMapping("/newSong")
    public String addSong(Model model){
        Song song = new Song();
        model.addAttribute("song",song);
        return "addSong";
    }

    @GetMapping("/deleteSong/{id}")
    public String deleteSong( @PathVariable(value = "id") long id)
    {
        this.songRepository.deleteById(id);
        return "redirect:/songs";
    }
}
