package edu.purdue.cs;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class UserProfile extends AppCompatActivity {


    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    private static void setAvatarInBackground(Bitmap avatar, final SaveCallback callback) {
        ByteArrayOutputStream pngOut = new ByteArrayOutputStream();
        avatar.compress(Bitmap.CompressFormat.PNG, 100, pngOut);
        ParseFile file = new ParseFile("avatar.png", pngOut.toByteArray());
        ParseUser user = ParseUser.getCurrentUser();
        user.put("avatar", file);
        user.saveInBackground(callback);
    }

    private static Bitmap getAvatar() throws ParseException {
        ParseUser user = ParseUser.getCurrentUser();
        ParseFile file = user.getParseFile("avatar");
        if(file == null){
            return null;
        }
        ByteArrayInputStream pngIn = new ByteArrayInputStream(file.getData());
        Bitmap avatar = BitmapFactory.decodeStream(pngIn);
        return avatar;
    }

    public static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar action = (Toolbar)findViewById(R.id.actionBar);
        setSupportActionBar(action);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final ParseUser user = ParseUser.getCurrentUser();
        String username = user.getString("name");
        action.setTitle(username);

        final TextView name = (TextView)findViewById(R.id.userName);
        name.setText(username);

        TextView email = (TextView)findViewById(R.id.emailAdd);
        email.setText(user.getEmail());

        Button chooseImage = (Button)findViewById(R.id.chooseImage);
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pick = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(pick, RESULT_LOAD_IMAGE);
            }
        });

        ImageView avatar = (ImageView)findViewById(R.id.avatarView);
        try {
            if(getAvatar() != null)
                avatar.setImageBitmap(getAvatar());
            else{
                avatar.setImageDrawable(getResources().getDrawable(R.mipmap.ic_user_default));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Button password = (Button)findViewById(R.id.password);
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassowrd();
            }
        });
    }

    protected void changePassowrd(){
        LayoutInflater layoutInflater = LayoutInflater.from(UserProfile.this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UserProfile.this);
        alertDialogBuilder.setView(promptView);

        final EditText newUser = (EditText)promptView.findViewById(R.id.newName);


        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String input = newUser.getText().toString();
                        Toast toast = Toast.makeText(getApplicationContext(), input, Toast.LENGTH_LONG);
                        toast.show();
                        ParseUser user = ParseUser.getCurrentUser();
                        user.put("name", input);
                        TextView name = (TextView)findViewById(R.id.userName);
                        name.setText(input);
                        try {
                            user.save();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        verifyStoragePermissions(this);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap pic = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(picturePath), 200, 200, true);
            setAvatarInBackground(pic, new SaveCallback() {
                @Override
                public void done(ParseException e) {

                }
            });
            ImageView ci = (ImageView)findViewById(R.id.avatarView);
            ci.setImageBitmap(pic);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            ParseUser.logOut();
            Intent back = new Intent(UserProfile.this, SplashScreen.class);
            startActivity(back);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
