package com.chinabrowser.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.bean.SearchRecommand;
import com.chinabrowser.cbinterface.SearchItemClick;
import com.chinabrowser.utils.GlideUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */

public class SearchRecommandAdapter extends RecyclerView.Adapter<SearchRecommandAdapter.RViewHolder> {
    Context context;
    List<SearchRecommand> recommands;
    SearchItemClick itemClick;

    public void setItemClick(SearchItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public SearchRecommandAdapter(Context context, List<SearchRecommand> recommands) {
        this.context = context;
        this.recommands = recommands;
    }

    @Override
    public RViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = View.inflate(context,R.layout.recommand_search_item,null);
        RViewHolder viewHolder = new RViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RViewHolder holder, int position) {
        final SearchRecommand recommand = recommands.get(position);
        holder.name.setText(recommand.getName());
        GlideUtils.loadImageView(context,recommand.getImages(),holder.cover);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClick!=null){
                    itemClick.ItemClick(recommand);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return recommands==null?0:recommands.size();
    }

    class RViewHolder extends RecyclerView.ViewHolder{
        ImageView cover;
        TextView name;

        public RViewHolder(View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.cover);
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
