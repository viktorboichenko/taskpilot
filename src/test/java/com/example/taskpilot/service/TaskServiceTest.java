package com.example.taskpilot.service;

import com.example.taskpilot.model.Task;
import com.example.taskpilot.model.TaskPriority;
import com.example.taskpilot.model.TaskStatus;
import com.example.taskpilot.model.User;
import com.example.taskpilot.repository.TaskRepository;
import com.example.taskpilot.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

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

    @Test
    @DisplayName("When WIP limit is not exceeded then assign to a user")
    void assignUser_Success() {
        UUID givenTaskId = UUID.randomUUID();
        UUID givenUserId = UUID.randomUUID();
        User givenUser = User.builder().id(givenUserId).name("Jake").build();
        Task givenTask = Task.builder().id(givenTaskId).status(TaskStatus.IN_PROGRESS).build();

        when(taskRepository.findById(givenTaskId)).thenReturn(Optional.of(givenTask));
        when(userRepository.findById(givenUserId)).thenReturn(Optional.of(givenUser));
        // user as 0 tasks
        when(taskRepository.countByAssigneeIdAndStatus(givenUserId, TaskStatus.IN_PROGRESS)).thenReturn(0);
        when(taskRepository.save(any(Task.class))).thenReturn(givenTask);

        Task actualResult = taskService.assignUser(givenTaskId, givenUserId);

        assertEquals(givenUser, actualResult.getAssignee());
        verify(taskRepository).save(givenTask);
    }

    @Test
    @DisplayName("When task exists then delete it")
    void deleteTask_Success() {
        UUID givenTaskId = UUID.randomUUID();
        when(taskRepository.existsById(givenTaskId)).thenReturn(true);

        taskService.deleteTask(givenTaskId);

        verify(taskRepository).deleteById(givenTaskId);
    }

    @Test
    @DisplayName("When task does not exist then throw exception")
    void deleteTask_NotFound() {
        UUID givenTaskId = UUID.randomUUID();
        when(taskRepository.existsById(givenTaskId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> taskService.deleteTask(givenTaskId));
        verify(taskRepository, never()).deleteById(any());
    }

}