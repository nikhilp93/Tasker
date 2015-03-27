package com.matthewtporter.tasker;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by mporter on 3/27/15.
 */
public class TaskListAdapter extends BaseAdapter {

    private List<ParseObject> objectList;
    private Context context;

    public TaskListAdapter(Context context, List<ParseObject> objectList) {
        this.objectList = objectList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return objectList.size();
    }

    @Override
    public Object getItem(int position) {
        return objectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.task_custom_list_item, null);
        }

        TextView taskNameTextView = (TextView) convertView.findViewById(R.id.textView);
        TextView taskDescTextView = (TextView) convertView.findViewById(R.id.textView2);

        taskNameTextView.setText(objectList.get(position).getString("taskName"));
        taskDescTextView.setText(objectList.get(position).getString("taskDescription"));

        return convertView;
    }

    public String getObjectId(int position) {
        return objectList.get(position).getObjectId();
    }

}
