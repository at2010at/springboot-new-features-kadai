package com.example.samuraitravel.controller;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
//import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
//import org.springframework.data.domain.Page;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.ReviewService;

@Controller
//@RequestMapping("/houses/{houseId}")
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	
    private final ReviewRepository reviewRepository;
    private final HouseRepository houseRepository;

    public ReviewController(ReviewRepository reviewRepository, HouseRepository houseRepository) {
        this.reviewRepository = reviewRepository;
        this.houseRepository = houseRepository;
    }


    
    @GetMapping("/houses/{houseId}/reviews")
    public String listReviews(
            @PathVariable Integer houseId,
            @RequestParam(defaultValue = "0") int page,
            Model model,
            @AuthenticationPrincipal UserDetailsImpl userDetails  // ログインユーザー情報を取得
    ) {
        House house = houseRepository.findById(houseId).orElseThrow();

        PageRequest pageable = PageRequest.of(page, 10, Sort.by("reviewDate").descending());
        Page<Review> reviewPage = reviewRepository.findByHouse(house, pageable);

        model.addAttribute("house", house);
        model.addAttribute("reviewPage", reviewPage);

        // ログインユーザーIDをモデルに追加
        if (userDetails != null) {
            model.addAttribute("loginUserId", userDetails.getId());
        }

        return "reviews/index";  // Thymeleafのテンプレート名
    }    

    @GetMapping("/houses/{houseId}/reviews/new")
//    @GetMapping("/reviews/create")
    public String showReviewForm(@PathVariable Integer houseId, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        House house = houseRepository.findById(houseId).orElseThrow();
        Review review = new Review();
        review.setHouse(house);
        review.setUser(userDetails.getUser());

        model.addAttribute("house", house);
        model.addAttribute("review", review);
        return "reviews/new";
    }

//    @PostMapping("/reviews/new")
    @PostMapping("/houses/{houseId}/reviews/create")
    public String createReview(@ModelAttribute Review review, 
                               @PathVariable Integer houseId,
                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // house をセット
        House house = houseRepository.findById(houseId).orElseThrow();
        review.setHouse(house);

        // ログイン中の user をセット
        review.setUser(userDetails.getUser());

        // 保存
        reviewRepository.save(review);

        // 民宿詳細ページへリダイレクト
        return "redirect:/houses/" + houseId;
    }

    
    @GetMapping("/reviews/edit/{id}")
    public String editReviewForm(@PathVariable("id") Long reviewId, Model model) {
        Review review = reviewService.findById(reviewId);
        if (review == null) {
            return "error/404";  // または適当なエラーページへ
        }
        model.addAttribute("review", review);
        return "review_edit";
    }

//    @PostMapping("/reviews/edit/{id}")
    @PostMapping("/reviews/edit/{id}")
    public String updateReview(@PathVariable("id") Long reviewId,
                               @RequestParam("reviewComment") String reviewComment,
                               @RequestParam("star") int star,
                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Review review = reviewService.findById(reviewId);
        if (review != null && review.getUser().getId().equals(userDetails.getId())) {
//    	if (review != null && review.getUser().getId().toString().equals(userDetails.getId())) {
            review.setReviewComment(reviewComment);
            review.setStar(star);
            reviewService.save(review);
        }
//        return "redirect:/houses/{houseId}/reviews(id=" + review.getHouse().getId() + ")";  // レビュー一覧にリダイレクト
//        return "redirect:/houses/" + review.getHouse().getId() + "/reviews";
        return "redirect:/houses/" + review.getHouse().getId();
    }
    
    
    @PostMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable("id") Long reviewId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Review review = reviewService.findById(reviewId);
        
        if (review != null && review.getUser().getId().equals(userDetails.getId())) {
            reviewService.delete(review);  // 削除処理
        }
        
//        return "redirect:/houses/{houseId}/reviews";  // レビュー一覧にリダイレクト
        return "redirect:/houses/" + review.getHouse().getId() + "/reviews";  // houseId を指定
    }

}

//@GetMapping("/reviews/edit/{id}")
////@GetMapping("/reviews/edit/{id}")
//public String editReviewForm(@PathVariable("id") Long reviewId, Model model) {
//  Review review = reviewService.findById(reviewId);
//  model.addAttribute("review", review);
//  return "review_edit";  // review_edit.html に遷移
//}

////@GetMapping("/reviews")
//@GetMapping("/houses/{houseId}/reviews")
//public String listReviews(
//    @PathVariable Integer houseId,
//    @RequestParam(defaultValue = "0") int page,
//    Model model
//) {
//House house = houseRepository.findById(houseId).orElseThrow();
//
//PageRequest pageable = PageRequest.of(page, 10, Sort.by("reviewDate").descending());
//Page<Review> reviewPage = reviewRepository.findByHouse(house, pageable);
//
//model.addAttribute("house", house);
//model.addAttribute("reviewPage", reviewPage);
//return "reviews/index";
//}

//@PostMapping("/reviews/create")
//public String createReview(@ModelAttribute Review review) {
//  reviewRepository.save(review);
//  return "redirect:/houses/" + review.getHouse().getId();
//}

//@RequestMapping("/houses/{houseId}/reviews/new")

//public ReviewController( HouseRepository houseRepository) {

//@GetMapping("/new")
//public String newReview(@PathVariable Integer houseId, Model model) {
//  House house = houseRepository.getReferenceById(houseId);
//  model.addAttribute("house", house);
//  model.addAttribute("reviewForm", new ReviewForm());
//  return "admin/reviews/new";
//}