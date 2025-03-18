import java.time.LocalDateTime;

public class Task {
    private String title;
    private String description;
    private LocalDateTime deadline;
    private boolean completed;
    private boolean notified;

    public Task(String title, String description, LocalDateTime deadline) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.completed = false;
        this.notified = false;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getDeadline() { return deadline; }
    public boolean isCompleted() { return completed; }
    public boolean isNotified() { return notified; }

    public void setCompleted(boolean completed) { this.completed = completed; }
    public void setNotified(boolean notified) { this.notified = notified; }

    public boolean hasDeadline() {
        return deadline != null;
    }

    public boolean isDeadlineApproaching() {
        if (!hasDeadline()) return false;
        LocalDateTime oneHourBefore = deadline.minusHours(1);
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(oneHourBefore) && now.isBefore(deadline);
    }

    @Override
    public String toString() {
        String status = completed ? "[✓] " : "[ ] ";
        String deadlineStr = hasDeadline() ? " (Due: " + deadline + ")" : "";
        return status + title + deadlineStr;
    }
} 