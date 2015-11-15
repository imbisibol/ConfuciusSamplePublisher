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
public class BooksAdapter extends ArrayAdapter<DATAReadingMaterial> {
    Context context;
    int layoutResourceId;
    ArrayList<DATAReadingMaterial> data = new ArrayList<DATAReadingMaterial>();

    public BooksAdapter(Context context, int layoutResourceId,
                                 ArrayList<DATAReadingMaterial> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    public DATAReadingMaterial getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        BookListItem holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new BookListItem();
            holder.Id  = (TextView) row.findViewById(R.id.lblBookId);
            holder.Title = (TextView) row.findViewById(R.id.lblTitle);
            holder.Authors = (TextView) row.findViewById(R.id.lblAuthors);
            holder.SubjectArea = (TextView) row.findViewById(R.id.lblSubjectArea);
            holder.Tags = (TextView) row.findViewById(R.id.lblTags);
            holder.CoverImageURL = (ImageView) row.findViewById(R.id.imgCoverImage);


            row.setTag(holder);

        } else {
            holder = (BookListItem) row.getTag();
        }

        DATAReadingMaterial item = data.get(position);
        holder.Id.setText(item.Id);
        holder.Title.setText(item.Title + " - " + item.Edition);
        holder.Authors.setText(item.Authors);
        holder.SubjectArea.setText(item.SubjectArea);
        holder.Tags.setText(item.Tags);

        if (item.CoverImageURL != null && item.CoverImageURL != "") {
            if(item.CoverImageURL.equals(getContext().getString(R.string.DEFAULT_user_avatar)))
                item.CoverImageURL = getContext().getString(R.string.DEFAULT_image_domain) + item.CoverImageURL;

            Common.getImageLoader(null).displayImage(item.CoverImageURL, holder.CoverImageURL);
        }


        return row;
    }

    static class BookListItem
    {
        public TextView Id;
        public TextView Title;
        public TextView Authors;
        public TextView Tags;
        public TextView SubjectArea;

        public ImageView CoverImageURL;
    }
}
