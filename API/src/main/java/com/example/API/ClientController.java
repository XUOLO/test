package com.example.API;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

@Controller
public class ClientController {
    @Autowired
    private ImageService imageService;
    @Autowired
    private ImageRepository imageRepository;
    @GetMapping("/ping")
    @ResponseBody
    public String hello_world(){
        return "Hello World!";
    }

    // display image
    @Transactional
    @GetMapping("/display")
    public ResponseEntity<byte[]> displayImage(@RequestParam("id") long id) throws IOException, SQLException
    {
        Image image = imageService.viewById(id);
        byte [] imageBytes = null;
        imageBytes = image.getImage().getBytes(1,(int) image.getImage().length());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    }
    @GetMapping("/table")
    public String table(Model model){

        List<Image> imageList = imageService.viewAll();
//        mv.addObject("imageList", imageList);
        model.addAttribute("imageList",imageList);
        return "table";
    }
    // view All images
    @GetMapping("/")
    public String home(Model model){

        List<Image> imageList = imageService.viewAll();
//        mv.addObject("imageList", imageList);
        model.addAttribute("imageList",imageList);
        return "index";
    }
    @GetMapping("/Sign-in")
    public String login(Model model){
        return "Sign-in";
    }
    @GetMapping("/about")
    public String aboutPage(Model model){
        return "about";
    }
    @GetMapping("/contact")
    public String contactPage(Model model){
        return "contact";
    }
    @GetMapping("/faq")
    public String faqPage(Model model){
        return "faq";
    }
    @GetMapping("/Sign-up")
    public String registerPage(Model model){
        return "Sign-up";
    }
    @GetMapping("/products")
    public String productsPage(Model model){
        return "products";
    }
    @GetMapping("/product-detail")
    public String productDetailPage(Model model){
        return "product-detail";
    }

    // add image - get
    @GetMapping("/add")
    public ModelAndView addImage(){
        return new ModelAndView("addimage");
    }

    // add image - post
    @PostMapping("/add")
    public String addImagePost(HttpServletRequest request, @RequestParam("image") MultipartFile file) throws IOException, SerialException, SQLException
    {
        byte[] bytes = file.getBytes();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);

        Image image = new Image();
        image.setImage(blob);
        imageService.create(image);
        return "redirect:/";
    }


    @GetMapping("/delete/{id}")
    public String deleteprod( @PathVariable(value = "id") long id)
    {
        this.imageRepository.deleteById(id);
        return "redirect:/";
    }


}