package com.Tech.Dafe.Modules.Proyecto.Proyectos.Export;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Service
public class FileSignatureService {

    /**
     * Clave secreta utilizada para generar el HMAC.
     *
     * Debe existir en application.properties
         
     */
    @Value("${dafe.export.secret}")
    private String secret;

    /**
     * Genera la firma HMAC SHA256 del contenido.
     */
    public String generateSignature(String payload) {

        try {

            Mac mac = Mac.getInstance("HmacSHA256");

            SecretKeySpec key =
                    new SecretKeySpec(
                            secret.getBytes(StandardCharsets.UTF_8),
                            "HmacSHA256"
                    );

            mac.init(key);

            byte[] hash =
                    mac.doFinal(
                            payload.getBytes(StandardCharsets.UTF_8)
                    );

            return Base64
                    .getEncoder()
                    .encodeToString(hash);

        }
        catch (Exception ex) {

            throw new RuntimeException(
                    "No fue posible generar la firma digital.",
                    ex
            );

        }

    }

    /**
     * Verifica que la firma recibida corresponda
     * exactamente al contenido.
     */
    public boolean verifySignature(
            String payload,
            String signature) {

        if (payload == null || signature == null) {
            return false;
        }

        String generated =
                generateSignature(payload);

        return MessageDigest.isEqual(
                generated.getBytes(StandardCharsets.UTF_8),
                signature.getBytes(StandardCharsets.UTF_8)
        );

    }

}