package com.example.kadai_002.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.kadai_002.entity.Category;
import com.example.kadai_002.entity.House;
import com.example.kadai_002.form.HouseEditForm;
import com.example.kadai_002.form.HouseRegisterForm;
import com.example.kadai_002.repository.CategoryRepository;
import com.example.kadai_002.repository.HouseRepository;

@Service
public class HouseService {
    private final HouseRepository houseRepository;
    private final CategoryRepository categoryRepository;

    public HouseService(HouseRepository houseRepository, CategoryRepository categoryRepository) {
        this.houseRepository = houseRepository;
        this.categoryRepository = categoryRepository;
    }
   
   @Transactional
   public void create(HouseRegisterForm houseRegisterForm) {
       House house = new House();        
       MultipartFile imageFile = houseRegisterForm.getImageFile();
       
       if (!imageFile.isEmpty()) {
           String imageName = imageFile.getOriginalFilename(); 
           String hashedImageName = generateNewFileName(imageName);
           Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
           copyImageFile(imageFile, filePath);
           house.setImageName(hashedImageName);
       }
       
       house.setName(houseRegisterForm.getName());                
       house.setDescription(houseRegisterForm.getDescription());
       house.setPriceMax(houseRegisterForm.getPrice());
       house.setPriceMin(houseRegisterForm.getPrice());
       house.setStartTime(houseRegisterForm.getStartTime());
       house.setEndTime(houseRegisterForm.getEndTime());
       house.setHoliday(houseRegisterForm.getHoliday());
       house.setPostalCode(houseRegisterForm.getPostalCode());
       house.setAddress(houseRegisterForm.getAddress());
       house.setPhoneNumber(houseRegisterForm.getPhoneNumber());
       
       Long categoryId = houseRegisterForm.getCategoryId();
       if (categoryId != null) {
           Category category = categoryRepository.findById(categoryId)
               .orElseThrow(() -> new RuntimeException("カテゴリが見つかりません"));
           house.setCategory(category);
       } else {
           house.setCategory(null);
       }
                   
       houseRepository.save(house);
   }  
   
   @Transactional
   public void update(HouseEditForm houseEditForm) {
       House house = houseRepository.getReferenceById(houseEditForm.getId());
       MultipartFile imageFile = houseEditForm.getImageFile();
       
       if (!imageFile.isEmpty()) {
           String imageName = imageFile.getOriginalFilename(); 
           String hashedImageName = generateNewFileName(imageName);
           Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
           copyImageFile(imageFile, filePath);
           house.setImageName(hashedImageName);
       }
       
       house.setName(houseEditForm.getName());                
       house.setDescription(houseEditForm.getDescription());
       house.setPriceMax(houseEditForm.getPriceMax());
       house.setPriceMin(houseEditForm.getPriceMin());
       house.setStartTime(houseEditForm.getStartTime());
       house.setEndTime(houseEditForm.getEndTime());
       house.setHoliday(houseEditForm.getHoliday());
       house.setPostalCode(houseEditForm.getPostalCode());
       house.setAddress(houseEditForm.getAddress());
       house.setPhoneNumber(houseEditForm.getPhoneNumber());
       
       Long categoryId = houseEditForm.getCategoryId();
       if (categoryId != null) {
           Category category = categoryRepository.findById(categoryId)
               .orElseThrow(() -> new RuntimeException("カテゴリが見つかりません"));
           house.setCategory(category);
       } else {
           house.setCategory(null);
       }

                   
       houseRepository.save(house);
   }    
   
   // UUIDを使って生成したファイル名を返す
   public String generateNewFileName(String fileName) {
       String[] fileNames = fileName.split("\\.");                
       for (int i = 0; i < fileNames.length - 1; i++) {
           fileNames[i] = UUID.randomUUID().toString();            
       }
       String hashedFileName = String.join(".", fileNames);
       return hashedFileName;
   }     
   
   // 画像ファイルを指定したファイルにコピーする
   public void copyImageFile(MultipartFile imageFile, Path filePath) {           
       try {
           Files.copy(imageFile.getInputStream(), filePath);
       } catch (IOException e) {
           e.printStackTrace();
       }          
   } 
   public House findById(Long id) {
       return houseRepository.findById(id)
           .orElseThrow(() -> new RuntimeException("House not found"));
   }

}
