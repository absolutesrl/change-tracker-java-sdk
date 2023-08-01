package it.changetracker.sdk.core;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import javax.crypto.spec.SecretKeySpec;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TokenGeneratorTest {

    @Test
    void generateJwtTokenWithTableOnly() {
        var secret = "bOZtA2LoJdc1ptDbAk0A6yhr1uYjqjab";
        var tableName ="TESTTABLE";
        var jwt = TokenGenerator.generateToken(secret, tableName);

        assertNotNull(jwt);

        var alg = SignatureAlgorithm.HS256;

        var jwtParser = Jwts.parserBuilder().setSigningKey(new SecretKeySpec(secret.getBytes(), alg.getJcaName()))
                .build();

        var claim = jwtParser.parseClaimsJws(jwt).getBody();


        assertEquals(claim.get("table", String.class), tableName);
        //default token expiry is set to 5 minutes
        assertEquals(claim.getExpiration(), Date.from(claim.getIssuedAt().toInstant().plus(5, ChronoUnit.MINUTES)));
    }
    @Test
    void generateJwtTokenWithTableAndRowKey() {
        var secret = "bOZtA2LoJdc1ptDbAk0A6yhr1uYjqjab";
        var tableName ="TESTTABLE";
        var rowKey ="TESTROWKEY";
        var jwt = TokenGenerator.generateToken(secret, tableName, rowKey);

        assertNotNull(jwt);

        var alg = SignatureAlgorithm.HS256;

        var jwtParser = Jwts.parserBuilder().setSigningKey(new SecretKeySpec(secret.getBytes(), alg.getJcaName()))
                .build();

        var claim = jwtParser.parseClaimsJws(jwt).getBody();


        assertEquals(claim.get("table", String.class), tableName);
        assertEquals(claim.get("key", String.class), rowKey);

        //default token expiry is set to 5 minutes
        assertEquals(claim.getExpiration(), Date.from(claim.getIssuedAt().toInstant().plus(5, ChronoUnit.MINUTES)));
    }

    @Test
    void generateJwtTokenWithTableAndCustomDuration() {
        var secret = "bOZtA2LoJdc1ptDbAk0A6yhr1uYjqjab";
        var tableName ="TESTTABLE";
        var duration = 30;
        var jwt = TokenGenerator.generateToken(secret, tableName, duration);

        assertNotNull(jwt);

        var alg = SignatureAlgorithm.HS256;

        var jwtParser = Jwts.parserBuilder().setSigningKey(new SecretKeySpec(secret.getBytes(), alg.getJcaName()))
                .build();

        var claim = jwtParser.parseClaimsJws(jwt).getBody();


        assertEquals(claim.get("table", String.class), tableName);
        assertEquals(claim.getExpiration(), Date.from(claim.getIssuedAt().toInstant().plus(duration, ChronoUnit.MINUTES)));
    }
}
