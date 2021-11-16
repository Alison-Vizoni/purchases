package br.com.alison.purchases;

import br.com.alison.purchases.domain.Category;
import br.com.alison.purchases.domain.Product;
import br.com.alison.purchases.repository.CategoryRepository;
import br.com.alison.purchases.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class PurchasesApplication implements CommandLineRunner {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository productRepository;

	public static void main(String[] args) {
		SpringApplication.run(PurchasesApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Category category_1 = new Category(null, "Computing");
		Category category_2 = new Category(null, "Office");

		Product product_1 = new Product(null, "Computer", 2000.00);
		Product product_2 = new Product(null, "printer", 800.00);
		Product product_3 = new Product(null, "Mouse", 80.00);

		category_1.getProducts().addAll(Arrays.asList(product_1, product_2, product_3));
		category_2.getProducts().addAll(Arrays.asList(product_2));

		product_1.getCategories().addAll(Arrays.asList(category_1));
		product_2.getCategories().addAll(Arrays.asList(category_1, category_2));
		product_3.getCategories().addAll(Arrays.asList(category_1));

		categoryRepository.saveAll(Arrays.asList(category_1, category_2));
		productRepository.saveAll(Arrays.asList(product_1, product_2, product_3));
	}
}
