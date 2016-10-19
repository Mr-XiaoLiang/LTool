package xiaoliang.ltool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import xiaoliang.ltool.R;
import xiaoliang.ltool.bean.MeizhiBean;
import xiaoliang.ltool.constant.MeizhiType;
import xiaoliang.ltool.dialog.LoadDialog;
import xiaoliang.ltool.fragment.MeizhiFragment;
import xiaoliang.ltool.util.DialogUtil;

public class MeizhiActivity extends AppCompatActivity implements MeizhiFragment.OnFragmentInteractionListener,View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<MeizhiFragment> fragments;
    private LoadDialog loadDialog;
    private MeizhiType[] meizhiTypes = {
            MeizhiType.GANK
            ,MeizhiType.MM_All,MeizhiType.MM_Recommended,MeizhiType.MM_Ranking,MeizhiType.MM_Label
            ,MeizhiType.Meitulu_Recommend,MeizhiType.Meitulu_Japan,MeizhiType.Meitulu_Hokon,MeizhiType.Meitulu_Domestic,MeizhiType.Meitulu_Highest,MeizhiType.Meitulu_God,MeizhiType.Meitulu_Model,MeizhiType.Meitulu_Net,MeizhiType.Meitulu_Mores,MeizhiType.Meitulu_Temperament,MeizhiType.Meitulu_Stunner,MeizhiType.Meitulu_Milk,MeizhiType.Meitulu_Sex,MeizhiType.Meitulu_Tempt,MeizhiType.Meitulu_Xiong,MeizhiType.Meitulu_Woman,MeizhiType.Meitulu_Tui,MeizhiType.Meitulu_Bud,MeizhiType.Meitulu_Loli,MeizhiType.Meitulu_Cute,MeizhiType.Meitulu_Outdoors,MeizhiType.Meitulu_Bikini,MeizhiType.Meitulu_Pure,MeizhiType.Meitulu_Aestheticism,MeizhiType.Meitulu_Fresh
            ,MeizhiType.DOUBAN_ALL,MeizhiType.DOUBAN_LIAN,MeizhiType.DOUBAN_SIWA,MeizhiType.DOUBAN_TUI,MeizhiType.DOUBAN_TUN,MeizhiType.DOUBAN_XIONG,MeizhiType.DOUBAN_OTHER
            ,MeizhiType.MEIZHI51_ALL,MeizhiType.MEIZHI51_COMIC,MeizhiType.MEIZHI51_JAPAN,MeizhiType.MEIZHI51_KITTY,MeizhiType.MEIZHI51_LIU,MeizhiType.MEIZHI51_PURE,MeizhiType.MEIZHI51_SEX,MeizhiType.MEIZHI51_TAIWAN,MeizhiType.MEIZHI51_WOMAN,MeizhiType.MEIZHI51_ZHAO
            ,MeizhiType.Meizhi_all,MeizhiType.Meizhi_Sex,MeizhiType.Meizhi_Private,MeizhiType.Meizhi_Pure,MeizhiType.Meizhi_Bud,MeizhiType.Meizhi_Fresh,MeizhiType.Meizhi_God,MeizhiType.Meizhi_Temperament,MeizhiType.Meizhi_Model,MeizhiType.Meizhi_Bikini,MeizhiType.Meizhi_Football,MeizhiType.Meizhi_Loli,MeizhiType.Meizhi_90,MeizhiType.Meizhi_Japan
    };
    private FloatingActionButton toTop;

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
        toTop = (FloatingActionButton) findViewById(R.id.activity_meizhi_fab);
        toTop.setOnClickListener(this);
        initFragments();
        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);
        DialogUtil.getAlertDialog(this,"当前分类数量为:"+ meizhiTypes.length+",\n大量分类处于二级页面,此处分类为图源常用分类");
    }

    private void initFragments(){
        fragments = new ArrayList<>();
        for(MeizhiType type:meizhiTypes){
            MeizhiFragment fragment = MeizhiFragment.newInstance(type);
            fragments.add(fragment);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_meizhi_fab:
                int p = viewPager.getCurrentItem();
                if(p>-1&&p<fragments.size())
                    fragments.get(p).selectedToTop();
                break;
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
        if(!TextUtils.isEmpty(bean.page)){
            intent = new Intent(this,MeizhiListActivity.class);
            intent.putExtra("bean",bean);
            intent.putExtra("type",fragment.getType());
        }else{
            intent = new Intent(this,MeizhiDetailedActivity.class);
            intent.putExtra("bean",bean);
            intent.putExtra("type",fragment.getType());
        }
        startActivity(intent);
    }

    @Override
    public void onScrollStateChanged(boolean show) {
        if(show){
            toTop.setVisibility(View.VISIBLE);
        }else{
            toTop.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_meizhi, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
//            case R.id.menu_meizhi_top:
//                int p = viewPager.getCurrentItem();
//                if(p>-1&&p<fragments.size())
//                    fragments.get(p).selectedToTop();
//                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
