package com.thinkdiffai.futurelove.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.thinkdiffai.futurelove.databinding.FragmentHomeBinding;
import com.thinkdiffai.futurelove.model.DetailEventList;
import com.thinkdiffai.futurelove.model.DetailEventListParent;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.Server;

import com.thinkdiffai.futurelove.util.PaginationScrollListener;
import com.thinkdiffai.futurelove.view.activity.MainActivity;
import com.thinkdiffai.futurelove.view.adapter.EventHomeAdapter;

import java.util.ArrayList;
import java.util.List;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private KProgressHUD kProgressHUD;
    private FragmentHomeBinding fragmentHomeBinding;
    private MainActivity mainActivity;
    private EventHomeAdapter eventHomeAdapter;
    private List<DetailEventList> eventList;
    private LinearLayoutManager linearLayoutManager;

    private boolean isLoading;
    private boolean isLastPage;
    private int currentPage = 1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        mainActivity = (MainActivity) getActivity();
        kProgressHUD = mainActivity.createHud();

//        checkClickSetImageMale =  true;
        try {
            initUi();
            getData(currentPage);
            initListener();

        } catch (Exception e) {
            Log.e("ExceptionRuntime", e.toString());
        }


        return fragmentHomeBinding.getRoot();
    }

    private void initListener() {
        fragmentHomeBinding.rcvHome.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItem() {
                isLoading = true;
                currentPage++;
                loadNextPage();
            }
            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLagePage() {
                return isLastPage;
            }

            @Override
            public void ReloadItem() {
                currentPage = 1;
                getData(currentPage);
            }
        });
    }

    private void loadNextPage() {
        getData(currentPage);
    }

    private void initUi() {
        //Reset all events
        mainActivity.soThuTuSuKien = 0;
        eventList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity(), GridLayoutManager.VERTICAL, false);
        fragmentHomeBinding.rcvHome.setLayoutManager(linearLayoutManager);
        // It automatically get the id of all events in EventHomeAdapter and assign into this::goToEventDetail
        eventHomeAdapter = new EventHomeAdapter(eventList, this::goToEventDetail, getContext());
        fragmentHomeBinding.rcvHome.setAdapter(eventHomeAdapter);
    }

    // Current page is 4 because it is Timeline Fragment
    private void goToEventDetail(int idToanBoSuKien) {
        mainActivity.eventSummaryCurrentId = idToanBoSuKien;
        mainActivity.setCurrentPage(4);
    }

    private void getData(int currentPage) {
        if (!kProgressHUD.isShowing()) {
            kProgressHUD.show();
        }
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Call<DetailEventListParent> call = apiService.getEventListForHome(currentPage);
        call.enqueue(new Callback<DetailEventListParent>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<DetailEventListParent> call, Response<DetailEventListParent> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DetailEventListParent detailEventListParent = response.body();
                    List<DetailEventList> detailEventLists = detailEventListParent.getListSukien();
                    if (!detailEventLists.isEmpty()){
                        eventHomeAdapter.setData(detailEventLists);
                        eventHomeAdapter.notifyDataSetChanged();
                    }
                }
                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                }

                isLoading = false;
            }

            @Override
            public void onFailure(Call<DetailEventListParent> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage() + "", Toast.LENGTH_SHORT).show();
                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                    Log.e("MainActivityLog", t.getMessage().toString());
                }
                isLoading = false;
            }
        });
    }
}
