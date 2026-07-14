package io.yapilayer.provider.mockbank.simulator.consent;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/** In-memory store of AIS and PIS consents, keyed by consent id. */
@Component
public class ConsentStore {

    /** OB-style consent statuses used by the simulator. */
    public enum Status {
        AWAITING_AUTHORISATION,
        AUTHORISED,
        REJECTED
    }

    public record SimConsent(
            String id, Type type, Status status, List<String> permissions, Instant expiresAt) {

        public enum Type {
            AIS,
            PIS
        }
    }

    private final Map<String, SimConsent> consents = new ConcurrentHashMap<>();

    public SimConsent createAis(List<String> permissions, Instant expiresAt) {
        SimConsent consent =
                new SimConsent(
                        "aisc-" + UUID.randomUUID(),
                        SimConsent.Type.AIS,
                        Status.AWAITING_AUTHORISATION,
                        List.copyOf(permissions),
                        expiresAt);
        consents.put(consent.id(), consent);
        return consent;
    }

    public SimConsent createPis(Instant expiresAt) {
        SimConsent consent =
                new SimConsent(
                        "pisc-" + UUID.randomUUID(),
                        SimConsent.Type.PIS,
                        Status.AWAITING_AUTHORISATION,
                        List.of(),
                        expiresAt);
        consents.put(consent.id(), consent);
        return consent;
    }

    public Optional<SimConsent> byId(String id) {
        return Optional.ofNullable(consents.get(id));
    }

    public Optional<SimConsent> decide(String id, boolean approved) {
        return Optional.ofNullable(
                consents.computeIfPresent(
                        id,
                        (k, c) ->
                                new SimConsent(
                                        c.id(),
                                        c.type(),
                                        approved ? Status.AUTHORISED : Status.REJECTED,
                                        c.permissions(),
                                        c.expiresAt())));
    }
}
