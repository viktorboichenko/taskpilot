package com.example.taskpilot.repository;

import com.example.taskpilot.model.Task;
import com.example.taskpilot.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    int countByAssigneeIdAndStatus(UUID assigneeId, TaskStatus status);
}
