package com.chinabrowser.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.utils.Navigator;

/**
 * Created by 95470 on 2018/4/16.
 */

public class FirstTranslateAdapter extends RecyclerView.Adapter<FirstTranslateAdapter.TViewHolder> {

    private Context context;

    public FirstTranslateAdapter(Context context) {
        this.context = context;
    }

    public static final int[] mCatIcons = new int[]{R.mipmap.airport, R.mipmap.traffic, R.mipmap.hotel, R.mipmap.restaurant,
            R.mipmap.attractions, R.mipmap.game, R.mipmap.malls, R.mipmap.back,
            R.mipmap.post, R.mipmap.car, R.mipmap.life, R.mipmap.hospital,
            R.mipmap.emergency, R.mipmap.message};

    public static final int[] mCatTitles = new int[]{R.string.translate_parent_airport, R.string.translate_parent_traffic, R.string.translate_parent_hotel, R.string.translate_parent_restaurant,
            R.string.translate_parent_attraction, R.string.translate_parent_entertainment, R.string.translate_parent_shopping, R.string.translate_parent_bank,
            R.string.translate_parent_postoffice, R.string.translate_parent_car, R.string.translate_parent_life, R.string.translate_parent_hospital,
            R.string.translate_parent_emergency, R.string.translate_parent_classics};

    @Override
    public TViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.first_translate,parent,false);
        TViewHolder viewHolder = new TViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TViewHolder holder, final int position) {
        holder.pic.setImageResource(mCatIcons[position]);
        holder.name.setText(mCatTitles[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigator.startTranslateActivity(v.getContext(),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCatIcons.length;
    }

    class TViewHolder extends RecyclerView.ViewHolder{
        ImageView pic;
        TextView name;
        public TViewHolder(View itemView) {
            super(itemView);
            pic = (ImageView) itemView.findViewById(R.id.pic);
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
