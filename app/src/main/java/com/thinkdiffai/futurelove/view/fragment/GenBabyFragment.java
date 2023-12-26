package com.thinkdiffai.futurelove.view.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
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

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.DialogBottomSheetSelectedHomeBinding;
import com.thinkdiffai.futurelove.databinding.FragmentGenBabyBinding;

public class GenBabyFragment extends Fragment {

    private FragmentGenBabyBinding fragmentGenBabyBinding ;

    private DialogBottomSheetSelectedHomeBinding dialogBinding;

    private Uri selectedImageMaleUri;
    private Uri selectedImageFemaleUri;

    private int check = 0;

    private BottomSheetDialog bottomSheetDialog;

    private static final int PERMISSION_REQUEST_CODE = 2;


    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        fragmentGenBabyBinding = FragmentGenBabyBinding.inflate(inflater,container, false);
        return fragmentGenBabyBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAction();
        initData();
        
    }

    private void initAction() {
        pickImageFemale();
        pickImageMale();
    }

    private void initData() {
        postMaleResponse();
        postFemaleResponse();
    }

    private void postFemaleResponse() {

    }

    private void postMaleResponse() {

    }

    private void pickImageFemale() {
        fragmentGenBabyBinding.btnSelectPersonFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = 0;
                openDialog();
            }
        });
    }

    private void openDialog() {
        dialogBinding = DialogBottomSheetSelectedHomeBinding.inflate(LayoutInflater.from(getContext()));
        bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(dialogBinding.getRoot());
        bottomSheetDialog.show();
        dialogBinding.btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        dialogBinding.btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) requireContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    openStorage();
                } else {
                    openStorage();
                }
            }
        });
    }

    private void openStorage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        launchSomeActivity.launch(intent);
    }

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if(result.getResultCode() == RESULT_OK){
                    Intent data = result.getData();
                    if ( data != null && check == 0) {
                        selectedImageFemaleUri = data.getData();
                        fragmentGenBabyBinding.btnSelectPersonFemale.setImageURI(selectedImageFemaleUri);
                        bottomSheetDialog.dismiss();
                    } else if (data != null && check ==1) {
                        selectedImageMaleUri = data.getData();
                        fragmentGenBabyBinding.btnSelectPersonMale.setImageURI(selectedImageMaleUri);
                        bottomSheetDialog.dismiss();
                    }
                }
            }
    );
    private void openCamera() {
    }

    private void pickImageMale() {
        fragmentGenBabyBinding.btnSelectPersonMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = 1;
                openDialog();
            }
        });
    }

}