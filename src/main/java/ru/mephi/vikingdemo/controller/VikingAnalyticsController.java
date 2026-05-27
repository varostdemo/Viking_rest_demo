/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ru.mephi.vikingdemo.controller;

/**
 *
 * @author admin
 */
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingAnalyticsService;
import java.util.List;
import ru.mephi.vikingdemo.service.VikingService;

@RestController
@RequestMapping("/api/analytics")
@Tag(name = "Analytics", description = "Аналитические операции с викингами (лямбда-функции)")
public class VikingAnalyticsController {

    private final VikingAnalyticsService analyticsService;
    private final VikingService vikingService;

    public VikingAnalyticsController(VikingAnalyticsService analyticsService, VikingService vikingService) {
        this.analyticsService = analyticsService;
        this.vikingService = vikingService;
    }

    @GetMapping("/count/age/greater/{age}")
    public long countAgeGreater(@PathVariable int age) {
        return analyticsService.countByAgeGreaterThan(age);
    }

    @GetMapping("/count/age/less/{age}")
    public long countAgeLess(@PathVariable int age) {
        return analyticsService.countByAgeLessThan(age);
    }

    @GetMapping("/count/age/between")
    public long countAgeBetween(@RequestParam int min, @RequestParam int max) {
        return analyticsService.countByAgeBetween(min, max);
    }

    @GetMapping("/count/age/outside")
    public long countAgeOutside(@RequestParam int min, @RequestParam int max) {
        return analyticsService.countByAgeOutside(min, max);
    }

    @GetMapping("/count/beard-and-hair")
    public long countByBeardAndHair(@RequestParam BeardStyle beard, @RequestParam HairColor hair) {
        return analyticsService.countByBeardStyleAndHairColor(beard, hair);
    }

    @GetMapping("/axes-statistics")
    public long getAxesStatistics() {
        return analyticsService.getAxesStatistics();
    }

    @GetMapping("/random-tall")
    public Viking getRandomTallViking() {
        return analyticsService.getRandomVikingTallerThan180();
    }

    @GetMapping("/legendary-gear")
    public List<Viking> getVikingsWithLegendaryGear() {
        return analyticsService.getVikingsWithLegendaryGear();
    }

    @GetMapping("/red-bearded/sorted-by-age")
    public List<Viking> getRedBeardedSortedByAge() {
        return analyticsService.getRedBeardedVikingsSortedByAge();
    }

    @GetMapping("/max-id")
    public Integer getMaxId() {
        return analyticsService.getMaxId();
    }

    @GetMapping("/even-ids")
    public Integer[] getEvenIds() {
        return analyticsService.getEvenIds();
    }

    @PostMapping("/generate40")
    public String generate40Vikings() {
        List<Viking> newVikings = vikingService.generateManyRandomVikings(40);
        return "Сгенерировано " + newVikings.size() + " викингов";
    }
}
