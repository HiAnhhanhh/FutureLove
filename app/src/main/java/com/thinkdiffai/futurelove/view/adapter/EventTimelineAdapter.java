package com.thinkdiffai.futurelove.view.adapter;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.FragmentShowFullImageBinding;
import com.thinkdiffai.futurelove.databinding.ItemTimelineEventBinding;
import com.thinkdiffai.futurelove.model.DetailEvent;
import com.squareup.picasso.Picasso;
import com.thinkdiffai.futurelove.view.fragment.dialog.MyOwnDialogFragment;

import java.util.List;

public class EventTimelineAdapter extends RecyclerView.Adapter<EventTimelineAdapter.EventTimelineViewHolder> {
    private List<DetailEvent> detailEventLists;
    Context context;

    public void setData(List<DetailEvent> detailEventLists) {
        this.detailEventLists = detailEventLists;
    }

    public EventTimelineAdapter(List<DetailEvent> eventList, IOnClickAddEventListener iOnClickAddEvent, IOnScrollEventList iOnScrollEventList, Context context) {
        this.detailEventLists = eventList;
        this.iOnClickAddEvent = iOnClickAddEvent;
        this.iOnScrollEventList = iOnScrollEventList;
        this.context = context;
    }

    public final IOnClickAddEventListener iOnClickAddEvent;
    public final IOnScrollEventList iOnScrollEventList;
    public interface IOnClickAddEventListener {
        void onClickAddEvent(long id_event);
    }

    // Interface for callback soThuTuSuKien where it scrolls to
    public interface IOnScrollEventList {
        void onScrollPosition(int soThuTuSuKien);
    }

    @NonNull
    @Override
    public EventTimelineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTimelineEventBinding itemTimelineEventBinding = ItemTimelineEventBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new EventTimelineAdapter.EventTimelineViewHolder(itemTimelineEventBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventTimelineViewHolder holder, int position) {
        DetailEvent event = detailEventLists.get(position);
        if (event == null)
            return;

        Picasso.get().load(event.getLinkDaSwap()).into(holder.itemTimelineEventBinding.imgContent);

        int commaIndex = event.getRealTime().indexOf(",");
        String date = event.getRealTime().substring(0, commaIndex);
        holder.itemTimelineEventBinding.tvContent.setText(event.getNoiDungSuKien());
        holder.itemTimelineEventBinding.tvDate.setText(date);
        // Toast.makeText(context,  event.getId()+"", Toast.LENGTH_SHORT).show();
        holder.itemTimelineEventBinding.btnAddEvnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iOnClickAddEvent.onClickAddEvent(event.getId());
            }
        });

        holder.itemTimelineEventBinding.imgContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open a custom dialog to display the image
                showFullImageDialog(((AppCompatActivity) context).getSupportFragmentManager(), event.getLinkDaSwap());
            }
        });
    }

    private void showFullImageDialog(FragmentManager supportFragmentManager, String linkDaSwap) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        FragmentShowFullImageBinding fragmentShowFullImageBinding = FragmentShowFullImageBinding.inflate(LayoutInflater.from(context));
        // Load Image
        Glide.with(context).load(linkDaSwap).into(fragmentShowFullImageBinding.imgFullView);
        // Set image on dialog
        builder.setView(fragmentShowFullImageBinding.getRoot());
        AlertDialog showFullImageDialog = builder.create();
        showFullImageDialog.show();

        fragmentShowFullImageBinding.btnDownloadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOwnDialogFragment downLoadDialog = new MyOwnDialogFragment();
                downLoadDialog.setDialogTitle("Download Image");
                downLoadDialog.setDialogMessage("Do you want to download this image?");
                downLoadDialog.setImageSrc(R.drawable.ic_info);
                downLoadDialog.setListener(new MyOwnDialogFragment.MyOwnDialogListener() {
                    @Override
                    public void onConfirm() {
                        downloadImage(linkDaSwap);
                    }
                });
                downLoadDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "delete_dialog");
            }
        });
    }

    private void downloadImage(String linkDaSwap) {
        // Create a Download Manager request
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(linkDaSwap));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setTitle("Image Download")
                .setDescription("Downloading image ...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "image.jpg");
        // Get the Download Manager service and enqueue the request
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
    }

    @Override
    public int getItemCount() {
        return null == detailEventLists ? 0 : detailEventLists.size();
    }

    public static class EventTimelineViewHolder extends RecyclerView.ViewHolder {
        private final ItemTimelineEventBinding itemTimelineEventBinding;

        public EventTimelineViewHolder(ItemTimelineEventBinding itemTimelineEventBinding) {
            super(itemTimelineEventBinding.getRoot());
            this.itemTimelineEventBinding = itemTimelineEventBinding;
        }
    }
}
