package com.zone.zoneapp.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.zone.zoneapp.R;
import com.zone.zoneapp.model.Chat;
import com.zone.zoneapp.model.IdEmailPair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PrivateMessageActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayList<IdEmailPair> mList;

    private ContactListAdapter mAdpter;
    private ChattingListAdapter mChattingListAdapter;

    private ParseUser mParseUser;
    private ParseObject mParseObject;
    private TextView mChatWithTextView;

    private ArrayList<IdEmailPair> mChatList;

    private Firebase mFireBaseRef;
    private EditText mInputText;

    private ListView mChattingListView;

    private ArrayList<Chat> mAllMessageList;
    private ArrayList<ValueEventListener> mListenerList;

    private IdEmailPair mChatWithPair;

    private ValueEventListener mEventListener;
    private String mChatWithString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_message);
        Log.i("aaa", "here");

        mFireBaseRef = new Firebase("https://zone-app.firebaseio.com/");

        mListenerList = new ArrayList<>();

        mList = new ArrayList<IdEmailPair>();

        mListView = (ListView)findViewById(R.id.private_message_list_container);

        mChatWithTextView = (TextView)findViewById(R.id.chat_with_title);

        mChattingListView = (ListView)findViewById(R.id.list_chatting);

        mChatWithPair = new IdEmailPair();

        mChatWithString = "";

        mAllMessageList = new ArrayList<>();


        mInputText = (EditText) findViewById(R.id.messageInput);
        mInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (mChatWithPair.getEmail() != null && actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChatWithPair.getEmail() != null) {
                    sendMessage();
                }
            }
        });

        Log.i("aaa", "here1");

        mParseUser = ParseUser.getCurrentUser();
        Log.i("aaa", "here2");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Contacts");
        query.whereEqualTo("currentUser", mParseUser.getEmail());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size()==0){

                    Log.i("aaa","not contat");
                    mList.clear();
                    mParseObject = null;
                }
                else if(objects.size()!=0){
                    Log.i("aaa","have contact");

                    mParseObject = objects.get(0);


                    ArrayList<String> list = new ArrayList<String>();
                    list = (ArrayList)mParseObject.getList("contactList");
                    list.remove(mParseUser.getEmail()+"-"+mParseUser.getObjectId());
                    for (String s : list){
                        int index = s.indexOf("com-");

                        IdEmailPair pair = new IdEmailPair(s.substring(0,index+3),s.substring(index+4,s.length()),false);
                        mList.add(pair);
                    }
                    populateList();
                }
            }
        });




    }



    private void populateList(){

        mAdpter = new ContactListAdapter();
        mListView.setAdapter(mAdpter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IdEmailPair pair = (IdEmailPair) parent.getItemAtPosition(position);
                mChatWithPair = pair;
                mChatWithString = mParseUser.getObjectId() + "&" + mChatWithPair.getId();
                mChatWithTextView.setText("Chatting with " + mChatWithPair.getEmail());
                getMessage();
            }
        });


        //Long click to use implicit intent to send email
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final IdEmailPair pair = (IdEmailPair) parent.getItemAtPosition(position);
                new AlertDialog.Builder(PrivateMessageActivity.this)
                        .setTitle(PrivateMessageActivity.this.getString(R.string.set_email))
                        .setMessage(PrivateMessageActivity.this.getString(R.string.set_email_to_this_user))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.setAction(Intent.ACTION_SEND);
                                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{pair.getEmail()});
                                emailIntent.setType("text/plain");
                                startActivity(emailIntent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                return;
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return false;
            }
        });

        mChattingListAdapter = new ChattingListAdapter();
        mChattingListView.setAdapter(mChattingListAdapter);
    }


    private void sendMessage() {
        if (mChatWithPair==null){
            Toast.makeText(PrivateMessageActivity.this,PrivateMessageActivity.this.getString(R.string.select_first),Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        int sec = c.get(Calendar.SECOND);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DATE);

        String hourString = String.format("%02d", hour);
        String minString = String.format("%02d", min);
        String secString = String.format("%02d", sec);
        String yearString = String.format("%04d", year);
        String monthString = String.format("%02d", month);
        String dayString = String.format("%02d", day);

        String time = hourString+":"+minString+":"+secString+" - "+dayString+"/"+monthString+"/"+yearString;

        ArrayList<String> list = new ArrayList<>();

        String fromId = mParseUser.getObjectId()+"&"+ mChatWithPair.getId();
        String toId = mChatWithPair.getId()+"&"+mParseUser.getObjectId();
        Log.i("aaa", "chat with" + fromId);

        Chat chatObject = new Chat(mInputText.getText().toString(), mParseUser.getEmail(), time);
        mFireBaseRef.child(fromId).push().setValue(chatObject);
        mFireBaseRef.child(toId).push().setValue(chatObject);


        mInputText.setText("");
    }


    private void getMessage() {

        if (mChatWithString.equals("")){
            return;
        }


        ArrayList<String> list = new ArrayList<>();
        Log.i("aaa", mChatWithString);

        mFireBaseRef.child(mChatWithString).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mAllMessageList.clear();
                for (DataSnapshot chatSnapshot : dataSnapshot.getChildren()){
                    Chat chat = chatSnapshot.getValue(Chat.class);

                    mAllMessageList.add(chat);
                    //mChattingListAdapter.notifyDataSetChanged();

                    //mChattingListAdapter = new ChattingListAdapter();
                    //mChattingListView.setAdapter(mChattingListAdapter);

                }
                //mChattingListAdapter = new ChattingListAdapter();
                //mChattingListView.setAdapter(mChattingListAdapter);
                mChattingListAdapter.notifyDataSetChanged();
                Log.i("aaa", mAllMessageList.toString());

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        /**
        for (IdEmailPair pair : mChatList){
            String id = mParseUser.getObjectId()+"&"+pair.getId();
            list.add(id);
        }

        for (String s : list){
            ValueEventListener listener = mFireBaseRef.child(s).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //mAllMessageList.add((Chat) dataSnapshot.getValue());
                    //ArrayList<Chat> chatList = (ArrayList<Chat>)dataSnapshot.getValue();

                    for (DataSnapshot chatSnapshot : dataSnapshot.getChildren()){
                        Chat chat = chatSnapshot.getValue(Chat.class);

                        mAllMessageList.add(chat);

                        //mChattingListAdapter.notifyDataSetChanged();
                        Log.i("aaa", String.valueOf(mAllMessageList.get(0).getAuthor()));
                        Log.i("aaa", String.valueOf(mAllMessageList.get(0).getMessage()));
                        Log.i("aaa", String.valueOf(mAllMessageList.get(0).getTime()));
                        mChattingListAdapter = new ChattingListAdapter();
                        mChattingListView.setAdapter(mChattingListAdapter);


                    }

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            mListenerList.add(listener);



        }
         */

        Log.i("aaa","populate 4");


    }


    private class ContactListAdapter extends ArrayAdapter<IdEmailPair> {

        public ContactListAdapter() {
            super(PrivateMessageActivity.this, 0, mList);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            try{
                if (convertView==null){
                    convertView = PrivateMessageActivity.this.getLayoutInflater().inflate(R.layout.list_private_message,null);
                }

                final IdEmailPair pair = getItem(position);

                /**
                RelativeLayout layout = (RelativeLayout)convertView.findViewById(R.id.list_item_layout);
                if (pair.isSelectFlag()==true){
                    layout.setBackgroundColor(Color.LTGRAY);
                }
                else {
                    layout.setBackgroundColor(Color.WHITE);
                }

                 */

                TextView userIdView = (TextView)convertView.findViewById(R.id.userid_private);
                userIdView.setText(pair.getEmail());

                ImageView deleteButton = (ImageView)convertView.findViewById(R.id.Button_delete_private);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mList.remove(getItem(position));
                        mAdpter.notifyDataSetChanged();
                        //populateList();

                        //mParseObject.remove("contactList");
                        //mParseObject.saveInBackground();
                        //mParseUser.remove("contactList");
                        //mParseUser.saveInBackground();



                    }
                });


            }catch (Exception e){
                e.printStackTrace();
            }

            return convertView;
        }
    }



    private class ChattingListAdapter extends ArrayAdapter<Chat> {

        public ChattingListAdapter() {
            super(PrivateMessageActivity.this, 0, mAllMessageList);
            Log.i("aaa", "here11");

        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
                if (convertView==null){
                    convertView = PrivateMessageActivity.this.getLayoutInflater().inflate(R.layout.list_chatting,null);
                    Log.i("aaa","here12");

                }


                final Chat chat = getItem(position);
            Log.i("aaa","here13");

                //TextView userIdView = (TextView)convertView.findViewById(R.id.chatting_user_name);
                //userIdView.setText(chat.getAuthor());

                TextView time = (TextView)convertView.findViewById(R.id.chatting_time);
                time.setText(chat.getTime());

                TextView text = (TextView)convertView.findViewById(R.id.chatting_text);
                text.setText(chat.getAuthor()+": "+chat.getMessage());
            Log.i("aaa", "here14");

            return convertView;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (mParseObject != null && mParseObject.getList("contactList").size()!=0){
            mParseObject.remove("contactList");
            ArrayList<String> list = new ArrayList<>();
            for (IdEmailPair p : mList){
                String s = p.getEmail()+"-"+p.getId();
                list.add(s);
            }
            mParseObject.addAllUnique("contactList", list);
            mParseObject.saveInBackground();

        }

    }

    /**
    @Override
    public void onStop() {
        super.onStop();
        if (mChatWithPair.getEmail()!=null){
            mFireBaseRef.child(mChatWithString).removeEventListener(mEventListener);

        }
    }
*/

}
