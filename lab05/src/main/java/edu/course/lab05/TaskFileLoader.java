package edu.course.lab05;

import edu.course.lab05.task.ProjectTask;
import edu.course.lab05.task.TaskId;
import edu.course.lab05.task.TaskStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class TaskFileLoader {

    private static final char DELIMITER = ';';
    private static final char QUOTE = '"';
    private static final String[] HEADER = {"id", "title", "category", "estimatedHours", "status"};

    public LoadResult load(Path path) {
        if (path == null) {
            throw new IllegalArgumentException("path cannot be null");
        }
        List<String> lines = readLines(path);
        if (lines.isEmpty()) {
            throw new DataLoadingException("File is empty: " + path);
        }
        checkHeader(lines.get(0), path);

        List<ProjectTask> tasks = new ArrayList<>();
        List<ValidationError> errors = new ArrayList<>();
        for (int index = 1; index < lines.size(); index++) {
            String line = lines.get(index);
            if (line.isBlank()) {
                continue;
            }
            parseRow(line, index + 1, tasks, errors);
        }
        return new LoadResult(tasks, errors);
    }

    private List<String> readLines(Path path) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
        } catch (IOException cause) {
            throw new DataLoadingException("Cannot read " + path, cause);
        }
        return lines;
    }

    private void checkHeader(String header, Path path) {
        List<String> fields = splitRow(header);
        if (fields.size() != HEADER.length) {
            throw new DataLoadingException("Invalid header in " + path + ": expected "
                    + HEADER.length + " columns but found " + fields.size());
        }
        for (int index = 0; index < HEADER.length; index++) {
            if (!HEADER[index].equalsIgnoreCase(fields.get(index))) {
                throw new DataLoadingException("Invalid header in " + path + ": expected column '"
                        + HEADER[index] + "' but found '" + fields.get(index) + "'");
            }
        }
    }

    private void parseRow(String line, int lineNumber, List<ProjectTask> tasks, List<ValidationError> errors) {
        List<String> fields = splitRow(line);
        if (fields.size() != HEADER.length) {
            errors.add(new ValidationError(lineNumber, "row",
                    "expected " + HEADER.length + " fields but found " + fields.size()));
            return;
        }

        String id = fields.get(0);
        String title = fields.get(1);
        String category = fields.get(2);
        String hours = fields.get(3);
        String status = fields.get(4);

        if (id.isBlank()) {
            errors.add(new ValidationError(lineNumber, "id", "value cannot be empty"));
            return;
        }
        if (title.isBlank()) {
            errors.add(new ValidationError(lineNumber, "title", "value cannot be empty"));
            return;
        }
        if (category.isBlank()) {
            errors.add(new ValidationError(lineNumber, "category", "value cannot be empty"));
            return;
        }
        if (hours.isBlank()) {
            errors.add(new ValidationError(lineNumber, "estimatedHours", "value cannot be empty"));
            return;
        }
        if (status.isBlank()) {
            errors.add(new ValidationError(lineNumber, "status", "value cannot be empty"));
            return;
        }

        int estimatedHours;
        try {
            estimatedHours = Integer.parseInt(hours);
        } catch (NumberFormatException cause) {
            errors.add(new ValidationError(lineNumber, "estimatedHours", "'" + hours + "' is not a number"));
            return;
        }
        if (estimatedHours <= 0) {
            errors.add(new ValidationError(lineNumber, "estimatedHours", "value must be positive but was " + estimatedHours));
            return;
        }

        TaskStatus taskStatus = findStatus(status);
        if (taskStatus == null) {
            errors.add(new ValidationError(lineNumber, "status", "unknown status '" + status + "'"));
            return;
        }

        tasks.add(new ProjectTask(new TaskId(id), title, category, taskStatus, estimatedHours));
    }

    private TaskStatus findStatus(String value) {
        for (TaskStatus status : TaskStatus.values()) {
            if (status.name().equals(value)) {
                return status;
            }
        }
        return null;
    }

    private List<String> splitRow(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean insideQuotes = false;
        boolean quotedField = false;
        for (int index = 0; index < line.length(); index++) {
            char symbol = line.charAt(index);
            if (insideQuotes) {
                if (symbol == QUOTE) {
                    if (index + 1 < line.length() && line.charAt(index + 1) == QUOTE) {
                        field.append(QUOTE);
                        index++;
                    } else {
                        insideQuotes = false;
                    }
                } else {
                    field.append(symbol);
                }
            } else if (symbol == QUOTE && field.isEmpty()) {
                insideQuotes = true;
                quotedField = true;
            } else if (symbol == DELIMITER) {
                fields.add(quotedField ? field.toString() : field.toString().trim());
                field.setLength(0);
                quotedField = false;
            } else {
                field.append(symbol);
            }
        }
        fields.add(quotedField ? field.toString() : field.toString().trim());
        return fields;
    }
}
