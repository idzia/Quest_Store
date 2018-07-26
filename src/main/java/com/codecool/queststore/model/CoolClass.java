package com.codecool.queststore.model;

import java.util.ArrayList;
import java.util.List;

public class CoolClass {

    private String className;
    private List<Mentor> classMentors;
    private List<Student> classStudents;

    public CoolClass(String className) {
        this.className = className;
        classMentors = new ArrayList<>();
        classStudents = new ArrayList<>();
    }

    public void addToClassMentors(Mentor mentor) {
        classMentors.add(mentor);
    }

    public void addToClassStudents(Student student) {
        classStudents.add(student);
    }

    public String getClassName() {
        return className;
    }

    public List<Mentor> getClassMentors() {
        return classMentors;
    }

    public List<Student> getClassStudents() {
        return classStudents;
    }
}
