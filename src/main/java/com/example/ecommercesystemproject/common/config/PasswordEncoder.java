package com.example.ecommercesystemproject.common.config;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {

    public String encode(String rawPassword) {
        // BCrypt: 같은 비밀번호를 넣어도 매번 다른 암호화 결과가 나오는 해시 알고리즘
        // MIN_COST : 암호화 강도인데, 나중에 쓰실때는 높은 값으로 사용 하시면 됩니다.
        return BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, rawPassword.toCharArray());
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        // 원문과 암호화된 값을 비교, 두 해시값을 직접 == 비교하면 안 되고 라이브러리가 제공하는 verify()로 비교해야 합니다.
        // BCrypt로 암호화하면 같은 "password123"을 넣어도 매번 다른 문자열이 나오기 때문임
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword);
        return result.verified;
    }
}
