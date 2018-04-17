package com.chinabrowser.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinabrowser.R;

/**
 * Created by Administrator on 2018/4/15.
 */

public class RightAdapter extends RecyclerView.Adapter<RightAdapter.RViewHolder> {
    private Context context;


    @Override
    public RViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context,R.layout.right_item,null);
        RViewHolder rViewHolder = new RViewHolder(view);
        return rViewHolder;
    }

    @Override
    public void onBindViewHolder(final RViewHolder holder, int position) {
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.delete.setVisibility(View.VISIBLE);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }



    class RViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView cover,delete;
      public RViewHolder(View itemView) {
          super(itemView);
          name = (TextView) itemView.findViewById(R.id.name);
          cover = (ImageView) itemView.findViewById(R.id.cover);
          delete = (ImageView) itemView.findViewById(R.id.delete_conner);
      }
  }
}
