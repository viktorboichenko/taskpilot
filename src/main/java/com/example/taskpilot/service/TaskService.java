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
}
