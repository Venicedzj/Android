package com.example.zhaungjie.news.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhaungjie.news.R;

/**
 * Created by zhaungjie on 17-4-10.
 */

public class NewsTitleFragment extends Fragment {


    public static boolean isTwoPane;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.news_title_list,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity().findViewById(R.id.newsinfolayout)!=null){
            isTwoPane=true;
        }else {
            isTwoPane=false;
        }
    }

}
