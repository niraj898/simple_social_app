package com.niraj.social;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ViewFeed extends AppCompatActivity {

    ListView listView;
    SimpleAdapter simpleAdapter;
    List<Map<String, String>> tweetData = new ArrayList<Map<String, String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feed);
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        listView = (ListView) findViewById(R.id.yourFeedListView);

        tweetData = new ArrayList<Map<String, String>>();

        simpleAdapter = new SimpleAdapter(this, tweetData, android.R.layout.simple_list_item_2, new String[]{"content", "username"}, new int[]{android.R.id.text1, android.R.id.text2});
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                alertDisplayer("Login Sucessful", ParseUser.getUsername().toString());
            }
        });*/


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tweet");
        query.whereEqualTo("sentby", ParseUser.getCurrentUser().getUsername());
        query.orderByDescending("createdAt");
        query.setLimit(20);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> tweetObjects, ParseException e) {
                if (e == null) {

                    if (tweetObjects.size() > 0) {


                        for (ParseObject tweetObject : tweetObjects) {

                            Map<String, String> tweet = new HashMap<String, String>(2);
                            tweet.put("content", tweetObject.getString("content"));
                            tweet.put("username", tweetObject.getString("username"));

                            tweetData.add(tweet);

                        }


                        listView.setAdapter(simpleAdapter);

                    }


                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
        progressBar.setVisibility(View.INVISIBLE);





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


