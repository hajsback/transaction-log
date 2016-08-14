package com.pawmot.hajsback.transactionLog.routes;

import com.auth0.jwt.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class TestRoute extends SpringRouteBuilder {
    @Value("${security.secret}")
    private String secret;

    @Override
    public void configure() throws Exception {
        from("jms:queue:transations")
                .process(ex -> {
                    String jwt = ex.getIn().getHeader("JWT", String.class);

                    final String issuer = "ApiGateway";
                    final String audience = "Hajsback";

                    JWTVerifier verifier = new JWTVerifier(secret, audience, issuer);
                    Map<String, Object> claims = verifier.verify(jwt);

                    String email = (String)claims.get("com.pawmot.hajsback.user.email");

                    log.info("Received request from user {}", email);

                    ex.getIn().setBody("REPLY to " + email);
                });
    }
}
