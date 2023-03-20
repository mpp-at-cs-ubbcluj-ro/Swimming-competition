package org.example.repository;

import org.example.model.User;

public interface IUserRepository extends IRepository<User, Integer> {
    User findByEmail(String email);
}
