package com.example.taskpilot.api;

public record UpdateUserRequest(
        String name,
        String email
) {}
