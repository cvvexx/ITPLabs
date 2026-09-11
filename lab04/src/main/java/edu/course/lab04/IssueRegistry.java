package edu.course.lab04;

import edu.course.lab04.task.ProjectTask;

import java.util.HashMap;

public class IssueRegistry {

    private HashMap<IssueId, ProjectTask> registry = new HashMap<>();

    public void addTask(IssueId id, ProjectTask task) {
        registry.put(id, task);
    }

    public ProjectTask getTask(IssueId id) {
        return registry.get(id);
    }

    public void removeTask(IssueId id) {
        registry.remove(id);
    }

    public boolean containsTask(IssueId id) {
        return registry.containsKey(id);
    }

    public int size() {
        return registry.size();
    }

}