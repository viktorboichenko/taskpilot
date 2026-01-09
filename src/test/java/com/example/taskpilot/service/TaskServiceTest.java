package com.example.taskpilot.service;

import com.example.taskpilot.model.Task;
import com.example.taskpilot.model.TaskPriority;
import com.example.taskpilot.model.TaskStatus;
import com.example.taskpilot.repository.TaskRepository;
import com.example.taskpilot.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock TaskRepository taskRepository;
    @Mock UserRepository userRepository;
    @InjectMocks TaskService taskService;

    @Test
    @DisplayName("When createTask then task is created with TODO status")
    void createTask_Success() {
        String givenTitle = "New Feature";
        String givenDescription = "Description";
        TaskPriority givenPriority = TaskPriority.HIGH;

        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArguments()[0]);

        Task actualResult = taskService.createTask(givenTitle, givenDescription, givenPriority);

        assertNotNull(actualResult);
        assertEquals(givenTitle, actualResult.getTitle());
        assertEquals(TaskStatus.TODO, actualResult.getStatus());
        verify(taskRepository).save(any(Task.class));
    }

}