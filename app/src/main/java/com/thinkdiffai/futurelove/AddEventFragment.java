package com.thinkdiffai.futurelove;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public class AddEventFragment extends Fragment {
    private AppCompatImageButton thanhtoan, btn_add_events;
    private LinearLayoutCompat layoutCompat;
    private AppCompatTextView btn_cancle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_add_event, container, false);
        //get id
        thanhtoan=view.findViewById(R.id.coins);
        layoutCompat=view.findViewById(R.id.ll_eventdetail);
        btn_cancle=view.findViewById(R.id.cancel_button);
        btn_add_events=view.findViewById(R.id.btn_add_event);
        //
        // click layoutCompat
        layoutCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickLayoutCompat();
            }
        });
        //click pay
        thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickThanhtoan();
            }
        });
        // button cancle
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AddEventFragment.this).navigate(R.id.action_addEventFragment_to_eventsFragment);
            }
        });
        // btn add event
        btn_add_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AddEventFragment.this).navigate(R.id.action_addEventFragment_to_loadingEventFragment);
            }
        });
        //
        return view;
    }
    private  void ClickLayoutCompat(){
        final Dialog dialog= new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_event_detail);
        Window window= dialog.getWindow();
        if(window== null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable( new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes= window.getAttributes();
        windowAttributes.gravity= Gravity.BOTTOM;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(true);
        // anh xa dialog

        //
        dialog.show();
    }
    private void ClickThanhtoan(){
        final Dialog dialog= new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pay);
        Window window= dialog.getWindow();
        if(window== null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable( new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes= window.getAttributes();
        windowAttributes.gravity= Gravity.CENTER;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(true);
        // anh xa dialog

        //
        dialog.show();
    }
}