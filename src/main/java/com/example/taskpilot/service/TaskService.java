package com.example.taskpilot.service;

import com.example.taskpilot.model.Task;
import com.example.taskpilot.model.TaskPriority;
import com.example.taskpilot.model.TaskStatus;
import com.example.taskpilot.repository.TaskRepository;
import com.example.taskpilot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

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
}
