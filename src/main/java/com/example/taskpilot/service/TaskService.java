package com.example.taskpilot.service;

import com.example.taskpilot.repository.TaskRepository;
import com.example.taskpilot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
}
