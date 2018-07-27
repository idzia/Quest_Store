package com.codecool.queststore.model;

public class Admin extends User {

    private Integer adminId;

    public Admin(Integer userId, String firstName, String lastName, String phoneNumber,
                 String email, String role, Integer adminId) {
        super(userId, firstName, lastName, phoneNumber, email, role);
        this.adminId = adminId;
    }
//    public Admin(Integer userId, String firstName, String lastName, String phoneNumber,
//                 String email, String role) {
//        super(firstName, lastName, phoneNumber, email, role);
//    }


    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }
}