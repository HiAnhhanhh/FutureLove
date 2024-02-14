//package com.thinkdiffai.futurelove.view;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.navigation.fragment.NavHostFragment;
//
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
//import com.thinkdiffai.futurelove.R;
//import com.thinkdiffai.futurelove.databinding.CustomBottomSheetChangeAvatarBinding;
//
//public class BottomSheetDialog extends BottomSheetDialogFragment {
//
//    CustomBottomSheetChangeAvatarBinding customBottomSheetChangeAvatarBinding;
//    boolean isChecked = false;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        customBottomSheetChangeAvatarBinding = CustomBottomSheetChangeAvatarBinding.inflate(inflater,container,false);
//        return customBottomSheetChangeAvatarBinding.getRoot();
//    }
//
////    @Override
////    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
////        super.onViewCreated(view, savedInstanceState);
////            customBottomSheetChangeAvatarBinding.viewAvatar.setOnClickListener(v -> {
////                if(isChecked){
////                    customBottomSheetChangeAvatarBinding.viewAvatar.setBackgroundResource(R.color.white);
////                }else{
////                    customBottomSheetChangeAvatarBinding.viewAvatar.setBackgroundResource(R.color.gray_white);
////                    NavHostFragment.findNavController(BottomSheetDialog.this).navigate(R.id.fullScreenImageFragment);
////                }
////                isChecked = true;
////            });
////            customBottomSheetChangeAvatarBinding.changeAvatar.setOnClickListener(v -> {
////                if(isChecked){
////                    customBottomSheetChangeAvatarBinding.viewAvatar.setBackgroundResource(R.color.white);
////                }else{
////                    customBottomSheetChangeAvatarBinding.viewAvatar.setBackgroundResource(R.color.gray_white);
////                }
////                isChecked = true;
////            });
////    }
//}
