package com.example.taskpilot.service;

import com.example.taskpilot.model.Task;
import com.example.taskpilot.model.TaskPriority;
import com.example.taskpilot.model.TaskStatus;
import com.example.taskpilot.repository.TaskRepository;
import com.example.taskpilot.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    public static final int MAX_WIP_LIMIT = 3;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task createTask(String title, String description, TaskPriority priority) {
        var task = Task.builder()
                .title(title)
                .description(description)
                .priority(priority)
                .status(TaskStatus.TODO)
                .build();
        return taskRepository.save(task);
    }

    @Transactional
    public Task assignUser(UUID taskId, UUID userId) {
        var task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (task.getStatus() == TaskStatus.IN_PROGRESS) {
            int activeTasks = taskRepository.countByAssigneeIdAndStatus(userId, TaskStatus.IN_PROGRESS);
            if (activeTasks >= MAX_WIP_LIMIT) {
                throw new IllegalStateException(
                        "User " + user.getName() + " is overloaded! Max " + MAX_WIP_LIMIT + " tasks allowed."
                );
            }
        }
        task.setAssignee(user);
        return taskRepository.save(task);
    }

    @Transactional
    public Task updateStatus(UUID taskId, TaskStatus newStatus) {
        var task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        if (!isValidTransition(task.getStatus(), newStatus)) {
            throw new IllegalArgumentException("Invalid transition from " + task.getStatus() + " to " + newStatus);
        }

        if (newStatus == TaskStatus.IN_PROGRESS && task.getAssignee() != null) {
            int activeTasks = taskRepository.countByAssigneeIdAndStatus(task.getAssignee().getId(), TaskStatus.IN_PROGRESS);
            if (activeTasks >= MAX_WIP_LIMIT) {
                throw new IllegalStateException("User is overloaded, cannot start new task.");
            }
        }

        task.setStatus(newStatus);
        return taskRepository.save(task);
    }

    public boolean isValidTransition(TaskStatus oldStatus, TaskStatus newStatus) {
        if (oldStatus == newStatus) return true;

        return switch (oldStatus) {
            case TODO -> newStatus == TaskStatus.IN_PROGRESS;
            case IN_PROGRESS -> List.of(TaskStatus.REVIEW, TaskStatus.TODO).contains(newStatus);
            case REVIEW -> List.of(TaskStatus.DONE, TaskStatus.IN_PROGRESS).contains(newStatus);
            case DONE -> newStatus == TaskStatus.IN_PROGRESS;
        };
    }
}
