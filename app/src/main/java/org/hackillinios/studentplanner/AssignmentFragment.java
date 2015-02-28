package org.hackillinios.studentplanner;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Michael Smith on 2/28/2015.
 */
public class AssignmentFragment extends android.app.Fragment {
    ListView assign;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_assignment, container, false);
        assign = (ListView)rootview.findViewById(R.id.lvAssignments);
        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(assign.getPositionForView(v)){

                }
            }
        });

        return rootview;
    }

    public class fetchAssignmentsTaskFromNet extends AsyncTask<Void, Void, Assignments[]>{

        @Override
        protected Assignments[] doInBackground(Void... params) {
            return new Assignments[0];
        }
    }
}
