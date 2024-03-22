package com.example.Proj_Refactory.controller;

import com.example.Proj_Refactory.domain.Image;
import com.example.Proj_Refactory.repository.ImageRepository;
import com.example.Proj_Refactory.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class ImageController {
    ImageRepository imageRepository;
    @Autowired
    public void ImageController(ImageRepository imageRepository){
        this.imageRepository = imageRepository;
    }
    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String start(Model model){
        List<Image> images = imageRepository.findAll();
        model.addAttribute("images",images);
        return "index";
    }
    @GetMapping("/upload")
    public String upload(Model model){
        Image image = new Image();
        model.addAttribute("image",image);
        return "/upload";
    }
    @PostMapping("/upload")
    public String uploadImage(@RequestParam("title")String title,
                              @RequestParam("data") MultipartFile file,
                              @RequestParam("content")String content){
        Image image = new Image();
        try{
            image.setContent(content);
            image.setTitle(title);
            image.setData(file.getBytes());
            image.setName(file.getOriginalFilename());
            image.setMimeType(file.getContentType()); // MIME 타입 설정
            imageRepository.save(image);
        }catch(IOException e){
            e.printStackTrace();
        }
        return "redirect:/index";
    }
    @GetMapping("/images/display/{id}") //image 데이터를 변환해주는
    @ResponseBody
    public ResponseEntity<byte[]> displayImage(@PathVariable Long id) {
        Optional<Image> imageOptional = imageRepository.findById(id);
        if (imageOptional.isPresent()) {
            Image image = imageOptional.get();
            byte[] imageData = image.getData();
            String mimeType = image.getMimeType();
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.parseMediaType(mimeType)) // 동적 MIME 타입 설정
                    .body(imageData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/viewer/{id}")
    public String viewer(@PathVariable Long id,Model model){
        Optional<Image> imageOptional = imageRepository.findById(id);
        if(imageOptional.isPresent()){
            Image image = imageOptional.get();
            model.addAttribute("image",image);
            return "viewer";
        }
        else{
            return "index";
        }
    }
}
