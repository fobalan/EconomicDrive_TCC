package br.com.economicdrive;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.List;

/**
 * Created by Flavio on 12/08/2015.
 */
public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{
    private TextView primaryTextView;
    private TextView secondaryTextView;
    private ImageButton editImageButton;
    private CheckBox checkBox;
    private OnListViewListener listViewListener;

    public MyViewHolder(View itemView, OnListViewListener listViewListener, Context context) {
        super(itemView);
        this.listViewListener = listViewListener;
        primaryTextView = (TextView) itemView.findViewById(R.id.customPrimaryTextView);
        secondaryTextView = (TextView) itemView.findViewById(R.id.customSecondaryTextView);
        editImageButton = (ImageButton) itemView.findViewById(R.id.editImageButton);
        checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        editImageButton.setColorFilter(context.getResources().getColor(R.color.colorSecondaryText)); // White Tint
        itemView.setOnClickListener(this);
        editImageButton.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        listViewListener.onViewClick(v, getPosition());
    }


    public ImageButton getEditImageButton() {
        return editImageButton;
    }

    public TextView getPrimaryTextView() {
        return primaryTextView;
    }

    public TextView getSecondaryTextView(){
        return secondaryTextView;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            listViewListener.onCheckedClick(buttonView,isChecked, getPosition());
    }
}
