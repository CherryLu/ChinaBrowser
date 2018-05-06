package com.chinabrowser.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.bean.Content;
import com.chinabrowser.cbinterface.RightClick;
import com.chinabrowser.utils.GlideUtils;
import com.chinabrowser.utils.LogUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/4/15.
 */

public class RightAdapter extends RecyclerView.Adapter<RightAdapter.RViewHolder> {
    private Context context;
    private List<Content> contents;
    private RightClick rightClick;

    public void setRightClick(RightClick rightClick) {
        this.rightClick = rightClick;
    }

    public RightAdapter(Context context, List<Content> contents) {
        this.context = context;
        this.contents = contents;
    }

    @Override
    public RViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context,R.layout.right_item,null);
        RViewHolder rViewHolder = new RViewHolder(view);
        return rViewHolder;
    }

    @Override
    public void onBindViewHolder(final RViewHolder holder, int position) {
        final Content content = contents.get(position);
        holder.name.setText(content.getTitle());
        GlideUtils.loadImageView(context,content.getCover_image(),holder.cover);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rightClick!=null){
                    rightClick.startUrl(content);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return contents==null?0:contents.size();
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
