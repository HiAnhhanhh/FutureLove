package com.thinkdiffai.futurelove.presenter.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.model.event.UploadingEvent;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.Server;
import com.thinkdiffai.futurelove.view.fragment.activity.MainActivity;
import com.thinkdiffai.futurelove.view.fragment.dialog.MyOwnDialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateImplicitEventImpl implements ApiEventInteracting {

    public static CreateImplicitEventImpl getInstance() {
        return new CreateImplicitEventImpl();
    }

    @Override
    public void generateImplicitEvent(Context context, UploadingEvent myEvent) {
        Log.d("IMPLICIT_DATA_GEN", "START CREATING IMPLICIT EVENTS");
        if (context instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) context;
            // Handle delete account action using the fragment activity
            ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
            Call<Object> call = apiService.postImplicitEvent(
                    myEvent.getDevice_them_su_kien(),
                    myEvent.getIp_them_su_kien(),
                    myEvent.getId_user(),
                    myEvent.getTen_nam(),
                    myEvent.getTen_nu()
            );
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("IMPLICIT_DATA_GEN", response.body().toString());
                        MyOwnDialogFragment myOwnDialogFragment = new MyOwnDialogFragment("Success!",
                                "Generating more exciting events",
                                R.drawable.register_success, new MyOwnDialogFragment.MyOwnDialogListener() {
                            @Override
                            public void onConfirm() {
                                // Nav to home fragment
//                            mainActivity.navController.navigate(R.id.fragment_home);
                                Intent intent = new Intent(context, MainActivity.class);
                                context.startActivity(intent);
                            }

                        });
                        myOwnDialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "pairing_dialog");
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Log.d("IMPLICIT_DATA_GEN", "implicit data is failed");
                }
            });
            Log.d("IMPLICIT_DATA_GEN", "END_CREATE_IMPLICIT_EVENTS");

        } else if (context instanceof Activity) {
            Activity activity = (Activity) context;
            // Handle delete account action using the activity
        }
    }
}
