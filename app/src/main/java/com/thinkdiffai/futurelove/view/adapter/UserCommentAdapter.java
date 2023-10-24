package com.thinkdiffai.futurelove.view.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.ItemCommentBinding;
import com.thinkdiffai.futurelove.model.comment.CommentUser;
import com.thinkdiffai.futurelove.util.Util;

import java.util.List;

public class UserCommentAdapter extends RecyclerView.Adapter<UserCommentAdapter.UserCommnetAdapterViewHolder>{

    private String urlImgMale;
    private String urlImgFemale;
    private List<CommentUser> comments;

    public final IOnClickItemListener iOnClickItem;

    public interface IOnClickItemListener {
        void onClickItem(long idToanBoSuKien, int soThuTuSuKienCon);
    }

    public void setData(List<CommentUser> comments) {
        this.comments = comments;
    }
    public void setData(List<CommentUser> comments, String urlImgMale, String urlImgFemale) {
        this.comments = comments;
        this.urlImgMale = urlImgMale;
        this.urlImgFemale = urlImgFemale;
    }
    public UserCommentAdapter(List<CommentUser> comments, IOnClickItemListener iOnClickItem) {
        this.comments = comments;
        this.iOnClickItem = iOnClickItem;
    }

//    public UserCommentAdapter(String urlImgMale, String urlImgFemale, List<CommentUser> comments, IOnClickItemListener iOnClickItem) {
//        this.urlImgMale = urlImgMale;
//        this.urlImgFemale = urlImgFemale;
//        this.comments = comments;
//        this.iOnClickItem = iOnClickItem;
//    }

    @NonNull
    @Override
    public UserCommnetAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCommentBinding itemCommentBinding = ItemCommentBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new UserCommnetAdapterViewHolder(itemCommentBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UserCommnetAdapterViewHolder holder, int position) {
        CommentUser comment = comments.get(position);
        if (comment == null)
            return;
        Glide.with(holder.itemView.getContext()).load(comment.getAvatar_user()).error(R.drawable.baseline_account_circle_24).into(holder.itemCommentBinding.imageAvatar1);
        if (comment.getDevice_cmt().trim().equals("")) {
            holder.itemCommentBinding.tvDeviceName.setVisibility(View.GONE);
        } else {
            holder.itemCommentBinding.tvDeviceName.setText("dv: "+comment.getDevice_cmt());
            holder.itemCommentBinding.tvDeviceName.setVisibility(View.VISIBLE);

        }
        if (comment.getDia_chi_ip().trim().equals("")) {
            holder.itemCommentBinding.tvDeviceName.setVisibility(View.GONE);
        } else {
            holder.itemCommentBinding.tvDeviceName.setText("ip: " + comment.getDia_chi_ip());
            holder.itemCommentBinding.tvDeviceName.setVisibility(View.VISIBLE);
        }
        holder.itemCommentBinding.tvContent.setText(comment.getNoi_dung_cmt());
        if (comment.getThoi_gian_release() == null) {
            holder.itemCommentBinding.tvTime.setVisibility(View.INVISIBLE);
        } else {
            holder.itemCommentBinding.tvTime.setVisibility(View.VISIBLE);
            holder.itemCommentBinding.tvTime.setText(Util.calTimeStampComment(comment.getThoi_gian_release()));
        }


        holder.itemCommentBinding.imageComment.setImageDrawable(null);
        holder.itemCommentBinding.imageComment.setVisibility(View.GONE);
        if (comment.getImageattach() != null && !comment.getImageattach().trim().equals("") && !comment.getImageattach().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(comment.getImageattach())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.itemCommentBinding.imageComment.setVisibility(View.GONE);
                            return true;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.itemCommentBinding.imageComment.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(holder.itemCommentBinding.imageComment);
        }

        holder.itemCommentBinding.layoutComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iOnClickItem.onClickItem(comment.getId_toan_bo_su_kien(), (int) comment.getId_toan_bo_su_kien());
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == comments ? 0 : comments.size();
    }
    public static class UserCommnetAdapterViewHolder extends RecyclerView.ViewHolder {
        private final ItemCommentBinding itemCommentBinding;
        public UserCommnetAdapterViewHolder(ItemCommentBinding itemCommentBinding) {
            super(itemCommentBinding.getRoot());
            this.itemCommentBinding = itemCommentBinding;
        }
    }
}
