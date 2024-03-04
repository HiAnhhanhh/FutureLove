package com.thinkdiffai.futurelove.view.fragment;


import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;


import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.FragmentListVideoBinding;
import com.thinkdiffai.futurelove.model.DetailListVideoModel;
import com.thinkdiffai.futurelove.model.ListVideoModel;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.Server;
import com.thinkdiffai.futurelove.view.adapter.ListVideoAdapter;
import com.thinkdiffai.futurelove.view.adapter.PageVideoAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListVideoFragment extends Fragment implements RecyclerViewClickListener {

    private static final int PERMISSION_REQUEST_CODE = 2;

    private List<ListVideoModel> listVideoModelArrayList;
    Uri selectedVideoUri;

    private FragmentListVideoBinding fragmentListVideoBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentListVideoBinding = FragmentListVideoBinding.inflate(inflater, container, false);
        return fragmentListVideoBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initAction();
    }

    private void toChooseVideoFragment(Uri selectedVideoUri) {

        ListVideoFragmentDirections.ActionListVideoFragmentToChooseVideoFragment action = ListVideoFragmentDirections.actionListVideoFragmentToChooseVideoFragment(String.valueOf(selectedVideoUri));
        NavHostFragment.findNavController(ListVideoFragment.this).navigate(action);
    }

    private void initAction() {
        fragmentListVideoBinding.chooseFileBtn.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) requireContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                openStorage();
            } else {
                openStorage();
            }
        });
        fragmentListVideoBinding.backBtn.setOnClickListener(v ->{
            requireActivity().onBackPressed();
        });
    }

    private void openStorage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        launchSomeActivity.launch(intent);
    }

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK ){
                    Intent data = result.getData();
                    if( data!= null){
                        selectedVideoUri = data.getData();
                        Log.d("check_pass_data", ":video "+ selectedVideoUri);
                        toChooseVideoFragment(selectedVideoUri);
                    }
                }
            });

    private void initView() {
        ArrayList<Integer> pageListVideo = new ArrayList<>();
        for(int i=1; i<=5;i++){
            pageListVideo.add(i);
        }
        PageVideoAdapter pageVideoAdapter = new PageVideoAdapter(pageListVideo, getActivity(), this::goToPageVideo);
        fragmentListVideoBinding.pageVideoAdapter.setAdapter(pageVideoAdapter);
        loadSwapFacePage1();

    }

    private void loadSwapFacePage1() {
        getData(0);
    }

    private void goToPageVideo(int position) {
        getData(position);
    }


    private void initViewListVideo(List<ListVideoModel> listVideoModelArrayList) {
        ListVideoAdapter listVideoAdapter = new ListVideoAdapter(listVideoModelArrayList, this, getContext());
        fragmentListVideoBinding.listViewRec.setLayoutManager(new GridLayoutManager(getActivity(),2));
        fragmentListVideoBinding.listViewRec.setAdapter(listVideoAdapter);
    }

    private void getData(int position) {
        Log.d("check_list_video", "getData: "+ position);
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Call<DetailListVideoModel> call = apiService.getListVideo(position,0);
        call.enqueue(new Callback<DetailListVideoModel>() {
            @Override
            public void onResponse(@NonNull Call<DetailListVideoModel> call, @NonNull Response<DetailListVideoModel> response) {
                if(response.isSuccessful() && response.body()!=null){
                    listVideoModelArrayList = response.body().getListSukienVideo();
//                    Log.d("check_list_video", "onResponse: "+ listVideoModelArrayList.get(7).getLink_video());
                    Log.d("check_list_video", "onResponse: "+ listVideoModelArrayList.size());
                    initViewListVideo(listVideoModelArrayList);
                }
            }
            @Override
            public void onFailure(@NonNull Call<DetailListVideoModel> call, @NonNull Throwable t) {
            }
        });
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onItemClick(String data1, int id_video_int) {
        Log.d("check_url_your_video", "onItemClick: "+ data1);
        ListVideoFragmentDirections.ActionListVideoFragmentToPickImageToSwapFragment action = ListVideoFragmentDirections.actionListVideoFragmentToPickImageToSwapFragment(data1,id_video_int);
        NavHostFragment.findNavController(ListVideoFragment.this).navigate(action);
    }
}