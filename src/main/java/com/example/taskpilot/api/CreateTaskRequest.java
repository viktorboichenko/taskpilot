package com.example.taskpilot.api;

import com.example.taskpilot.model.TaskPriority;

public record CreateTaskRequest(
        String title,
        String description,
        TaskPriority priority
) {}
