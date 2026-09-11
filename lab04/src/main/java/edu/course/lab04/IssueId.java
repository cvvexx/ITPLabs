package edu.course.lab04;

import java.util.Objects;

public final class IssueId {

    private final int projectCode;
    private final int value;

    public IssueId(int projectCode, int value) {
        this.projectCode = projectCode;
        this.value = value;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        IssueId issueId = (IssueId) object;
        return projectCode == issueId.projectCode && value == issueId.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectCode, value);
    }
}
