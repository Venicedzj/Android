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

public class NewsInfoFragment extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.news_info_fragment,container,false);
        return view;
    }
}
