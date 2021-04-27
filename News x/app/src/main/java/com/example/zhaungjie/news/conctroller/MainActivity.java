package com.example.zhaungjie.news.conctroller;

import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.zhaungjie.news.model.GetNewsDataInterface;
import com.example.zhaungjie.news.model.NewsData;
import com.example.zhaungjie.news.model.NewsDataAdapter;
import com.example.zhaungjie.news.model.RefreshInterface;
import com.example.zhaungjie.news.view.NewsInfoActivity;
import com.example.zhaungjie.news.view.NewsInfoFragment;
import com.example.zhaungjie.news.view.NewsTitleFragment;
import com.example.zhaungjie.news.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RefreshInterface,GetNewsDataInterface{

    public static final String URL="http://v.juhe.cn/toutiao/index?type=top&key=68f6bdb5961f65d4d579342244423d68";
    private ListView listView;
    private List<NewsData> datas=new ArrayList<>();
    private NewsDataAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=(ListView)findViewById(R.id.listview);
        getDatas(URL);
        adapter=new NewsDataAdapter(datas,this);
        if(NewsTitleFragment.isTwoPane){
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.newsinfolayout,new NewsInfoFragment());
            fragmentTransaction.commit();

        }else{
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(MainActivity.this,NewsInfoActivity.class);

                    intent.putExtra("newsTitle", datas.get(position).getNewsTitle());
                    intent.putExtra("newsDate", datas.get(position).getNewsDate());
                    intent.putExtra("newsImgUrl", datas.get(position).getNewsImgUrl());
                    intent.putExtra("newsUrl", datas.get(position).getNewsUrl());

                    startActivity(intent);

                }
            });
            Instrantiation();
        }
    }

    public void getDatas(String url){
        final RequestQueue mQueue= Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest =new JsonObjectRequest(url,null,new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject jsonObject) {
                try{
                    JSONObject jsonObject2 = jsonObject.getJSONObject("result");
                    JSONArray jsonArray = jsonObject2.getJSONArray("data");

                    for (int i = 0; i <jsonArray.length() ; i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        NewsData data = new NewsData();
                        data.setNewsTitle(item.getString("title"));
                        data.setNewsDate(item.getString("date"));
                        data.setNewsImgUrl(item.getString("thumbnail_pic_s"));
                        data.setNewsUrl(item.getString("url"));
                        datas.add(data);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(objectRequest);
    }


    public void Instrantiation(){
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(5);
        swipeRefreshLayout.setProgressViewEndTarget(true,100);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        datas.clear();
                        getDatas(URL);
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(1);
                    }
                }).start();
            }
        });

    }


    private android.os.Handler handler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    swipeRefreshLayout.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
}
