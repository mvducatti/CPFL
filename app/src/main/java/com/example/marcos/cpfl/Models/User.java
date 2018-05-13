package com.example.marcos.cpfl.Models;

public class User {

    private int id, user_type;
    private String cpf;

    public User(int id, String cpf, int user_type) {
        this.id = id;
        this.cpf = cpf;
        this.user_type = user_type;
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
