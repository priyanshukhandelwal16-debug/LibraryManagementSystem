package com.library.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Member {
    private int id;
    private String memberId;
    private String fullName;
    private String email;
    private String phone;
    private String gender;
    private String course;
    private String semester;
    private String address;
    private Date registrationDate;
    private Timestamp createdAt;

    public Member() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Date getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(Date registrationDate) { this.registrationDate = registrationDate; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
