package io.yapilayer.platform.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Entry point for the Yapilayer platform.
 *
 * <p>Wires together the platform layers (domain, application, API, security, persistence, webhooks,
 * observability) and all registered provider connectors. JPA repository/entity scanning is
 * module-package based and does not follow {@code scanBasePackages}, hence the explicit
 * annotations.
 */
@SpringBootApplication(scanBasePackages = "io.yapilayer")
@EnableJpaRepositories(basePackages = "io.yapilayer.platform.persistence")
@EntityScan(basePackages = "io.yapilayer.platform.persistence")
public class YapilayerApplication {

    public static void main(String[] args) {
        SpringApplication.run(YapilayerApplication.class, args);
    }
}
