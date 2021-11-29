package br.com.alison.purchases.service;

import br.com.alison.purchases.domain.*;
import br.com.alison.purchases.domain.enums.ClientType;
import br.com.alison.purchases.domain.enums.PaymentStatus;
import br.com.alison.purchases.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

@Service
public class DBService {

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

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public void instantianceTestDatabase() throws ParseException {
        Category category_1 = new Category(null, "Computing");
        Category category_2 = new Category(null, "Office");
        Category category_3 = new Category(null, "clothing bed and bath");
        Category category_4 = new Category(null, "Eletronic");
        Category category_5 = new Category(null, "Garden");
        Category category_6 = new Category(null, "Decor");
        Category category_7 = new Category(null, "Perfumery");

        Product product_1 = new Product(null, "Computer", 2000.00);
        Product product_2 = new Product(null, "printer", 800.00);
        Product product_3 = new Product(null, "Mouse", 80.00);
        Product product_4 = new Product(null, "Office table", 80.00);
        Product product_5 = new Product(null, "Towel", 80.00);
        Product product_6 = new Product(null, "Quilt", 80.00);
        Product product_7 = new Product(null, "TV", 80.00);
        Product product_8 = new Product(null, "Cutter", 80.00);
        Product product_9 = new Product(null, "lampshade", 80.00);
        Product product_10 = new Product(null, "Pen", 80.00);
        Product product_11 = new Product(null, "Shampoo", 80.00);

        category_1.getProducts().addAll(Arrays.asList(product_1, product_2, product_3));
        category_2.getProducts().addAll(Arrays.asList(product_2, product_4));
        category_3.getProducts().addAll(Arrays.asList(product_5, product_6));
        category_4.getProducts().addAll(Arrays.asList(product_1, product_2, product_3, product_7));
        category_5.getProducts().addAll(Arrays.asList(product_8));
        category_6.getProducts().addAll(Arrays.asList(product_9, product_10));
        category_7.getProducts().addAll(Arrays.asList(product_11));

        product_1.getCategories().addAll(Arrays.asList(category_1, category_4));
        product_2.getCategories().addAll(Arrays.asList(category_1, category_2, category_4));
        product_3.getCategories().addAll(Arrays.asList(category_1, category_4));
        product_4.getCategories().addAll(Arrays.asList(category_2));
        product_5.getCategories().addAll(Arrays.asList(category_3));
        product_6.getCategories().addAll(Arrays.asList(category_3));
        product_7.getCategories().addAll(Arrays.asList(category_4));
        product_8.getCategories().addAll(Arrays.asList(category_5));
        product_9.getCategories().addAll(Arrays.asList(category_6));
        product_10.getCategories().addAll(Arrays.asList(category_6));
        product_11.getCategories().addAll(Arrays.asList(category_7));

        categoryRepository.saveAll(Arrays.asList(category_1, category_2, category_3, category_4, category_5, category_6,
                category_7));
        productRepository.saveAll(Arrays.asList(product_1, product_2, product_3, product_4, product_5, product_6, product_7,
                product_8, product_9, product_10, product_11));

        State state_1 = new State(null, "Minas Gerais");
        State state_2 = new State(null, "São Paulo");

        City city_1 = new City(null, "Uberlândia", state_1);
        City city_2 = new City(null, "São Paulo", state_2);
        City city_3 = new City(null, "Campinas", state_2);

        state_1.getCities().addAll(Arrays.asList(city_1));
        state_2.getCities().addAll(Arrays.asList(city_2, city_3));

        stateRepository.saveAll(Arrays.asList(state_1, state_2));
        cityRepository.saveAll(Arrays.asList(city_1, city_2, city_3));

        Client client_1 = new Client(null,"Maria Silva", "alison.vizoni@gmail.com", "36378912377",
                ClientType.LEGAL_PERSON);client_1.getPhoneNumbers().addAll(Arrays.asList("27363323", "93838393"));

        Address address_1 = new Address(null, "Rua Flores", "300", "Apt 203",
                "Jardim", "38220834", client_1, city_1);
        Address address_2 = new Address(null, "Avenida Matos", "105", "Sala 800",
                "Centro", "38220834", client_1, city_2);

        client_1.getAdresses().addAll(Arrays.asList(address_1, address_2));

        clientRepository.saveAll(Arrays.asList(client_1));
        addressRepository.saveAll(Arrays.asList(address_1, address_2));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Order order_1 = new Order(null, simpleDateFormat.parse("30/09/2017 10:32"), client_1, address_1);
        Order order_2 = new Order(null, simpleDateFormat.parse("10/10/2017 19:35"), client_1, address_2);

        Payment cardPayment = new CardPayment(null, PaymentStatus.OK, order_1, 6);
        order_1.setPayment(cardPayment);

        Payment ticketPayment = new TicketPayment(null, PaymentStatus.PENDING, order_2,
                simpleDateFormat.parse("20/10/2017 00:00"), null);
        order_2.setPayment(ticketPayment);

        client_1.getOrders().addAll(Arrays.asList(order_1, order_2));

        orderRepository.saveAll(Arrays.asList(order_1, order_2));
        paymentRepository.saveAll(Arrays.asList(cardPayment, ticketPayment));

        OrderItem orderItem_1 = new OrderItem(order_1, product_1, 0.00, 1, 2000.00);
        OrderItem orderItem_2 = new OrderItem(order_1, product_3, 0.00, 2, 80.00);
        OrderItem orderItem_3 = new OrderItem(order_2, product_2, 100.00, 1, 800.00);

        order_1.getItems().addAll(Arrays.asList(orderItem_1, orderItem_2));
        order_2.getItems().addAll(Arrays.asList(orderItem_3));

        product_1.getItems().addAll(Arrays.asList(orderItem_1));
        product_2.getItems().addAll(Arrays.asList(orderItem_3));
        product_3.getItems().addAll(Arrays.asList(orderItem_2));

        orderItemRepository.saveAll(Arrays.asList(orderItem_1, orderItem_2, orderItem_3));
    }
}
