package tasteMap.backend.domain.root.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RootDTO {
    @NotEmpty(message = " 루트 제목는 필수 입력 값입니다.")
    private String title;
    @NotEmpty(message = " 루트 내용는 필수 입력 값입니다.")
    private String content;
    @NotEmpty(message = " 루트 주소는 필수 입력 값입니다.")
    private String address;
}