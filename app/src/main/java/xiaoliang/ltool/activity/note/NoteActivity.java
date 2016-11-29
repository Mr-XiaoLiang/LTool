package xiaoliang.ltool.activity.note;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import xiaoliang.ltool.R;
import xiaoliang.ltool.fragment.note.NoteFragment;
import xiaoliang.ltool.fragment.note.NoteInterface;
import xiaoliang.ltool.listener.OnNoteFragmentListener;
import xiaoliang.ltool.view.NoScrollViewPager;
import xiaoliang.ltool.view.ZoomPageTransformer;

/**
 * 记事本主页
 */
public class NoteActivity extends AppCompatActivity implements View.OnClickListener,OnNoteFragmentListener{

    private NoteFragment[] noteFragments;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_note_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab = (FloatingActionButton) findViewById(R.id.activity_note_fab);
        fab.setOnClickListener(this);
        init();
    }

    private void init(){
        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_note_tablayout);
        NoScrollViewPager viewPager = (NoScrollViewPager) findViewById(R.id.activity_note_viewpager);
        viewPager.setNoScroll(false);
        noteFragments = new NoteFragment[2];
        noteFragments[0] = NoteFragment.newInstance(NoteFragment.TYPE_NOTE);
        noteFragments[1] = NoteFragment.newInstance(NoteFragment.TYPE_CALENDAR);
        viewPager.setAdapter(new NotePageAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public void onNoteClick(int noteId) {
        Intent intent = new Intent(this,NoteAddActivity.class);
        intent.putExtra(NoteAddActivity.ARG_NOTE_ID,noteId);
        startActivity(intent);
    }

    @Override
    public void OnScrollChange(boolean isScroll) {
        if(isScroll){
            fab.hide();
        }else{
            fab.show();
        }
    }

    private class NotePageAdapter extends FragmentStatePagerAdapter {

        NotePageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return noteFragments[position];
        }

        @Override
        public int getCount() {
            return noteFragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return noteFragments[position].getTitle();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarColor(int colot){
        getWindow().setStatusBarColor(colot);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_note_fab:
                startActivity(new Intent(this,NoteAddActivity.class));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
