package ru.timdzha.parts.entities;

public enum PartCategory {
    MOTHERBOARD("материнская плата", true),
    CPU("процессор", true),
    SSD("SSD диск", true),
    RAM("память", true),
    BOX("корпус", true),
    HDD("HDD диск", false),
    VIDEO_CARD("видеокарта", false),
    AUDIO_CARD ("звуковая карта", false),
    BOX_LIGHT("подсветка корпуса", false);

    PartCategory(String s, Boolean b) {
        this.name = s;
        this.isMainPart = b;
    }

    private String name;
    private Boolean isMainPart;

    public String getName() {
        return name;
    }

    public Boolean getIsMainPart() {
        return isMainPart;
    }

    public static int countMainCategories() {
        int result = 0;
        for (PartCategory partCategory : PartCategory.values()) {
            if (partCategory.isMainPart) result++;
        }
        return result;
    }
}
