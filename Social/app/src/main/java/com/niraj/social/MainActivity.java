package com.niraj.social;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener  {

    EditText usernameField;
    EditText passwordField;
    TextView changeSignUpModeTextView;
    Button signUpButton;
    ImageView logo;
    RelativeLayout relativeLayout;

    Boolean signUpModeActive;

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {


        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

            signUpOrLogIn(v);

        }

        return false;

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.changeSignUpMode) {

            if (signUpModeActive == true) {

                signUpModeActive = false;
                changeSignUpModeTextView.setText("New to Social?Let's Sign Up");
                signUpButton.setText("Log In");


            } else {

                signUpModeActive = true;
                changeSignUpModeTextView.setText("Log In");
                signUpButton.setText("Sign Up");

            }

        } else if (v.getId() == R.id.logo || v.getId() == R.id.relativeLayout){

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        }

    }

    public void signUpOrLogIn(View view) {

        if (signUpModeActive == true) {
            attemptRegister();
            /*ParseUser user = new ParseUser();
            user.setUsername(String.valueOf(usernameField.getText()));
            user.setEmail(String.valueOf(usernameField.getText()));
            user.setPassword(String.valueOf(passwordField.getText()));

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {

                    if (e == null) {

                        Log.i("AppInfo", "Signup Successful");

                        showUserList();

                    } else {

                        Toast.makeText(getApplicationContext(), e.getMessage().substring(e.getMessage().indexOf(" ")), Toast.LENGTH_LONG).show();

                    }

                }
            });*/

        } else {

            attemptLogin();

            /*ParseUser.logInInBackground(String.valueOf(usernameField.getText()), String.valueOf(passwordField.getText()), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {

                    if (user != null) {

                        Log.i("AppInfo", "Login Successful");

                        showUserList();

                    } else {

                        Toast.makeText(getApplicationContext(), e.getMessage().substring(e.getMessage().indexOf(" ")), Toast.LENGTH_LONG).show();


                    }

                }
            });*/

        }
    }

    private void attemptLogin() {
        // Reset errors.
        usernameField.setError(null);
        passwordField.setError(null);

        // Store values at the time of the login attempt.
        String email = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordField.setError("error_invalid_password");
            focusView = passwordField;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            usernameField.setError("error_field_required");
            focusView = usernameField;
            cancel = true;
        } else if (!isEmailValid(email)) {
            usernameField.setError("error_invalid_email");
            focusView = usernameField;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);

            ParseUser.logInInBackground(email, password, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if (parseUser != null) {
                        //showProgress(false);
                        if(parseUser.getBoolean("emailVerified")){

                            showUserList();
                            alertDisplayer("Login Sucessful", parseUser.getUsername().toString());

                        }

                        else
                        {
                            ParseUser.logOut();
                            alertDisplayer("Login Fail", "Please Verify Your Email first");
                        }
                    } else {
                        //showProgress(false);
                        alertDisplayer("Login Fail", e.getMessage() + " Please re-try");
                    }
                }
            });
        }
    }

    private void attemptRegister() {
        // Reset errors.
        usernameField.setError(null);
        passwordField.setError(null);

        // Store values at the time of the login attempt.
        String email = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        String username = usernameField.getText().toString();



        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordField.setError("error_invalid_password");
            focusView = passwordField;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            usernameField.setError("error_field_required");
            focusView = usernameField;
            cancel = true;
        } else if (!isEmailValid(email)) {
            usernameField.setError("error_invalid_email");
            focusView = usernameField;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            ParseUser user = new ParseUser();
            user.setUsername(email);
            user.setPassword(password);
            user.setEmail(email);

            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        final String title = "Account Created Successfully!";
                        final String message = "Please verify your email before Login";
                        alertDisplayer(title, message);
                    } else {
                        final String title = "Error Account Creation failed";
                        final String message = "Account could not be created";
                        alertDisplayer(title, message + " :" + e.getMessage());
                    }
                }
            });
        }
    }
    void alertDisplayer(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public void showUserList () {

        Intent i = new Intent(getApplicationContext(), UserList.class);
        startActivity(i);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if (ParseUser.getCurrentUser() != null) {

            showUserList();

        }

        signUpModeActive = true;

        usernameField = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);
        changeSignUpModeTextView = (TextView) findViewById(R.id.changeSignUpMode);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        logo = (ImageView) findViewById(R.id.logo);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        changeSignUpModeTextView.setOnClickListener(this);
        logo.setOnClickListener(this);
        relativeLayout.setOnClickListener(this);

        usernameField.setOnKeyListener(this);
        passwordField.setOnKeyListener(this);



        ParseAnalytics.trackAppOpenedInBackground(getIntent());


    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage("Are you sure you want to close this activity?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
