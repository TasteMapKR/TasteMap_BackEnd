package tasteMap.backend.global.oauth2.dto;

import java.util.Map;

public class NaverResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    public NaverResponse(Map<String, Object> attribute) {
        // "response" 필드에서 실제 사용자 정보를 추출
        this.attribute = (Map<String, Object>) attribute.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }

    public String getProfileImage() {
        // "profile_image" 필드에서 프로필 이미지 URL을 가져옴
        return attribute.get("profile_image") != null ? attribute.get("profile_image").toString() : null;
    }
}
