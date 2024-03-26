package com.example.Proj_Refactory.controller;

import com.example.Proj_Refactory.domain.Image;
import com.example.Proj_Refactory.domain.Member;
import com.example.Proj_Refactory.repository.ImageRepository;
import com.example.Proj_Refactory.repository.MemberRepository;
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
    MemberRepository memberRepository;
    @Autowired
    public void ImageController(ImageRepository imageRepository,
                                MemberRepository memberRepository){
        this.imageRepository = imageRepository;
        this.memberRepository=memberRepository;
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
        System.out.println("get 메소드 upload 사용");
        return "/upload";
    }
    @PostMapping("/upload")
    public String uploadImage(@RequestParam("title")String title,
                              @RequestParam("data") MultipartFile file,
                              @RequestParam("content")String content){
        Image image = new Image();
        System.out.println("upload로 들어감.");
        try{
            image.setContent(content);
            image.setTitle(title);
            image.setData(file.getBytes());
            image.setName(file.getOriginalFilename());
            image.setMimeType(file.getContentType()); // MIME 타입 설정
            imageRepository.save(image);
        }catch(IOException e){
            System.out.println("IOException error");
            e.printStackTrace();
        }
        return "redirect:/index";
    }
    @PostMapping("upload/{id}")
    public String fix(@PathVariable Long id,
                      @RequestParam("title")String title,
                      @RequestParam("data")MultipartFile file,
                      @RequestParam("content")String content){
        Optional<Image> imageOptional = imageRepository.findById(id);
        System.out.println("upload fix 제대로 들어감.");
        if (imageOptional.isPresent()) {
            try {
                Image image = imageOptional.get();
                image.setContent(content);
                image.setTitle(title);
                if (!file.isEmpty()) image.setData(file.getBytes());
                image.setName(file.getOriginalFilename());
                image.setMimeType(file.getContentType()); // MIME 타입 설정
                imageRepository.save(image);
            }
            catch (IOException e) {
                System.out.println("IOException error");
                e.printStackTrace();
            }
        }
        else{
            System.out.println("fix error");
            return "redirect:/index";
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
    @GetMapping("/viewer/fix/{id}")
    public String fixButton(@PathVariable Long id,Model model){
        Optional<Image> imageOptional = imageRepository.findById(id);
        if(imageOptional.isPresent()){
            Image image = imageOptional.get();
            model.addAttribute("image",image);
            return "upload";
        }
        else{
            return "viewer";
        }
    }
    @PostMapping("/viewer/delete/{id}")
    public String delete(@PathVariable Long id){
        Optional<Image> imageOptional = imageRepository.findById(id);
        if(imageOptional.isPresent()){
            Image image = imageOptional.get();
            imageRepository.delete(image);
            return "redirect:/index";
        }
        else{
            System.out.println("delete 실패");
            return "viewer";
        }
    }
    @PostMapping("/login")
    public String login(@RequestParam("username")String name,
                        @RequestParam("password")Long password){
        Member member=new Member();
        member.setLoginId(name);
        member.setPassword(password);
        memberRepository.save(member);
        return "redirect:/index";
    }
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }
}
@@@@@@@@@test