package com.ibm.grocery.core.security;

import com.ibm.grocery.domain.AppUser;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Signs RS256 JSON Web Tokens that the MicroProfile JWT runtime verifies with the matching public key.
 * The key pair is generated during the container image build, so no key material lives in the repository.
 */
@ApplicationScoped
public class JsonWebTokenIssuer {

    private static final Base64.Encoder URL_ENCODER = Base64.getUrlEncoder().withoutPadding();

    @Inject
    @ConfigProperty(name = "grocery.jwt.private-key-location", defaultValue = "/config/privateKey.pem")
    String privateKeyLocation;

    @Inject
    @ConfigProperty(name = "grocery.jwt.issuer", defaultValue = "https://grocery.local/auth")
    String issuer;

    @Inject
    @ConfigProperty(name = "grocery.jwt.expiry-seconds", defaultValue = "28800")
    long expirySeconds;

    private PrivateKey privateKey;

    @PostConstruct
    void loadPrivateKey() {
        try {
            String pem = Files.readString(Path.of(privateKeyLocation), StandardCharsets.UTF_8)
                    .replaceAll("-----BEGIN (.*)-----", "")
                    .replaceAll("-----END (.*)-----", "")
                    .replaceAll("\\s", "");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(pem));
            privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);
        } catch (IOException | GeneralSecurityException exception) {
            throw new IllegalStateException("Unable to load the JWT signing key", exception);
        }
    }

    public long expirySeconds() {
        return expirySeconds;
    }

    public String issueFor(AppUser user) {
        Instant now = Instant.now();
        JsonObject header = Json.createObjectBuilder()
                .add("alg", "RS256")
                .add("typ", "JWT")
                .build();
        JsonObject claims = Json.createObjectBuilder()
                .add("iss", issuer)
                .add("jti", UUID.randomUUID().toString())
                .add("sub", user.getUsername())
                .add("upn", user.getUsername())
                .add("name", user.getFullName())
                .add("groups", Json.createArrayBuilder().add(user.getRole().name()))
                .add("iat", now.getEpochSecond())
                .add("exp", now.plusSeconds(expirySeconds).getEpochSecond())
                .build();

        String signingInput = encode(header.toString()) + "." + encode(claims.toString());
        return signingInput + "." + URL_ENCODER.encodeToString(sign(signingInput));
    }

    private byte[] sign(String signingInput) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(signingInput.getBytes(StandardCharsets.UTF_8));
            return signature.sign();
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("Unable to sign the token", exception);
        }
    }

    private String encode(String value) {
        return URL_ENCODER.encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }
}
