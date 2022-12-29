package com.example.tocare.BLL.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tocare.DAL.Data;
import com.example.tocare.BLL.Model.Task;
import com.example.tocare.R;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    public Context mContext;
    public List<Task> mTask;

    public TaskAdapter(Context mContext, List<Task> mTask) {
        this.mContext = mContext;
        this.mTask = (mTask==null)?new ArrayList<>():mTask;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Task task =(Task)mTask.get(position);
        holder.Description.setText(task.getDescription());
        holder.title.setText("" + task.getStatus());
        holder.mtDelete.setOnClickListener(v -> Data.getInstance().deleteTaskByUserId(task.getUserIdTask(),task));

    }

    @Override
    public int getItemCount() {
        return mTask.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView  taskImage;
        private final TextView Description, title;
        private MaterialButton mtDelete;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskImage = itemView.findViewById(R.id.img);
            Description = itemView.findViewById(R.id.description);
            title = itemView.findViewById(R.id.Title);
            mtDelete=itemView.findViewById(R.id.mb_delete_task);
        }
    }
}