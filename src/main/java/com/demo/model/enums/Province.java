package com.demo.model.enums;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;

@Getter
public enum Province {
    MADRID("Madrid"),
    BARCELONA("Barcelona"),
    VALENCIA("Valencia"),
    SEVILLA("Sevilla"),
    MALAGA("Málaga"),
    BILBAO("Bilbao"),
    ASTURIAS("Asturias"),
    ALICANTE("Alicante"),
    ZARAGOZA("Zaragoza"),
    TOLEDO("Toledo"),
    JAEN("Jaén");

    private final String label;

    Province(String label) {
        this.label = label;
    }

    public static List<String> getLabels() {
        return Arrays.stream(values())
                .map(Province::getLabel)
                .toList();
    }

    @Override
    public String toString() {
        return label;
    }
}
