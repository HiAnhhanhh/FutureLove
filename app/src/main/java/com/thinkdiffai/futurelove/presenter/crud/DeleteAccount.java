package com.thinkdiffai.futurelove.presenter.crud;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.Server;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteAccount implements CommonCrud{

    public static DeleteAccount getInstance() {
        return new DeleteAccount();
    }
    public DeleteAccount(){};

    @Override
    public void deleteAccount(Context context, long id) {
        if (context instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) context;
            // Handle delete account action using the fragment activity
            ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
            Call<Object> callDeleteAccount = apiService.deleteAccount(id);
            callDeleteAccount.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("check_delete", "onResponse: ");
                        Toast.makeText(fragmentActivity, "Delete Account Complete!", Toast.LENGTH_SHORT).show();
                        Log.i("DELETE_ACCOUNT", "Delete Account Complete!");
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Toast.makeText(fragmentActivity, "Delete Account Failed!", Toast.LENGTH_SHORT).show();
                    Log.i("DELETE_ACCOUNT", "Delete Account Failed!");
                }
            });
        } else if (context instanceof Activity) {
            Activity activity = (Activity) context;
            // Handle delete account action using the activity
        }
    }


}
