package com.pawmot.hajsback.transactionLog.routes.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

@Service
public class JWTProcessorFactory {
    public Processor create(String secret) {
        return ex -> {
            String token = ex.getIn().getHeader("JWT", String.class);

            final String issuer = "com.pawmot.hajsback.api";
            final String audience = "com.pawmot.hajsback";

            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer(issuer)
                    .withAudience(audience)
                    .build();
            DecodedJWT jwt = verifier.verify(token);

            ex.setProperty("userEmail", jwt.getClaims().get("com.pawmot.hajsback.user.email").asString());
        };
    }
}
