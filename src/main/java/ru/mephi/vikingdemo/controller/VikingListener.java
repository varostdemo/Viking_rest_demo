/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ru.mephi.vikingdemo.controller;

import org.springframework.stereotype.Component;
import ru.mephi.vikingdemo.gui.VikingDesktopFrame;
import ru.mephi.vikingdemo.model.Viking;
import javax.swing.*;

/**
 *
 * @author test2023
 */
@Component
public class VikingListener {
    private VikingDesktopFrame gui;

    public void setGui(VikingDesktopFrame gui) {
        this.gui = gui;
    }

    public void addViking (Viking viking) {
        if (gui != null) {
            SwingUtilities.invokeLater(() -> gui.addNewViking(viking));
        }
    }

    public void deleteViking (int id) {
        if (gui != null) {
            SwingUtilities.invokeLater(() -> gui.removeViking(id));
        }
    }

    public void updateViking(Viking viking) {
        if (gui != null) {
            SwingUtilities.invokeLater(() -> gui.updateViking(viking));
        }
    }
}