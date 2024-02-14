package com.thinkdiffai.futurelove.view.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.FragmentCommentEventDetailBinding;
import com.thinkdiffai.futurelove.databinding.FragmentEventDetailBinding;
import com.thinkdiffai.futurelove.model.comment.CommentPage;
import com.thinkdiffai.futurelove.model.comment.eacheventcomment.EachEventCommentsList;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.Server;
import com.thinkdiffai.futurelove.view.adapter.CommentAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentEventDetailFragment extends Fragment {
    String so_thu_tu_su_kien, id_toan_bo_su_kien;
    String token_auth;
    String urlImgMale,urlImgFemale;
    private CommentAdapter commentAdapter;


    ArrayList<CommentPage> eachEventCommentsListList;
    int id_user;
    FragmentCommentEventDetailBinding fragmentCommentEventDetailBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentCommentEventDetailBinding = FragmentCommentEventDetailBinding.inflate(inflater,container,false);
        return fragmentCommentEventDetailBinding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initAction();
    }

    private void initAction() {
        fragmentCommentEventDetailBinding.submitComment.setOnClickListener(v -> {
            postComment();
        });
    }

    private void postComment() {

    }

    private void initData() {
        loadData();
        loadIdUser();
        callApiAddress();
        loadCommentEvent();
    }

    private void callApiAddress() {

    }

    private void loadCommentEvent() {
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Call<EachEventCommentsList> call = apiService.getListCommentByEventId(Integer.parseInt(so_thu_tu_su_kien),Long.parseLong(id_toan_bo_su_kien),id_user);
        call.enqueue(new Callback<EachEventCommentsList>() {
            @Override
            public void onResponse(Call<EachEventCommentsList> call, Response<EachEventCommentsList> response) {
                if (response.isSuccessful() && response.body() != null){
                    if (response.body().getEachEventCommentList().size()!=0) {
                        eachEventCommentsListList = new ArrayList<>();
                        eachEventCommentsListList.addAll(response.body().getEachEventCommentList());
                    }
                }
            }
            @Override
            public void onFailure(Call<EachEventCommentsList> call, Throwable t) {

            }
        });
    }

    private void loadIdUser() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("id_user",0);
        String id_user_str = sharedPreferences.getString("id_user_str","");
        token_auth = sharedPreferences.getString("token","");
        Log.d("check_share_id", "loadIdUser: "+ id_user_str +"_" +token_auth);
        if(id_user_str.equals("")){
            id_user = 0;
        }else{
            id_user = Integer.parseInt(id_user_str);
        }
    }

    private void loadData() {
        Bundle bundle = getArguments();
        so_thu_tu_su_kien = bundle != null ? bundle.getString("s_t_t_s_k") : null;
        id_toan_bo_su_kien = bundle != null ? bundle.getString("id_t_b_s_k") : null;
    }
}