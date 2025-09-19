package org.example.model.market;

import java.util.List;

public record OpeningHours(
        List<String> mon,
        List<String> tue,
        List<String> wed,
        List<String> thu,
        List<String> fri,
        List<String> sat,
        List<String> sun,
        String zone
) {}
