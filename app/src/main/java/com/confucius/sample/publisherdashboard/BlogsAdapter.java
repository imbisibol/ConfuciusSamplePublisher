package com.confucius.sample.publisherdashboard;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by imbisibol on 11/15/2015.
 */
public class BlogsAdapter extends ArrayAdapter<DATAArticle> {
    Context context;
    int layoutResourceId;
    ArrayList<DATAArticle> data = new ArrayList<DATAArticle>();

    public BlogsAdapter(Context context, int layoutResourceId,
                        ArrayList<DATAArticle> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    public DATAArticle getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        BlogListItem holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new BlogListItem();
            holder.Id  = (TextView) row.findViewById(R.id.lblArticleId);
            holder.Title = (TextView) row.findViewById(R.id.lblTitle);
            holder.SubTitle = (TextView) row.findViewById(R.id.lblSubTitle);
            holder.SubjectArea = (TextView) row.findViewById(R.id.lblSubjectArea);
            holder.Tags = (TextView)row.findViewById(R.id.lblTags);


            row.setTag(holder);

        } else {
            holder = (BlogListItem) row.getTag();
        }

        DATAArticle item = data.get(position);
        holder.Id.setText(item.Id);
        holder.Title.setText(item.Title);
        holder.SubTitle.setText(item.SubTitle);
        holder.SubjectArea.setText(item.SubjectArea);
        holder.Tags.setText(item.Tags);


        return row;
    }

    static class BlogListItem
    {
        public TextView Id;
        public TextView Title;
        public TextView SubTitle;
        public TextView SubjectArea;
        public TextView Tags;

    }
}
