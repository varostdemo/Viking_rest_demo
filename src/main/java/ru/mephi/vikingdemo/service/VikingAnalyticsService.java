/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ru.mephi.vikingdemo.service;

/**
 *
 * @author admin
 */
import java.util.Arrays;
import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.EquipmentItem;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class VikingAnalyticsService {

    private final VikingService vikingService;
    private final VikingFactory vikingFactory;

    public VikingAnalyticsService(VikingService vikingService, VikingFactory vikingFactory) {
        this.vikingService = vikingService;
        this.vikingFactory = vikingFactory;
    }

    public long countByAgeGreaterThan(int age) {
        return vikingService.findAll().stream()
                .filter(v -> v.age() > age)
                .count();
    }

    public long countByAgeLessThan(int age) {
        return vikingService.findAll().stream()
                .filter(v -> v.age() < age)
                .count();
    }

    public long countByAgeBetween(int min, int max) {
        return vikingService.findAll().stream()
                .filter(v -> v.age() >= min && v.age() <= max)
                .count();
    }

    public long countByAgeOutside(int min, int max) {
        return vikingService.findAll().stream()
                .filter(v -> v.age() < min || v.age() > max)
                .count();
    }

    public long countByBeardStyleAndHairColor(BeardStyle beardStyle, HairColor hairColor) {
        return vikingService.findAll().stream()
                .filter(v -> v.beardStyle() == beardStyle && v.hairColor() == hairColor)
                .count();
    }

    public String getAxesStatistics() {
        long oneAxe = vikingService.findAll().stream()
                .filter(v -> countAxes(v.equipment()) == 1)
                .count();

        long twoAxes = vikingService.findAll().stream()
                .filter(v -> countAxes(v.equipment()) == 2)
                .count();

        return "Викингов с 1 топором: " + oneAxe + ", с 2 топорами: " + twoAxes;
    }

    private long countAxes(List<EquipmentItem> equipment) {
        return equipment.stream()
                .filter(item -> item.name().equalsIgnoreCase("axe"))
                .count();
    }

    public Viking getRandomVikingTallerThan180() {
        List<Viking> tallVikings = vikingService.findAll().stream()
                .filter(v -> v.heightCm() > 180)
                .toList();

        if (tallVikings.isEmpty()) return null;
        Random random = new Random();
        return tallVikings.get(random.nextInt(tallVikings.size()));
    }

    public List<Viking> getVikingsWithLegendaryGear() {
        return vikingService.findAll().stream()
                .filter(v -> v.equipment().stream()
                        .anyMatch(item -> item.quality().equalsIgnoreCase("legendary")))
                .toList();
    }

    public List<Viking> getRedBeardedVikingsSortedByAge() {
        return vikingService.findAll().stream()
                .filter(v -> v.beardStyle() != BeardStyle.CLEAN_SHAVEN && v.hairColor() == HairColor.Red)
                .sorted(Comparator.comparingInt(Viking::age))
                .toList();
    }

    
    public Integer getMaxId() {
        Integer[] ids = vikingService.findAll().stream()
                .map(Viking::id)
                .toArray(Integer[]::new);

        return Arrays.stream(ids)
                .max(Integer::compareTo)
                .orElse(null);
    }

    public Integer[] getEvenIds() {
        Integer[] ids = vikingService.findAll().stream()
                .map(Viking::id)
                .toArray(Integer[]::new);

        return Arrays.stream(ids)
                .filter(id -> id % 2 == 0)
                .toArray(Integer[]::new);
    }

}
