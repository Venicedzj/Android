package com.example.zhaungjie.news.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.zhaungjie.news.R;
import com.example.zhaungjie.news.model.NewsData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaungjie on 17-4-9.
 */

public class NewsDataAdapter extends BaseAdapter {
    private List<NewsData> datas=new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;

    public NewsDataAdapter(List<NewsData> datas, Context context) {
        this.datas = datas;
        this.context = context;
        this.layoutInflater = layoutInflater.from(context);
    }

    protected class ViewHolder{
        ImageView newsImg;
        TextView newsTitle;
        TextView newsDate;
        public ViewHolder(View view){
            newsImg=(ImageView)view.findViewById(R.id.newsimg);
            newsTitle=(TextView)view.findViewById(R.id.newstitle);
            newsDate=(TextView)view.findViewById(R.id.newsdate);
        }
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null){
            convertView=layoutInflater.inflate(R.layout.list_item,null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initView((NewsData) getItem(position),(ViewHolder)convertView.getTag());
        return convertView;
    }

    private void initView(NewsData data,ViewHolder holder){
        holder.newsImg.setTag(data.getNewsImgUrl());

        holder.newsTitle.setText(data.getNewsTitle());

        holder.newsDate.setText(data.getNewsDate());

        getImage(this.context, data.getNewsImgUrl(), holder.newsImg);
    }


    public void getImage(Context context, String imgUrl,
                         final ImageView imageView) {

        if (imageView.getTag().toString().equals(imgUrl)) {
            RequestQueue mQueue = Volley.newRequestQueue(context);
            ImageRequest imageRequest = new ImageRequest(imgUrl,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            imageView.setImageBitmap(response);
                        }
                    }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            mQueue.add(imageRequest);
        }
    }
}
