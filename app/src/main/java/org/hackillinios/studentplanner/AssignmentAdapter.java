package org.hackillinios.studentplanner;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Michael Smith on 2/28/2015.
 */
public class AssignmentAdapter extends ArrayAdapter<Assignments> {
    Assignments[] assignments;
    TextView title, dueDate, classTitle, type;

    public AssignmentAdapter(Context context, Assignments[] assign) {
        super(context, R.layout.assignment_item, assign);
        this.assignments = assign;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater customInflater = LayoutInflater.from(getContext());
        View customView = customInflater.inflate(R.layout.assignment_item, parent, false);

        title=(TextView)customView.findViewById(R.id.tvAssignmentTitle);
        dueDate = (TextView)customView.findViewById(R.id.tvDueDate);
        classTitle = (TextView)customView.findViewById(R.id.tvClassTitle);
        type = (TextView)customView.findViewById(R.id.tvAssignmentType);

        Calendar c = assignments[position].getDueDate();

        title.setText(assignments[position].getTitle());
        int day = c.get(Calendar.DAY_OF_WEEK);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);
        String dayOfWeek = new DateFormatSymbols().getShortWeekdays()[day];
        String month = new DateFormatSymbols().getShortMonths()[m];
        dueDate.setText(dayOfWeek + " " + month + " " + d);
        classTitle.setText(assignments[position].getTitle());
        type.setText(assignments[position].getType());
        if(assignments[position].getComplete()){
            title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            dueDate.setPaintFlags(dueDate.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            classTitle.setPaintFlags(classTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            type.setPaintFlags(type.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        return customView;
    }
}
