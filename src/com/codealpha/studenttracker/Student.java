package com.codealpha.studenttracker;

public class Student {
    private String id;
    private String name;
    private int age;
    private String gender;
    private double grade;

    public Student(String id, String name, int age, String gender, double grade) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.grade = grade;
    }

    // Getters & Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public double getGrade() { return grade; }

    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setGrade(double grade) { this.grade = grade; }

    @Override
    public String toString() {
        return id + "," + name + "," + age + "," + gender + "," + grade;
    }

    public static Student fromString(String line) {
        String[] p = line.split(",");
        if (p.length != 5) return null;
        return new Student(p[0], p[1], Integer.parseInt(p[2]), p[3], Double.parseDouble(p[4]));
    }
}
