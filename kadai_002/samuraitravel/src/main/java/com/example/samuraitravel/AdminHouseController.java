package com.example.samuraitravel;

import java.util.List;

//import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.form.HouseEditForm;
import com.example.samuraitravel.form.HouseRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.service.HouseService;

@Controller
@RequestMapping("/admin/houses")

public class AdminHouseController {
	
    private final HouseRepository houseRepository;       
    private final HouseService houseService;  
    private final ReviewRepository reviewRepository;
    
//    public AdminHouseController(HouseRepository houseRepository) {
//	public AdminHouseController(HouseRepository houseRepository, HouseService houseService) {
	public AdminHouseController(HouseRepository houseRepository, HouseService houseService, ReviewRepository reviewRepository) {
        this.houseRepository = houseRepository;     
        this.houseService = houseService;  
        this.reviewRepository = reviewRepository;
    }	
    

    
    @GetMapping
    public String index(Model model,
                        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
                        @RequestParam(name = "keyword", required = false) String keyword) {

        Page<House> housePage;

        if (keyword != null && !keyword.isEmpty()) {
            housePage = houseRepository.findByNameLike("%" + keyword + "%", pageable);                
        } else {
            housePage = houseRepository.findAll(pageable);
        }

        List<Review> reviews = reviewRepository.findAll();  // ★追加！
        model.addAttribute("housePage", housePage);        
        model.addAttribute("keyword", keyword);
        model.addAttribute("reviews", reviews);             // ★追加！

        return "admin/houses/index";
    }
    
    
    
    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id, Model model) {
        House house = houseRepository.getReferenceById(id);
        List<Review> reviews = reviewRepository.findByHouse(house); // House を渡す

        model.addAttribute("house", house);
        model.addAttribute("reviews", reviews);

        return "admin/houses/show";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("houseRegisterForm", new HouseRegisterForm());
        return "admin/houses/register";
    }      
    
    @PostMapping("/create")
    public String create(@ModelAttribute @Validated HouseRegisterForm houseRegisterForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {        
        if (bindingResult.hasErrors()) {
            return "admin/houses/register";
        }
        
        houseService.create(houseRegisterForm);
        redirectAttributes.addFlashAttribute("successMessage", "民宿を登録しました。");    
        
        return "redirect:/admin/houses";
    }    
    
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(name = "id") Integer id, Model model) {
        House house = houseRepository.getReferenceById(id);
        String imageName = house.getImageName();
        HouseEditForm houseEditForm = new HouseEditForm(house.getId(), house.getName(), null, house.getDescription(), house.getPrice(), house.getCapacity(), house.getPostalCode(), house.getAddress(), house.getPhoneNumber());
        
        model.addAttribute("imageName", imageName);
        model.addAttribute("houseEditForm", houseEditForm);
        
        return "admin/houses/edit";
    }   
    
    
    @PostMapping("/{id}/update")
    public String update(@ModelAttribute @Validated HouseEditForm houseEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {        
        if (bindingResult.hasErrors()) {
            return "admin/houses/edit";
        }
        
        houseService.update(houseEditForm);
        redirectAttributes.addFlashAttribute("successMessage", "民宿情報を編集しました。");
        
        return "redirect:/admin/houses";
    }  
    
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {        
        houseRepository.deleteById(id);
                
        redirectAttributes.addFlashAttribute("successMessage", "民宿を削除しました。");
        
        return "redirect:/admin/houses";
    }    
    

    
}


//@GetMapping("/{id}/reviews")
//public String reviewList(@PathVariable(name = "id") Integer id, Model model) {
//  House house = houseRepository.getReferenceById(id);
//  List<Review> reviews = reviewRepository.findByHouse(house);
//
//  model.addAttribute("house", house);
//  model.addAttribute("reviews", reviews);
//
//  return "admin/houses/review_list"; // 新しく作成するテンプレート
//}

//@GetMapping("/{id}/reviews")
//public String reviewList(@PathVariable(name = "id") Integer id,
//                       @PageableDefault(size = 10, sort = "reviewDate", direction = Direction.DESC) Pageable pageable,
//                       Model model) {
//  House house = houseRepository.getReferenceById(id);
//  Page<Review> reviewPage = reviewRepository.findByHouse(house, pageable); // Page型にする
//
//  model.addAttribute("house", house);
//  model.addAttribute("reviewPage", reviewPage);
//
//  return "admin/houses/review_list";
//}

//@GetMapping("/{id}")
//public String show(@PathVariable(name = "id") Integer id, Model model) {
//  House house = houseRepository.getReferenceById(id);
//  
//  model.addAttribute("house", house);
//  
//  return "admin/houses/show";
//}  

//@GetMapping
////public String index(Model model) {
////public String index(Model model, Pageable pageable) {
////public String index(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {	
//public String index(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, @RequestParam(name = "keyword", required = false) String keyword) {
//    	        	
////List<House> houses = houseRepository.findAll();       
////Page<House> housePage = houseRepository.findAll(pageable);
//
//Page<House> housePage;
//
//if (keyword != null && !keyword.isEmpty()) {
//    housePage = houseRepository.findByNameLike("%" + keyword + "%", pageable);                
//} else {
//    housePage = houseRepository.findAll(pageable);
//}  
//        
//
////model.addAttribute("houses", houses);             
//model.addAttribute("housePage", housePage);        
//model.addAttribute("keyword", keyword);
//
//return "admin/houses/index";
//
//}  

