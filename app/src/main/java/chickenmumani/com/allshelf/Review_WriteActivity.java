package chickenmumani.com.allshelf;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

import chickenmumani.com.allshelf.R;

public class Review_WriteActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    String isbn;
    String uid;
    final int RESULT_LOAD_IMG = 5;
    final int CROP_FROM_CAMERA = 6;
    FirebaseStorage storage;
    StorageReference storageRef;
    Uri selectedImage;
    Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_write);
        setTitle("리뷰 쓰기");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        isbn = intent.getStringExtra("isbn");
        uid = intent.getStringExtra("uid");
        mDatabase = FirebaseDatabase.getInstance().getReference("Review");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        Button imgbut = (Button) findViewById(R.id.writere_editbutton);
        imgbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_upload:
            {
                if(((EditText)findViewById(R.id.wredit)).getText().toString().length() < 10) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder .setMessage("리뷰가 10자 미만입니다. 조금 더 성의있게 작성해주세요.")
                            .setCancelable(false)
                            .setPositiveButton("확인", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;
                }

                StorageReference ImagesRef = storageRef.child("Review_Image/"+isbn +"_"+ uid +".jpg");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
                UploadTask uploadTask = ImagesRef.putBytes(data);
                final ProgressDialog dialog = ProgressDialog.show(Review_WriteActivity.this, "",
                        "Loading... Please wait");
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        dialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Review_WriteActivity.this);
                        builder .setMessage("이미지 업로드에 실패했습니다. 네트워크 연결을 확인해주세요.")
                                .setCancelable(false)
                                .setPositiveButton("확인", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.dismiss();
                        Review_WriteActivity.this.finish();
                    }
                });

                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.writere, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case RESULT_LOAD_IMG:
                {
                    selectedImage = data.getData();
                    ((TextView)findViewById(R.id.writere_title)).setText(getFileName(selectedImage));
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(selectedImage, "image/*");

                    intent.putExtra("outputX", 90);
                    intent.putExtra("outputY", 90);
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("scale", true);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, CROP_FROM_CAMERA);

                    break;
                }
                case CROP_FROM_CAMERA:
                {
                    try {
                        final Bundle extras = data.getExtras();
                        photo = extras.getParcelable("data");
                        File f = new File(selectedImage.getPath());
                        if(f.exists())
                        {
                            f.delete();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }

            }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
