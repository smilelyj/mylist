package com.axxess.yongjili.mylist.customViews;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.axxess.yongjili.mylist.R;
import com.axxess.yongjili.mylist.MainActivity;
import com.axxess.yongjili.mylist.fragments.ItemViewFragment;
import com.axxess.yongjili.mylist.models.Drawing;
import com.bumptech.glide.Glide;


import io.realm.Realm;

/**
 * Created by yongjili on 9/17/17.
 */

/**
 * This class was created because there will be some views that require showing a list, but since
 * the list is already a child of a scroll view, a linear layout was used instead of a normal
 * list view.
 */

public class CustomListElementGenerator {

    private LayoutInflater inflater;

    private static final String TAG = "CustomListElement";

    /**
     * Constructor, you need to pass the context calling this class so that in can be called when
     * referencing layouts. On a personal note, I think is better to pass the context once
     * rather than create a class with many static methods and pass the context every time.
     * @param context
     */
    public CustomListElementGenerator(Context context){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View generateAppointMentListItem(final MainActivity activity, final Drawing drawing,
                                            int notifications){
        View newView = inflater.inflate(R.layout.list_item, null);
        TextView idText = (TextView) newView.findViewById(R.id.id_text);
        TextView typeText = (TextView) newView.findViewById(R.id.type_text);
        TextView dateText = (TextView) newView.findViewById(R.id.date_text);
        TextView dataText = (TextView) newView.findViewById(R.id.data_text);
        ImageView dataImage = (ImageView) newView.findViewById(R.id.data_image_view);

        Realm seededData = Realm.getDefaultInstance();

        idText.setText(drawing.getId());
        typeText.setText(drawing.getType());
        dateText.setText(drawing.getDate());

        if (drawing.getType().equals("text")){
            idText.setVisibility(View.GONE);
            typeText.setVisibility(View.GONE);
            dateText.setVisibility(View.GONE);
            dataText.setVisibility(View.VISIBLE);
            dataText.setText(drawing.getData());
            dataImage.setVisibility(View.GONE);

        }  else if (drawing.getType().equals("image")){
            idText.setVisibility(View.GONE);
            typeText.setVisibility(View.GONE);
            dateText.setVisibility(View.GONE);
            dataText.setVisibility(View.GONE);

            dataImage.setVisibility(View.VISIBLE);

            Glide.with(activity.getApplicationContext())
                    .load(Uri.parse(drawing.getData()))
                    .thumbnail(0.3f)
                    .placeholder(R.drawable.placeholder)
                    .into(dataImage);

        } else {
            idText.setVisibility(View.VISIBLE);
            typeText.setVisibility(View.VISIBLE);
            dateText.setVisibility(View.VISIBLE);
            dataText.setVisibility(View.VISIBLE);

        }

        newView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (drawing.getType().equals("text")){
                    ItemViewFragment fragment = new ItemViewFragment(drawing);
                    activity.addFragmentToStack(fragment);
                } else if (drawing.getType().equals("image")){
                    Dialog dialog = new Dialog(activity.getApplicationContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    dialog.setContentView(R.layout.dialog_press_image);

                    ImageView imageView = (ImageView) dialog.findViewById(R.id.container);

                    Glide.with(activity.getApplicationContext())
                            .load(drawing.getData())
                            .placeholder(R.drawable.placeholder)
                            .thumbnail(0.2f)
                            .into(imageView);

                    dialog.show();

                    //Grab the window of the dialog, and change the width
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    Window window = dialog.getWindow();
                    lp.copyFrom(window.getAttributes());
                    //This makes the dialog take up the full width
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    window.setAttributes(lp);
                }
            }
        });
        seededData.close();

        return newView;
    }


}
