package com.chinabrowser.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.bean.Collection;
import com.chinabrowser.ui.SwipeMenuView;

import java.util.List;

/**
 * Created by Administrator on 2018/4/14.
 */

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CViewHolder> {

    private Context context;
    private List<Collection> collections;

    public CollectionAdapter(Context context, List<Collection> collections) {
        this.context = context;
        this.collections = collections;
    }

    @Override
    public CViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context,R.layout.collection_item,null);
        CViewHolder viewHolder = new CViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CViewHolder holder, int position) {
        Collection collection = collections.get(position);
        holder.title.setText(collection.getTitle());
        holder.title.setText(collection.getUrl());
    }

    @Override
    public int getItemCount() {
        return collections.size();
    }

    class CViewHolder extends RecyclerView.ViewHolder{
        SwipeMenuView menuView;
        TextView title,url;
        public CViewHolder(View itemView) {
            super(itemView);
            menuView = (SwipeMenuView) itemView.findViewById(R.id.menuView);
            menuView.setSwipeEnable(true);
            title = (TextView) itemView.findViewById(R.id.title);
            url = (TextView) itemView.findViewById(R.id.url);
        }
    }
}
