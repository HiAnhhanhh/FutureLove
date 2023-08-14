package com.thinkdiffai.futurelove.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.model.comment.CommentUser;
import com.thinkdiffai.futurelove.util.Util;

import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class UserCommnetAdapter extends RecyclerView.Adapter<UserCommnetAdapter.UserCommentAdapter_Viewholder> implements Filterable {

    private List<CommentUser> comments;
    private List<CommentUser> commentsold;
    private Context context;

    public UserCommnetAdapter(List<CommentUser> comments, Context context) {
        this.commentsold=comments;
        this.comments = comments;
        this.context = context;
    }
    public void setData(List<CommentUser> comments){
        this.comments=comments;
        this.commentsold=comments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserCommentAdapter_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);

        return new UserCommnetAdapter.UserCommentAdapter_Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserCommentAdapter_Viewholder holder, int position) {
        CommentUser comment = comments.get(position);
        if (comment == null)
            return;
        Glide.with(holder.itemView.getContext()).load(comment.getAvatar_user()).error(R.drawable.baseline_account_circle_24).into(holder.avatar);
        if (comment.getDevice_cmt().trim().equals("")) {
            holder.TenUser.setVisibility(View.GONE);
        } else {
            holder.TenUser.setText(""+comment.getUser_name());
            holder.TenUser.setVisibility(View.VISIBLE);

        }
        if (comment.getDia_chi_ip().trim().equals("")) {
            holder.addressUser.setVisibility(View.GONE);
        } else {
            holder.addressUser.setText("ip: " + comment.getDia_chi_ip());
            holder.addressUser.setVisibility(View.VISIBLE);
        }
        holder.NoiDungComment.setText(comment.getNoi_dung_cmt());
        holder.Thoigian.setText(comment.getNoi_dung_cmt());
        if (comment.getThoi_gian_release() == null) {
            holder.Thoigian.setVisibility(View.INVISIBLE);
        } else {
            holder.Thoigian.setVisibility(View.VISIBLE);
            holder.Thoigian.setText(Util.calTimeStampComment(comment.getThoi_gian_release()));
        }


        holder.img_comment.setImageDrawable(null);
        holder.img_comment.setVisibility(View.GONE);
        if (comment.getImageattach() != null && !comment.getImageattach().trim().equals("") && !comment.getImageattach().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(comment.getImageattach())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.img_comment.setVisibility(View.GONE);
                            return true;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.img_comment.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(holder.img_comment);
        }
    }

    @Override
    public int getItemCount() {
        if(comments!=null){
            return comments.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchString = constraint.toString();
                if (searchString.isEmpty()) {
                    comments= new ArrayList<>(commentsold);
                } else {
                    List<CommentUser> filteredList = new ArrayList<>();
                    for (CommentUser item : commentsold) {
                        // dieu kien tim kiem
                        if (item.getNoi_dung_cmt().toLowerCase().contains(searchString.toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                    comments= filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = comments;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                comments=(List<CommentUser>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class UserCommentAdapter_Viewholder extends  RecyclerView.ViewHolder {
        TextView TenUser,addressUser,NoiDungComment,Thoigian;
        ImageView avatar, img_comment;
        public UserCommentAdapter_Viewholder(@NonNull View itemView) {
            super(itemView);
            TenUser=itemView.findViewById(R.id.tv_device_name);
            addressUser=itemView.findViewById(R.id.tv_address);
            NoiDungComment= itemView.findViewById(R.id.tv_content);
            Thoigian=itemView.findViewById(R.id.tv_time);
            avatar=itemView.findViewById(R.id.image_avatar1);
            img_comment=itemView.findViewById(R.id.image_comment);
        }
    }
}

