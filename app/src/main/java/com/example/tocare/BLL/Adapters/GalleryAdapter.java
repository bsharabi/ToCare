package com.example.tocare.BLL.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tocare.R;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Uri> mImage;
    private final List<String> chooseImage;
    private final ImageView imageView;
    private final ImageView fit;


    public GalleryAdapter(Context mContext, List<Uri> mImage, List<String> chooseImage, final ImageView imageView, final ImageView fit) {
        this.mContext = mContext;
        this.mImage = (mImage == null) ? new ArrayList<>() : mImage;
        this.imageView = imageView;
        this.chooseImage = chooseImage;
        this.fit = fit;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.image_gallery_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Uri uri = mImage.get(position);
        System.out.println(mImage);
        Picasso.get().load(uri).fit().into(holder.imageItem);
        holder.imageItem.setAdjustViewBounds(true);

        if (position == 0 && chooseImage.isEmpty())
            if (fit.getTag().equals("fit"))
                Picasso.get().load(uri).fit().into(imageView);
            else
                Picasso.get().load(uri).into(imageView);


        if (chooseImage.contains(uri.toString())) {
            holder.numberSelect.setText(chooseImage.indexOf(uri.toString()) + 1 + "");
            holder.numberSelect.setVisibility(View.VISIBLE);
            if (fit.getTag().equals("fit"))
                Picasso.get().load(uri).fit().into(imageView);
            else
                Picasso.get().load(uri).into(imageView);
        } else
            holder.numberSelect.setVisibility(View.GONE);

        holder.imageItem.setOnClickListener(v -> {
            if (chooseImage.contains(uri.toString())) {
                chooseImage.remove(uri.toString());
                holder.numberSelect.setVisibility(View.GONE);
                notifyDataSetChanged();
            } else if (chooseImage.size() != 10) {
                holder.numberSelect.setVisibility(View.VISIBLE);
                chooseImage.add(uri.toString());
                holder.numberSelect.setText(chooseImage.indexOf(uri.toString()) + 1 + "");
                if (fit.getTag().equals("fit"))
                    Picasso.get().load(uri).fit().into(imageView);
                else
                    Picasso.get().load(uri).into(imageView);
            } else
                Toast.makeText(mContext, "You can choose up to 10 photos only", Toast.LENGTH_SHORT).show();

        });

    }

    @Override
    public int getItemCount() {
        return mImage.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageItem;
        private final TextView numberSelect;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageItem = itemView.findViewById(R.id.image_item);
            numberSelect = itemView.findViewById(R.id.number_item_select);

        }
    }

}
