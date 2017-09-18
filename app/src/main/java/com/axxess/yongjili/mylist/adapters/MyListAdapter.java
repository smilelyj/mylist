package com.axxess.yongjili.mylist.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.axxess.yongjili.mylist.models.Drawing;
import com.axxess.yongjili.mylist.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import android.view.View.OnClickListener;

/**
 * Created by yongjili on 9/16/17.
 */

public class MyListAdapter
        extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {
    private Context context;
    private List<Drawing> drawings = new ArrayList<>();

    public MyListAdapter(Context context, RealmResults<Drawing> drawings){
        this.context = context;
        this.drawings = drawings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Drawing drawing = drawings.get(position);

        holder.idText.setText(drawing.getId());
        holder.typeText.setText(drawing.getType());
        holder.dateText.setText(drawing.getDate());

        if (drawing.getType().equals("text")){
            holder.idText.setVisibility(View.GONE);
            holder.typeText.setVisibility(View.GONE);
            holder.dateText.setVisibility(View.GONE);
            holder.dataText.setVisibility(View.VISIBLE);

            holder.dataText.setText(drawing.getData());
            holder.dataImage.setVisibility(View.GONE);

        }  else if (drawing.getType().equals("image")){
            holder.idText.setVisibility(View.GONE);
            holder.typeText.setVisibility(View.GONE);
            holder.dateText.setVisibility(View.GONE);
            holder.dataText.setVisibility(View.GONE);

            holder.dataImage.setVisibility(View.VISIBLE);

            Glide.with(context)
                .load(Uri.parse(drawing.getData()))
                .thumbnail(0.3f)
                .placeholder(R.drawable.placeholder)
                .into(holder.dataImage);

        } else {
            holder.idText.setVisibility(View.VISIBLE);
            holder.typeText.setVisibility(View.VISIBLE);
            holder.dateText.setVisibility(View.VISIBLE);
            holder.dataText.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public int getItemCount() {
        return drawings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView idText;
        public final TextView typeText;
        public final TextView dateText;
        public final TextView dataText;
        public final ImageView dataImage;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            idText = (TextView) view.findViewById(R.id.id_text);
            typeText = (TextView) view.findViewById(R.id.type_text);
            dateText = (TextView) view.findViewById(R.id.date_text);
            dataText = (TextView) view.findViewById(R.id.data_text);
            dataImage = (ImageView) view.findViewById(R.id.data_image_view);
        }
    }

}
