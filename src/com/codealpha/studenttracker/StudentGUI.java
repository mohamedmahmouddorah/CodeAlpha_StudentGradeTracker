package com.codealpha.studenttracker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class StudentGUI extends JFrame {
    private JTextField idField, nameField, ageField, gradeField;
    private JComboBox<String> genderBox;
    private JTable table;
    private DefaultTableModel model;
    private JButton saveButton;
    private int editingRow = -1;

    private File file = new File("students.txt");

    public StudentGUI() {
        setTitle("📚 Student Grade Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 500);
        setLocationRelativeTo(null);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        // =====  Enter Panel  =====
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Student Tracker"));
        inputPanel.setBackground(new Color(245, 245, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(idLabel, gbc);

        idField = new JTextField();
        idField.setFont(fieldFont);
        gbc.gridx = 1; gbc.gridy = 0;
        inputPanel.add(idField, gbc);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(nameLabel, gbc);

        nameField = new JTextField();
        nameField.setFont(fieldFont);
        gbc.gridx = 1; gbc.gridy = 1;
        inputPanel.add(nameField, gbc);

        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(ageLabel, gbc);

        ageField = new JTextField();
        ageField.setFont(fieldFont);
        gbc.gridx = 1; gbc.gridy = 2;
        inputPanel.add(ageField, gbc);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(genderLabel, gbc);

        genderBox = new JComboBox<>(new String[]{"Male", "Female"});
        genderBox.setFont(fieldFont);
        gbc.gridx = 1; gbc.gridy = 3;
        inputPanel.add(genderBox, gbc);

        JLabel gradeLabel = new JLabel("Grade:");
        gradeLabel.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 4;
        inputPanel.add(gradeLabel, gbc);

        gradeField = new JTextField();
        gradeField.setFont(fieldFont);
        gbc.gridx = 1; gbc.gridy = 4;
        inputPanel.add(gradeField, gbc);

        // =====  Button Panel  =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton addButton = new JButton(" Add");
        JButton editButton = new JButton(" Edit");
        JButton showButton = new JButton(" Show Students");
        JButton summaryButton = new JButton(" Summary Report");        JButton deleteButton = new JButton(" Delete");
        JButton clearAllButton = new JButton(" Clear All");
        saveButton = new JButton(" Save");
        saveButton.setEnabled(false);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(showButton);
        buttonPanel.add(summaryButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearAllButton);

        // ===== Display=====
        String[] columns = {"ID", "Name", "Age", "Gender", "Grade"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setVisible(false);

        // ===== Events =====
        addButton.addActionListener(e -> addStudent());
        editButton.addActionListener(e -> editStudent());
        saveButton.addActionListener(e -> saveStudent());
        summaryButton.addActionListener(e -> showSummary());

        deleteButton.addActionListener(e -> deleteStudent());
        clearAllButton.addActionListener(e -> clearAllStudents());
        showButton.addActionListener(e -> {
            tableScroll.setVisible(true);
            revalidate();
            repaint();
        });

        // ===== Layout =====
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.WEST);
        add(buttonPanel, BorderLayout.SOUTH);
        add(tableScroll, BorderLayout.CENTER);

        // Load students if file exists
        loadStudents();
    }

    // Add New student
    private void addStudent() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String age = ageField.getText().trim();
        String gender = (String) genderBox.getSelectedItem();
        String grade = gradeField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || age.isEmpty() || grade.isEmpty()) {
            JOptionPane.showMessageDialog(this, " Please fill all fields!");
            return;
        }
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().equals(id)) {
                JOptionPane.showMessageDialog(this, "ID already exists!");
                return;
            }
        }
        if (!id.matches("\\d+") || !age.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "ID and Age must be numbers!");
            return;
        }
        if (!name.matches("[a-zA-Z ]+")) {
            JOptionPane.showMessageDialog(this, "Name must contain only letters!");
            return;
        }

        model.addRow(new Object[]{id, name, age, gender, grade});
        saveToFile();
        clearFields();
    }

    // Edit
    private void editStudent() {
        int selected = table.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to edit!");
            return;
        }
        // Return Value in form
        idField.setText(model.getValueAt(selected, 0).toString());
        nameField.setText(model.getValueAt(selected, 1).toString());
        ageField.setText(model.getValueAt(selected, 2).toString());
        genderBox.setSelectedItem(model.getValueAt(selected, 3).toString());
        gradeField.setText(model.getValueAt(selected, 4).toString());

        editingRow = selected;
        saveButton.setEnabled(true);
    }

    // Save Edits
    private void saveStudent() {
        if (editingRow == -1) return;

        model.setValueAt(idField.getText(), editingRow, 0);
        model.setValueAt(nameField.getText(), editingRow, 1);
        model.setValueAt(ageField.getText(), editingRow, 2);
        model.setValueAt(genderBox.getSelectedItem(), editingRow, 3);
        model.setValueAt(gradeField.getText(), editingRow, 4);

        saveToFile(); // <<<=== هنا بيتخزن بعد التعديل
        clearFields();
        editingRow = -1;
        saveButton.setEnabled(false);
    }
    // Summary Report
    private void showSummary() {
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No students available!");
            return;
        }

        double total = 0;
        double highest = Double.MIN_VALUE;
        double lowest = Double.MAX_VALUE;

        for (int i = 0; i < model.getRowCount(); i++) {
            try {
                double grade = Double.parseDouble(model.getValueAt(i, 4).toString());
                total += grade;
                if (grade > highest) highest = grade;
                if (grade < lowest) lowest = grade;
            } catch (NumberFormatException e) {
                // لو فيه جريد مش رقم نتجاهل
            }
        }

        double average = total / model.getRowCount();

        String report = "📊 Summary Report\n"
                + "Total Students: " + model.getRowCount() + "\n"
                + "Average Grade: " + String.format("%.2f", average) + "\n"
                + "Highest Grade: " + highest + "\n"
                + "Lowest Grade: " + lowest;

        JOptionPane.showMessageDialog(this, report, "Summary Report", JOptionPane.INFORMATION_MESSAGE);
    }

    // Deleting
    private void deleteStudent() {
        int selected = table.getSelectedRow();
        if (selected != -1) {
            model.removeRow(selected);
            saveToFile();
        } else {
            JOptionPane.showMessageDialog(this, "Select a student to delete!");
        }
    }

    // Clear all students
    private void clearAllStudents() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete ALL students?",
                "Confirm", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            model.setRowCount(0);
            saveToFile();
            JOptionPane.showMessageDialog(this, "All students deleted!");
        }
    }

    // Clear Boxes
    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        gradeField.setText("");
    }

    // Save students to file
    private void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                bw.write(model.getValueAt(i, 0) + "," +
                        model.getValueAt(i, 1) + "," +
                        model.getValueAt(i, 2) + "," +
                        model.getValueAt(i, 3) + "," +
                        model.getValueAt(i, 4));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load students from file
    private void loadStudents() {
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                model.addRow(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentGUI().setVisible(true));
    }
}
