package com.chinabrowser.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.bean.History;

import java.util.List;

/**
 * Created by Administrator on 2018/4/14.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HViewHolder>{
    private Context context;
    private List<History> historys;

    public HistoryAdapter(Context context, List<History> historys) {
        this.context = context;
        this.historys = historys;
    }

    @Override
    public HViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context,R.layout.history_item,null);
        HViewHolder hViewHolder = new HViewHolder(view);
        return hViewHolder;
    }

    @Override
    public void onBindViewHolder(HViewHolder holder, int position) {
        History history = historys.get(position);
        holder.title.setText(history.getTitle());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class HViewHolder extends RecyclerView.ViewHolder{
            TextView title;
            public HViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
