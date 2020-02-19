package com.msa.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.msa.dto.CommentDTO;
import com.msa.dto.ReviewDTO;
import com.msa.dto.ReviewDetailDTO;
import com.msa.dto.ReviewerDTO;
import com.msa.service.ReviewService;
 
@RestController
@RequestMapping("/review")
public class ReviewController {
	
	@Autowired
	ReviewService reviewService;
    
    @PostMapping("/addReview")
    public String addReview(@RequestBody ReviewDTO _reviewDTO) {
    	ReviewDTO reviewDTO = reviewService.addReview(_reviewDTO);
        return "added id:" + reviewDTO.get_id();
    }

    @GetMapping("/getReviewList")
    public List<ReviewDTO> getReviewList() {
        return reviewService.getReviewList();
    }    

    @GetMapping("/getReviewList1")
    public List<ReviewDTO> getReviewList1() {
        return reviewService.getReviewList1();
    }   
    
    @GetMapping("/getReviewList2")
    public List<ReviewDTO> getReviewList2() {
        return reviewService.getReviewList2();
    }   
    
    @GetMapping("/getReviewList3/{name}")
    public List<ReviewDTO> getReviewList3(@PathVariable String name) {
        return reviewService.getReviewList3(name);
    }  
    
    @GetMapping("/getReviewList4")
    public List<ReviewDTO> reviews(@RequestParam String mode
    							 , @RequestParam String reviewCl
    							 , @RequestParam int pageNo
    							 , @RequestParam int sort
    							 , @RequestParam String key
    							 , @RequestParam String uage
    							 , @RequestParam String skintypecd1
    							 , @RequestParam String skintypecd2
    							 , @RequestParam String skintypecd3
    							 , @RequestParam String skintypecd4
    							 , @RequestParam String skintypecd5
    							 , @RequestParam String skintypecd6
    							 , @RequestParam String skintypecd7
    							 , @RequestParam String skintypecdyn
    							 , @RequestParam String skinetcinfo1
    							 , @RequestParam String skinetcinfo2
    							 , @RequestParam String skinetcinfo3
    							 , @RequestParam String skinetcinfoyn
    							 , @RequestParam String skintonecd1
    							 , @RequestParam String skintonecd2
    							 , @RequestParam String skintonecd3
    							 , @RequestParam String skintonecdyn
    							 ) {
    	ReviewDTO reviewdto = new ReviewDTO();
    	reviewdto.setReviewCl(reviewCl);
    	reviewdto.setPageNo(pageNo);
    	reviewdto.setSort(sort);
    	reviewdto.setKey(key);
    	reviewdto.setUage(uage);
    	
        return reviewService.getReviewList4(reviewdto);
    }  
    
    @GetMapping("/getReview/{id}")
    public ReviewDTO getReview(@PathVariable String id) {	
        return reviewService.getReview(id);
    }

    @DeleteMapping("/delReview/{id}")
    public String delReview(@PathVariable String id) {
    	reviewService.delReview(id);
        return "deleted id:" + id;
    }
    

    @GetMapping("/Reviewer/{id}")
    public ReviewerDTO getReviewer(@PathVariable String id) {
    	return reviewService.getReviewer(id);
    	
    }
    
    @GetMapping("/Comments/{id}")
    public List<CommentDTO> getComments(@PathVariable String id) {
    	return reviewService.getComments(id);
    	
    }

    @GetMapping("/Comments/{id}/{pageNo}")
    public List<CommentDTO> getComments2(@PathVariable String id, @PathVariable int pageNo) {
    	return reviewService.getComments2(id, pageNo);
    	
    }
    
    @GetMapping("/CommentsCount/{id}")
    public int getCommentsTotalCount(@PathVariable String id) {
    	return reviewService.getCommentsTotalCount(id);
    }
    
    /*
    @GetMapping("/getReview1/{id}")
    public ReviewDetailDTO getReview1(@PathVariable String id) {	
        ReviewDetailDTO review = reviewService.getReview1(id);
        List<CommentDTO> comments = reviewService.getComments1(review);
        review.setComment(comments);
        return review;
    }
    */

}
