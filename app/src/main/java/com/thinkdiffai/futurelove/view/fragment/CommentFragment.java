package com.thinkdiffai.futurelove.view.fragment;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.thinkdiffai.futurelove.databinding.FragmentCommentBinding;
import com.thinkdiffai.futurelove.model.comment.CommentList;
import com.thinkdiffai.futurelove.model.comment.Comment;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.Server;

import com.thinkdiffai.futurelove.util.PaginationScrollListener;
import com.thinkdiffai.futurelove.view.activity.MainActivity;
import com.thinkdiffai.futurelove.view.adapter.CommentAdapter;


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

    private List<Comment> commentsForAdapter;
    private CommentAdapter commentAdapter;

    private int numberOfElements;

    private double pageNumber;
    private int currentPage = 1;
    private LinearLayoutManager linearLayoutManager;
    private boolean isLoading;
    private boolean isLastPage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentCommentBinding = FragmentCommentBinding.inflate(inflater, container, false);
        getActivity().getWindow().setSoftInputMode(SOFT_INPUT_ADJUST_PAN);
        mainActivity = (MainActivity) getActivity();
        kProgressHUD = mainActivity.createHud();

        try {
            initUi();
            getCommentNew();
            initListenerCommentNew();

        } catch (Exception e) {
            Log.e("ExceptionRuntime", e.toString());
        }
        return fragmentCommentBinding.getRoot();

    }


    private void initListenerCommentNew() {

        fragmentCommentBinding.rcvComment.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
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
                getCommentNew();
            }

        });
//        fragmentCommentBinding.rcvComment.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//
//                if (dy < 0&&linearLayoutManager.findFirstVisibleItemPosition() == 0 && dy < 0 && linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
//                    currentPage =1;
//                    getCommentNew();
//                }
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });
    }


//    private void loadNextNewComment(){
//        new PaginationScrollListener(linearLayoutManager) {
//            @Override
//            public void loadMoreItem() {
//                isLoading = true;
//                currentPage++;
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
//        };
//    }

    private void loadNextPage() {
        getCommentNew();

    }

    private void getCommentNew() {
        if (!kProgressHUD.isShowing()) {
            kProgressHUD.show();
        }
        if (currentPage == 1) {
            commentsForAdapter.clear();
        }
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Call<CommentList> call = apiService.getListCommentNew(currentPage);
        call.enqueue(new Callback<CommentList>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<CommentList> call, @NonNull Response<CommentList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CommentList _commentList = response.body();
                    commentsForAdapter = _commentList.getComment();
                    numberOfElements = _commentList.getSophantu();
                    pageNumber = _commentList.getSotrang();

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


    private void initUi() {
        commentsForAdapter = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity(), GridLayoutManager.VERTICAL, false);
        fragmentCommentBinding.rcvComment.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentAdapter(commentsForAdapter, this::iOnClickItem);
        fragmentCommentBinding.rcvComment.setAdapter(commentAdapter);
    }

    private void iOnClickItem(int idToanBoSuKien, int soThuTuSuKienCon) {
        mainActivity.eventSummaryCurrentId = idToanBoSuKien;
        mainActivity.soThuTuSuKien = soThuTuSuKienCon;
        mainActivity.setCurrentPage(4);
    }
}
