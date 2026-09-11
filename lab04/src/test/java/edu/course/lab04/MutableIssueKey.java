package edu.course.lab04;

import java.util.Objects;

class MutableIssueKey {

    private int projectCode;
    private int value;

    MutableIssueKey(int projectCode, int value) {
        this.projectCode = projectCode;
        this.value = value;
    }

    void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MutableIssueKey that = (MutableIssueKey) object;
        return projectCode == that.projectCode && value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectCode, value);
    }
}
