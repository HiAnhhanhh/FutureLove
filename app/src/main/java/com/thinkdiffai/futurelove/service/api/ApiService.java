package com.thinkdiffai.futurelove.service.api;

import androidx.room.Delete;

import com.thinkdiffai.futurelove.model.DetailEventList;
import com.thinkdiffai.futurelove.model.DetailEventListParent;
import com.thinkdiffai.futurelove.model.DetailListVideoModel;
import com.thinkdiffai.futurelove.model.EventCreateByUser;
import com.thinkdiffai.futurelove.model.EventHomeDto;
import com.thinkdiffai.futurelove.model.GenBabyModel;
import com.thinkdiffai.futurelove.model.GenImageModel;
import com.thinkdiffai.futurelove.model.GetVideoSwapResponse;
import com.thinkdiffai.futurelove.model.GetYourVideoSwapModel;
import com.thinkdiffai.futurelove.model.IpNetworkModel;
import com.thinkdiffai.futurelove.model.ListEventDetailModel;
import com.thinkdiffai.futurelove.model.ListEventVideo;
import com.thinkdiffai.futurelove.model.ListImageUploadModel;
import com.thinkdiffai.futurelove.model.Login;
import com.thinkdiffai.futurelove.model.comment.CommentList;
import com.thinkdiffai.futurelove.model.comment.CommentPage;
import com.thinkdiffai.futurelove.model.comment.DetailUser;
import com.thinkdiffai.futurelove.model.comment.EventsUser.EventsUser;
import com.thinkdiffai.futurelove.model.comment.UserComment;
import com.thinkdiffai.futurelove.model.comment.eacheventcomment.EachEventCommentsList;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @GET("https://thinkdiff.us/getdata")
    Call<Object> postEvent(
            @Header("linknam") String linknam,
            @Header("linknu") String linknu,
            @Header("Authorization") String authorization,
            @Query("device_them_su_kien") String device,
            @Query("ip_them_su_kien") String ip,
            @Query("id_user") String userId,
            @Query("ten_nam") String nam,
            @Query("ten_nu") String nu
    );

    @GET(Server.GET_VIDEO + "{page}")
    Call<DetailListVideoModel> getListVideo(
            @Path("page") int id,
            @Query("category") int id_categories
    );

    @GET(Server.URI_CREATE_IMPLICIT_DATA)
    Call<Object> postImplicitEvent(
            @Query("device_them_su_kien") String deviceThemSuKien,
            @Query("ip_them_su_kien") String ipThemSuKien,
            @Query("id_user") long userId,
            @Query("ten_nam") String tenNam,
            @Query("ten_nu") String tenNu
    );


    @GET("https://videoswap.mangasocial.online/getdata/genvideo")
    Call<GetVideoSwapResponse> getUrlVideoSwap(
            @Header("Authorization") String authorization,
            @Query("id_video") int idVideo,
            @Query("device_them_su_kien") String deviceThemSuKien,
            @Query("ip_them_su_kien") String ipThemSuKien,
            @Query("id_user") int idUser,
            @Query("image") String image,
            @Query("ten_video") String tenVideo
    );

    @GET("https://thinkdiff.us/getdata/sukien/baby")
    Call<GenBabyModel> getUrlImageBaby (
            @Header("Authorization") String authorization,
            @Header("linknam") String linknam,
            @Header("linknu") String linknu,
            @Query("device_them_su_kien") String deviceThemSK,
            @Query("ip_them_su_kien") String ipThemSK,
            @Query("id_user") int id_user
    );

    @GET("https://thinkdiff.us/getdata/swap/2/image")
    Call<GenImageModel> getUrlImageSwap(
            @Header("Authorization") String authorization,
            @Header("link1") String link1,
            @Header("link2") String link2,
            @Query("device_them_su_kien") String deviceThemSK,
            @Query("ip_them_su_kien") String ipThemSK,
            @Query("id_user") int id_user
    );



    @GET(Server.IMAGE_UPLOAD+ "{id_user}")
    Call<ListImageUploadModel> getImageUpload (
            @Path("id_user") int id_user,
            @Query("type") String type
    );

    @GET(Server.EVENT_CREATE_BY_USER+"{id_user}")
    Call<EventCreateByUser> getEventCreateByUser (
            @Path("id_user") int id_user
    );

    @GET("https://databaseswap.mangasocial.online/lovehistory/user/video/"+ "{id_user}")
    Call<ListEventVideo> GenVideoWithUser (
            @Path("id_user") int id_user,
            @Query("trang") int page
    );

    @Multipart
    @POST(Server.UPLOAD_IMAGE +"{id_user}")
    Call<String> uploadImage(
            @Path("id_user") int id_user,
            @Query("type") String fileType,
            @Part MultipartBody.Part src_img
    );

    @Multipart
    @POST("https://videoswap.mangasocial.online/getdata/genvideo/swap/imagevid")
    Call<GetYourVideoSwapModel> PostVid (
            @Header("Authorization") String authorization,
            @Query("device_them_su_kien") String deviceThemSuKien,
            @Query("ip_them_su_kien") String ipThemSuKien,
            @Query("id_user") int id_user,
            @Query("src_img") String src_img,
            @Part MultipartBody.Part src_vid
    );

    @GET(Server.URI_GET_NETWORK_STATUS)
    Call<IpNetworkModel> getIpApiResponse();

    @GET(Server.URI_LIST_EVENT_HOME + "page")
    Call<List<List<EventHomeDto>>> getListAllEventHome(@Path("page") long id);

    @GET(Server.URI_LIST_EVENT_HOME + "{page}" )
    Call<DetailEventListParent> getEventListForHome(@Path("page") long id,
                                                    @Query("id_user")int id_user);

    @GET(Server.URI_LIST_EVENT_TIMELINE + "{id}")
    Call<DetailEventList> getListEventDetail(@Path("id") long id);

    @GET(Server.URI_LIST_EVENT_TIMELINE + "{id_toan_bo_sk}")
    Call<ListEventDetailModel> getListEventDetailUser(@Path("id_toan_bo_sk") long id);

    // Get all comments of each event
    @GET(Server.URI_LIST_COMMENT_BY_EVENT_ID + "{so_thu_tu_su_kien}")
    Call<EachEventCommentsList> getListCommentByEventId(
            @Path("so_thu_tu_su_kien") int soThuTuSuKien,
            @Query("id_toan_bo_su_kien") long idToanBoSuKien,
            @Query("id_user") int idUser
    );

    @GET(Server.URI_LIST_COMMENT_NEW + "{id}")
    Call<CommentList> getListCommentNew(@Path("id") int id,
                                        @Query("id_user") int id_user);



    @FormUrlEncoded
    @POST("https://databaseswap.mangasocial.online/lovehistory/add/"+"{id_toan_bo_sk}")
    Call<Object> postListEventDetail(
                                    @Path("id_toan_bo_sk") String id_toan_bo_sk,
                                     @Header("link1") String link1,
                                     @Header("link2") String link2,
                                     @Header("Authorization") String auth,
                                     @Field("id_user") int id,
                                     @Field("link_img") String link_img,
                                     @Field("ten_nam") String ten_nam,
                                     @Field("ten_nu") String ten_nu,
                                     @Field("noidung_su_kien") String noidung_su_kien,
                                     @Field("device_them_su_kien") String device_them_su_kien,
                                     @Field("ip_them_su_kien") String ip_them_su_kien,
                                     @Field("ten_su_kien") String tensukien,
                                     @Field("id_template") int id_template,
                                     @Field("link_video") String link_video);

    @FormUrlEncoded
    @POST("https://databaseswap.mangasocial.online/lovehistory/comment")
    Call<CommentPage> postComment (@Field("id_user") int id_user,
                                   @Field("noi_dung_cmt") String noi_dung_comment,
                                   @Field("device_cmt") String device_cmt,
                                   @Field("id_toan_bo_su_kien") String id_toan_bo_su_kien,
                                   @Field("so_thu_tu_su_kien") int so_thu_tu_su_kien,
                                   @Field("ipComment") String ipComment,
                                   @Field("imageattach")String imageattach,
                                   @Field("id_user_cmt") int id_user_cmt,
                                   @Field("location") String location,
                                   @Field("link_imagesk") String link_imagesk);



    @FormUrlEncoded
    @POST(Server.URI_POST_COMMENT)
    Call<Object> postDataComment(@Field("id_user") int idUser,
                                 @Field("noi_dung_cmt") String content,
                                 @Field("device_cmt") String device,
                                 @Field("id_toan_bo_su_kien") String idSummary,
                                 @Field("so_thu_tu_su_kien") int soThuTuSuKien,
                                 @Field("ipComment") String ip,
                                 @Field("imageattach") String imagEattach);
    @FormUrlEncoded
    @POST(Server.URI_LOG_IN)
    Call<Login> login(
            @Field("email_or_username") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST(Server.URI_SIGN_UP)
    Call<Object> signUp(
            @Field("email") String email,
            @Field("password") String password,
            @Field("user_name") String userName,
            @Field("link_avatar") String linkAvatar,
            @Field("ip_register") String registerIp
    );
    // get detail user
    @GET(Server.URI_PROFILE_USER + "{id_user}")
    Call<DetailUser> getProfileUser(@Path("id_user") int id_user);

    // GET comments user
    @GET(Server.URI_COMMENTS_USER + "{page}")
    Call<UserComment> getCommentUser(@Path("page") long id);

    // get events theo user
    @GET(Server.URI_EVENTS_USER + "{page}")
    Call<EventsUser> getEventUser(@Path("page") long id);

    @GET(Server.URI_DELETE_ACCOUNT + "{user_id}")
    Call<Object> deleteAccount(@Path("user_id") long id);

    // CHANGE PASSWORD
    @FormUrlEncoded
    @POST(Server.URI_CHANGE_PASSWORD+"{id_user}")
    Call<Object> Change_Password(
           @Path("id_user") int id_user,
            @Header("Authorization") String auth,
//            @Field("email_or_username") String email,
            @Field("old_password") String oldPassword,
            @Field("new_password") String newPassword
    );

    @DELETE("https://databaseswap.mangasocial.online/lovehistory/delete/"+ "{id_comment}")
    Call<Object> deleteComment (
            @Path("id_comment") int id_comment,
            @Header("Authorization") String authorization
    );

}

