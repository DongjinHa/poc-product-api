package com.msa.controller;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msa.CommonUtils;
import com.msa.dto.CommentDTO;
import com.msa.dto.ReviewDTO;
import com.msa.dto.ProductDTO;
import com.msa.dto.ReviewerDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/review")
public class ReviewController {
	/* PocReviewApplication.java에 rootUri 설정과 함께 Bean으로 등록하면서 주석 및 수정
	배포시 수정을 간편하게 하기 위해 상수로 선언
	final String BASE_URL = "http://localhost:9091/review";
	 */
	
	@Autowired
	RestTemplate restTemplate;	//PocReviewApplication.java에서 Bean 등록 및 rootUri 설정
	
	@Autowired 
	WebClient.Builder builder;	//PocReviewApplication.java에서 Bean 등록 및 baseUrl 설정

    @GetMapping("/")
    public String initMain(Model model) {
    	
        return "/review/review";
    }	
	
    @PostMapping("/allreview")
    public String getReviewList(Model model, @ModelAttribute("reviewDTO") ReviewDTO reviewDTO) throws JsonProcessingException {

    	// 검색조건이 있으면 상품정보 조회
    	String key = reviewDTO.getKey();
    	//String key = "화산";
    	
    	if (key != null && !"".equals(key)) {
    		ProductDTO[] productArr = restTemplate.getForObject("http://localhost:9092/getProductListByPrdNm/"+key, ProductDTO[].class);
    		
    		if (productArr != null && productArr.length > 0) {
    			List<String> prdSeqList = new ArrayList<>();
        		for (int i=0; i<productArr.length; i++) {
        			prdSeqList.add(productArr[i].getPrdSeq());
        		}
        		
        		// 조회필요한 상품정보 세팅
        		reviewDTO.setPrdSeqList(prdSeqList);
    		}
    	}
    	
    	ObjectMapper mapper = new ObjectMapper();
    	String jsonStr = mapper.writeValueAsString(reviewDTO);
    	HttpEntity<?> requestEntity = CommonUtils.apiClientHttpEntity(MediaType.APPLICATION_JSON_VALUE, jsonStr);
    	
    	ResponseEntity<List<ReviewDTO>> reviewResponse = restTemplate.exchange("/allreview", HttpMethod.POST, requestEntity, new ParameterizedTypeReference<List<ReviewDTO>>() {});
        List<ReviewDTO> reviewList = reviewResponse.getBody();

        String reviewTotCnt = "0";
        int pageno = reviewDTO.getPageNo();
        if (reviewList != null && reviewList.size() > 0 && pageno == 1) {
        	ResponseEntity<String> reviewCntResponse = restTemplate.exchange("/allreview-totcnt", HttpMethod.POST, requestEntity, new ParameterizedTypeReference<String>() {});
        	reviewTotCnt = reviewCntResponse.getBody(); 
        }
        model.addAttribute("reviewTotCnt", reviewTotCnt);
        
        for(ReviewDTO review : reviewList) {
        	ProductDTO product = restTemplate.getForObject("http://localhost:9092/getProductListByPrdSeq/"+review.getPrdSeq(), ProductDTO.class);
        	review.setProduct(product);
        }
        model.addAttribute("reviewList", reviewList);

        
        String mode = reviewDTO.getMode();

        if ("more".equals(mode)) {
        	return "/review/moreReviewList";
        }else {
        	return "/review/allReviewList";
        }
        
    }

	@GetMapping("/productList") 
	public String productList(Model model){
		
		ResponseEntity<List<ProductDTO>> productList = restTemplate.exchange("/getProductList", HttpMethod.GET, null, new ParameterizedTypeReference<List<ProductDTO>>() {});
        List<ProductDTO> result= productList.getBody();
		
        model.addAttribute("Product", result);
		return "/product/productListMD";
	}
  
	@GetMapping("/getReviewList1") //파워리뷰 - bestFL=Y & hit count desc로 15건 출력하도록 임시 api 사용
	public String getReviewList1(Model model) {
		/*	//WebClient ver.
		WebClient webClient = builder.build();
	    List<ReviewDTO> result = webClient.get().uri("/getReviewList1")
				.retrieve() // 응답값을 가져옴 
				.bodyToFlux(ReviewDTO.class)
				.collectList().block();
	    model.addAttribute("Review",result);
		return "apitest";	*/
		
			//RestTemplate ver.
		ResponseEntity<List<ReviewDTO>> PowerReviewResponse = restTemplate.exchange("/getReviewList1", HttpMethod.GET, null, new ParameterizedTypeReference<List<ReviewDTO>>() {});
        List<ReviewDTO> powerReviews= PowerReviewResponse.getBody();
        
        for(ReviewDTO powerReview : powerReviews) {
        	ProductDTO product = restTemplate.getForObject("http://localhost:9092/getProductListByPrdSeq/"+powerReview.getPrdSeq(), ProductDTO.class);
        	powerReview.setProduct(product);
        }
        model.addAttribute("powerReview",powerReviews);
		return "powerReview";
	}
	
	
    @GetMapping("/getReviewList2")	//리뷰 전체 출력
    public String getReviewList2(Model model) {
    	/*	//WebClient ver.
    	WebClient webClient = builder.build();
		List<ReviewDTO> result = webClient.get().uri("/getReviewList2")
				.retrieve() // 응답값을 가져옴 
				.bodyToFlux(ReviewDTO.class)
				.collectList().block();
		
	    model.addAttribute("Review",result);
		return "apitest";	*/
    	
    		//RestTemplate ver.
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
    	System.out.println(builder.build().encode().toUri());
    	restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
    	ResponseEntity<List<ReviewDTO>> reviewResponse = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, new ParameterizedTypeReference<List<ReviewDTO>>() {});
        List<ReviewDTO> reviews = reviewResponse.getBody();
        
        for(ReviewDTO review : reviews) {
        	ProductDTO product = restTemplate.getForObject("http://localhost:9092/getProductListByPrdSeq/"+review.getPrdSeq(), ProductDTO.class);
        	review.setProduct(product);
        }
        
        model.addAttribute("Review", reviews);
        
        if(reviewDTO.getMode().equals("0")) { // mode 0: 리뷰 리스트 조회, 1: 리뷰 필터 검색, 2: 페이징
        	return "apitest2";
        } else {
        	return "apitest";
        }
        
    }   
    
    @GetMapping("/reviewDetail/{id}")	//1개 리뷰에 대한 출력
    public String reviewDetail(Model model,@PathVariable("id") String _id) throws Exception { 	
    	/* //WebClient ver.
	   	WebClient webClient = builder.build();
	   	ReviewDTO result = webClient.get().uri("/getReview/"+_id)
					.retrieve() // 응답값을 가져옴 
					.bodyToMono(ReviewDTO.class)
					.block();  
		model.addAttribute("ReviewData",result);
		return "apitest1";	*/
		
    	 //RestTemplate ver.

    	ReviewDTO review = restTemplate.getForObject("/getReview/"+_id, ReviewDTO.class);
    	model.addAttribute("ReviewData", review);
    	
    	try {
    	ReviewerDTO reviewer = restTemplate.getForObject("/Reviewer/"+review.getReviewer_id(),ReviewerDTO.class);
    	model.addAttribute("ReviewerData",reviewer);
    	
    	 int currentYear  = Calendar.getInstance().get(Calendar.YEAR);
    	 int age = ((currentYear-(Integer.parseInt(reviewer.getBirthDay().substring(0,4))))/10)*10;
    	 model.addAttribute("reviewerAge", age);	//연령대 구하기    	 
    	 
    	ResponseEntity<List<CommentDTO>> commentsResponse = restTemplate.exchange("/Comments/"+review.get_id()
    								, HttpMethod.GET, null, new ParameterizedTypeReference<List<CommentDTO>>() {});
        List<CommentDTO> comments= commentsResponse.getBody();
    	model.addAttribute("CommentData",comments);
    	
    	}catch(Exception e){
    		e.printStackTrace();
    	}

    	ProductDTO product = restTemplate.getForObject("http://localhost:9092/getProductListByPrdSeq/"+review.getPrdSeq(), ProductDTO.class);
    	model.addAttribute("Product", product);
    	
    	// 댓글 페이징 버튼 컨트롤 
    	ResponseEntity<String> countResponse = restTemplate.exchange("/CommentsCount/"+ _id, HttpMethod.GET, null, 
    											new ParameterizedTypeReference<String>() {}); 
    	String count = countResponse.getBody(); 
    	model.addAttribute("Count", count);
    	
    	return "detailTest";
    }
    
    // 댓글 페이징 
    @GetMapping("reviewDetail/{id}/getMoreComments/{pageNo}")
    public String reviewDetail(Model model,@PathVariable("id") String _id, @PathVariable("pageNo") String pageNo) {	
    	
    	ResponseEntity<List<CommentDTO>> commentsResponse = restTemplate.exchange("/Comments/"+ _id 
    								+"/" + pageNo
    								, HttpMethod.GET, null, new ParameterizedTypeReference<List<CommentDTO>>() {});
        List<CommentDTO> comments= commentsResponse.getBody();
    	model.addAttribute("CommentData", comments);

    	return "detailComment";
    }
    
	/*
	 * @GetMapping("reviewDetail/{id}/getCommentsCount") public @ResponseBody String
	 * getCommentsCount(Model model, @PathVariable("id") String _id) {
	 * 
	 * ResponseEntity<String> countResponse =
	 * restTemplate.exchange("/CommentsCount/"+ _id, HttpMethod.GET, null, new
	 * ParameterizedTypeReference<String>() {}); String count =
	 * countResponse.getBody(); model.addAttribute("Count", count);
	 * System.out.println(count);
	 * 
	 * return count; }
	 */
    
}