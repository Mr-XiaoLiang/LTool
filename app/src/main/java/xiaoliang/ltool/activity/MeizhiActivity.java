package xiaoliang.ltool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import java.util.ArrayList;

import xiaoliang.ltool.R;
import xiaoliang.ltool.bean.MeizhiBean;
import xiaoliang.ltool.constant.MeizhiType;
import xiaoliang.ltool.dialog.LoadDialog;
import xiaoliang.ltool.fragment.MeizhiFragment;

public class MeizhiActivity extends AppCompatActivity implements MeizhiFragment.OnFragmentInteractionListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<MeizhiFragment> fragments;
    private LoadDialog loadDialog;
    private MeizhiType[] meizhiTypes = {
            MeizhiType.GANK,
            MeizhiType.DOUBAN_ALL,MeizhiType.DOUBAN_LIAN,MeizhiType.DOUBAN_SIWA,MeizhiType.DOUBAN_TUI,MeizhiType.DOUBAN_TUN,MeizhiType.DOUBAN_XIONG,MeizhiType.DOUBAN_OTHER,
            MeizhiType.MEIZHI51_ALL,MeizhiType.MEIZHI51_COMIC,MeizhiType.MEIZHI51_JAPAN,MeizhiType.MEIZHI51_KITTY,MeizhiType.MEIZHI51_LIU,MeizhiType.MEIZHI51_PURE,MeizhiType.MEIZHI51_SEX,MeizhiType.MEIZHI51_TAIWAN,MeizhiType.MEIZHI51_WEIBO,MeizhiType.MEIZHI51_WOMAN,MeizhiType.MEIZHI51_ZHAO,MeizhiType.MEIZHI51_B
    };

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
        initFragments();
        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initFragments(){
        fragments = new ArrayList<>();
        for(MeizhiType type:meizhiTypes){
            MeizhiFragment fragment = MeizhiFragment.newInstance(type);
            fragments.add(fragment);
        }
    }

    private class PageAdapter extends FragmentStatePagerAdapter {

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
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
    public void onCardClick(MeizhiFragment fragment, MeizhiBean bean) {
        Intent intent;
        if(bean.page!=null&&!bean.page.equals("")){
            intent = new Intent(this,MeizhiListActivity.class);
            intent.putExtra("bean",bean);
            intent.putExtra("type",fragment.getType());
        }else{
            intent = new Intent(this,MeizhiDetailedActivity.class);
            intent.putExtra("url",bean.url);
            intent.putExtra("title",bean.title);
            intent.putExtra("from",bean.from);
        }
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_meizhi, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_meizhi_top:
                int p = viewPager.getCurrentItem();
                if(p>-1&&p<fragments.size())
                    fragments.get(p).selectedToTop();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
