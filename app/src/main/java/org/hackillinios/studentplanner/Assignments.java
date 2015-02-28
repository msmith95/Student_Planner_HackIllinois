package org.hackillinios.studentplanner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Michael Smith on 2/28/2015.
 */
public class Assignments {
    private String title, classA, description, type;
    private UUID id;
    private Calendar dueDate, reminderDate;
    private DateFormat df;


    void setTitle(String aTitle){
        this.title = aTitle;
    }

    void setClassA(String aClass){
        this.classA = aClass;
    }

    void setDescription(String descrip){
        this.description = descrip;
    }

    void setDueDate(Date date){
        df = new SimpleDateFormat("yyyy MM dd HH mm ss");
        String mDate = df.format(date);
        String[] temp = mDate.split(" ");
       this.dueDate.set(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), Integer.parseInt(temp[3]), Integer.parseInt(temp[4]), Integer.parseInt(temp[5]));
    }

    void setReminderDate(Date date){
        df = new SimpleDateFormat("yyyy MM dd HH mm ss");
        String mDate = df.format(date);
        String[] temp = mDate.split(" ");
        this.reminderDate.set(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), Integer.parseInt(temp[3]), Integer.parseInt(temp[4]), Integer.parseInt(temp[5]));
    }

    void setUUID(){
        this.id = UUID.randomUUID();
    }

    void setType(String type){
        this.type = type;
    }

    void setAll(String aTitle, String aClass, String descrip, String type, Date d1, Date d2){
        setTitle(aTitle);
        setClassA(aClass);
        setDescription(descrip);
        setType(type);
        setDueDate(d1);
        setReminderDate(d2);
        setUUID();
    }

    String getTitle(){
        return this.title;
    }

    String getClassA(){
        return this.classA;
    }

    String getDescription(){
        return this.description;
    }

    Calendar getDueDate(){
        return this.dueDate;
    }

    Calendar getReminderDate(){
        return this.reminderDate;
    }

    String getUUID(){
        return this.id.toString();
    }

    String getType(){
        return this.type;
    }
}
