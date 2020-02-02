package com.alan.updatablefragmentstateadapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class PageFragment extends Fragment {

    public static PageFragment newInstance(int position) {

        Bundle args = new Bundle();
        args.putInt("position", position);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView) view.findViewById(R.id.fragment_title)).setText(String.format(Locale.US, "%d", getSomeIdentifier()));
    }

    public int getSomeIdentifier() {
        return getArguments().getInt("position");
    }
}
