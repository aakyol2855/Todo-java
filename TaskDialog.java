import javax.swing.*;
import java.awt.*;
import java.time.*;

public class TaskDialog extends JDialog {
    private Task task = null;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JCheckBox deadlineCheckBox; 
    private JSpinner dateSpinner;
    private JSpinner timeSpinner;

    public TaskDialog(JFrame parent) {
        super(parent, "New Task", true);
        setupDialog();
    }

    private void setupDialog() {
        setLayout(new BorderLayout());
        setSize(400, 300);
        setLocationRelativeTo(getParent());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(40, 40, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Title
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel titleLabel = createLabel("Title:");
        mainPanel.add(titleLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        titleField = new JTextField(20);
        styleTextField(titleField);
        mainPanel.add(titleField, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel descLabel = createLabel("Description:");
        mainPanel.add(descLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        descriptionArea = new JTextArea(3, 20);
        styleTextArea(descriptionArea);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        mainPanel.add(scrollPane, gbc);

        // Deadline
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        deadlineCheckBox = new JCheckBox("Add Deadline");
        deadlineCheckBox.setForeground(Color.WHITE);
        deadlineCheckBox.setBackground(new Color(40, 40, 40));
        mainPanel.add(deadlineCheckBox, gbc);

        // Tarih ve Saat
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 1;
        dateSpinner = new JSpinner(new SpinnerDateModel());
        timeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd.MM.yyyy");
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        dateSpinner.setEditor(dateEditor);
        timeSpinner.setEditor(timeEditor);
        styleSpinner(dateSpinner);
        styleSpinner(timeSpinner);

        JPanel dateTimePanel = new JPanel(new FlowLayout());
        dateTimePanel.setBackground(new Color(40, 40, 40));
        dateTimePanel.add(dateSpinner);
        dateTimePanel.add(timeSpinner);
        gbc.gridx = 1;
        mainPanel.add(dateTimePanel, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(40, 40, 40));

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        styleButton(saveButton);
        styleButton(cancelButton);

        saveButton.addActionListener(e -> saveTask());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        return label;
    }

    private void styleTextField(JTextField field) {
        field.setBackground(new Color(60, 60, 60));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
    }

    private void styleTextArea(JTextArea area) {
        area.setBackground(new Color(60, 60, 60));
        area.setForeground(Color.WHITE);
        area.setCaretColor(Color.WHITE);
        area.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
    }

    private void styleSpinner(JSpinner spinner) {
        spinner.getEditor().getComponent(0).setBackground(new Color(60, 60, 60));
        spinner.getEditor().getComponent(0).setForeground(Color.WHITE);
        ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().setCaretColor(Color.WHITE);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(70, 70, 70));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(90, 90, 90)));
    }

    private void saveTask() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a title.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDateTime deadline = null;
        if (deadlineCheckBox.isSelected()) {
            java.util.Date dateValue = (java.util.Date) dateSpinner.getValue();
            java.util.Date timeValue = (java.util.Date) timeSpinner.getValue();
            
            LocalDateTime dateTime = dateValue.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
            LocalDateTime time = timeValue.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
            
            deadline = LocalDateTime.of(
                dateTime.getYear(),
                dateTime.getMonth(),
                dateTime.getDayOfMonth(),
                time.getHour(),
                time.getMinute()
            );
        }

        task = new Task(title, description, deadline);
        dispose();
    }

    public Task showDialog() {
        setVisible(true);
        return task;
    }
} 