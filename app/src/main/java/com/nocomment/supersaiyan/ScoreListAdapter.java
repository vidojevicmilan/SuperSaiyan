package com.nocomment.supersaiyan;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Comment;

import java.util.ArrayList;

/**
 * Created by milanvidojevic on 06-Oct-16.
 */

public class ScoreListAdapter extends ArrayAdapter<Score> {

    private Context context;
    private ArrayList<Score> allScores;
    private LayoutInflater mInflater;
    private boolean mNotifyOnChange = true;

    public ScoreListAdapter(Context context, ArrayList<Score> mScores, Activity activity) {
        super(context, R.layout.scoreboard_listview_row);
        this.context = context;
        this.allScores = new ArrayList<Score>(mScores);
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return allScores.size();
    }

    @Override
    public Score getItem(int position) {
        return allScores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getPosition(Comment item) {
        return allScores.indexOf(item);
    }

    @Override
    public int getViewTypeCount() {
        return 1; //Number of types + 1 !!!!!!!!
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        int type = getItemViewType(position);
        if (convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case 1:
                    convertView = mInflater.inflate(R.layout.scoreboard_listview_row,parent, false);
                    holder.username = (TextView) convertView.findViewById(R.id.username_text_view);
                    holder.xp = (TextView) convertView.findViewById(R.id.exp_textview);
                    holder.rank = (TextView)convertView.findViewById(R.id.rank_textview);
                    holder.delimiter = (View)convertView.findViewById(R.id.delimiter_view);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.username.setText(allScores.get(position).getUsername());
        holder.xp.setText(allScores.get(position).getXP());
        holder.rank.setText(Integer.toString(position+1));

        holder.pos = position;

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }


    //---------------static views for each row-----------//
    static class ViewHolder {

        TextView username;
        TextView xp;
        TextView rank;
        View delimiter;
        int pos; //to store the position of the item within the list
    }
}
