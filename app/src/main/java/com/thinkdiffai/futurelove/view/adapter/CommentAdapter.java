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
import com.thinkdiffai.futurelove.util.Util;
import com.thinkdiffai.futurelove.model.comment.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private String urlImgMale;
    private String urlImgFemale;
    private List<Comment> comments;

    public final IOnClickItemListener iOnClickItem;

    public interface IOnClickItemListener {
        void onClickItem(int idToanBoSuKien, int soThuTuSuKienCon);
    }

    public void setData(List<Comment> comments) {
        this.comments = comments;
    }
    public void setData(List<Comment> comments, String urlImgMale, String urlImgFemale) {
        this.comments = comments;
        this.urlImgMale = urlImgMale;
        this.urlImgFemale = urlImgFemale;
    }
    public CommentAdapter(List<Comment> comments, IOnClickItemListener iOnClickItem) {
        this.comments = comments;

        this.iOnClickItem = iOnClickItem;
    }

    public CommentAdapter(String urlImgMale, String urlImgFemale, List<Comment> comments, IOnClickItemListener iOnClickItem) {
        this.urlImgMale = urlImgMale;
        this.urlImgFemale = urlImgFemale;
        this.comments = comments;
        this.iOnClickItem = iOnClickItem;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCommentBinding itemCommentBinding = ItemCommentBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new CommentViewHolder(itemCommentBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        if (comment == null)
            return;
        if (comment.getLinkNamGoc() != null&&!comment.getLinkNamGoc().isEmpty() && comment.getLinkNuGoc() != null&&!comment.getLinkNuGoc().isEmpty()) {
            Glide.with(holder.itemView.getContext()).load(comment.getLinkNamGoc()).error(R.drawable.baseline_account_circle_24).into(holder.itemCommentBinding.imageAvatar1);
//            Glide.with(holder.itemView.getContext()).load(comment.getLinkNuGoc()).error(R.drawable.baseline_account_circle_24).into(holder.itemCommentBinding.imageAvatar2);
        }else if (urlImgFemale!=null&&!urlImgFemale.isEmpty()&&urlImgMale!=null&&!urlImgMale.isEmpty()){
            Glide.with(holder.itemView.getContext()).load(urlImgMale).error(R.drawable.baseline_account_circle_24).into(holder.itemCommentBinding.imageAvatar1);
//            Glide.with(holder.itemView.getContext()).load(urlImgFemale).error(R.drawable.baseline_account_circle_24).into(holder.itemCommentBinding.imageAvatar2);
        }
        if (comment.getDeviceCmt().trim().equals("")) {
            holder.itemCommentBinding.tvDeviceName.setVisibility(View.GONE);
        } else {
            holder.itemCommentBinding.tvDeviceName.setText("dv: "+comment.getDeviceCmt());
            holder.itemCommentBinding.tvDeviceName.setVisibility(View.VISIBLE);

        }
        if (comment.getDiaChiIp().trim().equals("")) {
            holder.itemCommentBinding.tvDeviceName.setVisibility(View.GONE);
        } else {
            holder.itemCommentBinding.tvDeviceName.setText("ip: " + comment.getDiaChiIp());
            holder.itemCommentBinding.tvDeviceName.setVisibility(View.VISIBLE);
        }
        holder.itemCommentBinding.tvContent.setText(comment.getNoiDungCmt());
        if (comment.getThoiGianRelease() == null) {
            holder.itemCommentBinding.tvTime.setVisibility(View.INVISIBLE);
        } else {
            holder.itemCommentBinding.tvTime.setVisibility(View.VISIBLE);
            holder.itemCommentBinding.tvTime.setText(Util.calTimeStampComment(comment.getThoiGianRelease()));
        }


        holder.itemCommentBinding.imageComment.setImageDrawable(null);
        holder.itemCommentBinding.imageComment.setVisibility(View.GONE);
        if (comment.getImageattach() != null && !comment.getImageattach().trim().equals("") && !comment.getImageattach().isEmpty()) {

            //        holder.itemCommentBinding.imageComment.setImageDrawable(null);
//        if (comment.getImageattach() != null && !comment.getImageattach().trim().equals("") && !comment.getImageattach().isEmpty()) {
//            Glide.with(holder.itemView.getContext()).load(comment.getImageattach()).into(holder.itemCommentBinding.imageComment);
//            if (holder.itemCommentBinding.imageComment.getDrawable() != null) {
//                holder.itemCommentBinding.imageComment.setVisibility(View.VISIBLE);
//            } else {
//                holder.itemCommentBinding.imageComment.setVisibility(View.GONE);
//            }
//        }

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

//                Picasso.get().load(comment.getImageattach()).into(holder.itemCommentBinding.imageComment, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        holder.itemCommentBinding.imageComment.setVisibility(View.VISIBLE);
//
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        holder.itemCommentBinding.imageComment.setVisibility(View.GONE);
////
//                    }
//                });

        }

        holder.itemCommentBinding.layoutComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iOnClickItem.onClickItem(comment.getIdToanBoSuKien(), comment.getSoThuTuSuKien());
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == comments ? 0 : comments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        private final ItemCommentBinding itemCommentBinding;

        public CommentViewHolder(ItemCommentBinding itemCommentBinding) {
            super(itemCommentBinding.getRoot());
            this.itemCommentBinding = itemCommentBinding;
        }
    }



}
