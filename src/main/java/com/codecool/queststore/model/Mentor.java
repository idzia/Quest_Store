package com.codecool.queststore.model;

import java.util.ArrayList;
import java.util.List;

public class Mentor extends User {

    private Integer mentorId;
    private List<CoolClass> classes;


    public Mentor(Integer userId, String firstName, String lastName, String phoneNumber, String email, String role, Integer mentorId) {
        super(userId, firstName, lastName, phoneNumber, email, role);
        this.mentorId = mentorId;
    }

    public Mentor(String firstName, String lastName, String phoneNumber, String email, String role) {
        super(firstName, lastName, phoneNumber, email, role);
        classes = new ArrayList<>();
    }

    public List<CoolClass> getClasses() {
        return classes;
    }

    public Integer getMentorId() {return mentorId;}
}