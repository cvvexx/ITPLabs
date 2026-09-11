package edu.course.lab02;

public class ProjectTask {

    private final TaskId id;
    private final String title;
    private TaskStatus status;
    private int estimatedHours;

    public ProjectTask(TaskId id, String title, TaskStatus status, int estimatedHours) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title cannot be null or blank");
        }
        if (status == null) {
            throw new IllegalArgumentException("status cannot be null");
        }
        if (estimatedHours <= 0) {
            throw new IllegalArgumentException("estimatedHours must be positive");
        }
        this.id = id;
        this.title = title;
        this.status = status;
        this.estimatedHours = estimatedHours;
    }

    public void changeStatus(TaskStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("status cannot be null");
        }
        if (this.status == newStatus) {
            throw new IllegalStateException("this status is already set");
        }
        this.status = newStatus;
    }

    public boolean isCompleted() {
        return status == TaskStatus.COMPLETED;
    }

    public void increaseEstimate(int hours) {
        if (hours <= 0) {
            throw new IllegalArgumentException("hours must be positive");
        }
        if (hours > Integer.MAX_VALUE - estimatedHours) {
            throw new IllegalArgumentException("estimatedHours is too large");
        }
        estimatedHours += hours;
    }

    public TaskId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public int getEstimatedHours() {
        return estimatedHours;
    }
}
