package com.msa.service;

import java.util.List;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.msa.dto.CommentDTO;
import com.msa.dto.ReviewDTO;
import com.msa.dto.ReviewDetailDTO;
import com.msa.dto.ReviewerDTO;
import com.msa.repository.ReviewRepository;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;
	
	private final MongoTemplate mongoTemplate;
	
	public ReviewServiceImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
    public ReviewDTO addReview(ReviewDTO reviewDTO) {
        return reviewRepository.save(reviewDTO);
    }
    
	public List<ReviewDTO> getReviewList() {
		Query query = new Query()
				.addCriteria(Criteria.where("title").is("DongjinHa2"))
				.with(Sort.by(Sort.Order.desc("revrSeq")))
				.limit(2);
		return mongoTemplate.find(query, ReviewDTO.class);
	}
	
	public List<ReviewDTO> getReviewList1() {		//20개 출력을 위한 서비스
		Query query = new Query()
				.with(Sort.by(Sort.Order.desc("revrSeq")))
				.limit(20);
		return mongoTemplate.find(query, ReviewDTO.class);    
	} 
	
    public List<ReviewDTO> getReviewList2() {
        return reviewRepository.findAll();
    } 
    
	public List<ReviewDTO> getReviewList3(String title) {
		Query query = new Query()
				.addCriteria(Criteria.where("title").is("DongjinHa2"))
				.with(Sort.by(Sort.Order.desc("revrSeq")))
				.limit(2);
		return mongoTemplate.find(query, ReviewDTO.class);
	}
	
    public ReviewDTO getReview(String id) {
//    	ReviewDTO review = mongoTemplate.findById(new ObjectId("5e3c27f099a991312ca22243"), ReviewDTO.class,"reviews");
    	ReviewDTO review = mongoTemplate.findById(new ObjectId(id), ReviewDTO.class,"reviews");
    	return review;
    }
    
    public void delReview(String id) {
    	reviewRepository.deleteById(id);
    }
    
    public ReviewerDTO getReviewer(String id) {
    	ReviewerDTO reviewer = mongoTemplate.findById(new ObjectId(id), ReviewerDTO.class,"reviewers");
    	return reviewer;
    }
    
	public List<CommentDTO> getComments(String id) {
		Query query = new Query()
				.with(Sort.by(Sort.Order.asc("regDate")))
				.limit(3);
		return mongoTemplate.find(query, CommentDTO.class);    
	}

	
	public ReviewDetailDTO getReview1(String id) {
		//ReviewDetailDTO reviewDetail = mongoTemplate.findById(new ObjectId(id), ReviewDetailDTO.class,"reviews");
		Query query = new Query()
				.addCriteria(Criteria.where("review.$id").is(id)).limit(1);
		ReviewDetailDTO reviewDetail = (ReviewDetailDTO) mongoTemplate.find(query, ReviewDetailDTO.class);
		System.out.println(reviewDetail);
    	return reviewDetail;
	}
	public ReviewDetailDTO getReviewer1(String id) {
		ReviewDetailDTO reviewDetail = mongoTemplate.findById(new ObjectId(id), ReviewDetailDTO.class,"reviewers");
    	return reviewDetail;
	}
	public List<ReviewDetailDTO> getComments1(String id){
		Query query = new Query()
				.with(Sort.by(Sort.Order.asc("regDate")))
				.limit(3);
		return mongoTemplate.find(query, ReviewDetailDTO.class);    
				
			//	find(query, ReviewDetailDTO.class);    
	}
    
}
