package com.example.coviduniversity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class TestImageUpload extends AppCompatActivity {

    Button choose, upload;
    ImageView img;
    StorageReference storageRefRoot;
    Uri imguri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_image_upload);
        choose = (Button)findViewById(R.id.button_ch);
        upload = (Button)findViewById(R.id.button_up);
        img = (ImageView)findViewById(R.id.image_view);
        storageRefRoot = FirebaseStorage.getInstance().getReference();


        StorageReference loc = storageRefRoot.child("profilePics/" + "pfp_for_y6paytCh4PaExeYIIow0GHhfNfj1");
        loc.getBytes(2000*2000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                img.setImageBitmap(bitmap);
            }
        });

        choose.setOnClickListener(new View.OnClickListener ()  {
            @Override
            public void onClick(View view) {
                FileChooser();
            }
        });

        upload.setOnClickListener(new View.OnClickListener ()  {
            @Override
            public void onClick(View view) {
                FileUploader();
                Intent intent = new Intent(TestImageUpload.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void FileUploader() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading...");
        pd.show();
        StorageReference storageRefFile = storageRefRoot.child("images/"+System.currentTimeMillis()+"."+getExtension(imguri));
        storageRefFile.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_LONG).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progressPercent = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        pd.setMessage((int) progressPercent + "%");
                    }

                });
    }

    private String getExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void FileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData() != null) {
            imguri=data.getData();
            img.setImageURI(imguri);
        }
    }


}