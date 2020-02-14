package com.msa.controller;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.msa.dto.ReviewDTO;

@Controller
@RequestMapping("/review")
public class ReviewController {
	/* PocReviewApplication.java에 rootUri 설정과 함께 Bean으로 등록하면서 주석 및 수정
	배포시 수정을 간편하게 하기 위해 상수로 선언
	final String BASE_URL = "http://localhost:9091/review";
	 */
	
	@Autowired
	RestTemplate restTemplate;	//PocReviewApplication.java에서 Bean 등록
	
    @GetMapping("/getReviewList")
    public String getReviewList(Model model) {
    	model.addAttribute("Review", null);
        return "/review/review";
    }

	@GetMapping("/productList") 
	public String productList(Model model){
		return "/product/productList";
	}
    
    @GetMapping("/getReviewList2")
    public String getReviewList2(Model model) {
        /* PocReviewApplication.java에 rootUri 설정과 함께 Bean으로 등록하면서 주석 및 수정
    	RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<ReviewDTO>> reviewResponse = restTemplate.exchange(BASE_URL+"/getReviewList2", HttpMethod.GET, null, new ParameterizedTypeReference<List<ReviewDTO>>() {});
        */
        ResponseEntity<List<ReviewDTO>> reviewResponse = restTemplate.exchange("/getReviewList2", HttpMethod.GET, null, new ParameterizedTypeReference<List<ReviewDTO>>() {});
        List<ReviewDTO> result= reviewResponse.getBody();
        
        
        model.addAttribute("Review", result);
        return "apitest";
    }    
    
    @GetMapping("/getReviewList3")
    public String getReviewList3(Model model) {
    	/* PocReviewApplication.java에 rootUri 설정과 함께 Bean으로 등록하면서 주석 및 수정
    	RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<ReviewDTO>> reviewResponse = restTemplate.exchange(BASE_URL+"/getReviewList3/yulimkang", HttpMethod.GET, null, new ParameterizedTypeReference<List<ReviewDTO>>() {});
        */
        ResponseEntity<List<ReviewDTO>> reviewResponse = restTemplate.exchange("/getReviewList3/yulimkang", HttpMethod.GET, null, new ParameterizedTypeReference<List<ReviewDTO>>() {});
        List<ReviewDTO> result= reviewResponse.getBody();
        
        model.addAttribute("Review", result);
        return "apitest";
    }    
    
    //페이징테스트
    @GetMapping("/reviewList/{page}")
    public String reviewList(Model model,@PathVariable("page") int page) {
        ResponseEntity<List<ReviewDTO>> reviewResponse = restTemplate.exchange("/getReviewList4/"+ page, HttpMethod.GET, null, new ParameterizedTypeReference<List<ReviewDTO>>() {});
        List<ReviewDTO> result= reviewResponse.getBody();
        
        model.addAttribute("Review", result);
        return "apitest";
    }    
    
    //review list 구현 테스트용
    @GetMapping("/reviewList")
    public String reviewList(Model model,@ModelAttribute("reviewDTO") ReviewDTO reviewDTO) {
    	
    	HttpHeaders headers = new HttpHeaders();
    	headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

    	UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:9091/review/getReviewList4")
    			.queryParam("mode", reviewDTO.getMode())
    	        .queryParam("reviewCl", reviewDTO.getReviewCl())
    	        .queryParam("pageNo", reviewDTO.getPageNo())
    	        .queryParam("sort", reviewDTO.getSort())
    	        .queryParam("key", reviewDTO.getKey())
    	        .queryParam("uage", reviewDTO.getUage())
    	        .queryParam("skintypecd1", reviewDTO.getSkintypecd1())
    	        .queryParam("skintypecd2", reviewDTO.getSkintypecd2())
    	        .queryParam("skintypecd3", reviewDTO.getSkintypecd3())
    	        .queryParam("skintypecd4", reviewDTO.getSkintypecd4())
    	        .queryParam("skintypecd5", reviewDTO.getSkintypecd5())
    	        .queryParam("skintypecd6", reviewDTO.getSkintypecd6())
    	        .queryParam("skintypecd7", reviewDTO.getSkintypecd7())
    	        .queryParam("skintypecdyn", reviewDTO.getSkintypecdyn())
    	        .queryParam("skinetcinfo1", reviewDTO.getSkinetcinfo1())
    	        .queryParam("skinetcinfo2", reviewDTO.getSkinetcinfo2())
    	        .queryParam("skinetcinfo3", reviewDTO.getSkinetcinfo3())
    	        .queryParam("skinetcinfoyn", reviewDTO.getSkinetcinfoyn())
    	        .queryParam("skintonecd1", reviewDTO.getSkintonecd1())
    	        .queryParam("skintonecd2", reviewDTO.getSkintonecd2())
    	        .queryParam("skintonecd3", reviewDTO.getSkintonecd3())
    	        .queryParam("skintonecdyn", reviewDTO.getSkintonecdyn())
    	        .encode(StandardCharsets.UTF_8);

    	HttpEntity<?> entity = new HttpEntity<>(headers);
    	
    	restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
    	ResponseEntity<List<ReviewDTO>> reviewResponse = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, new ParameterizedTypeReference<List<ReviewDTO>>() {});
        List<ReviewDTO> result= reviewResponse.getBody();
        
        model.addAttribute("Review", result);
        
        String mode = reviewDTO.getMode();
        System.out.println(reviewDTO.getKey());
        if(StringUtils.isEmpty(mode)) { // mode 0: 리뷰 리스트 조회, 1: 리뷰 필터 검색, 2: 페이징
        	return "apitest2";
        } else {
        	return "apitest";
        }
        
    }   
    
    //review detail 구현 테스트용
    @GetMapping("/reviewDetail/{id}")
    public String productDetailTest(Model model,@PathVariable("id") String _id) {
    	/* PocReviewApplication.java에 rootUri 설정과 함께 Bean으로 등록하면서 주석 및 수정
    	RestTemplate restTemplate = new RestTemplate();
    	review = restTemplate.getForObject(BASE_URL+"/getReview/"+_id, ReviewDTO.class);
    	*/
    	ReviewDTO review = restTemplate.getForObject("/getReview/"+_id, ReviewDTO.class);
    	model.addAttribute("Review", review);
    	return "apitest1";
    }
}
