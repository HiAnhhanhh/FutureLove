package com.thinkdiffai.futurelove.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.thinkdiffai.futurelove.DbStorage.EventHistoryDb;
import com.thinkdiffai.futurelove.databinding.FragmentHistoryBinding;
import com.thinkdiffai.futurelove.model.EventHomeDto;

import com.thinkdiffai.futurelove.view.activity.MainActivity;
import com.thinkdiffai.futurelove.view.adapter.HistoryAdapter;

import java.util.ArrayList;
import java.util.List;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding fragmentHistoryBinding;
    private MainActivity mainActivity;
    private KProgressHUD kProgressHUD;
    private List<EventHomeDto> eventHomeDtoList;
    private HistoryAdapter historyAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentHistoryBinding = FragmentHistoryBinding.inflate(inflater, container, false);

        mainActivity = (MainActivity) getActivity();
        kProgressHUD = mainActivity.createHud();


//        try {
        initUi();
        getEventHomeDtos();
        initListener();

//        } catch (Exception e) {
//            Log.e("ExceptionRuntime", e.toString());
//        }
        return fragmentHistoryBinding.getRoot();

    }

    private void initListener() {

    }

    @SuppressLint("NotifyDataSetChanged")
    private void getEventHomeDtos() {
        eventHomeDtoList = EventHistoryDb.getInstance(getActivity()).eventHistoryDao().getAllEvent();
        historyAdapter.setData(eventHomeDtoList);
        historyAdapter.notifyDataSetChanged();
    }

    private void initUi() {
        eventHomeDtoList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity(), GridLayoutManager.VERTICAL, false);
        fragmentHistoryBinding.rcvHistory.setLayoutManager(linearLayoutManager);
        historyAdapter = new HistoryAdapter(eventHomeDtoList, new HistoryAdapter.IOnClickItemListener() {
            @Override
            public void onClickItem(int id) {
                mainActivity.eventSummaryCurrentId = id;
                mainActivity.setCurrentPage(3);
            }

            @Override
            public void onClickDelete(int id) {
                deleteStorageDb(id);
            }
        });
        fragmentHistoryBinding.rcvHistory.setAdapter(historyAdapter);
    }



    private void deleteStorageDb(int numberOder_id) {

        EventHomeDto event = EventHistoryDb.getInstance(getActivity()).eventHistoryDao().getEventByOrderNumber_id(numberOder_id);
        if (event == null)
            return;
        EventHistoryDb.getInstance(getActivity()).eventHistoryDao().delete(numberOder_id);
        getEventHomeDtos();

    }
}
