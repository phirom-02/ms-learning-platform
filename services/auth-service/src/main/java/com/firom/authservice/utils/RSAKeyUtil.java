package com.firom.authservice.utils;

import com.nimbusds.jose.jwk.RSAKey;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public final class RSAKeyUtil {

    public static RSAKey loadFromPem() {
        return loadFromPem(
                "services/auth-service/src/main/resources/keys/private_key.pem",
                "services/auth-service/src/main/resources/keys/public_key.pem"
        );
    }

    public static RSAKey loadFromPem(String privateKeyPath, String publicKeyPath) {
        try {
            RSAPrivateKey privateKey = loadPrivateKey(privateKeyPath);
            RSAPublicKey publicKey = loadPublicKey(publicKeyPath);

            return new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID("rsa-key") // optional
                    .build();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static RSAPrivateKey loadPrivateKey(String path) throws Exception {
        String key = Files.readString(Paths.get(path))
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) kf.generatePrivate(keySpec);
    }

    private static RSAPublicKey loadPublicKey(String path) throws Exception {
        String key = Files.readString(Paths.get(path))
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) kf.generatePublic(keySpec);
    }

}
