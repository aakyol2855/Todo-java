import javax.swing.*;
import java.awt.*;

public class TaskListRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(
        JList<?> list,
        Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus
    ) {
        JLabel label = (JLabel) super.getListCellRendererComponent(
            list, value, index, isSelected, cellHasFocus);
        
        Task task = (Task) value;
        
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(50, 50, 55)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        if (!isSelected) {
            label.setBackground(new Color(40, 40, 45));
            if (task.isCompleted()) {
                label.setForeground(new Color(150, 150, 150));
            } else {
                label.setForeground(new Color(220, 220, 220));
            }
        }
        
        if (task.hasDeadline() && task.isDeadlineApproaching()) {
            label.setForeground(new Color(255, 150, 150));
        }
        
        return label;
    }
} 