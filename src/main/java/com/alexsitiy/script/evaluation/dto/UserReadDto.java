package com.alexsitiy.script.evaluation.dto;

import org.springframework.hateoas.RepresentationModel;

public class UserReadDto extends RepresentationModel<UserReadDto> {
    private final String id;
    private final String firstname;
    private final String lastname;
    private final String username;
    private final String email;

    public UserReadDto(String id, String firstname, String lastname, String username, String email) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
