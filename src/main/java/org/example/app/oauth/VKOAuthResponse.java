package org.example.app.oauth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class VKOAuthResponse {
    private String access_token;
    private int expires_in;
    private long user_id;
    private String email;
}
