package com.chinabrowser.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.bean.Content;
import com.chinabrowser.cbinterface.RightClick;
import com.chinabrowser.utils.GlideUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/4/15.
 */

public class RightAdapter extends RecyclerView.Adapter<RightAdapter.RViewHolder> {
    private Context context;
    private List<Content> contents;
    private RightClick rightClick;
    private int which;

    public void setWhich(int which) {
        this.which = which;
    }

    public void setRightClick(RightClick rightClick) {
        this.rightClick = rightClick;
    }

    public RightAdapter(Context context, List<Content> contents) {
        this.context = context;
        this.contents = contents;
    }

    public List<Content> getContents() {
        return contents;
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
        if (which==0){
            holder.control.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.delete.getVisibility()==View.GONE){
                        if (rightClick!=null){
                            rightClick.startUrl(content);
                        }
                    }else {
                        holder.delete.setVisibility(View.GONE);
                    }

                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    holder.delete.setVisibility(View.VISIBLE);
                    return true;
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (rightClick!=null){
                        rightClick.deleteContent(content);
                    }
                }
            });


        }else {
            holder.control.setVisibility(View.VISIBLE);
            holder.open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.open.setBackgroundResource(R.drawable.click_red);
                    holder.open.setTextColor(Color.WHITE);


                    TimerTask task = new TimerTask(){
                        public void run(){
                            holder.open.setBackgroundResource(R.drawable.black_line);
                            holder.open.setTextColor(Color.parseColor("#666666"));
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(task, 1000);
                    if (rightClick!=null){
                        rightClick.startUrl(content);
                    }
                }
            });
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.add.setBackgroundResource(R.drawable.click_red);
                    holder.add.setTextColor(Color.WHITE);

                    TimerTask task = new TimerTask(){
                        public void run(){
                            holder.add.setBackgroundResource(R.drawable.red_line);
                            holder.add.setTextColor(Color.RED);
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(task, 1000);

                    if (rightClick!=null){
                        rightClick.addContent(content);
                    }
                }
            });



        }


    }

    @Override
    public int getItemCount() {
        return contents==null?0:contents.size();
    }



    class RViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView cover,delete;
        LinearLayout control;
        TextView add,open;
      public RViewHolder(View itemView) {
          super(itemView);
          name = (TextView) itemView.findViewById(R.id.name);
          cover = (ImageView) itemView.findViewById(R.id.cover);
          delete = (ImageView) itemView.findViewById(R.id.delete_conner);
          control = itemView.findViewById(R.id.control);
          add = itemView.findViewById(R.id.add);
          open = itemView.findViewById(R.id.open);
      }
  }
}
