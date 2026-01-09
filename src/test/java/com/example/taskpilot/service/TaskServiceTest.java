package com.example.taskpilot.service;

import com.example.taskpilot.repository.TaskRepository;
import com.example.taskpilot.repository.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock TaskRepository repository;
    @Mock UserRepository userRepository;
    @InjectMocks TaskService taskService;

}