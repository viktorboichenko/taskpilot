package com.example.taskpilot.api;

public record CreateUserRequest(
        String name,
        String email
) {
}
