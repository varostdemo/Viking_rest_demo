package ru.mephi.vikingdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.controller.VikingListener;
import ru.mephi.vikingdemo.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class VikingService {
    private final Map<Integer, Viking> vikingSlovar = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);
    private final VikingFactory vikingFactory;
    
    @Autowired
    private VikingListener vikingListener;

    public VikingService(VikingFactory vikingFactory) {
        this.vikingFactory = vikingFactory;
    }

    public List<Viking> findAll() {
        return new ArrayList<>(vikingSlovar.values());
    }

    public Viking createRandomViking() {
        Viking randomViking = vikingFactory.createRandomViking();
        return addViking(randomViking);
    }

    public Viking addViking(Viking entity) {
        int id = idCounter.getAndIncrement();

        Viking newViking = new Viking(
                id,
                entity.name(),
                entity.age(),
                entity.heightCm(),
                entity.hairColor(),
                entity.beardStyle(),
                entity.equipment()
        );

        vikingSlovar.put(id, newViking);
        vikingListener.addViking(newViking);
        return newViking;
    }

    public void deleteViking(int id) {
        if (vikingSlovar.remove(id) != null) {
            vikingListener.deleteViking(id);
        }
    }

    public Viking updateViking(int id, Viking updatedData) {
        Viking old = vikingSlovar.get(id);
        if (old == null) {
            return null;
        }

        String name = (updatedData.name() != null && !updatedData.name().isEmpty()) 
                ? updatedData.name() : old.name();
        int age = (updatedData.age() != 0) ? updatedData.age() : old.age();
        int height = (updatedData.heightCm() != 0) ? updatedData.heightCm() : old.heightCm();
        HairColor hair = (updatedData.hairColor() != null) 
                ? updatedData.hairColor() : old.hairColor();
        BeardStyle beard = (updatedData.beardStyle() != null) 
                ? updatedData.beardStyle() : old.beardStyle();
        List<EquipmentItem> equipment = (updatedData.equipment() != null && !updatedData.equipment().isEmpty()) 
                ? updatedData.equipment() : old.equipment();

        Viking updatedViking = new Viking(id, name, age, height, hair, beard, equipment);
        vikingSlovar.put(id, updatedViking);
        vikingListener.updateViking(updatedViking);
        return updatedViking;
    }
}