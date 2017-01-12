package com.ptrprograms.gdgboulderfirebase;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FirebaseStorageActivity extends Activity {

    private ImageView imageView;

    private UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_storage);
        imageView = (ImageView) findViewById(R.id.image);
    }

    public void downloadBytes(View v) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://gdgboulder-firebase.appspot.com").child("gdgicon.png");

        final long ONE_MEGABYTE = 1024 * 1024;
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp,
                        imageView.getWidth(),
                        imageView.getHeight(),
                        false));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("Firebase Storage", "Downloading Bytes Exception: " + exception.getMessage() );
            }
        });
    }

    public void downloadFile(View v) {
        try {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://gdgboulder-firebase.appspot.com").child("gdgicon.png");

            final File localFile = File.createTempFile("images", "png");

            storageRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d("Firebase Storage", "File complete! " + localFile.toString() );
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firebase Storage", "Downloading File Exception: " + e.getMessage() );
                    }
                });
        } catch(IOException e) {

        }
    }

    public void getFileUrl(View v) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://gdgboulder-firebase.appspot.com").child("gdgicon.png");

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("Firebase Storage", "URL: " + uri.toString());
            }
        });
    }

    public void uploadFile(View v) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://gdgboulder-firebase.appspot.com");

        try {
            File file = File.createTempFile("tmp", ".png");
            Uri fileUri = Uri.fromFile(file);

            StorageReference imageStorageRef = storageRef.child(fileUri.getLastPathSegment());
            uploadTask = imageStorageRef.putFile(fileUri);
        } catch( IOException e ) {

        }
    }

    public void uploadStream(View v) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://gdgboulder-firebase.appspot.com").child("storage-upload.png");

        try {
            //File is intentionally empty for demo
            AssetFileDescriptor fileDescriptor = getAssets().openFd("gdgicon.png");
            FileInputStream stream = fileDescriptor.createInputStream();

            uploadTask = storageRef.putStream(stream);

        } catch( FileNotFoundException e ) {
            Log.e("Firebase Storage", "Upload Stream exception: " + e.getMessage() );
        } catch( IOException e ) {
            Log.e("Firebase Storage", "IO exception: " + e.getMessage() );
        }

    }

    public void uploadBytes(View v) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://gdgboulder-firebase.appspot.com").child("storage-upload.png");

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] data = outputStream.toByteArray();


        uploadTask = storageRef.putBytes(data);
    }

    public void pauseResumeUpload(View v) {
        if( uploadTask.isInProgress() ) {
            if( !uploadTask.isPaused() ) {
                uploadTask.pause();
            } else {
                uploadTask.resume();
            }
        }
    }

    public void deleteFile(View v) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://gdgboulder-firebase.appspot.com").child("storage-upload.png");

        storageRef.delete();
    }


}
