package com.mr_abdali.childmonitor.MyChat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mr_abdali.childmonitor.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity {

    // TODO: 8/8/2018 Variables Declaration Section...
    Firebase reference1, reference2;

    @BindView(R.id.sendButton) ImageView btnSendButton;
    @BindView(R.id.messageArea) EditText edMessageArea;
    @BindView(R.id.layout1) LinearLayout layout;
    @BindView(R.id.scrollView) ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        final String CurrentParentId=getIntent().getStringExtra("ParentId");
        final String PassChildId=getIntent().getStringExtra("childId");

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://childmonitor-849e8.firebaseio.com/Messages/"+ CurrentParentId + "_" + PassChildId);
        reference2 = new Firebase("https://childmonitor-849e8.firebaseio.com/Messages/"+ PassChildId + "_" +CurrentParentId);

        btnSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = edMessageArea.getText().toString();
                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("Message", messageText);
                    map.put("Users", CurrentParentId);

                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                }
                edMessageArea.setText("");
            }
        });


        // TODO: 8/14/2018 for showing sms of both...
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("Message").toString();
                String userName = map.get("Users").toString();

                if(userName.equals(CurrentParentId)){
                    addMessageBox("You: \n" + message, 1);
                }
                else if (userName.equals(PassChildId)){
                    addMessageBox("Child: \n"+ message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(ChatActivity.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 10);
        textView.setLayoutParams(lp);

        if(type == 1) {
            textView.setBackgroundResource(R.drawable.rounded_corner1);
        }
        else if (type==2){
            textView.setBackgroundResource(R.drawable.rounded_corner2);
        }

        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_UP);
    }
}
