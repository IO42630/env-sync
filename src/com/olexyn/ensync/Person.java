package com.olexyn.ensync;

import java.util.Date;

public class Person {

    private String firstName;
    private String surname;
    private Date date;
    private String occupation;
    private double salary;




    Person(String firstName, String surname, Date date, String occupation, double salary){
        this.firstName = firstName;
        this.surname = surname;
        this.date = date;
        this.occupation = occupation;
        this.salary = salary;

    }

    public double getSalary(){
        return 0.0;
    }

    public Date getDateOfBirth(){
        return null;
    }

    public String getFirstName(){
        return "firstName";
    }

    public String getSurname(){
        return "surname";
    }

    public String getOccupation(){
        return "occupation";
    }
}
