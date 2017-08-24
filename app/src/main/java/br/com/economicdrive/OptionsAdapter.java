package br.com.economicdrive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.economicdrive.R;

public class OptionsAdapter extends BaseAdapter {

    private Object[] optionList;
    private int[] imageList;
    private Context context;
    private int resource;

    public OptionsAdapter(Context context, Object[] array, int resource) {
        optionList = array;
        imageList = new int[optionList.length];
        this.context = context;
        this.resource = resource;
    }

    public void addImage(int position, int value) {
        imageList[position] = value;
    }

    @Override
    public int getCount() {

        return optionList.length;
    }

    @Override
    public Object getItem(int position) {
        return optionList[position];
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = null;
        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(this.resource, parent, false);
        } else {
            row = convertView;
        }
        TextView titleTextView =
                (TextView) row.findViewById(R.id.customTextView);
        ImageView titleImageView =
                (ImageView) row.findViewById(R.id.customImageView);
        titleTextView.setText(optionList[position].toString());
        titleImageView.setImageResource(imageList[position]);
        return row;
    }
}
