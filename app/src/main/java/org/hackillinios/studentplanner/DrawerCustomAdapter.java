package org.hackillinios.studentplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * Created by Michael Smith on 2/28/2015.
 */
public class DrawerCustomAdapter extends ArrayAdapter<String> {

    String[] drawerItems;

    public DrawerCustomAdapter(Context context, String[] items) {
        super(context, R.layout.drawer_item, items);
        this.drawerItems = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater customInflater = LayoutInflater.from(getContext());
        View customView = customInflater.inflate(R.layout.drawer_item, parent, false);

        String title = getItem(position);
        TextView dTitle = (TextView)customView.findViewById(R.id.tvDrawerItem);
        ImageView dIcon = (ImageView)customView.findViewById(R.id.ivDrawerPic);

        dTitle.setText(title);
        dIcon.setImageResource(R.drawable.ic_launcher);

        return customView;
    }
}
