package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;
import ru.mephi.vikingdemo.model.*;
import ru.mephi.vikingdemo.service.VikingAnalyticsService;


public class VikingDesktopFrame extends JFrame {

    private final VikingService vikingService;
    private final VikingTableModel tableModel = new VikingTableModel();
    private final VikingAnalyticsService analyticsService;
    
    public VikingDesktopFrame(VikingService vikingService, VikingAnalyticsService analyticsService) {
        this.vikingService = vikingService;
        this.analyticsService = analyticsService;
        
        setTitle("Viking Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1000, 420));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Viking Demo", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18f));
        add(header, BorderLayout.NORTH);

        JTable vikingTable = new JTable(tableModel);
        vikingTable.setRowHeight(28);
        add(new JScrollPane(vikingTable), BorderLayout.CENTER);

        JButton createButton = new JButton("Create random viking");
        createButton.addActionListener(event -> onCreateViking());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(createButton);
        add(bottomPanel, BorderLayout.SOUTH);
        
        JButton generate40Button = new JButton("Generate 40 random vikings");
        generate40Button.addActionListener(event -> onGenerate40());
        bottomPanel.add(generate40Button);
        
        JButton statsButton = new JButton("Show statistics");
        statsButton.addActionListener(event -> showAllStatistics());
        bottomPanel.add(statsButton);

        onInit();
    }

    private void onCreateViking() {
        Viking viking = vikingService.createRandomViking();
        tableModel.addViking(viking);
    }
    
    public void addNewViking(Viking viking){
        tableModel.addViking(viking);
    }

    private void onInit() {
        List<Viking> all = vikingService.findAll();
        if (!all.isEmpty()){
            for (Viking viking : all) {
                tableModel.addViking(viking);
            }
        }
    }
    
    private void onGenerate40() {
        List<Viking> newVikings = vikingService.generateManyRandomVikings(40);
        refreshTable();
        JOptionPane.showMessageDialog(this, "Создано " + newVikings.size() + " викингов");
    }
    
    private void refreshTable() {
        tableModel.clear();
        vikingService.findAll().forEach(tableModel::addViking);
    }
    
    private void showAllStatistics() {
        long ageGreater30 = analyticsService.countByAgeGreaterThan(30);
        long ageLess20 = analyticsService.countByAgeLessThan(20);
        long ageBetween25_35 = analyticsService.countByAgeBetween(25, 35);
        long ageOutside18_40 = analyticsService.countByAgeOutside(18, 40);
        long braidedRed = analyticsService.countByBeardStyleAndHairColor(BeardStyle.BRAIDED, HairColor.Red);
        String axesStats = analyticsService.getAxesStatistics();

        Viking randomTall = analyticsService.getRandomVikingTallerThan180();
        List<Viking> legendaryVikings = analyticsService.getVikingsWithLegendaryGear();
        List<Viking> redBeardedSorted = analyticsService.getRedBeardedVikingsSortedByAge();

        Integer maxId = analyticsService.getMaxId();
        Integer[] evenIds = analyticsService.getEvenIds();

        StringBuilder sb = new StringBuilder();
        sb.append("Age > 30: ").append(ageGreater30).append("\n");
        sb.append("Age < 20: ").append(ageLess20).append("\n");
        sb.append("Age 25-35: ").append(ageBetween25_35).append("\n");
        sb.append("Age outside 18-40: ").append(ageOutside18_40).append("\n");
        sb.append("Braided + Red hair: ").append(braidedRed).append("\n");
        sb.append(axesStats).append("\n\n");
        sb.append("Random Viking >180cm: ").append(randomTall != null ? randomTall.name() : "none").append("\n\n");
        sb.append("Vikings with Legendary gear: ").append(legendaryVikings.size()).append("\n");
        sb.append("Red bearded sorted by age: ").append(redBeardedSorted.size()).append("\n\n");
        sb.append("Max ID: ").append(maxId != null ? maxId : "none").append("\n");
        sb.append("Even IDs: ").append(Arrays.toString(evenIds)).append("\n");

        JOptionPane.showMessageDialog(this, sb.toString(), "Statistics", JOptionPane.INFORMATION_MESSAGE);
    }
}
