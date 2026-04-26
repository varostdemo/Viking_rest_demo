package ru.mephi.vikingdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;

import java.util.List;

@RestController
@RequestMapping("/api/vikings")
@Tag(name = "Vikings", description = "Операции с викингами")
public class VikingController {
    private final VikingService vikingService;

    public VikingController(VikingService vikingService) {
        this.vikingService = vikingService;
    }

    @GetMapping
    @Operation(summary = "Получить список всех викингов")
    public List<Viking> getAll() {
        return vikingService.findAll();
    }

    @PostMapping
    @Operation(summary = "Добавить конкретного викинга")
    public Viking addCustom(@RequestBody Viking viking) {
        return vikingService.addViking(viking);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить викинга по ID")
    public void delete(@PathVariable int id) {
        vikingService.deleteViking(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить параметры викинга")
    public Viking update(@PathVariable int id, @RequestBody Viking updated) {
        return vikingService.updateViking(id, updated);
    }
}