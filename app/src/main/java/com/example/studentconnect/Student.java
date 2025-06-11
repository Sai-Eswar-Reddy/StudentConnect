package com.example.studentconnect;

public class Student {
    private String fullName;
    private String rollNo;
    private String branch;
    private String section;
    private String year;
    private String phoneNo;
    private String email;
    private String password;
    private String uid; // Added field for the student's UID

    // Default constructor required for Firebase
    public Student() {
    }

    public Student(String fullName, String rollNo, String branch, String section, String year, String phoneNo, String email, String password) {
        this.fullName = fullName;
        this.rollNo = rollNo;
        this.branch = branch;
        this.section = section;
        this.year = year;
        this.phoneNo = phoneNo;
        this.email = email;
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter for UID
    public String getUid() {
        return uid;
    }

    // Setter for UID
    public void setUid(String uid) {
        this.uid = uid;
    }
}