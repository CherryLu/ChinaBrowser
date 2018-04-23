package com.chinabrowser.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.bean.CollectionItem;
import com.chinabrowser.cbinterface.DelCollectionCallBack;
import com.chinabrowser.ui.SwipeMenuView;

import java.util.List;

/**
 * Created by Administrator on 2018/4/14.
 */

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CViewHolder> {

    private Context context;
    private List<CollectionItem> collections;

    private DelCollectionCallBack collectionCallBack;

    public void setCollectionCallBack(DelCollectionCallBack collectionCallBack) {
        this.collectionCallBack = collectionCallBack;
    }

    public CollectionAdapter(Context context, List<CollectionItem> collections) {
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
    public void onBindViewHolder(final CViewHolder holder, int position) {
        final CollectionItem collection = collections.get(position);
        holder.title.setText(collection.title);
        holder.title.setText(collection.copy_url);
        holder.slip_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//删除收藏
                if (collectionCallBack!=null){
                    collectionCallBack.swipClick(collection);
                }
            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (collectionCallBack!=null){
                    collectionCallBack.itemClick(collection);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return collections.size();
    }

    class CViewHolder extends RecyclerView.ViewHolder{
        SwipeMenuView menuView;
        TextView title,url,slip_txt;
        LinearLayout layout;
        public CViewHolder(View itemView) {
            super(itemView);
            menuView = (SwipeMenuView) itemView.findViewById(R.id.menuView);
            menuView.setSwipeEnable(true);
            menuView.setLeftSwipe(true);
            title = (TextView) itemView.findViewById(R.id.title);
            url = (TextView) itemView.findViewById(R.id.url);
            slip_txt = (TextView) itemView.findViewById(R.id.slip_txt);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
        }
    }
}
