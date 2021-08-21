package com.eastbabel.bo.login;
import lombok.Data;

@Data
public class TokenBo {
    private String authToken;
    private String refreshToken;

    public TokenBo(String authToken, String refreshToken) {
        this.authToken = authToken;
        this.refreshToken = refreshToken;
    }
}
