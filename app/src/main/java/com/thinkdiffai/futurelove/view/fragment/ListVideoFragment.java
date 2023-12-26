package com.thinkdiffai.futurelove.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.google.android.exoplayer2.ExoPlayer;
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

import io.github.rupinderjeet.kprogresshud.KProgressHUD;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListVideoFragment extends Fragment implements RecyclerViewClickListener {

    private KProgressHUD kProgressHUD;

    private boolean isShowView = false;

    BubbleNavigationLinearView bubbleNavigationLinearView;

    private Context context;
    private ExoPlayer exoPlayer;

    private ListVideoAdapter listVideoAdapter;

    private PageVideoAdapter pageVideoAdapter;

    private List<ListVideoModel> listVideoModelArrayList;

    private ArrayList<Integer> pageListVideo;

    private FragmentListVideoBinding fragmentListVideoBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentListVideoBinding = FragmentListVideoBinding.inflate(inflater, container, false);
        return fragmentListVideoBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initAction();
//        navigateToOtherFragment();
    }

    private void initAction() {
        fragmentListVideoBinding.chooseFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ListVideoFragment.this).navigate(R.id.action_listVideoFragment_to_swapFaceWithYourVideo);
            }
        });
    }

    private void initView() {
        pageListVideo = new ArrayList<>();
        for(int i=1; i<=5;i++){
            pageListVideo.add(i);
        }
        pageVideoAdapter = new PageVideoAdapter(pageListVideo,getActivity(),position -> goToPageVideo(position));
        fragmentListVideoBinding.pageVideoAdapter.setAdapter(pageVideoAdapter);

        loadSwapFacePage1();

    }

    private void loadSwapFacePage1() {
        getData(0);
    }

    private void goToPageVideo(int position) {
        getData(position);
    }


    //    private void navigateToOtherFragment() {
//        bubbleNavigationLinearView = fragmentListVideoBinding.bubbleNavigation;
//        bubbleNavigationLinearView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
//            @Override
//            public void onNavigationChanged(View view, int position) {
//                switch (position){
//                    case 1:
//                        fragmentListVideoBinding.homeBubble.setVisibility(View.GONE);
//                        fragmentListVideoBinding.pairingBubble.setVisibility(View.GONE);
//                        fragmentListVideoBinding.commentBubble.setVisibility(View.GONE);
//                        goToCommentFragment();
//                        break;
//                    case 2:
//                        fragmentListVideoBinding.homeBubble.setVisibility(View.GONE);
//                        fragmentListVideoBinding.pairingBubble.setVisibility(View.GONE);
//                        fragmentListVideoBinding.commentBubble.setVisibility(View.GONE);
//                        goToPairingFragment();
//                        break;
//                    case 0:
//                        fragmentListVideoBinding.homeBubble.setVisibility(View.GONE);
//                        fragmentListVideoBinding.pairingBubble.setVisibility(View.GONE);
//                        fragmentListVideoBinding.commentBubble.setVisibility(View.GONE);
//                        goToHomeFragment();
//                        break;
//                }
//            }
//        });
//    }
    private void goToHomeFragment() {
        NavHostFragment.findNavController(ListVideoFragment.this).navigate(R.id.action_listVideoFragment_to_homeFragment);

    }

    private void goToPairingFragment() {
        NavHostFragment.findNavController(ListVideoFragment.this).navigate(R.id.action_listVideoFragment_to_pairingFragment);

    }

    private void goToCommentFragment() {
        NavHostFragment.findNavController(ListVideoFragment.this).navigate(R.id.action_listVideoFragment_to_commentFragment);

    }

    private void initViewListVideo(List<ListVideoModel> listVideoModelArrayList) {
        listVideoAdapter = new ListVideoAdapter(listVideoModelArrayList, this::onItemClick,getContext() );
        fragmentListVideoBinding.listViewRec.setLayoutManager(new GridLayoutManager(getActivity(),2));
        fragmentListVideoBinding.listViewRec.setAdapter(listVideoAdapter);
    }



    private void getData(int position) {
        Log.d("check_list_video", "getData: "+ position);
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Call<DetailListVideoModel> call = apiService.getListVideo(position,0);
        call.enqueue(new Callback<DetailListVideoModel>() {
            @Override
            public void onResponse(Call<DetailListVideoModel> call, Response<DetailListVideoModel> response) {
                if(response.isSuccessful() && response.body()!=null){
                    listVideoModelArrayList = response.body().getListSukienVideo();
                    Log.d("check_list_video", "onResponse: "+ listVideoModelArrayList.get(7).getLink_video());
                    Log.d("check_list_video", "onResponse: "+ listVideoModelArrayList.size());
                    initViewListVideo(listVideoModelArrayList);
//                    if(!listVideoModelArrayList.isEmpty()) {
//                        listVideoAdapter.setData(listVideoModelArrayList);
//                    }
                }
            }
            @Override
            public void onFailure(Call<DetailListVideoModel> call, Throwable t) {

            }
        });
    }
    private void initVideo(String urlVideo) {
//        VideoView videoView = fragmentListVideoBinding.checkVideo;
//        MediaController mediaController = new MediaController(getActivity());
//        videoView.setMediaController(mediaController);
//        mediaController.setAnchorView(videoView);
//        videoView.setVideoURI(Uri.parse(urlVideo));
//        videoView.start();
//        StyledPlayerView styledPlayerView = fragmentListVideoBinding.checkVideo;
//        exoPlayer = new ExoPlayer.Builder(getActivity()).build();
//        styledPlayerView.setPlayer(exoPlayer);
//        MediaItem mediaItem = MediaItem.fromUri(urlVideo);
//        exoPlayer.addMediaItem(mediaItem);
//        exoPlayer.prepare();
//        exoPlayer.play();
    }

    @Override
    public void onStop() {
        super.onStop();
//        exoPlayer.setPlayWhenReady(false);;
//        exoPlayer.release();
//        exoPlayer = null;
    }

    @Override
    public void onItemClick(String data1, String data2, int data3) {
        Log.d("check_data_video", "onItemClick: "+ data1+ data2+ data3);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("urlVideo",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("url_video",data1);
        editor.putString("name_video", data2);
        editor.putInt("id_video",data3);
        editor.commit();
        NavHostFragment.findNavController(ListVideoFragment.this).navigate(R.id.action_listVideoFragment_to_swapFaceFragment);
    }
}