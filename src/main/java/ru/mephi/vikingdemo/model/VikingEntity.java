package ru.mephi.vikingdemo.model;

public record VikingEntity(
        int id,
        String name,
        int age,
        int heightCm,
        HairColor hairColor,
        BeardStyle beardStyle,
        String desc
) {
}
