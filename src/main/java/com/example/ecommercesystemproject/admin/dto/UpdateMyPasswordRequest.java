package com.example.ecommercesystemproject.admin.dto;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateMyPasswordRequest {
    @NotBlank(message = "기존 패스워드를 입력하세요")
    private String oldPassword;

    @NotBlank(message = "변경 할 패스워드를 입력하세요")
    @Size(min = 8, max = 255, message = "비밀번호는 최소 8글자 이상 최대 255글자까지 입력 가능합니다." )
    private String newPassword;

    @NotBlank(message = "검증 패스워드를 입력하세요")
    @Size(min = 8, max = 255, message = "비밀번호는 최소 8글자 이상 최대 255글자까지 입력 가능합니다." )
    private String checkPassword;

    @JsonIgnore // 💡 "이 메서드는 JSON으로 변환하거나 유추할 때 없는 셈 쳐라!" 라는 뜻
    public boolean isDifferentPassword() {
        return !this.newPassword.equals(this.checkPassword);
    }
}
