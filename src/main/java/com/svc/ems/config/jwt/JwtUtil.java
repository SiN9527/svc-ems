package com.svc.ems.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component // Spring Bean，方便依賴注入
public class JwtUtil {

    private final SecretKey secretKey; // JWT 簽名密鑰
    private final long expirationMillis; // JWT 過期時間

    // 從 application.properties 讀取密鑰和過期時間
    public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expirationMillis) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)); // Base64 解碼密鑰
        this.expirationMillis = expirationMillis;
    }

    // **生成 JWT Token**
    public String generateToken(String username, String type, List<String> roles) {
        return Jwts.builder()
                .setSubject(username) // 設定 Token 的持有者 (用戶名)
                .claim("roles", roles) // 儲存使用者角色資訊
                .claim("type", type) // 記錄該 Token 屬於 `USER` 還是 `MEMBER`
                .setIssuedAt(new Date()) // 設定發行時間
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis)) // 設定過期時間
                .signWith(secretKey, SignatureAlgorithm.HS256) // 使用 HMAC-SHA256 簽名
                .compact(); // 生成 JWT Token 字串
    }

    // **驗證 Token 是否有效 & 是否屬於指定身份**
    public boolean validateToken(String token, String expectedType) {
        try {
            Claims claims = extractAllClaims(token); // 解析 Token
            return expectedType.equals(claims.get("type", String.class)) && !isTokenExpired(token);
        } catch (Exception e) {
            return false; // Token 無效或已過期
        }
    }

    public String extractUserType(String token) {
        return extractClaim(token, claims -> claims.get("type", String.class));  // **提取 USER / MEMBER**
    }

    // **解析 Token 取得用戶名**
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // **解析 Token 取得角色列表**
    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> claims.get("roles", List.class));
    }

    // **通用方法：解析 Token 內的 Claims**
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // **通用方法：提取特定 Claim**
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    // **判斷 Token 是否過期**
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}

//public class JwtUtil {
//
//    private final AuthTokenRepository authTokenRepository;
//
//    // JWT 密鑰
//    private final String secret = "your_jwt_secret";
//
//    public JwtUtil(AuthTokenRepository authTokenRepository) {
//        this.authTokenRepository = authTokenRepository;
//    }
//
//    /**
//     * 生成 JWT Token
//     *
//     * @param username  使用者名稱
//     * @param roles     使用者角色
//     * @param userId    USER ID（可為 null）
//     * @param memberId  MEMBER ID（可為 null）
//     * @return 生成的 JWT Token
//     */
//    public String generateToken(String username, List<String> roles, Long userId, String memberId) {
//        Date now = new Date();
//        Date expiry = new Date(now.getTime() + 3600 * 1000); // 1 小時有效期
//
//        return Jwts.builder()
//                .setSubject(username)
//                .claim("roles", roles)
//                .claim("userId", userId)
//                .claim("memberId", memberId)
//                .setIssuedAt(now)
//                .setExpiration(expiry)
//                .signWith(SignatureAlgorithm.HS256, secret)
//                .compact();
//    }
//
//
//
//
//
//    /**
//     * 儲存 Token 到資料庫
//     *
//     * @param token     JWT Token
//     * @param userId    USER ID（可為 null）
//     * @param memberId  MEMBER ID（可為 null）
//     * @param expiry    Token 過期時間
//     */
//    public void saveToken(String token, Long userId, Long memberId, Date expiry) {
//        AuthToken authToken = new AuthToken();
//        authToken.setToken(token);
//        authToken.setUserId(userId);
//        authToken.setMemberId(memberId);
//        authToken.setExpiredAt(new Timestamp(expiry.getTime()));
//        authTokenRepository.save(authToken);
//    }
//
//    /**
//     * 驗證 JWT Token
//     *
//     * @param token JWT Token
//     * @return 是否有效
//     */
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder().build().setSigningKey(secret).parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//}