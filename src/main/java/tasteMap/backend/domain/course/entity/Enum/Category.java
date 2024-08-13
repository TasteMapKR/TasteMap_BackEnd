package tasteMap.backend.domain.course.entity.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
public enum Category {
    DESSERT("디저트"),
    MEAL("식사"),
    MIXED("혼합");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }
    public static Category fromDisplayName(String displayName) {
        for (Category category : values()) {
            if (category.getDisplayName().equals(displayName)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown display name: " + displayName);
    }
}
