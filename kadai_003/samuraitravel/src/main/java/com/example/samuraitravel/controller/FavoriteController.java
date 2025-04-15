package com.example.samuraitravel.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.security.UserDetailsImpl;

@Controller
@RequestMapping("/favorites")
public class FavoriteController {
	
	private final FavoriteRepository favoriteRepository;
    private final HouseRepository houseRepository;   
	
    public FavoriteController(FavoriteRepository favoriteRepository ,HouseRepository houseRepository) {
        this.favoriteRepository = favoriteRepository;   
        this.houseRepository = houseRepository;    
//        this.reviewRepository = reviewRepository; // ← 追加！
    }  
	
    // お気に入り一覧を表示
    @GetMapping("/list")
    public String listFavorites(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        User loginUser = userDetails.getUser();

        // ページごとに取得（1ページ6件）
        Pageable pageable = PageRequest.of(page, 10);
        Page<Favorite> favoritesPage = favoriteRepository.findByUserId(loginUser.getId(), pageable);

        List<House> favoriteHouses = favoritesPage.getContent().stream()
                .map(Favorite::getHouse)
                .collect(Collectors.toList());

        model.addAttribute("favoriteHouses", favoriteHouses);
        model.addAttribute("favoritesPage", favoritesPage); // ページネーション用

        return "favorites/list";
    }

    
//    @GetMapping("/list")
//    public String listFavorites(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
//        User loginUser = userDetails.getUser();
//        List<Favorite> favorites = favoriteRepository.findByUserId(loginUser.getId());
//        
//        // お気に入りの家を取得
//        List<House> favoriteHouses = favorites.stream()
//                                              .map(favorite -> favorite.getHouse())
//                                              .collect(Collectors.toList());
//        
//        model.addAttribute("favoriteHouses", favoriteHouses);
//        return "favorites/list"; // favorites/list.html というビューに遷移
//    }
    
	@PostMapping("/add")
	public String addFavorite(@RequestParam Integer houseId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
	    House house = houseRepository.findById(houseId).orElseThrow();
	    User user = userDetails.getUser();

	    if (favoriteRepository.findByHouseAndUser(house, user).isEmpty()) {
	        Favorite favorite = new Favorite();
	        favorite.setHouse(house);
	        favorite.setUser(user);
	        favoriteRepository.save(favorite);
	    }

	    return "redirect:/houses/" + houseId;
	}
	
    // お気に入り解除
    @PostMapping("/remove")
    public String removeFavorite(@RequestParam("houseId") Integer houseId,
                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User loginUser = userDetails.getUser();
        Favorite favorite = favoriteRepository.findByHouseIdAndUserId(houseId, loginUser.getId());

        if (favorite != null) {
            favoriteRepository.delete(favorite);
        }

        return "redirect:/houses/" + houseId;
    }

//	@PostMapping("/favorites/remove")
//	public String removeFavorite(@RequestParam Integer houseId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//	    House house = houseRepository.findById(houseId).orElseThrow();
//	    User user = userDetails.getUser();
//
//	    favoriteRepository.deleteByHouseAndUser(house, user);
//	    return "redirect:/houses/" + houseId;
//	}

}
