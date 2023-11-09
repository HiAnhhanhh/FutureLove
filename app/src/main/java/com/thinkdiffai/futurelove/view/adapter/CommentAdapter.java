package com.thinkdiffai.futurelove.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.thinkdiffai.futurelove.DbStorage.Constant;
import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.presenter.api.CityCalledByIpApi;
import com.thinkdiffai.futurelove.databinding.ItemCommentBinding;
import com.thinkdiffai.futurelove.service.api.QueryValueCallback;
import com.thinkdiffai.futurelove.util.Util;
import com.thinkdiffai.futurelove.model.comment.CommentPage;
import com.thinkdiffai.futurelove.view.fragment.dialog.MyOwnDialogFragment;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private String urlImgMale;
    private String urlImgFemale;
    private List<CommentPage> comments;

    private String city = "empty address";
    public final IOnClickItemListener iOnClickItem;
    private Context context;

    public interface IOnClickItemListener {
        void onClickItem(long idToanBoSuKien, int soThuTuSuKienCon);
    }

    public void setData(List<CommentPage> comments) {
        this.comments = comments;
        Log.d("adapter_comment", "setData: "+ comments.size());
    }

    public void setData(List<CommentPage> comments, String urlImgMale, String urlImgFemale) {
        this.comments = comments;
        this.urlImgMale = urlImgMale;
        this.urlImgFemale = urlImgFemale;
    }

//    public CommentAdapter(Context context, List<Comment> comments, IOnClickItemListener iOnClickItem, String city) {
//        this.context = context;
//        this.comments = comments;
//        this.city = city;
//        this.iOnClickItem = iOnClickItem;
//    }

    public CommentAdapter(Context context, List<CommentPage> comments, IOnClickItemListener iOnClickItem) {
        this.context = context;
        this.comments = comments;
        this.iOnClickItem = iOnClickItem;
    }

    public CommentAdapter(String urlImgMale, String urlImgFemale, List<CommentPage> comments, IOnClickItemListener iOnClickItem) {
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
        CommentPage comment = comments.get(position);

        if (comment == null)
            return;
        if (comment.getLinkNamGoc() != null && !comment.getLinkNamGoc().isEmpty() && comment.getLinkNuGoc() != null && !comment.getLinkNuGoc().isEmpty()) {
            Glide.with(holder.itemView.getContext()).load(comment.getLinkNamGoc()).error(R.drawable.baseline_account_circle_24).into(holder.itemCommentBinding.imageAvatar1);
//            Glide.with(holder.itemView.getContext()).load(comment.getLinkNuGoc()).error(R.drawable.baseline_account_circle_24).into(holder.itemCommentBinding.imageAvatar2);
        } else if (urlImgFemale != null && !urlImgFemale.isEmpty() && urlImgMale != null && !urlImgMale.isEmpty()) {
            Glide.with(holder.itemView.getContext()).load(urlImgMale).error(R.drawable.baseline_account_circle_24).into(holder.itemCommentBinding.imageAvatar1);
//            Glide.with(holder.itemView.getContext()).load(urlImgFemale).error(R.drawable.baseline_account_circle_24).into(holder.itemCommentBinding.imageAvatar2);
        }
        if (comment.getDeviceCmt().trim().equals("")) {
            holder.itemCommentBinding.tvDeviceName.setVisibility(View.GONE);
        } else {
            holder.itemCommentBinding.tvDeviceName.setText("dv: " + comment.getDeviceCmt());
            holder.itemCommentBinding.tvDeviceName.setVisibility(View.VISIBLE);
        }

        // When Ip Address called
        String returnedIpAddress = comment.getDiaChiIp().trim();
        Log.d("IP", "IP: " + returnedIpAddress);

        if (!returnedIpAddress.equals("") && isIpAddressForm(returnedIpAddress)) {
            // Show Ip
            holder.itemCommentBinding.tvDeviceName.setText("ip: " + comment.getDiaChiIp());
            holder.itemCommentBinding.tvDeviceName.setVisibility(View.VISIBLE);
            // Show city name from API
            CityCalledByIpApi.getInstance().getCityName(returnedIpAddress, new QueryValueCallback() {
                @Override
                public void onQueryValueReceived(String queryValue) {
                    city = queryValue;
                    holder.itemCommentBinding.tvAddress.setText(city);
                    holder.itemCommentBinding.tvAddress.setVisibility(View.VISIBLE);
                }
                @Override
                public void onApiCallFailed(Throwable t) {

                }
            });

        } else {
            holder.itemCommentBinding.tvDeviceName.setVisibility(View.GONE);
            holder.itemCommentBinding.tvAddress.setVisibility(View.GONE);
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
                iOnClickItem.onClickItem(comment.getIdToanBoSuKien(), comment.getSoThuTuSuKien());
            }
        });

        holder.itemCommentBinding.layoutComment.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteDialog(holder.getAbsoluteAdapterPosition());
                return true;
            }
        });
    }

    private boolean isIpAddressForm(String returnedIpAddress) {
        Pattern ipv4Pattern = Pattern.compile(Constant.ipv4Regex);
        Pattern ipv6Pattern = Pattern.compile(Constant.ipv6Regex);
        Matcher ipv4Matcher = ipv4Pattern.matcher(returnedIpAddress);
        Matcher ipv6Matcher = ipv6Pattern.matcher(returnedIpAddress);
        if (ipv4Matcher.matches() || ipv6Matcher.matches()) {
            return true;
        }
        return false;
    }

    private void showDeleteDialog(int position) {
        MyOwnDialogFragment deleteDialogFragment = new MyOwnDialogFragment();
        deleteDialogFragment.setDialogTitle("Delete Comment");
        deleteDialogFragment.setDialogMessage("Do you want to delete this comment?");
        deleteDialogFragment.setImageSrc(R.drawable.ic_warning);
        deleteDialogFragment.setListener(new MyOwnDialogFragment.MyOwnDialogListener() {
            @Override
            public void onConfirm() {
                comments.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(context, "Delete Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        deleteDialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "delete_dialog");
    }

    @Override
    public int getItemCount() {
        Log.d("adapter_comment", "getItemCount: "+ comments.size());
        return comments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        private final ItemCommentBinding itemCommentBinding;

        public CommentViewHolder(ItemCommentBinding itemCommentBinding) {
            super(itemCommentBinding.getRoot());
            this.itemCommentBinding = itemCommentBinding;
        }
    }


}
