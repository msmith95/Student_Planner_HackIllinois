package org.hackillinios.studentplanner;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Michael Smith on 2/28/2015.
 */
public class Assignments {
    String title, classA, description, type;
    UUID id;
    Calendar dueDate=Calendar.getInstance(), reminderDate=Calendar.getInstance();
    //DateFormat df=new SimpleDateFormat();
    Boolean complete;

    public Assignments(){
    }

    public void setTitle(String aTitle){
        title = aTitle;
    }

    public void setClassA(String aClass){
        classA = aClass;
    }

    public void setDescription(String descrip){
        description = descrip;
    }

    public void setDueDate(Date date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String mDate = df.format(date);
        String[] temp = mDate.split(" ");
        String[] t1 = temp[0].split("-");
        String[] t2 = temp[1].split(":");
        dueDate.set(Integer.parseInt(t1[0]), Integer.parseInt(t1[1]), Integer.parseInt(t1[2]), Integer.parseInt(t2[0]), Integer.parseInt(t2[1]), Integer.parseInt(t2[2]));
    }

    public void setReminderDate(Date date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String mDate = df.format(date);
        String[] temp = mDate.split(" ");
        String[] t1 = temp[0].split("-");
        String[] t2 = temp[1].split(":");
        reminderDate.set(Integer.parseInt(t1[0]), Integer.parseInt(t1[1]), Integer.parseInt(t1[2]), Integer.parseInt(t2[0]), Integer.parseInt(t2[1]), Integer.parseInt(t2[2]));
    }

   public void setUUID(){
        id = UUID.randomUUID();
    }

   public void setType(String type2){
        type = type2;
    }

    public void setComplete(Boolean complete2){
        complete = complete2;
    }

    public void setAll(String aTitle, String aClass, String descrip, Date d1, Date d2){
        Log.v("String", aTitle);
        setTitle(aTitle);
        setClassA(aClass);
        setDescription(descrip);
        setDueDate(d1);
        setReminderDate(d2);
        setUUID();
        setComplete(false);
    }

    public String getTitle(){
        return title;
    }

    public String getClassA(){
        return classA;
    }

    public String getDescription(){
        return description;
    }

    public Calendar getDueDate(){
        return dueDate;
    }

    public Calendar getReminderDate(){
        return reminderDate;
    }

    public String getUUID(){
        return id.toString();
    }

    public String getType(){
        return type;
    }

    public Boolean getComplete(){
        return complete;
    }
}
