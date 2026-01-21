package com.example.frizerapp.exception;

public class AppointmentConflictException extends RuntimeException {
  public AppointmentConflictException(String message) {
    super(message);
  }
}