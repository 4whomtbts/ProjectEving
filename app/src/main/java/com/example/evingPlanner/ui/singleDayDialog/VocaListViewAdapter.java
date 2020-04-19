package com.example.evingPlanner.ui.singleDayDialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.evingPlanner.UIUtils;
import com.example.evingPlanner.domain.VocaDayJoinWithVoca;

import java.util.List;

public class VocaListViewAdapter extends BaseAdapter {

    private Context context;
    private Fragment fragment;
    private ListView listView;
    public List<VocaDayJoinWithVoca> vocaList;

    public VocaListViewAdapter(Context context, Fragment fragment, ListView listView, List<VocaDayJoinWithVoca> vocaList) {
        this.vocaList = vocaList;
        this.context = context;
        this.fragment = fragment;
        this.listView = listView;
    }

    @Override
    public int getCount() {
        return vocaList.size();
    }

    @Override
    public Object getItem(int position) {
        return vocaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VocabularyItem vocabularyItem = new VocabularyItem(context, fragment, (VocaDayJoinWithVoca) getItem(position), this, position);
        vocabularyItem.initView();
        return vocabularyItem;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        UIUtils.setListViewHeightBasedOnItems(listView);
    }
}
