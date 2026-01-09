package com.example.taskpilot.api;

import com.example.taskpilot.model.Task;
import com.example.taskpilot.model.TaskStatus;
import com.example.taskpilot.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @PatchMapping("/{id}/assign/{userId}")
    public ResponseEntity<Task> assign(@PathVariable UUID id, @PathVariable UUID userId) {
        return ResponseEntity.ok(taskService.assignUser(id, userId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> updateStatus(@PathVariable UUID id, @RequestParam TaskStatus status) {
        return ResponseEntity.ok(taskService.updateStatus(id, status));
    }

}
