package com.thinkdiffai.futurelove.view.fragment;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.FragmentCommentBinding;
import com.thinkdiffai.futurelove.model.comment.CommentList;
import com.thinkdiffai.futurelove.model.comment.CommentPage;
import com.thinkdiffai.futurelove.model.comment.CommentUser;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.Server;

import com.thinkdiffai.futurelove.view.fragment.activity.MainActivity;
import com.thinkdiffai.futurelove.view.adapter.CommentAdapter;
import com.thinkdiffai.futurelove.view.adapter.PageCommentAdapter;


import java.util.ArrayList;
import java.util.List;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentFragment extends Fragment {
    private FragmentCommentBinding fragmentCommentBinding;
    private MainActivity mainActivity;
    private KProgressHUD kProgressHUD;

    private BubbleNavigationLinearView bubbleNavigationLinearView;


    // Returned list
    private List<CommentPage> commentsForAdapter;
    private PageCommentAdapter pageCommentAdapter;
    // Returned city
    private String city;
    private CommentAdapter commentAdapter;

    private int numberOfElements;
    private int id_user;

    private double pageNumber;
    private int currentPage = 1;
    private LinearLayoutManager linearLayoutManager;

    private ArrayList<Integer> pageCommentArrayList;
    private boolean isLoading;

    // It will be initialed in initUi() and reload rcv
    private boolean isLoadingMore;

    private boolean isLastPage;
    private List<CommentUser> commentList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentCommentBinding = FragmentCommentBinding.inflate(inflater, container, false);
        getActivity().getWindow().setSoftInputMode(SOFT_INPUT_ADJUST_PAN);
        mainActivity = (MainActivity) getActivity();
        kProgressHUD = mainActivity.createHud();

        try {
            initUi();
//            getCommentNew();
            initListenerCommentNew();
            initLoadData();
            initData();

        } catch (Exception e) {
            Log.e("ExceptionRuntime", e.toString());
        }
        return fragmentCommentBinding.getRoot();
    }

    private void initData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("id_user",0);
        String id_user_str = sharedPreferences.getString("id_user", "");
        if (id_user_str == "") {
            id_user = 0;
        }else{
            id_user = Integer.parseInt(id_user_str);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initLoadData() {
        if (!kProgressHUD.isShowing() && isLoadingMore) {
            kProgressHUD.show();
        }
//        if (currentPage == 1) {
//            commentsForAdapter.clear();
//        }
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Call<CommentList> call = apiService.getListCommentNew(0,id_user);
        call.enqueue(new Callback<CommentList>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<CommentList> call, @NonNull Response<CommentList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CommentList _commentList = response.body();
                    commentsForAdapter = _commentList.getComment();
//                    numberOfElements = _commentList.getSophantu();
//                    pageNumber = _commentList.getSotrang();
                    Log.d("check_newComment", "onResponse: thành công  " );
                    if (!commentsForAdapter.isEmpty()) {
                        commentAdapter.setData(commentsForAdapter);
                        commentAdapter.notifyDataSetChanged();
                    }
                }
                isLoading = false;
                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommentList> call, @NonNull Throwable t) {
                isLoading = false;
                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();

                }
                Log.e("MainActivityLog", t.getMessage());
            }
        });
    }

//    private void goToListVideoFragment() {
//        NavHostFragment.findNavController(CommentFragment.this).navigate(R.id.action_commentFragment_to_listVideoFragment);
//    }
//
//    private void goToHomeFragment() {
//        NavHostFragment.findNavController(CommentFragment.this).navigate(R.id.action_commentFragment_to_homeFragment);
//    }
//
//    private void goToPairingFragment() {
//        NavHostFragment.findNavController(CommentFragment.this).navigate(R.id.action_commentFragment_to_pairingFragment);
//    }
//    private void goToEventFragment(){
//        NavHostFragment.findNavController(CommentFragment.this).navigate(R.id.action_commentFragment_to_eventsFragment);
//    }
    private void goToTimeLineFragment() {
        NavHostFragment.findNavController(CommentFragment.this).navigate(R.id.action_commentFragment_to_timelineFragment);
    }
//    private void goToUserDetailFragment(){
//        NavHostFragment.findNavController(CommentFragment.this).navigate(R.id.action_commentFragment_to_userDetailFragment);
//        mainActivity.commentToUserDetail = true;
//    }
    private void initListenerCommentNew() {

//        fragmentCommentBinding.rcvComment.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
//            @Override
//            public void loadMoreItem() {
//                isLoading = true;
//                // It help the kud not loading many times
//                isLoadingMore = false;
////                currentPage++;
//                loadNextPage();
//            }
//
//            @Override
//            public boolean isLoading() {
//                return isLoading;
//            }
//
//            @Override
//            public boolean isLagePage() {
//
//                return isLastPage;
//            }
//
//            @Override
//            public void ReloadItem() {
//                // It help the kud not loading many times
//                isLoadingMore = false;
//                currentPage = 1;
//                getCommentNew();
//            }
//
//        });
    }

//    private void loadNextPage() {
////        getCommentNew();
//        if (currentPage == 1) {
//            commentsForAdapter.clear();
//        }
//        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
//        Call<CommentList> call = apiService.getListCommentNew(currentPage);
//        call.enqueue(new Callback<CommentList>() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onResponse(@NonNull Call<CommentList> call, @NonNull Response<CommentList> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    CommentList _commentList = response.body();
//                    commentsForAdapter = _commentList.getComment();
//                    numberOfElements = _commentList.getSophantu();
//                    pageNumber = _commentList.getSotrang();
//
//                    if (!commentsForAdapter.isEmpty()) {
//                        commentAdapter.setData(commentsForAdapter);
//                        commentAdapter.notifyDataSetChanged();
//                    }
//                }
//                isLoading = false;
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<CommentList> call, @NonNull Throwable t) {
//                isLoading = false;
//                Log.e("MainActivityLog", t.getMessage());
//            }
//        });
//    }

    private void getData(int position) {
        if (!kProgressHUD.isShowing() && isLoadingMore) {
            kProgressHUD.show();
        }
//        if (currentPage == 1) {
//            commentsForAdapter.clear();
//        }
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Call<CommentList> call = apiService.getListCommentNew(position, id_user);
        call.enqueue(new Callback<CommentList>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<CommentList> call, @NonNull Response<CommentList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CommentList _commentList = response.body();
                    commentsForAdapter = _commentList.getComment();
//                    numberOfElements = _commentList.getSophantu();
//                    pageNumber = _commentList.getSotrang();
                    Log.d("check_newComment", "onResponse: thành công"+ pageNumber+" " + response.body());


                    if (!commentsForAdapter.isEmpty()) {
                        commentAdapter.setData(commentsForAdapter);
                        commentAdapter.notifyDataSetChanged();
                    }
                }
                isLoading = false;
                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommentList> call, @NonNull Throwable t) {
                isLoading = false;
                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();

                }
                Log.e("MainActivityLog", t.getMessage());
            }
        });
    }

//    private void getCommentNew() {
//        if (!kProgressHUD.isShowing() && isLoadingMore) {
//            kProgressHUD.show();
//        }
//        if (currentPage == 1) {
//            commentsForAdapter.clear();
//        }
//        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN1).getRetrofit().create(ApiService.class);
//        Call<CommentList> call = apiService.getListCommentNew(currentPage);
//        call.enqueue(new Callback<CommentList>() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onResponse(@NonNull Call<CommentList> call, @NonNull Response<CommentList> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    CommentList _commentList = response.body();
//                    Log.d("check_newComment", "onResponse: thành công");
//                    commentsForAdapter = _commentList.getComment();
//                    numberOfElements = _commentList.getSophantu();
//                    pageNumber = _commentList.getSotrang();
//
//                    if (!commentsForAdapter.isEmpty()) {
//                        commentAdapter.setData(commentsForAdapter);
//                        commentAdapter.notifyDataSetChanged();
//                    }
//                }
//                isLoading = false;
//                if (kProgressHUD.isShowing()) {
//                    kProgressHUD.dismiss();
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<CommentList> call, @NonNull Throwable t) {
//                isLoading = false;
//                if (kProgressHUD.isShowing()) {
//                    kProgressHUD.dismiss();
//
//                }
//                Log.e("MainActivityLog", t.getMessage());
//            }
//        });
//    }


    private void initUi() {
        // Set isLoadingMore = true in order to set kud loading in the first api calling time
        isLoadingMore = true;
        commentsForAdapter = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity(), GridLayoutManager.VERTICAL, false);
        fragmentCommentBinding.rcvComment.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentAdapter(requireContext(), commentsForAdapter, this::iOnClickItem);
        fragmentCommentBinding.rcvComment.setAdapter(commentAdapter);

        pageCommentArrayList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity(), GridLayoutManager.VERTICAL, false);
        fragmentCommentBinding.rcvComment.setLayoutManager(linearLayoutManager);
        for(int i=1; i<=5;i++){
            pageCommentArrayList.add(i);
        }
        pageCommentAdapter = new PageCommentAdapter(getContext(),pageCommentArrayList,position -> goToPageComment(position));
        fragmentCommentBinding.tabItem.setAdapter(pageCommentAdapter);
    }

    private void goToPageComment(int position) {
        getData(position);
    }


    private void iOnClickItem(long idToanBoSuKien, int soThuTuSuKienCon) {
        mainActivity.eventSummaryCurrentId = idToanBoSuKien;
        mainActivity.soThuTuSuKien = soThuTuSuKienCon;
        goToTimeLineFragment();
    }
}
