package edu.purdue.cs;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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


    private static void setAvatarInBackground(Bitmap avatar, SaveCallback callback) {
        ByteArrayOutputStream pngOut = new ByteArrayOutputStream();
        avatar.compress(Bitmap.CompressFormat.PNG, 100, pngOut);
        ParseFile file = new ParseFile("avatar.png", pngOut.toByteArray());
        ParseUser user = ParseUser.getCurrentUser();
        user.add("avatar", file);
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
        ParseUser user = ParseUser.getCurrentUser();
        String username = user.getString("name");
        action.setTitle(username);

        TextView User = (TextView)findViewById(R.id.userName);
        User.setText(username);

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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap pic = BitmapFactory.decodeFile(picturePath);
//            setAvatarInBackground(pic, new SaveCallback() {
//                @Override
//                public void done(ParseException e) {
//
//                }
//            });
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
