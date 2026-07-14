package io.yapilayer.provider.mockbank.simulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Standalone simulated ASPSP ("fake bank").
 *
 * <p>Runs as its own service, separate from the Yapilayer platform, exposing
 * OAuth 2.0 / consent / account / payment endpoints that the mock-bank-connector
 * integrates against — so connector code exercises a realistic bank boundary
 * rather than an in-process stub.
 */
@SpringBootApplication
public class MockBankSimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MockBankSimulatorApplication.class, args);
    }
}
