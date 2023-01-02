package com.example.tocare.UIL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tocare.BLL.Adapters.CommentsAdapter;
import com.example.tocare.BLL.Listener.OnChangeCallback;
import com.example.tocare.BLL.Model.Comment;
import com.example.tocare.DAL.Data;
import com.example.tocare.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {


    private EditText newComment;
    private ImageView imageProfile;
    private RecyclerView recyclerView;
    private TextView post;
    private String postId;
    private String publishId;
    private List<Comment> mComment;
    private Data localData;
    private CommentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        mComment=new ArrayList<>();
        localData = Data.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        newComment = findViewById(R.id.add_comment);
        imageProfile = findViewById(R.id.image_profile);
        recyclerView = findViewById(R.id.recycler_view);
        post = findViewById(R.id.post);

        recyclerView.setHasFixedSize(true);
        adapter = new CommentsAdapter(this, mComment);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        publishId = intent.getStringExtra("publish");


        Picasso.get().load(localData.getCurrentUser().getImageUrl()).into(imageProfile);

        post.setOnClickListener(v -> {

            if (newComment.getText().toString().equals("")) {
                Toast.makeText(CommentsActivity.this, "Most comment", Toast.LENGTH_SHORT).show();
            } else {
                Comment comments = new Comment(
                        localData.getCurrentUserId(),
                        postId,
                        newComment.getText().toString().trim(),
                        publishId);
                localData.addComment(postId, comments, newComment);
            }

        });

        localData.getAllComment(mComment, postId, () -> adapter.notifyDataSetChanged());
    }

}