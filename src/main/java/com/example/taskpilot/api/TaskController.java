package com.example.taskpilot.api;

import com.example.taskpilot.model.Task;
import com.example.taskpilot.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public List<Task> getAll() {
        return taskService.getAllTasks();
    }

    @PostMapping
    public ResponseEntity<Task> create(@RequestBody CreateTaskRequest req) {
        return ResponseEntity.ok(taskService.createTask(req.title(), req.description(), req.priority()));
    }

}
