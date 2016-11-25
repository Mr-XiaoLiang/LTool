package xiaoliang.ltool.fragment.note;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import xiaoliang.ltool.R;
import xiaoliang.ltool.listener.OnNoteFragmentListener;

public class NoteFragment extends NoteInterface {

    private OnNoteFragmentListener mListener;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private String title;
    private int type;
    private static final String ARG_TYPE = "ARG_TYPE";
    public static final int TYPE_NOTE = 0;
    public static final int TYPE_CALENDAR = 1;

    public NoteFragment() {
        // Required empty public constructor
    }

    public static NoteFragment newInstance(int type) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TYPE,type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = (int) getArguments().getSerializable(ARG_TYPE);
            switch (type){
                case TYPE_NOTE:
                    title = "备忘录";
                    break;
                case TYPE_CALENDAR:
                    title = "日程表";
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_note, container, false);

        return root;
    }

    public void onButtonPressed(int noteId) {
        if (mListener != null) {
            mListener.onNoteClick(noteId);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNoteFragmentListener) {
            mListener = (OnNoteFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public String getTitle(){
        return title;
    }
}
