package com.niraj.social;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class UserFeed extends AppCompatActivity {
      LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        linearLayout =(LinearLayout) findViewById(R.id.linearLayout);

        Intent i = getIntent();
        final String activeUsername = i.getStringExtra("username");

        final DateFormat df = new SimpleDateFormat("DD/mm/yyyy HH:mm:ss");



        Log.i("AppInfo",activeUsername);

        setTitle(activeUsername  +"'s Feed");
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);



        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("images");

        query.whereEqualTo("username",activeUsername);
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null){
                    if(list.size()>0){
                        for(final ParseObject object : list){
                            ParseFile file = (ParseFile) object.get("image");
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] bytes, ParseException e) {
                                    if (e== null){
                                        Bitmap image = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                                        ImageView imageView = new ImageView(getApplicationContext());
                                        TextView textView = new TextView(getApplicationContext());
                                        textView.setTextSize(55);
                                        textView.setTextColor(Color.BLUE);
                                        textView.setText("***************");

                                        imageView.setImageBitmap(image);
                                        imageView.setImageBitmap(Bitmap.createScaledBitmap(image, 600,600, false));
                                        linearLayout.addView(imageView);
                                        linearLayout.addView(textView);

                                    }
                                }
                            });;
                        }

                    }
                    else {
                        TextView textView = new TextView(getApplicationContext());
                        textView.setTextSize(55);
                        textView.setTextColor(Color.BLUE);
                        textView.setText("Sorry user hasen't uploded any image");

                    }

                }
            }
        });
        progressBar.setVisibility(View.GONE);



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_feed, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       if (id == R.id.tweet) {
           Intent i = getIntent();
           final String activeUsername = i.getStringExtra("username");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Send a DirectMessage");
            final EditText tweetContent = new EditText(this);
            builder.setView(tweetContent);

            builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Log.i("AppInfo", String.valueOf(tweetContent.getText()));

                    ParseObject tweet = new ParseObject("Tweet");
                    tweet.put("content", String.valueOf(tweetContent.getText()));
                    tweet.put("username", ParseUser.getCurrentUser().getUsername());
                    tweet.put("sentby", String.valueOf(activeUsername));
                    tweet.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {

                                Toast.makeText(getApplicationContext(), "Your tweet has been sent!", Toast.LENGTH_LONG).show();

                            } else {

                                Toast.makeText(getApplicationContext(), "Your tweet could not be sent - please try again!", Toast.LENGTH_LONG).show();


                            }
                        }
                    });

                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();

                }
            });

            builder.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }





}

