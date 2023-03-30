package com.example.projectjavafx.validator;

public interface IValidator<E> {
    void validate(E entity) throws ValidationException;
}