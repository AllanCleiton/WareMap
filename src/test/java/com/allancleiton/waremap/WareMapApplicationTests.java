package com.allancleiton.waremap;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.allancleiton.waremap.entities.Product;
import com.allancleiton.waremap.services.IntegrationService;

@SpringBootTest
class WareMapApplicationTests {

	@Test
	void contextLoads() {
		IntegrationService repo = new IntegrationService("src/main/resources/temp");
		
		try {
			List<Product> list = repo.LoadProductsOfxlsx("src/main/resources/temp");
			list.forEach(System.out::println);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
