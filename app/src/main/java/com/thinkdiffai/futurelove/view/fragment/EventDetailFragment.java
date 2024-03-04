package com.thinkdiffai.futurelove.view.fragment;

import static com.jaredrummler.android.device.DeviceName.getDeviceName;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thinkdiffai.futurelove.databinding.DialogCommentBinding;
import com.thinkdiffai.futurelove.databinding.FragmentEventDetailBinding;
import com.thinkdiffai.futurelove.model.ListEventDetailModel;
import com.thinkdiffai.futurelove.model.comment.CommentPage;
import com.thinkdiffai.futurelove.model.comment.DetailUser;
import com.thinkdiffai.futurelove.model.comment.eacheventcomment.EachEventCommentsList;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.Server;
import com.thinkdiffai.futurelove.view.adapter.CommentInEventDetailAdapter;
import com.thinkdiffai.futurelove.view.adapter.ListEventDetailUserAdapter;
import com.thinkdiffai.futurelove.view.fragment.activity.AddEventActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetailFragment extends Fragment {

    FragmentEventDetailBinding eventDetailBinding;

    String token_auth;


    private String deviceName;

    DialogCommentBinding dialogCommentBinding;

    String nameUser,email, imageUser;

    Dialog dialog;

    ArrayList<CommentPage> eachEventCommentsListList;
    int id_user;
    String noi_dung_cmt;

    CommentInEventDetailAdapter commentInEventDetailAdapter;

    ListEventDetailUserAdapter listEventDetailUserAdapter;

    ArrayList<ListEventDetailModel.EventDetailModel> listEventDetailModels;
    String id_toan_bo_su_kien;
    int so_thu_tu_su_kien;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        eventDetailBinding = FragmentEventDetailBinding.inflate(inflater,container,false);
        return eventDetailBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
        initAction();
    }

    private void initAction() {
        eventDetailBinding.addEvent.setOnClickListener(v ->{
            Intent intent = new Intent(getActivity(), AddEventActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("id_event", Long.parseLong(id_toan_bo_su_kien));
            intent.putExtra("send_id", bundle);
            startActivity(intent);
        });

        eventDetailBinding.backBtn.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
        eventDetailBinding.submitComment.setOnClickListener(v -> {
            noi_dung_cmt = String.valueOf(eventDetailBinding.commentEt.getText());
            if(!noi_dung_cmt.equals("")){
                eventDetailBinding.commentEt.setText("");
                postComment(noi_dung_cmt);
            }
        });

    }

    private void postComment(String noi_dung_cmt) {
        ApiService apiService = RetrofitClient.getInstance("").getRetrofit().create(ApiService.class);
        Log.d("check_comment", "postComment: "+ id_user+noi_dung_cmt+deviceName+ Long.parseLong(id_toan_bo_su_kien)+so_thu_tu_su_kien);
        Call<CommentPage> call = apiService.postComment(id_user,noi_dung_cmt,deviceName, id_toan_bo_su_kien,so_thu_tu_su_kien,"","",id_user,"","");
        call.enqueue(new Callback<CommentPage>() {
            @Override
            public void onResponse(Call<CommentPage> call, Response<CommentPage> response) {
                if(response.isSuccessful() && response.body()!=null){
                    eventDetailBinding.commentEt.setText("");
                    loadCommentEvent();
                }
            }

            @Override
            public void onFailure(Call<CommentPage> call, Throwable t) {

            }
        });
    }

    private void initView() {

    }

    private void initData() {
        loadProfile();
        loadIdUser();
        deviceName = getDeviceName();
        loadIdToanBoSuKien();
        loadEventSuKien();
        loadCommentEvent();
    }

    private String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    private void loadProfile() {
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Call<DetailUser> call = apiService.getProfileUser(id_user);
        call.enqueue(new Callback<DetailUser>() {
            @Override
            public void onResponse(Call<DetailUser> call, Response<DetailUser> response) {
                if(response.isSuccessful()){
                    DetailUser detailUser = response.body();
                    nameUser = detailUser.getUser_name();
                    email = detailUser.getEmail();
                    imageUser = detailUser.getLink_avatar();
                }
            }
            @Override
            public void onFailure(Call<DetailUser> call, Throwable t) {
                Log.d("check_profile", "onFailure: "+ t.getMessage());
            }
        });
    }
    private void loadCommentEvent() {
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Call<EachEventCommentsList> call = apiService.getListCommentByEventId(so_thu_tu_su_kien,Long.parseLong(id_toan_bo_su_kien),id_user);
        call.enqueue(new Callback<EachEventCommentsList>() {
            @Override
            public void onResponse(Call<EachEventCommentsList> call, Response<EachEventCommentsList> response) {
                if (response.isSuccessful() && response.body() != null){
                    if (response.body().getEachEventCommentList().size()!=0) {
                        eachEventCommentsListList = new ArrayList<>();
                        eachEventCommentsListList.addAll(response.body().getEachEventCommentList());
                        initRecView();
                    }
                }
            }
            @Override
            public void onFailure(Call<EachEventCommentsList> call, Throwable t) {
            }
        });
    }
    private void initRecView() {
//        commentInEventDetailAdapter.updateData(eachEventCommentsListList);
        commentInEventDetailAdapter = new CommentInEventDetailAdapter(eachEventCommentsListList,getContext(), ((id_comment, position) -> {
            goToComment(id_comment,position);
        }));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        eventDetailBinding.commentAdapter.setLayoutManager(linearLayoutManager);
        eventDetailBinding.commentAdapter.setAdapter(commentInEventDetailAdapter);
    }

    private void goToComment(int id_comment, int position) {
        dialogCommentBinding= DialogCommentBinding.inflate(LayoutInflater.from(requireActivity()));
        dialog= new Dialog(requireActivity());
        dialog.setContentView(dialogCommentBinding.getRoot());
        dialog.show();

        dialogCommentBinding.buttonDeleteComment.setOnClickListener(v -> {
            deleteComment(id_comment,position);
        });
    }

    private void deleteComment(int id_comment, int position) {
        ApiService apiService = RetrofitClient.getInstance("").getRetrofit().create(ApiService.class);
        Call<Object> call = apiService.deleteComment(id_comment,"Bearer "+token_auth);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                dialog.dismiss();
                if(eachEventCommentsListList.size()>0){
                    eachEventCommentsListList.remove(position);
                    eventDetailBinding.commentAdapter.getAdapter().notifyItemRemoved(position);
                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
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

    private void loadEventSuKien() {
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Call<ListEventDetailModel> call = apiService.getListEventDetailUser(Long.parseLong(id_toan_bo_su_kien));
        call.enqueue(new Callback<ListEventDetailModel>() {
            @Override
            public void onResponse(Call<ListEventDetailModel> call, Response<ListEventDetailModel> response) {
                if(response.isSuccessful() && response.body()!=null){
                    listEventDetailModels = new ArrayList<>();
                    listEventDetailModels.addAll(response.body().getEventDetailModels());
                    Log.d("check_event_detail", "onResponse: "+ listEventDetailModels.size());
                    initViewRec();
                }
            }
            @Override
            public void onFailure(Call<ListEventDetailModel> call, Throwable t) {

            }
        });
    }

    private void initViewRec() {
        listEventDetailUserAdapter = new ListEventDetailUserAdapter(listEventDetailModels, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        eventDetailBinding.rcvComment.setLayoutManager(linearLayoutManager);
        eventDetailBinding.rcvComment.setAdapter(listEventDetailUserAdapter);
    }


    private void loadIdToanBoSuKien() {
        Bundle bundle = getArguments();
        if (bundle!=null){
            so_thu_tu_su_kien = Integer.parseInt(bundle.getString("so_thu_tu_su_kien")) ;
            id_toan_bo_su_kien = bundle.getString("id_toan_bo_su_kien");
            Log.d("check_id_sk", "loadIdToanBoSuKien: "+ so_thu_tu_su_kien + id_toan_bo_su_kien);
        }
    }
}