package com.Nabangi.fuzu.Models;

import com.Nabangi.fuzu.Database.DatabaseLink;
import java.sql.*;

/**
 * _____________________________________________________    Nabangi Michael - ICS B - 134694, 21/03/2021    _________✔*/


public class Student {
    int id, admNo;
    String firstName, lastName, gender, password, parentName;
    Date DOB;

    public Student(int id, String firstName, String lastName, String gender, String parentName, Date DOB) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.parentName = parentName;
        this.DOB = DOB;
    }

    public Student(int id, String firstName, String lastName, String gender, Date DOB, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.DOB = DOB;
        this.password = password;
    }



    /** METHOD ADD STUDENT */
    public int createStudent() {
        Connection link = DatabaseLink.connectDb();

        final String SQL_INSERT1 = "INSERT INTO tbl_users (first_name, last_name, gender, user_type, password) " +
                "VALUES (?, ?, ?, ?, ?)";
        final String SQL_INSERT2 = "INSERT INTO tbl_students (user_id, parent_id, dob) " +
                "VALUES (?, ?, ?)";

        PreparedStatement prepStatement1;
        PreparedStatement prepStatement2;

        //==    Insert Data Into Users Table
        try {
            assert link != null;
            prepStatement1 = link.prepareStatement(SQL_INSERT1,
                    Statement.RETURN_GENERATED_KEYS);
            prepStatement2 = link.prepareStatement(SQL_INSERT2,
                    Statement.RETURN_GENERATED_KEYS);

            prepStatement1.setString(1, firstName);
            prepStatement1.setString(2, lastName);
            prepStatement1.setString(3, gender);
            prepStatement1.setString(4, "student");
            prepStatement1.setString(5, password);

            if(prepStatement1.executeUpdate() == 1) {
                //==    Get Primary Key Value of Inserted User
                ResultSet rs = prepStatement1.getGeneratedKeys();
                int insertId = 0;
                if(rs.next())
                    insertId = rs.getInt(1);

                assert prepStatement2 != null;
                prepStatement2.setInt(1, insertId);
                prepStatement2.setInt(2, id);
                prepStatement2.setDate(3, DOB);

                //==    Insert Data Into Students Table
                try {
                    return prepStatement2.executeUpdate();
                } catch(SQLException se) {
                    se.printStackTrace();
                } catch(Exception e) {
                    e.getCause().printStackTrace();
                }
            } else{
                return 0;
            }
        } catch(SQLException se) {
            se.printStackTrace();
        } catch(Exception e) {
            e.getCause().printStackTrace();
        }
        return 0;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public Date getDOB() {
        return DOB;
    }

    public String getParentName() {
        return parentName;
    }
}
