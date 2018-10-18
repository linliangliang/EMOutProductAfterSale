package com.zhengyuan.emoutproductaftersale;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by 林亮 on 2018/10/18
 */

public class OutProductFragment extends Fragment {

    public static OutProductFragment newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name", name);
        OutProductFragment fragment = new OutProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outproduct, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button sweepButton = (Button) getActivity().findViewById(R.id.qulityEvent);
        sweepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从fragment跳转到activity中
                startActivity(new Intent(getActivity(), QulityEventActivity.class));
            }
        });
    }
}
