package edu.course.lab05.task;

import java.util.Objects;

public class ProjectTask {

    private final TaskId id;
    private final String title;
    private final String category;
    private TaskStatus status;
    private int estimatedHours;

    public ProjectTask(TaskId id, String title, String category, TaskStatus status, int estimatedHours) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title cannot be null or blank");
        }
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("category cannot be null or blank");
        }
        if (status == null) {
            throw new IllegalArgumentException("status cannot be null");
        }
        if (estimatedHours <= 0) {
            throw new IllegalArgumentException("estimatedHours must be positive");
        }
        this.id = id;
        this.title = title;
        this.category = category;
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ProjectTask that = (ProjectTask) object;
        return estimatedHours == that.estimatedHours
                && Objects.equals(id, that.id)
                && Objects.equals(title, that.title)
                && Objects.equals(category, that.category)
                && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, category, status, estimatedHours);
    }

    @Override
    public String toString() {
        return "ProjectTask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", status=" + status +
                ", estimatedHours=" + estimatedHours +
                '}';
    }

    public TaskId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public int getEstimatedHours() {
        return estimatedHours;
    }
}
