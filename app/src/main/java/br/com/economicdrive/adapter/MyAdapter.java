package br.com.economicdrive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.economicdrive.Information;
import br.com.economicdrive.holder.MyViewHolder;
import br.com.economicdrive.listener.OnListViewListener;
import br.com.economicdrive.R;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private List<Information> list;
    private int resource;
    private OnListViewListener viewClickListener;
    private LayoutInflater layoutInflater;
    private View view;
    private MyViewHolder myViewHolder;
    private Context context;
    private boolean deletable;

    public MyAdapter(Context context, List<Information> list, int resource) {
        this.list = list;
        this.context = context;
        this.resource = resource;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(resource, parent, false);
        myViewHolder = new MyViewHolder(view, viewClickListener, context);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (isDeletable()){
            holder.getEditImageButton().setVisibility(View.INVISIBLE);
            holder.getCheckBox().setVisibility(View.VISIBLE);
        }
        else{
            holder.getEditImageButton().setVisibility(View.VISIBLE);
            holder.getCheckBox().setVisibility(View.INVISIBLE);
        }
        holder.getEditImageButton().setImageResource(R.mipmap.ic_edit);
        holder.getPrimaryTextView().setText(((Information) list.get(position)).getPrimaryText());
        holder.getSecondaryTextView().setText(((Information)list.get(position)).getSecondaryText());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Object getItem(int position){
        return list.get(position);
    }

    public void setOnViewClickListener(OnListViewListener viewClickListener ){
        this.viewClickListener = viewClickListener;
    }

    public void removeItem(Information info) {
        int position = list.indexOf(info);
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void setList(List<Information> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addItem(Information info){
        list.add(info);
    }

    public void setDeletable(boolean data){
        deletable = data;
    }

    public boolean isDeletable(){
        return deletable;
    }

}

