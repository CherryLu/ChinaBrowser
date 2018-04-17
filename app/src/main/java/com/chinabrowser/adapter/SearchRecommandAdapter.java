package com.chinabrowser.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.bean.SearchRecommand;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */

public class SearchRecommandAdapter extends RecyclerView.Adapter<SearchRecommandAdapter.RViewHolder> {
    Context context;
    List<SearchRecommand> recommands;

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
        SearchRecommand recommand = recommands.get(position);
        holder.name.setText(recommand.getName());
        holder.cover.setImageResource(recommand.getImages());
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
