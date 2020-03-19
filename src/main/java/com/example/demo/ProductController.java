package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.ProductDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class ProductController {
	@Autowired
	private ProductService productService;
	
	@GetMapping("/getProductListByPrdSeq/{prdSeq}")
    public ProductDTO getProductListByPrdSeq(@PathVariable String prdSeq) {	
        return productService.getProductListByPrdSeq(prdSeq);
    }
	
	@GetMapping("/getProductListByPrdNm/{prdNm}")
    public List<ProductDTO> getProductListByPrdNm(@PathVariable String prdNm) {	
        return productService.getProductListByPrdNm(prdNm);
    }
	
    @PostMapping("/product")
    public List<ProductDTO> getProductListbyPrdSeqList(@RequestBody List<String> prdSeqList) throws JsonProcessingException {
    	return productService.getProductListbyPrdSeqList(prdSeqList);
    }
}
