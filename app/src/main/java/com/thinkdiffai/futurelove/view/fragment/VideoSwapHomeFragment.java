package com.thinkdiffai.futurelove.view.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.FragmentVideoSwapHomeBinding;
import com.thinkdiffai.futurelove.model.EventVideoModel;
import com.thinkdiffai.futurelove.model.ListEventVideo;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.Server;
import com.thinkdiffai.futurelove.view.adapter.VideoHighLightsAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VideoSwapHomeFragment extends Fragment {

    int id_user;

    private FragmentVideoSwapHomeBinding fragmentVideoSwapHomeBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        fragmentVideoSwapHomeBinding = FragmentVideoSwapHomeBinding.inflate(inflater,container,false);
        return fragmentVideoSwapHomeBinding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initAction();
    }
    private void initView(List<EventVideoModel> eventVideoModelList) {
       VideoHighLightsAdapter videoHighLightsAdapter = new VideoHighLightsAdapter(getContext(),eventVideoModelList);
       LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
       fragmentVideoSwapHomeBinding.highlightsVideoRec.setLayoutManager(linearLayoutManager);
       fragmentVideoSwapHomeBinding.highlightsVideoRec.setAdapter(videoHighLightsAdapter);
    }
    private void initAction() {
        fragmentVideoSwapHomeBinding.createVideoBtn.setOnClickListener(v -> navigateToListVideoFragment());
        fragmentVideoSwapHomeBinding.backBtn.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
    }
    private void navigateToListVideoFragment() {
        NavHostFragment.findNavController(VideoSwapHomeFragment.this).navigate(R.id.action_videoSwapHomeFragment_to_listVideoFragment);
    }
    private void initData() {
        loadIdUser();
        genVideoWithUser();
    }
    private void loadIdUser() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("id_user",0);
        String id_user_str = sharedPreferences.getString("id_user_str","0");
        id_user = Integer.parseInt(id_user_str);
    }
    private void genVideoWithUser() {
        ApiService apiService = RetrofitClient.getInstance("").getRetrofit().create(ApiService.class);
        Log.d("check_response", "genVideoWithUser: "+ id_user);
        Call<ListEventVideo> call = apiService.GenVideoWithUser(id_user,1);
        call.enqueue(new Callback<ListEventVideo>() {
            @Override
            public void onResponse(@NonNull Call<ListEventVideo> call, @NonNull Response<ListEventVideo> response) {
                if (response.isSuccessful() && response.body() != null ){
                    ListEventVideo listEventVideo = response.body();
                    List <EventVideoModel> eventVideoModelList = listEventVideo.getEventVideoModelList();
                    initView(eventVideoModelList);
                    Log.d("check_response", "onResponse: "+ response.body());
                    Log.d("check_response", "onResponse: "+ response.body().getEventVideoModelList().get(0).getListVideoHighlightsModels());
                }else{
                    Log.d("check_response", "onResponse: No No No");
                }
            }
            @Override
            public void onFailure(@NonNull Call<ListEventVideo> call, @NonNull Throwable t) {
                Log.d("check_response", "onFailure: "+ t.getMessage());
            }
        });
    }
}