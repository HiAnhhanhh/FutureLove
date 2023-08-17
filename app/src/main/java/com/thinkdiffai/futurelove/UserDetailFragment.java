package com.thinkdiffai.futurelove;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.thinkdiffai.futurelove.databinding.FragmentUserDetailBinding;
import com.thinkdiffai.futurelove.model.comment.CommentUser;
import com.thinkdiffai.futurelove.model.comment.DetailUser;
import com.thinkdiffai.futurelove.model.comment.UserComment;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.Server;
import com.thinkdiffai.futurelove.view.adapter.UserCommnetAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailFragment extends Fragment {
    private FragmentUserDetailBinding fragmentUserDetailBinding;
    private UserCommnetAdapter commentAdapter;
    private List<CommentUser> commentList= new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentUserDetailBinding= fragmentUserDetailBinding.inflate(inflater, container, false);
        //Get id
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        fragmentUserDetailBinding.rcvPersonalComments.setLayoutManager(linearLayoutManager);
        commentAdapter= new UserCommnetAdapter(commentList,getContext());
        fragmentUserDetailBinding.rcvPersonalComments.setAdapter(commentAdapter);
        // xu ly nut cancle
        fragmentUserDetailBinding.btnComeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(UserDetailFragment.this).navigate(R.id.action_userDetailFragment_to_homeFragment);
            }
        });
        performSearch();
        // hien thi thong tin detail user
        try {
            getDataUserDetail();
        } catch (Exception e) {
            Log.e("ExceptionRuntime", e.toString());
        }
        // hien thi comment
        try {
            GetDataCommentUser();
        } catch (Exception e) {
            Log.e("ExceptionRuntime", e.toString());
        }
        return fragmentUserDetailBinding.getRoot();
    }
    private void getDataUserDetail(){
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Call<DetailUser> call = apiService.getProfileUser(5);
        call.enqueue(new Callback<DetailUser>() {
            @Override
            public void onResponse(Call<DetailUser> call, Response<DetailUser> response) {
                if (response.isSuccessful()) {
                    DetailUser detailUsers = response.body();
                    fragmentUserDetailBinding.tvUserName.setText(""+detailUsers.getUser_name());
                    fragmentUserDetailBinding.tvUserEventsNumber.setText(""+detailUsers.getCount_sukien());
                    fragmentUserDetailBinding.tvUserCommentNumber.setText(""+detailUsers.getCount_comment());
                    fragmentUserDetailBinding.tvUserViewNumber.setText(""+detailUsers.getCount_view());
                    Glide.with(getActivity()).load(detailUsers.getLink_avatar()).into(fragmentUserDetailBinding.imgUserAvatar);
                } else {
                    fragmentUserDetailBinding.tvUserName.setText("null");
                    fragmentUserDetailBinding.tvUserEventsNumber.setText("null");
                    fragmentUserDetailBinding.tvUserCommentNumber.setText("null");
                    fragmentUserDetailBinding.tvUserViewNumber.setText("null");
                }
            }
            @Override
            public void onFailure(Call<DetailUser> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private  void GetDataCommentUser(){
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Call<UserComment> call = apiService.getCommentUser(5);
        call.enqueue(new Callback<UserComment>() {
            @Override
            public void onResponse(Call<UserComment> call, Response<UserComment> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserComment userComment = response.body();
                    commentList = userComment.getComment_user();
                    if (!commentList.isEmpty()){
                         commentAdapter.setData(commentList);
                         commentAdapter.notifyDataSetChanged();
                    }

                }
            }
            @Override
            public void onFailure(Call<UserComment> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void performSearch() {
        fragmentUserDetailBinding.edtSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                commentAdapter.getFilter().filter(query);
                commentAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                commentAdapter.getFilter().filter(newText);
                commentAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }
}