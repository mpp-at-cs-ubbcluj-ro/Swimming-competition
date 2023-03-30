package com.example.projectjavafx.repository;

import com.example.projectjavafx.model.User;

public interface IUserRepository extends IRepository<User, Integer> {
    User findByEmail(String email);
}
