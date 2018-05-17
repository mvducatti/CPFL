package com.example.marcos.cpfl.Models;

public class User {

    private int id, user_type;
    private String cpf, username;

    public User(int id, String cpf, int user_type, String username) {
        this.id = id;
        this.cpf = cpf;
        this.user_type = user_type;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getCpf() {
        return cpf;
    }

    public int getUser_type() {
        return user_type;
    }

    public int getId() {
        return id;
    }

}
