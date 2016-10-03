package xiaoliang.ltool.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import xiaoliang.ltool.R;
import xiaoliang.ltool.fragment.MeizhiFragment;

public class MeizhiActivity extends AppCompatActivity implements MeizhiFragment.OnFragmentInteractionListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<MeizhiFragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meizhi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_meizhi_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tabLayout = (TabLayout) findViewById(R.id.content_meizhi_viewpager_tab);
        viewPager = (ViewPager) findViewById(R.id.content_meizhi_viewpager_pager);
        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        initFragments();
    }

    private void initFragments(){
        fragments = new ArrayList<>();
        fragments.add(MeizhiFragment.newInstance(MeizhiFragment.MeizhiType.GANK));
    }

    private class PageAdapter extends FragmentPagerAdapter {

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments.get(position).getTitle();
        }
    }

    @Override
    public void onCardClick(Fragment fragment, String uri) {

    }

    @Override
    public void onError(Fragment fragment, String msg) {

    }

    @Override
    public void onLoad(Fragment fragment, String url) {

    }
}
