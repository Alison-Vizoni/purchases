package br.com.alison.purchases;

import br.com.alison.purchases.domain.*;
import br.com.alison.purchases.domain.enums.ClientType;
import br.com.alison.purchases.repository.*;
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

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private ClientRepository clientRepository;

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

		State state_1 = new State(null, "Minas Gerais");
		State state_2 = new State(null, "São Paulo");

		City city_1 = new City(null, "Uberlândia", state_1);
		City city_2 = new City(null, "São Paulo", state_2);
		City city_3 = new City(null, "Campinas", state_2);

		state_1.getCities().addAll(Arrays.asList(city_1));
		state_2.getCities().addAll(Arrays.asList(city_2, city_3));

		stateRepository.saveAll(Arrays.asList(state_1, state_2));
		cityRepository.saveAll(Arrays.asList(city_1, city_2, city_3));

		Client client_1 = new Client(null,"Maria Silva", "maria@ex.com", "36378912377", ClientType.LEGAL_PERSON);
		client_1.getPhoneNumbers().addAll(Arrays.asList("27363323", "93838393"));

		Address address_1 = new Address(null, "Rua Flores", "300", "Apt 203", "Jardim", "38220834", client_1, city_1);
		Address address_2 = new Address(null, "Avenida Matos", "105", "Sala 800", "Centro", "38220834", client_1, city_2);

		client_1.getAdresses().addAll(Arrays.asList(address_1, address_2));

		clientRepository.saveAll(Arrays.asList(client_1));
		addressRepository.saveAll(Arrays.asList(address_1, address_2));

		Order order_1 = new Order();

	}
}
