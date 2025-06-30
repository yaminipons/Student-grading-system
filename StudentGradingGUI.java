import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class StudentGradingGUI extends JFrame implements ActionListener {
    JTextField nameField, rollField, marksField;
    JTextArea outputArea;
    JButton submitButton, clearButton, saveButton;

    public StudentGradingGUI() {
        setTitle("Student Grading System");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        inputPanel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Roll Number:"));
        rollField = new JTextField();
        inputPanel.add(rollField);

        inputPanel.add(new JLabel("Marks (comma-separated):"));
        marksField = new JTextField();
        inputPanel.add(marksField);

        submitButton = new JButton("Generate Report");
        submitButton.addActionListener(this);
        inputPanel.add(submitButton);

        clearButton = new JButton("Clear");
        clearButton.addActionListener(this);
        inputPanel.add(clearButton);

        add(inputPanel, BorderLayout.NORTH);

        // Output Area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Save Button
        saveButton = new JButton("Save Report to File");
        saveButton.addActionListener(this);
        add(saveButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            generateReport();
        } else if (e.getSource() == clearButton) {
            clearFields();
        } else if (e.getSource() == saveButton) {
            saveReportToFile();
        }
    }

    private void generateReport() {
        String name = nameField.getText();
        String rollText = rollField.getText();
        String marksText = marksField.getText();

        try {
            int roll = Integer.parseInt(rollText);
            String[] markStrings = marksText.split(",");
            int[] marks = new int[markStrings.length];

            int total = 0;
            for (int i = 0; i < marks.length; i++) {
                marks[i] = Integer.parseInt(markStrings[i].trim());
                total += marks[i];
            }

            double average = (double) total / marks.length;
            char grade;
            if (average >= 90) grade = 'A';
            else if (average >= 75) grade = 'B';
            else if (average >= 60) grade = 'C';
            else if (average >= 40) grade = 'D';
            else grade = 'F';

            StringBuilder report = new StringBuilder();
            report.append("----- Student Report -----\n");
            report.append("Name: ").append(name).append("\n");
            report.append("Roll Number: ").append(roll).append("\n");
            report.append("Marks: ").append(marksText).append("\n");
            report.append("Total: ").append(total).append("\n");
            report.append("Average: ").append(String.format("%.2f", average)).append("\n");
            report.append("Grade: ").append(grade).append("\n");

            outputArea.setText(report.toString());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please check marks and roll number.");
        }
    }

    private void clearFields() {
        nameField.setText("");
        rollField.setText("");
        marksField.setText("");
        outputArea.setText("");
    }

    private void saveReportToFile() {
        String report = outputArea.getText();
        if (report.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No report to save!");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("StudentReport.txt", true))) {
            writer.write(report);
            writer.write("\n-------------------------\n");
            JOptionPane.showMessageDialog(this, "Report saved to StudentReport.txt");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save the file.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentGradingGUI::new);
    }
}
