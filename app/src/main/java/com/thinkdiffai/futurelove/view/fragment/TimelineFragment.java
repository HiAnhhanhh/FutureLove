package com.thinkdiffai.futurelove.view.fragment;


import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.thinkdiffai.futurelove.DbStorage.EventHistoryDb;
import com.thinkdiffai.futurelove.databinding.FragmentTimelineBinding;
import com.thinkdiffai.futurelove.model.DetailEvent;
import com.thinkdiffai.futurelove.model.DetailEventList;
import com.thinkdiffai.futurelove.model.comment.eacheventcomment.EachEventCommentsList;
import com.thinkdiffai.futurelove.model.EventHomeDto;
import com.thinkdiffai.futurelove.model.comment.Comment;
import com.thinkdiffai.futurelove.model.comment.CommentList;
import com.thinkdiffai.futurelove.modelfor4gdomain.NetworkModel;
import com.thinkdiffai.futurelove.service.api.QueryValueCallback;
import com.thinkdiffai.futurelove.service.api.RetrofitIp;
import com.thinkdiffai.futurelove.util.Util;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.Server;
import com.thinkdiffai.futurelove.view.activity.AddEventActivity;
import com.thinkdiffai.futurelove.view.activity.MainActivity;
import com.thinkdiffai.futurelove.view.adapter.CommentAdapter;
import com.thinkdiffai.futurelove.view.adapter.EventTimelineAdapter;
import com.thinkdiffai.futurelove.view.adapter.PaginationTimelineAdapter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class TimelineFragment extends Fragment {
    private static final int REQUEST_CODE_PERMISSIONS_STORAGE = 101;
    private static final int IMAGE_PICKER_SELECT = 1889;
    private FragmentTimelineBinding fragmentTimelineBinding;
    private MainActivity mainActivity;
    private KProgressHUD kProgressHUD;
    private List<DetailEvent> detailEventList;
    private EventTimelineAdapter eventTimelineAdapter;
    private PaginationTimelineAdapter pageAdapter;
    SimpleDateFormat dateFormat;
    private LinearLayoutManager linearLayoutManager;
    private CommentAdapter commentAdapter;

    // this model is used to serialize with api callback model
    private CommentList pageCommentList;

    private List<Comment> commentsForAdapter;
    private long elapsedTime;
    Handler handler;
    Runnable runnable;
    private String urlImageComment = "";
    private String imgBase64Female = "";
    private String urlImgFemale;
    private String urlImgMale;

    // Store a local device ip address
    private String networkIp;
    private List<EventHomeDto> eventListDto;

    @SuppressLint("SimpleDateFormat")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentTimelineBinding = FragmentTimelineBinding.inflate(inflater, container, false);
        String patternDate = "yyyy-MM-dd, HH:mm:ss";
        dateFormat = new SimpleDateFormat(patternDate);
        mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        kProgressHUD = mainActivity.createHud();
        try {
            initUi();
            initListener();
        } catch (Exception e) {
            Log.e("ExceptionRuntime", e.toString());
        }
        return fragmentTimelineBinding.getRoot();
    }

    private void initListener() {

        fragmentTimelineBinding.viewpagerTimeline.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                iOnScrollEventList(position + 1);
                getDataComment(position, mainActivity.eventSummaryCurrentId);
                linearLayoutManager.scrollToPosition(position);
                pageAdapter.setCurrentPosition(position);
                pageAdapter.notifyItemChanged(position);
                pageAdapter.notifyItemChanged(position - 1);
                pageAdapter.notifyItemChanged(position);
                pageAdapter.notifyItemChanged(position + 1);
                pageAdapter.notifyItemChanged(position - 2);
            }
        });

        fragmentTimelineBinding.btnSend.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                // Get a returned ip from api
                callDeviceIpAddress(new QueryValueCallback() {
                    @Override
                    public void onQueryValueReceived(String queryValue) {
                        // Assign the return ip address by queryValue
                        networkIp = queryValue;

                        // Post comment with the current IP
                        String content = fragmentTimelineBinding.edtComment.getText().toString().trim();
                        if (true) { //!content.isEmpty()
                            if (imgBase64Female != null && !imgBase64Female.trim().isEmpty()) {
                                kProgressHUD.show();
                                new AsyncTask<Void, Void, Void>() {
                                    @SuppressLint("StaticFieldLeak")
                                    @Override
                                    protected Void doInBackground(Void... params) {
                                        urlImageComment = Util.uploadImage2(imgBase64Female, getActivity());
                                        return null;
                                    }

                                    @SuppressLint("StaticFieldLeak")
                                    @Override
                                    protected void onPostExecute(Void result) {
                                        postComment(content, networkIp);

                                        resizeLayoutComment();
                                        closeKeyboard();
                                        if (kProgressHUD.isShowing())
                                            kProgressHUD.dismiss();

                                    }
                                }.execute();

                            } else {
                                urlImageComment = "";
                                postComment(content, networkIp);
                                fragmentTimelineBinding.edtComment.setText("");
                                closeKeyboard();
                                if (kProgressHUD.isShowing())
                                    kProgressHUD.dismiss();

                            }
//                            saveEventToStorage();
                        }
                    }

                    @Override
                    public void onApiCallFailed(Throwable t) {
                    }
                });


            }
        });

        fragmentTimelineBinding.btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStorage();
            }
        });
        fragmentTimelineBinding.closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resizeLayoutComment();

            }
        });
        fragmentTimelineBinding.nestScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            private int previousScrollY = 0;

            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int dy = scrollY - previousScrollY;

                if (dy > 20) {
                    fragmentTimelineBinding.btnFloating.hide();
                } else if (dy < 0) {
                    fragmentTimelineBinding.btnFloating.show();
                }

                previousScrollY = scrollY;
            }
        });
        fragmentTimelineBinding.btnFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = Server.URI_LINK_WEB_DETAIL + mainActivity.eventSummaryCurrentId; // Liên kết web mà bạn muốn mở
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    private void saveEventToStorage() {
        int number = new Random().nextInt(3) + 2;

        EventHistoryDb.getInstance(getActivity()).eventHistoryDao().insert(eventListDto.get(number));
    }

    private void openStorage() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            startOpenStorage();
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS_STORAGE);
        }
    }

    private void startOpenStorage() {

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/* video/*");
        startActivityForResult(pickIntent, IMAGE_PICKER_SELECT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        kProgressHUD.show();

        if (resultCode == RESULT_OK && requestCode == IMAGE_PICKER_SELECT) {
            try {
                Uri selectedMediaUri = data.getData();
                Bitmap bitmap;

                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);

//                bitmap = rotaImageHadlee(selectedMediaUri);
                if (selectedMediaUri.toString().contains("image")) {
                    imgBase64Female = Util.convertBitmapToBase64(bitmap);
                    fragmentTimelineBinding.imageComment.setImageBitmap(bitmap);
                    fragmentTimelineBinding.imageComment.setVisibility(View.VISIBLE);
                    fragmentTimelineBinding.closeImage.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) fragmentTimelineBinding.layoutComment.getLayoutParams();
                    layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 140, getResources().getDisplayMetrics()); // Đặt chiều cao là dpToPx pixels
                    fragmentTimelineBinding.layoutComment.setLayoutParams(layoutParams);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (kProgressHUD.isShowing()) {
                kProgressHUD.dismiss();

            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSIONS_STORAGE) {
            startOpenStorage();
        }
    }

    private void postComment(String content, String ipAddress) {
        if (!kProgressHUD.isShowing()) {
            kProgressHUD.show();
        }
        String deviceName = Build.MANUFACTURER + Build.MODEL;
        Comment comment = new Comment(
                1,
                content,
                deviceName,
                mainActivity.eventSummaryCurrentId,
                mainActivity.soThuTuSuKien,
                ipAddress,
                urlImageComment
        );

//        Map<String, String> headers = new HashMap<>();
//        headers.put("noi_dung_cmt", comment.getNoi_dung_cmt());
//        headers.put("device_cmt", comment.getDevice_cmt());
//        headers.put("id_toan_bo_su_kien", String.valueOf(comment.getId_toan_bo_su_kien()));
//        headers.put("ipComment", comment.getDia_chi_ip());
//        headers.put("imageattach", comment.getImageattach());
/*
        @Field("id_user") int idUser,
        @Field("noi_dung_cmt") String content,
        @Field("device_cmt") String device,
        @Field("id_toan_bo_su_kien") String idSummary,
        @Field("so_thu_tu_su_kien") int soThuTuSuKien,
        @Field("ipComment") String ip,
        @Field("imageattach") String imagEattach)

        */


        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Call<Object> call = apiService.postDataComment(
                comment.getIdUser(),
                comment.getNoiDungCmt(),
                comment.getDeviceCmt(),
                String.valueOf(comment.getIdToanBoSuKien()),
                comment.getSoThuTuSuKien(),
                comment.getDiaChiIp(),
                comment.getImageattach()
        );
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                if (response.isSuccessful() && response.body() != null) {
//                    xu ly sau khi comment
                    getDataComment(mainActivity.soThuTuSuKien, mainActivity.eventSummaryCurrentId);
                }
                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getActivity(), t.toString() + "", Toast.LENGTH_SHORT).show();
                Log.d("PairingFragmentPostRequest", t.toString());
                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                }
            }
        });
        imgBase64Female = "";
        urlImageComment = "";

    }

    private void initUi() {
//        fragmentTimelineBinding.edtComment.setImeOptions(EditorInfo.IME_ACTION_DONE);
        fragmentTimelineBinding.edtComment.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        if (mainActivity.eventSummaryCurrentId < 0) {
            mainActivity.eventSummaryCurrentId = new Random().nextInt(10);
        }
//        initViewpagerEvent
        detailEventList = new ArrayList<>();
        eventTimelineAdapter = new EventTimelineAdapter(detailEventList, this::iOnClickAddEvent, this::iOnScrollEventList, getContext());
        fragmentTimelineBinding.viewpagerTimeline.setAdapter(eventTimelineAdapter);

//        initRcvComment
        commentsForAdapter = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), GridLayoutManager.VERTICAL, false);
        fragmentTimelineBinding.rcvComment.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentAdapter(commentsForAdapter, this::iOnClickItemComment);
        fragmentTimelineBinding.rcvComment.setAdapter(commentAdapter);

    }

    private void iOnScrollEventList(int soThuTuSuKien) {
        mainActivity.soThuTuSuKien = soThuTuSuKien;
    }

    private void iOnClickAddEvent(int id_event) {
        Intent intent = new Intent(getActivity(), AddEventActivity.class);
        // intent.putExtra("id_summary_event", id_summary_event);
        // intent.putExtra("id_event", id_event);
        //   startActivity(intent);

        Bundle bundle = new Bundle();
        //bundle.putLong("id_summary_event", id_summary_event);
        bundle.putInt("id_event", id_event);
        intent.putExtra("send_id", bundle);
        startActivity(intent);

    }

    private void iOnClickItemComment(long idToanBoSuKien, double soThuTuSuKienCon) {
    }

    private void getDataEvent() {
        if (!kProgressHUD.isShowing()) {
            kProgressHUD.show();
        }
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN1).getRetrofit().create(ApiService.class);
        Call<DetailEventList> call = apiService.getListEventDetail(mainActivity.eventSummaryCurrentId);

        call.enqueue(new Callback<DetailEventList>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<DetailEventList> call, Response<DetailEventList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DetailEventList detailEventList = response.body();
                    List<DetailEvent> detailEvents = detailEventList.getSukien();

                    if (!detailEvents.isEmpty()) {
                        eventTimelineAdapter.setData(detailEvents);
                        eventTimelineAdapter.notifyDataSetChanged();
                        urlImgFemale = detailEvents.get(0).getLinkNuGoc();
                        urlImgMale = detailEvents.get(0).getLinkNamGoc();
                        setTimeIncrease(detailEvents.get(0).getRealTime());
                        displayPaginate(detailEvents.size());
                        fragmentTimelineBinding.layoutFormComment.setVisibility(View.VISIBLE);
                        // call API for comments
                        getDataComment(mainActivity.soThuTuSuKien, mainActivity.eventSummaryCurrentId);
                    }
                }
                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                }
            }

            @Override
            public void onFailure(Call<DetailEventList> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage() + "", Toast.LENGTH_SHORT).show();
                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                    Log.e("MainActivityLog", t.getMessage());

                }
            }
        });
    }

    // Get comments list of each event
    @SuppressLint("NotifyDataSetChanged")
    private void getDataComment(int soThuTuSuKien, int idToanBoSuKien) {
        commentsForAdapter.clear();
//        if (!kProgressHUD.isShowing()) {
//            kProgressHUD.show();
//        }

        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Call<EachEventCommentsList> call = apiService.getListCommentByEventId(soThuTuSuKien, idToanBoSuKien);

        call.enqueue(new Callback<EachEventCommentsList>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<EachEventCommentsList> call, Response<EachEventCommentsList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    EachEventCommentsList eachEventCommentsList = response.body();
                    List<Comment> _comments = eachEventCommentsList.getEachEventCommentList();
                    if (!_comments.isEmpty()) {
                        commentAdapter.setData(_comments, urlImgMale, urlImgFemale);
                        commentAdapter.notifyDataSetChanged();
                    }

                    if (kProgressHUD.isShowing()) {
                        kProgressHUD.dismiss();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<EachEventCommentsList> call, @NonNull Throwable t) {
                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                }
                Log.e("TimelineLog", t.getMessage());

            }
        });
        commentsForAdapter.clear();
    }

    private void setTimeIncrease(String date) {
        long currentTimestamp = Util.getTimeStampNow();
        long countIncreaseCurrent = currentTimestamp - convertDateToTimeStamp(date);
        setDataTimeIncrease(countIncreaseCurrent);
    }

    private long convertDateToTimeStamp(String timeString) {
        long timestamp;
        try {
            Date date = dateFormat.parse(timeString);
            timestamp = date.getTime();
        } catch (ParseException e) {
            timestamp = 0;
        }
        return timestamp;
    }

    private void setDataTimeIncrease(long countIncreaseCurrent) {
        long seconds = countIncreaseCurrent / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        final long[] days = {hours / 24};
        final long[] finalSeconds = {seconds % 60};
        final long[] finalMinutes = {minutes % 60};
        final long[] finalHours = {hours % 24};


        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                String secondsStr, minutesStr, hoursStr, daysStr;
                finalSeconds[0]++;
                if (finalSeconds[0] == 60) {
                    finalSeconds[0] = 0;
                    finalMinutes[0]++;
                }
                if (finalMinutes[0] == 60) {
                    finalMinutes[0] = 0;
                    finalHours[0]++;
                }
                if (finalHours[0] == 24) {
                    finalHours[0] = 0;
                    days[0]++;
                }

                if (finalSeconds[0] < 10) {
                    secondsStr = "0" + String.valueOf(finalSeconds[0]);
                } else {
                    secondsStr = String.valueOf(finalSeconds[0]);
                }
                if (finalMinutes[0] < 10) {
                    minutesStr = "0" + String.valueOf(finalMinutes[0]);
                } else {
                    minutesStr = String.valueOf(finalMinutes[0]);
                }
                if (finalHours[0] < 10) {
                    hoursStr = "0" + String.valueOf(finalHours[0]);
                } else {
                    hoursStr = String.valueOf(finalHours[0]);
                }
                if (days[0] < 10) {

                    daysStr = "0" + String.valueOf(days[0]);
                } else {
                    daysStr = String.valueOf(days[0]);
                }
                fragmentTimelineBinding.timeDay.setText(String.valueOf(daysStr));
                fragmentTimelineBinding.timeHours.setText(hoursStr);
                fragmentTimelineBinding.timeMinute.setText(minutesStr);
                fragmentTimelineBinding.timeSecond.setText(secondsStr);
                handler.postDelayed(runnable, 1000);
            }
        };
        handler.post(runnable);

    }

    public void stopTimer() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
            elapsedTime = 0;
        }
    }

    private void displayPaginate(int numberPage) {
        List<Integer> integerList = new ArrayList<>();
        for (int i = 1; i <= numberPage; i++) {
            integerList.add(i);
        }

//        SnapHelper snapHelper = new LinearSnapHelper();
//        snapHelper.attachToRecyclerView(fragmentTimelineBinding.rcvIndicator);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        fragmentTimelineBinding.rcvIndicator.setLayoutManager(linearLayoutManager);
        pageAdapter = new PaginationTimelineAdapter(integerList, this::goToIndexItem);
        fragmentTimelineBinding.rcvIndicator.setAdapter(pageAdapter);

    }

    private void goToIndexItem(Integer page, int indexOlderItem) {
        pageAdapter.notifyItemChanged(page - 1);
        pageAdapter.notifyItemChanged(indexOlderItem);
        fragmentTimelineBinding.viewpagerTimeline.setCurrentItem(page - 1);
    }


    /*
     * @Param callback: listen a returned value from calling api
     * */
    public void callDeviceIpAddress(QueryValueCallback callback) {
//        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        if (wifiManager != null && wifiManager.isWifiEnabled()) {
//            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//            int ipAddress = wifiInfo.getIpAddress();
//            String ip = android.text.format.Formatter.formatIpAddress(ipAddress);
//            return ip;
//        } else {
//            return null;
//        }
        ApiService getIpApi = RetrofitIp.getInstance("http://ip-api.com/").getRetrofit().create(ApiService.class);
        Call<NetworkModel> call = getIpApi.getIpApiResponse();

        call.enqueue(new Callback<NetworkModel>() {
            @Override
            public void onResponse(Call<NetworkModel> call, Response<NetworkModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NetworkModel networkModel = response.body();
                    String returnIp = networkModel.getQuery();
                    callback.onQueryValueReceived(returnIp);
                } else {
                    callback.onApiCallFailed(new Throwable("API for IP call failed"));
                }
            }

            @Override
            public void onFailure(Call<NetworkModel> call, Throwable t) {
                callback.onApiCallFailed(t);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        commentsForAdapter.clear();
        getDataEvent();
    }


    @Override
    public void onPause() {
        super.onPause();
        stopTimer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopTimer();
    }

    private void resizeLayoutComment() {
        urlImageComment = "";
        imgBase64Female = "";
        fragmentTimelineBinding.edtComment.setText("");
        fragmentTimelineBinding.imageComment.setVisibility(View.GONE);
        fragmentTimelineBinding.closeImage.setVisibility(View.GONE);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) fragmentTimelineBinding.layoutComment.getLayoutParams();
        layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        fragmentTimelineBinding.layoutComment.setLayoutParams(layoutParams);
    }

    private void closeKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
