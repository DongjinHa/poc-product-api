package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.example.demo.ProductDTO;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private final MongoTemplate mongoTemplate;
	
	public ProductServiceImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
	public ProductDTO getProductListByPrdSeq(String prdSeq) {
		Query query = new Query()
				.addCriteria(Criteria.where("prdSeq").is(prdSeq));
		ProductDTO product = mongoTemplate.findOne(query, ProductDTO.class,"products");
		return product;
	}
	
	public List<ProductDTO> getProductListByPrdNm(String prdNm) {
		Query query = new Query()
				.addCriteria(Criteria.where("prdNm").regex(prdNm));
		return mongoTemplate.find(query, ProductDTO.class);
	}
	
	public List<ProductDTO> getProductListbyPrdSeqList(List<String> prdSeqList) {
		Query query = new Query()
				.addCriteria(Criteria.where("prdSeq").in(prdSeqList));
		return mongoTemplate.find(query, ProductDTO.class);
	}
	
}
