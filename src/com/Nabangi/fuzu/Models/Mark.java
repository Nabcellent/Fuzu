package com.Nabangi.fuzu.Models;


import com.Nabangi.fuzu.Database.DatabaseLink;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * _____________________________________________________    Nabangi Michael - ICS B - 134694, 21/03/2021    _________✔*/

public class Mark {
    int id;
    String name;
    float math;
    float eng;
    float swa;
    float avg = (math + eng + swa) / 3;
    char grade;

    private static final DecimalFormat dp2 = new DecimalFormat("#.##");

    //  Constructors
    public Mark() {}

    public Mark(int id, String name, float math, float eng, float swa) {
        this.id = id;
        this.name = name;
        this.math = math;
        this.eng = eng;
        this.swa = swa;

        this.avg = calculateAverage();
        this.grade = calculateGrade(this.avg);
    }

    public float calculateAverage() {
        return Float.parseFloat(dp2.format((math + eng + swa) / 3));
    }

    public char calculateGrade(float avg) {
        if(avg >= 70) {
            return 'A';
        } else if(avg > 60) {
            return 'B';
        } else if(avg > 50) {
            return 'C';
        } else if(avg > 40) {
            return 'D';
        }
        return 'F';
    }

    public void getMarksById(int id, Label error) {
        Connection link = DatabaseLink.connectDb();

        final String SQL = "SELECT mathematics, english, swahili FROM tbl_marks WHERE student_id = " + id;

        try {
            Statement statement = link.createStatement();
            ResultSet result = statement.executeQuery(SQL);

            if(result.next()) {
                this.math = result.getInt("mathematics");
                this.eng = result.getInt("english");
                this.swa = result.getInt("swahili");
            } else {
                error.setText("No Marks to display!");
            }
        } catch (SQLException e) {
            Logger.getLogger(DatabaseLink.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public float getAvg() {
        return avg;
    }

    public char getGrade() {
        return grade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getMath() {
        return math;
    }

    public void setMath(float math) {
        this.math = math;
    }

    public float getEng() {
        return eng;
    }

    public void setEng(float eng) {
        this.eng = eng;
    }

    public float getSwa() {
        return swa;
    }

    public void setSwa(float swa) {
        this.swa = swa;
    }
}
