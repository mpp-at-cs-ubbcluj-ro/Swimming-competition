package org.example.hbm;

import org.example.model.Entity;

public class UserH extends Entity<Integer> {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public Integer getId() {
        return super.getId();
    }
    public void setId(Integer id) {
        super.setId(id);
    }
    public String getFirstName() {
        return this.firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return this.lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
