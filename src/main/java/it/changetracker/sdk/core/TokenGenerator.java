package it.changetracker.sdk.core;

import it.changetracker.sdk.utils.StringHelper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class TokenGenerator {
    public static String generateToken(String secret, String tableName) {
        return generateToken(secret, tableName, null, 5);
    }

    public static String generateToken(String secret, String tableName, String rowKey) {
        return generateToken(secret, tableName, rowKey, 5);
    }

    public static String generateToken(String secret, String tableName, int duration) {
        return generateToken(secret, tableName, null, duration);
    }

    public static String generateToken(String secret, String tableName, String rowKey, int duration) {
        // Create a test key suitable for the desired HMAC-SHA algorithm:
        var alg = SignatureAlgorithm.HS256;

        var currentDate = Instant.now();

        var builder = Jwts.builder()
                .setIssuedAt(Date.from(currentDate))
                .setHeaderParam("typ", "JWT")
                .setExpiration(Date.from(currentDate.plus(duration, ChronoUnit.MINUTES)))
                .claim("table", tableName).signWith(new SecretKeySpec(secret.getBytes(), alg.getJcaName()), alg);

        if (!StringHelper.isNullOrEmpty(rowKey))
            builder = builder.claim("key", rowKey);

        var jwt = builder.compact();

        return jwt;
    }
}
