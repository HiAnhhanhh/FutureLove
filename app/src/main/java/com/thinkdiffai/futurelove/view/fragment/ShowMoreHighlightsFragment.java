package com.thinkdiffai.futurelove.view.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.FragmentShowMoreHighlightsBinding;
import com.thinkdiffai.futurelove.model.EventVideoModel;
import com.thinkdiffai.futurelove.model.ListEventVideo;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.view.adapter.PageVideoAdapter;
import com.thinkdiffai.futurelove.view.adapter.ShowMoreHighlightAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowMoreHighlightsFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentShowMoreHighlightsBinding fragmentShowMoreHighlightsBinding;
    int id_user;
    int countPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentShowMoreHighlightsBinding =  FragmentShowMoreHighlightsBinding.inflate(inflater, container,false);
        return fragmentShowMoreHighlightsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initAction();
        initView();
    }

    private void initAction() {
        fragmentShowMoreHighlightsBinding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
    }

    private void initData() {
        loadIdUser();
        genVideoWithUser0();
    }

    private void genVideoWithUser0() {
        ApiService apiService = RetrofitClient.getInstance("").getRetrofit().create(ApiService.class);
        Log.d("check_response", "genVideoWithUser: "+ id_user);
        Call<ListEventVideo> call = apiService.GenVideoWithUser(id_user,0);
        call.enqueue(new Callback<ListEventVideo>() {
            @Override
            public void onResponse(@NonNull Call<ListEventVideo> call, @NonNull Response<ListEventVideo> response) {
                if (response.isSuccessful() && response.body() != null ){
                    ListEventVideo listEventVideo = response.body();
                    List<EventVideoModel> eventVideoModelList = listEventVideo.getEventVideoModelList();
                    initViewRec(eventVideoModelList);
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

    private void initView() {
        ArrayList<Integer> pageListVideo = new ArrayList<>();
        for(int i=1; i<=5;i++){
            pageListVideo.add(i);
        }
        PageVideoAdapter pageVideoAdapter = new PageVideoAdapter(pageListVideo, getActivity(), position -> {
            goToPageVideo(position);
        });
        fragmentShowMoreHighlightsBinding.pageVideoAdapter.setAdapter(pageVideoAdapter);
        
    }

    private void goToPageVideo(int position) {
        genVideoWithUser(position);
    }

    private void loadIdUser() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("id_user",0);
        String id_user_str = sharedPreferences.getString("id_user_str","0");
        id_user = Integer.parseInt(id_user_str);
    }

    private void genVideoWithUser(int position) {
        ApiService apiService = RetrofitClient.getInstance("").getRetrofit().create(ApiService.class);
        Log.d("check_response", "genVideoWithUser: "+ id_user);
        Call<ListEventVideo> call = apiService.GenVideoWithUser(id_user,position);
        call.enqueue(new Callback<ListEventVideo>() {
            @Override
            public void onResponse(@NonNull Call<ListEventVideo> call, @NonNull Response<ListEventVideo> response) {
                if (response.isSuccessful() && response.body() != null ){
                    ListEventVideo listEventVideo = response.body();
                    List<EventVideoModel> eventVideoModelList = listEventVideo.getEventVideoModelList();
                    initViewRec(eventVideoModelList);
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
    private void initViewRec(List<EventVideoModel> eventVideoModelList) {
        ShowMoreHighlightAdapter showMoreHighlightAdapter = new ShowMoreHighlightAdapter(getActivity(),eventVideoModelList,(urlVideo,id_video_int)-> {
            goToVideo(urlVideo,id_video_int);
        });
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 2);
        fragmentShowMoreHighlightsBinding.listViewRec.setLayoutManager(linearLayoutManager);
        fragmentShowMoreHighlightsBinding.listViewRec.setAdapter(showMoreHighlightAdapter);
    }
    private void goToVideo(String urlVideo, int id_video_int) {
        Bundle bundle = new Bundle();
        bundle.putString("url_video_full",urlVideo);
        NavHostFragment.findNavController(ShowMoreHighlightsFragment.this).navigate(R.id.videoFullScreenFragment,bundle);
    }


}