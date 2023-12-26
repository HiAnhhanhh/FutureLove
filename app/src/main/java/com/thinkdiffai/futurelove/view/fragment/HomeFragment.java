package com.thinkdiffai.futurelove.view.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.FragmentHomeBinding;
import com.thinkdiffai.futurelove.model.DetailEventList;
import com.thinkdiffai.futurelove.model.DetailEventListParent;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.Server;

import com.thinkdiffai.futurelove.view.fragment.activity.MainActivity;
import com.thinkdiffai.futurelove.view.adapter.EventHomeAdapter;
import com.thinkdiffai.futurelove.view.adapter.PageEventAdapter;

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

    private int id_user;
    private EventHomeAdapter eventHomeAdapter;

    private BubbleNavigationLinearView bubbleNavigationLinearView;

    private PageEventAdapter pageEventAdapter;
    private List<DetailEventList> eventList;

    private ArrayList<Integer> pageEventList;
    private LinearLayoutManager linearLayoutManager;

    private boolean isLoading;
    private boolean isLoadingMore;
    private boolean isLastPage;
//    private int currentPage = 1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        mainActivity = (MainActivity) getActivity();
        kProgressHUD = mainActivity.createHud();

//        checkClickSetImageMale =  true;
        performSearch();
        try {
            initUi();
            loadData();
//            initListener();
            initData();
        } catch (Exception e) {
            Log.e("ExceptionRuntime", e.toString());
        }

        return fragmentHomeBinding.getRoot();
    }


    private void loadData() {
        if (!kProgressHUD.isShowing() && isLoadingMore) {
            kProgressHUD.show();
        }
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Call<DetailEventListParent> call = apiService.getEventListForHome(1,id_user);
        Log.d("check_response", "getData: "+ call.toString());
        call.enqueue(new Callback<DetailEventListParent>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<DetailEventListParent> call, Response<DetailEventListParent> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("check_response_1", "onResponse: pke");
                    Log.d("check_response_1", "onResponse: "+ response.body());
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

    private void initData() {
        Log.d("CHECK_LOGIN_FRAGMENT", "initData: oke");
        Bundle data = getArguments();
        Log.d("CHECK_LOGIN_FRAGMENT", "initData: oke");
        if(data!=null){
            String user_id = data.getString("user_id");
            Log.d("id_user_detail", "initData: "+ user_id);
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("id_user",0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("id_user",user_id);
            editor.commit();
        }
        loadIdUser();
    }

    private void loadIdUser() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("id_user",0);
        String id_user_str = sharedPreferences.getString("id_user", "");
        Log.d("check_user_id", "loadIdUser: "+ id_user_str);
        if (id_user_str == "") {
            id_user = 0;
        }else{
            id_user = Integer.parseInt(id_user_str);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnClickUserDetail();
    }

    private void setOnClickUserDetail() {
        fragmentHomeBinding.btnUserAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToUserDetailFragment();
            }
        });
    }

    private void navigateToUserDetailFragment() {
        NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_homeFragment_to_userDetailFragment);
        mainActivity.homeToUserDetail = true;
    }


    private void goToListVideoFragment() {
        NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_homeFragment_to_listVideoFragment);
    }

    private void goToPairingFragment() {
        NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_homeFragment_to_pairingFragment);
    }

    private void goToTimeLineFragment() {
        NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_homeFragment_to_timelineFragment);
    }

    private void goToCommentFragment() {
        NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_homeFragment_to_commentFragment);
    }

//    private void initListener() {
//        fragmentHomeBinding.rcvHome.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
//            @Override
//            public void loadMoreItem() {
//                // It help the kud not loading many times
//                isLoadingMore = false;
//                isLoading = true;
//                currentPage++;
//                loadNextPage();
//            }
//            @Override
//            public boolean isLoading() {
//                return isLoading;
//            }
//
//            @Override
//            public boolean isLagePage() {
//                return isLastPage;
//            }
//
//            @Override
//            public void ReloadItem() {
//                // It help the kud not loading many times
//                isLoadingMore = false;
//                currentPage = 1;
//                getData(currentPage);
//            }
//        });
//    }

//    private void loadNextPage() {
//        getData(currentPage);
//    }

    private void initUi() {
        // Set isLoadingMore = true in order to set kud loading in the first api calling time
        isLoadingMore = true;

        //Reset all events
        mainActivity.soThuTuSuKien = 0;
        eventList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity(), GridLayoutManager.VERTICAL, false);
        fragmentHomeBinding.rcvHome.setLayoutManager(linearLayoutManager);
        // It automatically get the id of all events in EventHomeAdapter and assign into this::goToEventDetail
        eventHomeAdapter = new EventHomeAdapter(eventList, idToanBoSuKien -> goToEventDetail(idToanBoSuKien), getContext());
        fragmentHomeBinding.rcvHome.setAdapter(eventHomeAdapter);

        pageEventList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        for(int i=1; i<=19;i++){
            pageEventList.add(i);
        }
        pageEventAdapter = new PageEventAdapter(getContext(), pageEventList, position -> goToPageEvent(position));
        fragmentHomeBinding.tabItem.setAdapter(pageEventAdapter);
    }

    // Current page is 4 because it is Timeline Fragment
    private void goToEventDetail(long idToanBoSuKien) {
        mainActivity.eventSummaryCurrentId = idToanBoSuKien;
        goToTimeLineFragment();
//        mainActivity.setCurrentPage(4);
    }

    private void goToPageEvent(int position){
        getData(position);
    }

    private void getData(int position) {
        if (!kProgressHUD.isShowing() && isLoadingMore) {
            kProgressHUD.show();
        }
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Call<DetailEventListParent> call = apiService.getEventListForHome(position,id_user);
        Log.d("check_response", "getData: "+ call.toString());
        call.enqueue(new Callback<DetailEventListParent>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<DetailEventListParent> call, Response<DetailEventListParent> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("check_response", "onResponse: "+ response.body());
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

//    private void getData() {
//        if (!kProgressHUD.isShowing() && isLoadingMore) {
//            kProgressHUD.show();
//        }
//        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
//        Call<DetailEventListParent> call = apiService.getEventListForHome(1);
//        Log.d("check_response", "getData: "+ call.toString());
//        call.enqueue(new Callback<DetailEventListParent>() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onResponse(Call<DetailEventListParent> call, Response<DetailEventListParent> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    Log.d("check_response", "onResponse: pke");
//                    Log.d("check_response", "onResponse: "+ response.body().getListSukien().size());
//                    DetailEventListParent detailEventListParent = response.body();
//                    List<DetailEventList> detailEventLists = detailEventListParent.getListSukien();
//                    if (!detailEventLists.isEmpty()){
//                        eventHomeAdapter.setData(detailEventLists);
//                        eventHomeAdapter.notifyDataSetChanged();
//                    }
//                }
//                if (kProgressHUD.isShowing()) {
//                    kProgressHUD.dismiss();
//                }
//                isLoading = false;
//            }
//
//            @Override
//            public void onFailure(Call<DetailEventListParent> call, Throwable t) {
//                Toast.makeText(getActivity(), t.getMessage() + "", Toast.LENGTH_SHORT).show();
//                if (kProgressHUD.isShowing()) {
//                    kProgressHUD.dismiss();
//                    Log.e("MainActivityLog", t.getMessage().toString());
//                }
//                isLoading = false;
//            }
//        });
//    }
    private void performSearch() {
        fragmentHomeBinding.edtSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                eventHomeAdapter.getFilter().filter(query);
                eventHomeAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                eventHomeAdapter.getFilter().filter(newText);
                eventHomeAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }
}
