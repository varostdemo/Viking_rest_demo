package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.*;
import ru.mephi.vikingdemo.service.VikingService;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import ru.mephi.vikingdemo.service.EquipmentFactory;

public class VikingDesktopFrame extends JFrame {

    private final VikingService vikingService;
    private final VikingTableModel tableModel;
    
    private JTextField nameField, ageField, heightField, idField;
    private JComboBox<HairColor> hairColorBox;
    private JComboBox<BeardStyle> beardStyleBox;
    private JTable vikingTable;
    
    public VikingDesktopFrame(VikingService vikingService) {
        this.vikingService = vikingService;
        this.tableModel = new VikingTableModel(vikingService);

        setTitle("Viking Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1200, 650));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Viking Demo", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18f));
        add(header, BorderLayout.NORTH);

        vikingTable = new JTable(tableModel);
        vikingTable.setRowHeight(28);
        add(new JScrollPane(vikingTable), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Ввод данных викинга"));
        
        inputPanel.add(new JLabel("ID (для удаления/обновления):"));
        idField = new JTextField();
        inputPanel.add(idField);
        
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);
        
        inputPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        inputPanel.add(ageField);
        
        inputPanel.add(new JLabel("Height (cm):"));
        heightField = new JTextField();
        inputPanel.add(heightField);
        
        inputPanel.add(new JLabel("Hair Color:"));
        hairColorBox = new JComboBox<>(HairColor.values());
        inputPanel.add(hairColorBox);
        
        inputPanel.add(new JLabel("Beard Style:"));
        beardStyleBox = new JComboBox<>(BeardStyle.values());
        inputPanel.add(beardStyleBox);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton createRandomButton = new JButton("Create Random Viking");
        createRandomButton.addActionListener(e -> onCreateViking());
        
        JButton addSpecificButton = new JButton("Add Specific Viking");
        addSpecificButton.addActionListener(e -> onAddSpecificViking());
        
        JButton deleteButton = new JButton("Delete Viking by ID");
        deleteButton.addActionListener(e -> onDeleteViking());
        
        JButton updateButton = new JButton("Update Viking by ID");
        updateButton.addActionListener(e -> onUpdateViking());
        
        buttonPanel.add(createRandomButton);
        buttonPanel.add(addSpecificButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(inputPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(southPanel, BorderLayout.SOUTH);
        
        refreshTable();
    }
    
    private void onCreateViking() {
        Viking viking = vikingService.createRandomViking();
        refreshTable();
        JOptionPane.showMessageDialog(this, "Создан викинг: " + viking.name() + " (ID: " + viking.id() + ")");
    }
    
    private void onAddSpecificViking() {
        try {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Введите имя");
                return;
            }
            
            int age = Integer.parseInt(ageField.getText().trim());
            int height = Integer.parseInt(heightField.getText().trim());
            HairColor hairColor = (HairColor) hairColorBox.getSelectedItem();
            BeardStyle beardStyle = (BeardStyle) beardStyleBox.getSelectedItem();
            
            Viking viking = new Viking(0, name, age, height, hairColor, beardStyle, List.of(
            EquipmentFactory.createItem(),
            EquipmentFactory.createItem()));
            Viking created = vikingService.addViking(viking);
            
            refreshTable();
            clearInputFields();
            JOptionPane.showMessageDialog(this, "Викинг добавлен: " + name + " (ID: " + created.id() + ")");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ошибка: возраст и рост должны быть числами");
        }
    }
    
    private void onDeleteViking() {
        try {
            String idText = idField.getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Введите ID викинга для удаления");
                return;
            }
            
            int id = Integer.parseInt(idText);
            vikingService.deleteViking(id);
            
            refreshTable();
            clearInputFields();
            JOptionPane.showMessageDialog(this, "Викинг с ID " + id + " удален");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ошибка: ID должен быть числом");
        }
    }
    
    private void onUpdateViking() {
        try {
            String idText = idField.getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Введите ID викинга для обновления");
                return;
            }
            
            int id = Integer.parseInt(idText);
            String name = nameField.getText().trim();

            Viking existing = vikingService.findAll().stream()
                    .filter(v -> v.id() == id)
                    .findFirst()
                    .orElse(null);
            
            if (existing == null) {
                JOptionPane.showMessageDialog(this, "Викинг с ID " + id + " не найден");
                return;
            }
            
            String newName = name.isEmpty() ? existing.name() : name;
            int newAge = ageField.getText().trim().isEmpty() ? existing.age() : Integer.parseInt(ageField.getText().trim());
            int newHeight = heightField.getText().trim().isEmpty() ? existing.heightCm() : Integer.parseInt(heightField.getText().trim());
            HairColor newHair = (HairColor) hairColorBox.getSelectedItem();
            BeardStyle newBeard = (BeardStyle) beardStyleBox.getSelectedItem();
            
            Viking updatedData = new Viking(id, newName, newAge, newHeight, newHair, newBeard, existing.equipment());
            Viking updated = vikingService.updateViking(id, updatedData);
            
            if (updated != null) {
                refreshTable();
                clearInputFields();
                JOptionPane.showMessageDialog(this, "Викинг с ID " + id + " обновлен");
            } else {
                JOptionPane.showMessageDialog(this, "Викинг не найден");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ошибка: возраст, рост и ID должны быть числами");
        }
    }
    
    private void clearInputFields() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        heightField.setText("");
        hairColorBox.setSelectedIndex(0);
        beardStyleBox.setSelectedIndex(0);
    }
    
    private void refreshTable() {
        tableModel.refresh();
    }
    
    public void removeViking(int id) {
        refreshTable();
    }

    public void updateViking(Viking viking) {
        refreshTable();
    }

    public void addNewViking(Viking viking) {
        refreshTable();
    }
}