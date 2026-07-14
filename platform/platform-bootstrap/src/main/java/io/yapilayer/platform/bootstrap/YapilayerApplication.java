package io.yapilayer.platform.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Yapilayer platform.
 *
 * <p>Wires together the platform layers (domain, application, API, security,
 * persistence, webhooks, observability) and all registered provider connectors.
 */
@SpringBootApplication(scanBasePackages = "io.yapilayer")
public class YapilayerApplication {

    public static void main(String[] args) {
        SpringApplication.run(YapilayerApplication.class, args);
    }
}
