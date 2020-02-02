package com.alan.updatablefragmentstateadapter;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Integer> sDataSet;

    private static final int PAGE_SIZE = 3;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            sDataSet = new ArrayList<>();
            for (int i = 0; i < PAGE_SIZE; i++) {
                sDataSet.add(i);
            }
        } else {
            sDataSet = savedInstanceState.getIntegerArrayList("dataset");
        }

        viewPager = findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), sDataSet);
        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabLayoutTitle();

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sDataSet.add(sDataSet.size());
                adapter.notifyDataSetChanged();
                setupTabLayoutTitle();
            }
        });
        findViewById(R.id.btn_removeitem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sDataSet.size() > 0) {
                    sDataSet.remove(sDataSet.size() - 1);
                    adapter.notifyDataSetChanged();
                    setupTabLayoutTitle();
                }
            }
        });
    }

    private void setupTabLayoutTitle() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setText(i + "");
            }
        }
    }

    private static class PagerAdapter extends UpdatableFragmentStateAdapter {

        private final List<Integer> mItems;

        PagerAdapter(FragmentManager fm, List<Integer> items) {
            super(fm);
            mItems = items;
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.newInstance(mItems.get(position));
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public long getItemId(int position) {
            return mItems.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            PageFragment item = (PageFragment) object;
            int itemValue = item.getSomeIdentifier();
            for (int i = 0; i < mItems.size(); i++) {
                if (mItems.get(i).equals(itemValue)) {
                    return i;
                }
            }
            return POSITION_NONE;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList("dataset", sDataSet);
    }
}
