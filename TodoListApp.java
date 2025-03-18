import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.util.*;
import javax.swing.border.*;
import javax.swing.Timer;

public class TodoListApp extends JFrame {
    private DefaultListModel<Task> pendingTasksModel;
    private DefaultListModel<Task> completedTasksModel;
    private JList<Task> pendingTasksList;
    private JList<Task> completedTasksList;
    private Timer timer;

    public TodoListApp() {
        setTitle("To-Do List Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(25, 25, 30));

        // Liste modelleri
        pendingTasksModel = new DefaultListModel<>();
        completedTasksModel = new DefaultListModel<>();

        // Listeler
        pendingTasksList = new JList<>(pendingTasksModel);
        completedTasksList = new JList<>(completedTasksModel);

        // Liste özelleştirmeleri
        customizeList(pendingTasksList, "Pending Tasks");
        customizeList(completedTasksList, "Completed Tasks");

        // Split Panel
        JSplitPane splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            createListPanel(completedTasksList, "Completed Tasks"),
            createListPanel(pendingTasksList, "Pending Tasks")
        );
        splitPane.setResizeWeight(0.5);
        splitPane.setBorder(null);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Alt panel
        JPanel bottomPanel = createBottomPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        bottomPanel.setBackground(new Color(30, 30, 35));
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Timer kontrolü
        initializeTimer();

        add(mainPanel);
        setVisible(true);
    }

    private void customizeList(JList<Task> list, String name) {
        list.setBackground(new Color(40, 40, 45));
        list.setForeground(new Color(220, 220, 220));
        list.setSelectionBackground(new Color(58, 91, 124));
        list.setSelectionForeground(Color.WHITE);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        list.setFixedCellHeight(35);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        list.setCellRenderer(new TaskListRenderer());
    }

    private JPanel createListPanel(JList<Task> list, String title) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(new Color(30, 30, 35));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(new Color(240, 240, 240));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(70, 70, 75)),
            BorderFactory.createEmptyBorder(0, 0, 10, 0)
        ));
        
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(40, 40, 45));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(new Color(30, 30, 30));

        JButton addButton = createStyledButton("Add Task");
        JButton deleteSelectedButton = createStyledButton("Delete Selected");
        JButton clearAllButton = createStyledButton("Clear All");
        JButton completeButton = createStyledButton("Complete");

        addButton.addActionListener(e -> addNewTask());
        deleteSelectedButton.addActionListener(e -> deleteSelectedTasks());
        clearAllButton.addActionListener(e -> clearAllTasks());
        completeButton.addActionListener(e -> completeSelectedTasks());

        panel.add(addButton);
        panel.add(deleteSelectedButton);
        panel.add(clearAllButton);
        panel.add(completeButton);

        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(48, 71, 94));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(58, 91, 124));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(48, 71, 94));
            }
        });
        
        return button;
    }

    private void addNewTask() {
        TaskDialog dialog = new TaskDialog(this);
        Task task = dialog.showDialog();
        if (task != null) {
            pendingTasksModel.addElement(task);
        }
    }

    private void deleteSelectedTasks() {
        int[] pendingIndices = pendingTasksList.getSelectedIndices();
        int[] completedIndices = completedTasksList.getSelectedIndices();

        // Tersten silme (indeks kayması olmaması için)
        for (int i = pendingIndices.length - 1; i >= 0; i--) {
            pendingTasksModel.remove(pendingIndices[i]);
        }
        for (int i = completedIndices.length - 1; i >= 0; i--) {
            completedTasksModel.remove(completedIndices[i]);
        }
    }

    private void clearAllTasks() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete all tasks?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION
        );
        
        if (result == JOptionPane.YES_OPTION) {
            pendingTasksModel.clear();
            completedTasksModel.clear();
        }
    }

    private void completeSelectedTasks() {
        int[] indices = pendingTasksList.getSelectedIndices();
        ArrayList<Task> tasksToMove = new ArrayList<>();

        for (int index : indices) {
            Task task = pendingTasksModel.getElementAt(index);
            tasksToMove.add(task);
        }

        for (Task task : tasksToMove) {
            pendingTasksModel.removeElement(task);
            task.setCompleted(true);
            completedTasksModel.addElement(task);
        }
    }

    private void checkDeadlines() {
        for (int i = 0; i < pendingTasksModel.size(); i++) {
            Task task = pendingTasksModel.getElementAt(i);
            if (task.hasDeadline() && !task.isNotified() && task.isDeadlineApproaching()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Warning: Only 1 hour left for task '" + task.getTitle() + "'!",
                    "Time Warning",
                    JOptionPane.WARNING_MESSAGE
                );
                task.setNotified(true);
            }
        }
    }

    private void initializeTimer() {
        timer = new Timer(60000, e -> checkDeadlines());
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TodoListApp());
    }
} 