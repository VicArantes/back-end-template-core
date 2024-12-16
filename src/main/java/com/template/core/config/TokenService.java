package com.template.core.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

/**
 * Serviço responsável por gerar, validar e extrair informações do usuário de um JSON WebToken (JWT).
 */
@Service
public class TokenService {

    /**
     * Chave secreta usada para assinar o JWT.
     */
    @Value("${template.jwt.secret}")
    private String jwtSecret;

    /**
     * Gera uma chave secreta HMAC a partir de um segredo codificado em Base64.
     * <p>
     * Este método utiliza a string {@code jwtSecret}, que deve estar codificada em Base64,
     * para decodificar e criar uma chave secreta compatível com algoritmos HMAC,
     * como HS256, HS384 ou HS512. A chave gerada será utilizada para assinar e validar tokens JWT.
     *
     * @return Uma instância de {@link javax.crypto.SecretKey} derivada do segredo codificado em Base64.
     * @throws IllegalArgumentException Se {@code jwtSecret} não for uma string válida em Base64
     *                                  ou não atender aos requisitos mínimos de tamanho do algoritmo.
     */
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * Valida um token JWT.
     *
     * @param token O token JWT a ser validado.
     * @return true se o token for válido, false caso contrário.
     */
    public Boolean validatesToken(String token) {
        try {
            Jwts.parser().verifyWith(this.getSecretKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extrai o ID do usuário do token JWT.
     *
     * @param token O token JWT do qual extrair o ID do usuário.
     * @return O ID do usuário extraído do token.
     */
    public Long getUserId(String token) {
        return Long.parseLong(Jwts.parser().verifyWith(this.getSecretKey()).build().parseSignedClaims(token).getPayload().getSubject());
    }

}
