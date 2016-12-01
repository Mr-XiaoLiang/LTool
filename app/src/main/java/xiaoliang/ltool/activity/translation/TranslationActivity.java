package xiaoliang.ltool.activity.translation;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import xiaoliang.ltool.R;
import xiaoliang.ltool.adapter.LanguageSpinnerAdapter;
import xiaoliang.ltool.bean.TranslationLanguageBean;
import xiaoliang.ltool.util.ClipboardUtil;
import xiaoliang.ltool.util.translation.BaiduTranslationRequest;
import xiaoliang.ltool.util.translation.KingsoftTranslationRequest;
import xiaoliang.ltool.util.translation.Translation;
import xiaoliang.ltool.util.translation.YoudaoTranslationRequest;
import xiaoliang.ltool.view.LLoadView;

public class TranslationActivity extends AppCompatActivity implements
        View.OnClickListener,Spinner.OnItemSelectedListener,Translation.TranslationCallback{


    //F7XoprCI2rECCwpx6RBZiokv0MpSYaq0ha0L3X4dsnU=
    //LTool

    private Translation translation;
    private Translation.TranslationRequest[] translationRequests;
    private int[] sourceIcons = {R.drawable.ic_baidu,R.drawable.ic_youdao,R.drawable.ic_kingsoft,R.drawable.ic_microsoft};
    private AppCompatSpinner fromLanguageSpinner,toLanguageSpinner,sourceSpinner;
    private TextView fromLengthText,toLengthText;
    private TextInputEditText fromEditText,toEditText;
    private TextLengthChangeListener fromTextChnageListener;
    private ArrayList<TranslationLanguageBean> fromLanguageBeans;
    private ArrayList<TranslationLanguageBean> toLanguageBeans;
    private LanguageSpinnerAdapter fromLanguageAdapter;
    private LanguageSpinnerAdapter toLanguageAdapter;
    private Translation.TranslationRequest thisRequest;
    private LLoadView lLoadView;
    private boolean onLoad = false;
    private ClipboardUtil clipboardUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_translation_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    private void init(){
        fromLanguageSpinner = (AppCompatSpinner)findViewById(R.id.content_translation_from_language_spinner);
        toLanguageSpinner = (AppCompatSpinner)findViewById(R.id.content_translation_to_language_spinner);
        sourceSpinner = (AppCompatSpinner)findViewById(R.id.content_translation_source);
        fromLengthText = (TextView) findViewById(R.id.content_translation_from_length);
        toLengthText = (TextView) findViewById(R.id.content_translation_to_length);
        fromEditText = (TextInputEditText) findViewById(R.id.content_translation_from);
        toEditText = (TextInputEditText) findViewById(R.id.content_translation_to);
        lLoadView = (LLoadView) findViewById(R.id.content_translation_load);
        showLoad(false);
        fromLanguageSpinner.setOnItemSelectedListener(this);
        toLanguageSpinner.setOnItemSelectedListener(this);
        sourceSpinner.setOnItemSelectedListener(this);
        fromEditText.addTextChangedListener(fromTextChnageListener = new TextLengthChangeListener(fromEditText,fromLengthText));
        toEditText.addTextChangedListener(new TextLengthChangeListener(toEditText,toLengthText));
        sourceSpinner.setAdapter(new SourceAdapter(this));
        translationRequests = new Translation.TranslationRequest[sourceIcons.length];
        translationRequests[0] = new BaiduTranslationRequest();
        translationRequests[1] = new YoudaoTranslationRequest();
        translationRequests[2] = new KingsoftTranslationRequest();
        translation = new Translation();
        translation.setCallback(this);
        fromLanguageBeans = new ArrayList<>();
        toLanguageBeans = new ArrayList<>();
        fromLanguageSpinner.setAdapter(fromLanguageAdapter = new LanguageSpinnerAdapter(fromLanguageBeans,this));
        toLanguageSpinner.setAdapter(toLanguageAdapter = new LanguageSpinnerAdapter(toLanguageBeans,this));
        clipboardUtil = new ClipboardUtil(this);
        setRequest(0);
    }

    private void showLoad(boolean b){
        onLoad = b;
        if(onLoad){
            lLoadView.start();
            lLoadView.setVisibility(View.VISIBLE);
        }else{
            lLoadView.stop();
            lLoadView.setVisibility(View.INVISIBLE);
        }
    }

    private void setRequest(int index){
        thisRequest = translationRequests[index];
        translation.setRequest(thisRequest);
        fromTextChnageListener.setMaxLength(thisRequest.maxFromLength());
        fromLanguageBeans.clear();
        fromLanguageBeans.addAll(thisRequest.getFromLanguage());
        toLanguageBeans.clear();
        toLanguageBeans.addAll(thisRequest.getToLanguage());
        fromLanguageAdapter.notifyDataSetChanged();
        toLanguageAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.content_translation_exchange:
                int fl = fromLanguageSpinner.getSelectedItemPosition();
                fromLanguageSpinner.setSelection(toLanguageSpinner.getSelectedItemPosition(),true);
                toLanguageSpinner.setSelection(fl,true);
                break;
            case R.id.content_translation_from_copy:
                int fstart = fromEditText.getSelectionStart();
                int fend = fromEditText.getSelectionEnd();
                if(fstart==fend||fstart<0||fend<0||fend<fstart)
                    return;
                clipboardUtil.putText(fromEditText.getText().subSequence(fstart, fend).toString());
                Snackbar.make(fromEditText,"已复制",Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.content_translation_from_all:
                fromEditText.requestFocus();
                fromEditText.selectAll();
                break;
            case R.id.content_translation_from_paste:
                fromEditText.append(clipboardUtil.getString());
                break;
            case R.id.content_translation_from_clean:
                fromEditText.setText("");
                break;
            case R.id.content_translation_execute:
                execute();
                break;
            case R.id.content_translation_to_copy:
                int tstart = toEditText.getSelectionStart();
                int tend = toEditText.getSelectionEnd();
                if(tstart==tend||tstart<0||tend<0||tend<tstart)
                    return;
                clipboardUtil.putText(toEditText.getText().subSequence(tstart, tend).toString());
                Snackbar.make(toEditText,"已复制",Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.content_translation_to_all:
                toEditText.requestFocus();
                toEditText.selectAll();
                break;
        }
    }

    private void execute(){
        if(onLoad){
            return;
        }
        String from = fromEditText.getText().toString().trim();
        if(!TextUtils.isEmpty(from)){
//            Log.d("activity.execute","from:"+from);
            translation.execute(from);
            showLoad(true);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.content_translation_from_language_spinner:
                thisRequest.setFromLanguage(fromLanguageBeans.get(i).id);
                break;
            case R.id.content_translation_to_language_spinner:
                thisRequest.setToLanguage(toLanguageBeans.get(i).id);
                break;
            case R.id.content_translation_source:
                setRequest(i);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onTranslation(String from, String to) {
        toEditText.setText(to);
        showLoad(false);
    }

    private class TextLengthChangeListener implements TextWatcher{

        private EditText editView;
        private TextView showView;
        private int maxLength = -1;

        public TextLengthChangeListener(EditText editView, TextView showView) {
            this.editView = editView;
            this.showView = showView;
        }

        public void setMaxLength(int maxLength) {
            this.maxLength = maxLength;
            if(maxLength<0){
                editView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.MAX_VALUE)});
            }else{
                editView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
            }
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(TextUtils.isEmpty(charSequence.toString())){
                showView.setText("");
            }else{
                String show = charSequence.length()+"";
                if(maxLength>0)
                    show += ("/"+maxLength);
                showView.setText(show);
            }
        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void afterTextChanged(Editable editable) {}
    }

    private class SourceAdapter extends BaseAdapter{

        private LayoutInflater inflater;

        public SourceAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return sourceIcons.length;
        }
        @Override
        public Object getItem(int i) {
            return sourceIcons[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SourceHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_translation_source,parent,false);
                holder = new SourceHolder(convertView);
                convertView.setTag(holder);//绑定ViewHolder对象
            }else{
                holder = (SourceHolder)convertView.getTag();//取出ViewHolder对象
            }
            if(holder!=null)
                holder.onBind(sourceIcons[position]);
            return convertView;
        }
    }
    private class SourceHolder{
        ImageView icon;
        public SourceHolder(View itemView) {
            icon = (ImageView) itemView.findViewById(R.id.item_translation_source_icon);
        }
        public void onBind(int resId){
            if(icon!=null)
                icon.setImageResource(resId);
        }
    }
}
