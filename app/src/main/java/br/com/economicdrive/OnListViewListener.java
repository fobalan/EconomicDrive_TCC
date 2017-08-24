package br.com.economicdrive;

import android.view.View;
import android.widget.CompoundButton;

import java.util.List;

/**
 * Created by Flavio on 24/07/2015.
 */
public interface OnListViewListener {
    void onViewClick(View v, int position);
    void onCheckedClick(CompoundButton buttonView, boolean isChecked, int position);
}
