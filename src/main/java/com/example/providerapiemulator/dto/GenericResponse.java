package com.example.providerapiemulator.dto;

public record GenericResponse<T>(
        boolean success,
        T data,
        String message
) {
    public static <T> GenericResponse<T> ok(T data) {
        return new GenericResponse<>(true, data, null);
    }

    public static <T> GenericResponse<T> ok(T data, String message) {
        return new GenericResponse<>(true, data, message);
    }

    public static <T> GenericResponse<T> fail(String message) {
        return new GenericResponse<>(false, null, message);
    }
}
