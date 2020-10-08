package com.romeo.yourtaskmanager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.romeo.yourtaskmanager.Models.*;

import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private DatabaseReference mdatabase;
    private FirebaseAuth mauth;
    private RecyclerView recyclerView;
    private EditText note_upd, title_upd;
    private Button btn_deleteupd, btn_upd;
    private String title;
    private String note;
    private String post_key;
    private Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar= findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);


//        getSupportActionBar();
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        Objects.requireNonNull(getSupportActionBar()).setCustomView(R.layout.custom_toolbar);
//        getActionBar(R.id.custom_toolbare);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
//        TextView textView = getSupportActionBar().getCustomView().findViewById(R.id.toolbar_title);
//        textView.setText("Task Manager");

        mauth = FirebaseAuth.getInstance();
        FirebaseUser muser = mauth.getCurrentUser();
        String uID = muser.getUid();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("TaskNote").child(uID);
        mdatabase.keepSynced(true);
        recyclerView = findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        FloatingActionButton create = findViewById(R.id.float_create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myDialog = new AlertDialog.Builder(HomeActivity.this);
                LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
                View myView = inflater.inflate(R.layout.custominput, null);
                myDialog.setView(myView);
                final AlertDialog dialog = myDialog.create();
                final EditText title = myView.findViewById(R.id.title);
                final EditText note = myView.findViewById(R.id.note);
                Button btn_save = myView.findViewById(R.id.btn_save);
                dialog.show();
                btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mtitle = title.getText().toString().trim();
                        String mnote = note.getText().toString().trim();
                        if (TextUtils.isEmpty(mtitle)) {
                            title.setError("Title is empty");
                            return;
                        }
                        if (TextUtils.isEmpty(mnote)) {
                            note.setError("Note is empty");
                            return;
                        }
                        String id = mdatabase.push().getKey();
                        String today_date = DateFormat.getDateInstance().format(new Date());
                        Data data = new Data(mtitle, mnote, today_date, id);
                        mdatabase.child(id).setValue(data);
                        Toast.makeText(HomeActivity.this, "Data inserted", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                mauth.signOut();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void update_date() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(HomeActivity.this);
        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
        View myView = inflater.inflate(R.layout.update_dialog, null);
        myDialog.setView(myView);
        final AlertDialog dialog = myDialog.create();
        title_upd = myView.findViewById(R.id.title_update);
        note_upd = myView.findViewById(R.id.note_update);

        title_upd.setText(title);
        title_upd.setSelection(title.length());
        note_upd.setText(note);
        note_upd.setSelection(note.length());
        btn_upd = myView.findViewById(R.id.btn_update);
        btn_deleteupd = myView.findViewById(R.id.btn_delete_from_update);


        dialog.show();
        btn_upd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mtitle = title_upd.getText().toString().trim();
                String mnote = note_upd.getText().toString().trim();
                if (TextUtils.isEmpty(mtitle)) {
                    note_upd.setError("Title is empty");
                    return;
                }
                if (TextUtils.isEmpty(mnote)) {
                    note_upd.setError("Note is empty");
                    return;
                }
                mdatabase.child(post_key).removeValue();
                String id = mdatabase.push().getKey();
                String today_date = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(mtitle, mnote, today_date, id);
                mdatabase.child(id).setValue(data);
                Toast.makeText(HomeActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();


                dialog.dismiss();
            }
        });
        btn_deleteupd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mdatabase.child(post_key).removeValue();


                dialog.dismiss();
            }
        });
//        btn_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String mtitle = title.getText().toString().trim();
//                String mnote = note.getText().toString().trim();
//            }
//    };
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data, myViewHolder> adapter = new FirebaseRecyclerAdapter<Data, myViewHolder>(
                Data.class, R.layout.item_data, myViewHolder.class, mdatabase) {
            @Override
            protected void populateViewHolder(myViewHolder viewHolder, final Data data, final int i) {
                viewHolder.setDate(data.getDate());
                viewHolder.setTitle(data.getTitle());
                viewHolder.setNote(data.getNote());


                viewHolder.myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        post_key = getRef(i).getKey();
                        title = data.getTitle();
                        note = data.getNote();

                        update_date();
                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        View myView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            myView = itemView;
        }

        public void setTitle(String title) {
            TextView mtitle = myView.findViewById(R.id.title);
            mtitle.setText(title);
        }

        public void setNote(String note) {
            TextView mnote = myView.findViewById(R.id.note);
            mnote.setText(note);
        }

        public void setDate(String date) {
            TextView mdate = myView.findViewById(R.id.date);
            mdate.setText(date);
        }
    }
}

