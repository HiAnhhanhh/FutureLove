package com.thinkdiffai.futurelove.service.api;

import com.thinkdiffai.futurelove.model.DetailEventListParent;
import com.thinkdiffai.futurelove.model.comment.eacheventcomment.EachEventCommentsList;
import com.thinkdiffai.futurelove.model.EventHomeDto;
import com.thinkdiffai.futurelove.model.DetailEventList;
import com.thinkdiffai.futurelove.model.ResponsePairingDto;
import com.thinkdiffai.futurelove.model.comment.CommentList;
import com.thinkdiffai.futurelove.modelfor4gdomain.NetworkModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
//    Gson gson = new GsonBuilder()
//            .setDateFormat("yyyy-MM-dd HH:mm:ss")
//            .create();
//
//        Interceptor interceptor = chain -> {
//            Request request = chain.request();
//            Request.Builder  builder = request.newBuilder();
//            return chain.proceed(builder.build());
//        };
//
//        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder().addInterceptor(interceptor)
//                .callTimeout(50000,TimeUnit.MILLISECONDS) // Timeout cho toàn bộ cuộc gọi (bao gồm cả kết nối và đọc/phản hồi)
//        .connectTimeout(50000, TimeUnit.MILLISECONDS);
//        ApiService apiService = new Retrofit.Builder()
//            .baseUrl(Server.DOMAIN)
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .client(okBuilder.build())
//            .build()
//            .create(ApiService.class);


//    @FormUrlEncoded
//    @POST(Server.URI_PAIRING)
//    Call<ResponsePairingDto> postEvent(
//            @HeaderMap Map<String, String> headers);

//    @GET(Server.URI_PAIRING)
//    Call<DetailEventList> postEvent(
//            @HeaderMap Map<String, String> headers,
//            @Query("device_them_su_kien") String deviceThemSuKien,
//            @Query("ip_them_su_kien") String ipThemSuKien,
//            @Query("id_user") int idUser,
//            @Query("ten_nam") String tenNam,
//            @Query("ten_nu") String tenNu
//    );

    @GET(Server.URI_PAIRING)
    Call<Object> postEvent(
            @Header(Server.KEY_HEADER1) String imageLink1,
            @Header(Server.KEY_HEADER2) String imageLink2,
            @Query("device_them_su_kien") String deviceThemSuKien,
            @Query("ip_them_su_kien") String ipThemSuKien,
            @Query("id_user") int userId,
            @Query("ten_nam") String tenNam,
            @Query("ten_nu") String tenNu
    );


    @GET(Server.URI_GET_NETWORK_STATUS)
    Call<NetworkModel> getIpApiResponse();

    @GET(Server.URI_LIST_EVENT_HOME + "{page}")
    Call<List<List<EventHomeDto>>> getListAllEventHome(@Path("page") long id);

    @GET(Server.URI_LIST_EVENT_HOME + "{page}")
    Call<DetailEventListParent> getEventListForHome(@Path("page") long id);

    @GET(Server.URI_LIST_EVENT_TIMELINE + "{id}")
    Call<DetailEventList> getListEventDetail(@Path("id") long id);

    // Get all comments of each event
    @GET(Server.URI_LIST_COMMENT_BY_EVENT_ID + "{so_thu_tu_su_kien}")
    Call<EachEventCommentsList> getListCommentByEventId(
            @Path("so_thu_tu_su_kien") int soThuTuSuKien,
            @Query("id_toan_bo_su_kien") int idToanBoSuKien
    );


    @GET(Server.URI_LIST_COMMENT_NEW + "{id}")
    Call<CommentList> getListCommentNew(@Path("id") int id);

    @FormUrlEncoded
    @POST(Server.URI_POST_EVENT_TIMELINE)
    Call<Object> postListEventDetail(@Field("id") String id,
                                     @Field("link_da_swap") String linkdaswap,
                                     @Field("link_nam_chua_swap") String linknamchuaswap,
                                     @Field("link_nam_goc") String linknamgoc,
                                     @Field("link_nu_chua_swap") String linknuchuaswap,
                                     @Field("link_nu_goc") String link_nu_goc,
                                     @Field("noi_dung_su_kien") String noidungsukien,
                                     @Field("real_time") String realtime,
                                     @Field("so_thu_tu_su_kien") String sothutusukien,
                                     @Field("ten_su_kien") String tensukien,
                                     @Field("tom_Luoc_Text") String tom_Luoc_Text);

    @POST(Server.URI_POST_COMMENT)
    Call<Object> postComment(@HeaderMap Map<String, String> headers);

    @FormUrlEncoded
    @POST(Server.URI_POST_COMMENT)
    Call<Object> postDataComment(@Field("id_user") int idUser,
                                 @Field("noi_dung_cmt") String content,
                                 @Field("device_cmt") String device,
                                 @Field("id_toan_bo_su_kien") String idSummary,
                                 @Field("so_thu_tu_su_kien") int soThuTuSuKien,
                                 @Field("ipComment") String ip,
                                 @Field("imageattach") String imagEattach);


}

