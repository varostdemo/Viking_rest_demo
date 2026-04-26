package ru.mephi.vikingdemo.model;

public record EquipmentItemEntity(
        int id,
        int vikingId,
        String name,
        String quality
) {
}
