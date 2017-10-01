package com.mobisquid.mobicash.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.adapters.ChatAdapter;
import com.mobisquid.mobicash.dbstuff.ContactDetailsDB;
import com.mobisquid.mobicash.dbstuff.MessageDb;
import com.mobisquid.mobicash.utils.AppController;
import com.mobisquid.mobicash.utils.CircleTransform;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.NotificationUtils;
import com.mobisquid.mobicash.utils.Vars;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import github.ankushsachdeva.emojicon.EmojiconEditText;
import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = ChatActivity.class.getSimpleName();
    TextView mynumber;
    EmojiconsPopup popup;
    Toolbar toolbar;
    Vars vars;
    ImageView usersimage;
    private static String EXTRA_USERID = "userid";
    private static final String EXTRA_IMAGE = "com.mobisquid.mobicash.extraImage";
    TextView action_bar_username, onlinestatus;
    Bundle extras;
    String recep;
    ChatAdapter chatadapter;
    List<MessageDb> messageDbs;
    RecyclerView convo_list;
    ImageView emojiButton;
    RelativeLayout Rlayout;
    EmojiconEditText textMessage;
    FloatingActionButton animButton;
    boolean send_message, dialogruning;
    LinearLayout send_message_layout;
    ImageView layout_send_btn;
    //private FloatingActionMenu menuBlue;
    //private List<FloatingActionMenu> menus = new ArrayList<>();
    // FloatingActionButton fab_photo,fab_audio;
    ContactDetailsDB recepUser;
    LinearLayoutManager linearLayoutManager;
    String recepname, recepmobile;
    BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageDbs = new ArrayList<>();
        vars = new Vars(this);
        extras = getIntent().getExtras();
        setContentView(R.layout.activity_chat);
        animButton = (FloatingActionButton) findViewById(R.id.animButton);
        toolbar = (Toolbar) findViewById(R.id.toolbar_chats);
        usersimage = (ImageView) findViewById(R.id.usersimage);
        textMessage = (EmojiconEditText) findViewById(R.id.chat_edit_text1);
        textMessage.addTextChangedListener(chk_text_Watcher);
        convo_list = (RecyclerView) findViewById(R.id.convo_list);
        emojiButton = (ImageView) findViewById(R.id.emojiButton);
        Rlayout = (RelativeLayout) findViewById(R.id.grouplayout);
        send_message = false;
        layout_send_btn = (ImageView) findViewById(R.id.enter_chat1);
        //layout_send_btn.setVisibility(View.GONE);
        send_message_layout = (LinearLayout) findViewById(R.id.bottomlayout);
        popup = new EmojiconsPopup(Rlayout, this);
        popup.setSizeForSoftKeyboard();
        action_bar_username = (TextView) findViewById(R.id.action_bar_username);
        onlinestatus = (TextView) findViewById(R.id.onlinestatus);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "Clicked");
            }
        });
        Log.i(TAG, "STEP 5");
        if (extras != null) {
            if (extras.getString("message") == null) {
                Log.i(TAG, "STEP ===ID=" + extras.getLong(EXTRA_USERID));
                recep = extras.getString("recep");
                recepmobile = extras.getString("mobile");
                recepname = extras.getString("recepname");
                if (!Select.from(ContactDetailsDB.class).where(Condition.prop("userid").eq(recep))
                        .and(Condition.prop("mobile").eq(recepmobile)).list().isEmpty()) {

                } else {
                    //user not in contactlist
                }
            } else if (extras.getString("message") != null) {
                Log.i(TAG, "MESSAGE =====" + extras.getString("message"));
                MessageDb msg = new Gson().fromJson(extras.getString("message"), MessageDb.class);
                Log.e(TAG,vars.fullname+"== EXT=VARSS+++++++++++"+vars.chk);
                Log.e(TAG,msg.getOtherName()+"===EXT==SENDER+++++++++++"+msg.getSender());
                Log.e(TAG,msg.getUserName()+"===EXT=RECEP+++++++++++"+msg.getRecep());
                recep = msg.getSender();
                recepmobile = msg.getMobileNumber();
                recepname = msg.getUserName();
                fullReload();
                AppController.getInstance().getPrefManager(ChatActivity.this).clear(msg.getThreadid());

            }
            Picasso.with(this)
                    .load(Globals.USERIMAGE + recep + ".png".trim())
                    .transform(new CircleTransform())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .error(R.drawable.noimage)
                    .placeholder(R.drawable.noimage)
                    .into(usersimage);
            action_bar_username.setText(recepname);

        }
        //    mynumber = (TextView) findViewById(R.id.mynumber);
        if (getIntent().getData() != null) {
            Cursor cursor = managedQuery(getIntent().getData(), null, null, null, null);
            if (cursor.moveToNext()) {
                String username = cursor.getString(cursor.getColumnIndex("DATA1"));
                List<ContactDetailsDB> listcon = ContactDetailsDB.find(ContactDetailsDB.class,
                        "mobile = ?", username);
                for (ContactDetailsDB conatct : listcon) {
                    recepUser = ContactDetailsDB.findById(ContactDetailsDB.class, conatct.getId());
                }

                Log.e(TAG, "user====" + username + "====dd" + recepUser.getUsername());
                recep = recepUser.getUserid();
                recepmobile = recepUser.getMobile();
                recepname = recepUser.getUsername();
                Picasso.with(this)
                        .load(Globals.USERIMAGE + recepUser.getUserid() + ".png".trim())
                        .transform(new CircleTransform())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .error(R.drawable.noimage)
                        .placeholder(R.drawable.noimage)
                        .into(usersimage);
                action_bar_username.setText(recepUser.getUsername());
                // mynumber.setText("This is the profile for " + username+"=="+cursor.getString(cursor.getColumnIndex("DATA2")));
            }
        }
//message stuff
        messageDbs = Select.from(MessageDb.class).where(Condition.prop("recep").eq(recep)).or(Condition.prop("sender").eq(recep)).orderBy("id").list();
        Log.e(TAG, "secccc===dd" + messageDbs.size());
        Collections.sort(messageDbs, new Comparator<MessageDb>() {
            @Override
            public int compare(MessageDb u1, MessageDb u2) {

                return u2.getId().compareTo(u1.getId());
            }
        });
        chatadapter = new ChatAdapter(this, messageDbs);
        convo_list.setAdapter(chatadapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);

        convo_list.setLayoutManager(linearLayoutManager);
        convo_list.setHasFixedSize(true);


        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                changeEmojiKeyboardIcon(emojiButton, R.drawable.ic_msg_panel_smiles);
            }
        });
        popup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {

            @Override
            public void onKeyboardOpen(int keyBoardHeight) {

            }

            @Override
            public void onKeyboardClose() {
                if (popup.isShowing())
                    popup.dismiss();
            }
        });
        popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {

            @Override
            public void onEmojiconClicked(Emojicon emojicon) {
                if (textMessage == null || emojicon == null) {
                    return;
                }

                int start = textMessage.getSelectionStart();
                int end = textMessage.getSelectionEnd();
                if (start < 0) {
                    textMessage.append(emojicon.getEmoji());
                } else {
                    textMessage.getText().replace(Math.min(start, end),
                            Math.max(start, end), emojicon.getEmoji(), 0,
                            emojicon.getEmoji().length());
                }
            }
        });

        popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {

            @Override
            public void onEmojiconBackspaceClicked(View v) {
                KeyEvent event = new KeyEvent(
                        0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                textMessage.dispatchKeyEvent(event);
            }
        });
        emojiButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //If popup is not showing => emoji keyboard is not visible, we need to show it
                if (!popup.isShowing()) {

                    //If keyboard is visible, simply show the emoji popup
                    if (popup.isKeyBoardOpen()) {
                        popup.showAtBottom();
                        changeEmojiKeyboardIcon(emojiButton, R.drawable.ic_action_keyboard);
                    }

                    //else, open the text keyboard first and immediately after that show the emoji popup
                    else {
                        textMessage.setFocusableInTouchMode(true);
                        textMessage.requestFocus();
                        popup.showAtBottomPending();
                        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(textMessage, InputMethodManager.SHOW_IMPLICIT);
                        changeEmojiKeyboardIcon(emojiButton, R.drawable.ic_action_keyboard);
                    }
                }

                //If popup is showing, simply dismiss it to show the undelying text keyboard
                else {
                    popup.dismiss();
                }
            }
        });

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Globals.PUSH_INCOMINGMSG)) {

                    if (intent.getExtras().getString("message") != null) {
                        MessageDb msg = new Gson().fromJson(intent.getExtras().getString("message"), MessageDb.class);
                       Log.e(TAG,vars.fullname+"===VARSS+++++++++++"+vars.chk);
                        Log.e(TAG,msg.getOtherName()+"SENDER+++++++++++"+msg.getSender());
                        Log.e(TAG,msg.getUserName()+"RECEP+++++++++++"+msg.getRecep());
                        if(vars.chk.equalsIgnoreCase(msg.getRecep())){
                            recep = msg.getSender();
                            recepmobile = msg.getMobileNumber();
                            recepname = msg.getUserName();
                        }else {
                            recep = msg.getRecep();
                            recepmobile = msg.getRecepNumber();
                            recepname = msg.getOtherName();
                        }

                        fullReload();
                        AppController.getInstance().getPrefManager(ChatActivity.this).clear(msg.getThreadid());
                    }
                }

            }
        };

        Log.e(TAG, "RECEP=======" + recep);
    }

    public void sendMessage(View v) {
        Log.e(TAG, "RECEP===send====" + recep);
        if (textMessage.getText().toString().isEmpty()) {

        } else {
            if (vars.chk.equalsIgnoreCase(recep)) {
                Toast.makeText(this, "Not sending====", Toast.LENGTH_LONG).show();

            } else {
                if (send_message) {

                    int msgid = MessageDb.listAll(MessageDb.class).size() + 1;
                    MessageDb msg = new MessageDb(vars.chk + recep, System.currentTimeMillis(),
                            textMessage.getText().toString().trim(), "chat", recep, String.valueOf(msgid), recepname,
                            vars.chk, System.currentTimeMillis(), vars.fullname, vars.chk,
                            "no", "no", vars.mobile, recepmobile, vars.language, true, vars.chk + recep, "");
                    msg.save();

                    reload(msg);
                    textMessage.setText("");
                    Log.e(TAG, "RECEP===" + recep);
                    Log.e(TAG, "BEFORE===" + msg.getThread() + "====after===" + recep + vars.chk);
                    Log.e(TAG, "STORED===" + msg.getThread());

                    SendToServer(msg);
                }
            }
        }

    }

    private void SendToServer(final MessageDb message) {
        final String oringinal = message.getMessage();

        Log.i(TAG, "ID HRE========" + message.getId());
        long msgid = message.getId();

        String messagetosend = null;
        try {
            messagetosend = URLEncoder.encode(message.getMessage(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        message.setMessage(messagetosend);
        message.setMessageId(String.valueOf(msgid));
        Log.e(TAG, "SENDING THIS===" + new Gson().toJson(message));
        String url = Globals.SOCILA_SERV + "entities.chat/sendMessage";
        try {
            JSONObject json = new JSONObject(new Gson().toJson(message));
            ConnectionClass.JsonString(Request.Method.POST, this, url, json, "MESSAGE", new ConnectionClass.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.i(TAG, "feedback===========" + result);
                    try {
                        JSONObject reader = new JSONObject(result);
                        if (reader.getString("error").equalsIgnoreCase("success")) {
                            // MessageDb update = MessageDb.findById(MessageDb.class,message.getId());
                            message.setDelServ("yes");
                            message.setMessage(oringinal);
                            message.save();
                            ReloadNow();
                            //  reload(message);
                            // update.save();
                            //ReloadNow(messageDbs.size(),update);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void ReloadNow() {
        Collections.sort(messageDbs, new Comparator<MessageDb>() {
            @Override
            public int compare(MessageDb u1, MessageDb u2) {

                return u2.getId().compareTo(u1.getId());
            }
        });
        chatadapter.notifyDataSetChanged();
    }

    private void reload(MessageDb msg) {
        messageDbs.add(msg);
        Log.e(TAG, "SIZE===" + messageDbs.size());
        Collections.sort(messageDbs, new Comparator<MessageDb>() {
            @Override
            public int compare(MessageDb u1, MessageDb u2) {

                return u2.getId().compareTo(u1.getId());
            }
        });

        chatadapter.notifyDataSetChanged();

    }

    private void reloadNOTADDING() {
        Log.e(TAG, "SIZE===" + messageDbs.size());
        Collections.sort(messageDbs, new Comparator<MessageDb>() {
            @Override
            public int compare(MessageDb u1, MessageDb u2) {

                return u2.getId().compareTo(u1.getId());
            }
        });

        chatadapter.notifyDataSetChanged();

    }

    private void fullReload() {
        messageDbs = Select.from(MessageDb.class).where(Condition.prop("recep").eq(recep)).or(Condition.prop("sender").eq(recep)).orderBy("id").list();
        Log.e(TAG, "secccc=RELOD==dd" + messageDbs.size());
        Collections.sort(messageDbs, new Comparator<MessageDb>() {
            @Override
            public int compare(MessageDb u1, MessageDb u2) {

                return u2.getId().compareTo(u1.getId());
            }
        });
        chatadapter = new ChatAdapter(this, messageDbs);
        convo_list.setAdapter(chatadapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);

        convo_list.setLayoutManager(linearLayoutManager);
        convo_list.setHasFixedSize(true);

    }

    private final TextWatcher chk_text_Watcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            send_message = true;
            //	 sendmessage.setImageResource(R.mipmap.ic_send);
            layout_send_btn.setVisibility(View.GONE);
            animButton.setImageDrawable(ChatActivity.this.getResources().getDrawable(R.drawable.ic_menu_send));

        }

        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                send_message = false;
                animButton.setImageDrawable(ChatActivity.this.getResources().getDrawable(R.drawable.ic_audio));
                layout_send_btn.setVisibility(View.VISIBLE);


                //	 sendmessage.setImageResource(R.mipmap.ic_attach);
            } else {
                layout_send_btn.setVisibility(View.GONE);
                animButton.setImageDrawable(ChatActivity.this.getResources().getDrawable(R.drawable.ic_menu_send));
                send_message = true;
            }
        }
    };

    private void changeEmojiKeyboardIcon(ImageView iconToBeChanged, int drawableResourceId) {
        iconToBeChanged.setImageResource(drawableResourceId);
    }


    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        Handler handler = new Handler();

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Toast.makeText(Chat.this,"Hold to record,release to send",Toast.LENGTH_SHORT).show();
                    //  handler.postDelayed(run, 1000/* OR the amount of time you want */);
                    break;

                case MotionEvent.ACTION_CANCEL:
                   /* handler.removeCallbacks(run);
                    if(record){
                        record = false;
                        vars.log("YYYYYY===cancle===YYYYYY");
                    }*/
                    break;

                case MotionEvent.ACTION_UP:
                  /*  handler.removeCallbacks(run);
                    if(record){
                        record = false;
                        onRecord(false);
                        vars.log("========stoped recorning========");
                        play_audio();
                    }*/

                    break;

            }
            return true;
        }
    };

    @Override
    protected void onPause() {
        Globals.WHICHUSER = "";
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Globals.WHICHUSER = recep;
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Globals.PUSH_INCOMINGMSG));
        vars.log("====On resume===");
        NotificationUtils.clearNotifications();
        AppController.getInstance().getPrefManager(ChatActivity.this).clear(vars.chk + recep);
        AppController.getInstance().getPrefManager(ChatActivity.this).clear(recep+vars.chk);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.airtime_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
