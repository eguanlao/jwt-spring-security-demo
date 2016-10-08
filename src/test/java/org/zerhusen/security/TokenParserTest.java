package org.zerhusen.security;

import org.assertj.core.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenParserTest {

  private TokenParser tokenParser;

  @Before
  public void init() {
    tokenParser = new TokenParser();
    ReflectionTestUtils.setField(tokenParser, "expiration", 3600000L);
    ReflectionTestUtils.setField(tokenParser, "secret", "mySecret");
  }

  @Test
  public void testGenerateTokenGeneratesDifferentTokensForDifferentCreationDates() throws Exception {
    final Map<String, Object> claims = createClaims("2016-09-08T03:00:00");
    final String token = tokenParser.generateToken(claims);

    final Map<String, Object> claimsForLaterToken = createClaims("2016-09-08T08:00:00");
    final String laterToken = tokenParser.generateToken(claimsForLaterToken);

    assertThat(token).isNotEqualTo(laterToken);
  }

  private Map<String, Object> createClaims(String creationDate) {
    Map<String, Object> claims = new HashMap<>();
    claims.put(Claims.SUBJECT, "testUser");
    claims.put(Claims.AUDIENCE, "testAudience");
    claims.put(Claims.ISSUED_AT, DateUtil.parseDatetime(creationDate));
    return claims;
  }

}