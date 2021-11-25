package br.com.alison.purchases;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PurchasesApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(PurchasesApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
