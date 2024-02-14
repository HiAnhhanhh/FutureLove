package com.thinkdiffai.futurelove.view.fragment;

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
import com.thinkdiffai.futurelove.databinding.FragmentEventDetailBinding;
import com.thinkdiffai.futurelove.model.EventCreateByUser;
import com.thinkdiffai.futurelove.model.ListEventDetailModel;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.Server;
import com.thinkdiffai.futurelove.view.adapter.ImageUploadAdapter;
import com.thinkdiffai.futurelove.view.adapter.ListEventDetailUserAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetailFragment extends Fragment {

    FragmentEventDetailBinding eventDetailBinding;

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
        eventDetailBinding.backBtn.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
    }

    private void initView() {

    }

    private void initData() {
        loadIdToanBoSuKien();
        loadEventSuKien();
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
        listEventDetailUserAdapter = new ListEventDetailUserAdapter(listEventDetailModels, getContext(), this::gotoComment);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        eventDetailBinding.rcvComment.setLayoutManager(linearLayoutManager);
        eventDetailBinding.rcvComment.setAdapter(listEventDetailUserAdapter);
    }

    private void gotoComment(String id_toan_bo_su_kien, String so_thu_tu_su_kien) {
        Log.d("check_click_sk", "gotoComment: "+ id_toan_bo_su_kien+"&"+so_thu_tu_su_kien);
        Bundle bundle = new Bundle();
        bundle.putString("id_t_b_s_k",id_toan_bo_su_kien);
        bundle.putString("s_t_t_s_k",so_thu_tu_su_kien);
        NavHostFragment.findNavController(EventDetailFragment.this).navigate(R.id.action_eventDetailFragment_to_commentEventDetalFragment,bundle);
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