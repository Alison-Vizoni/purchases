package br.com.alison.purchases.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("integration_test_with_docker")
public class TestDockerConfig {

}
