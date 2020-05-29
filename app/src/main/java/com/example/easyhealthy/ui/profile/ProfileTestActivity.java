package com.example.easyhealthy.ui.profile;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.easyhealthy.MainActivity;
import com.example.easyhealthy.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;


public class ProfileTestActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button btnUpload;
    private TextView textViewChooseImage;
    private TextView textViewShowUpload;
    private ImageView imageView;
    private ProgressBar mProgressBar;

    private Uri mImageUri;
    String namaFileMeta = "test";
    //untuk uid
    private FirebaseAuth firebaseAuth;
    private String userId;
    //akhir uid//
    private StorageTask taskChecker;

    //db
    private FirebaseFirestore firebaseFirestoreDb;
    //connect storage
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_test);

        btnUpload = findViewById(R.id.btnUploadTestProfile);
        textViewChooseImage = findViewById(R.id.textViewChooseFileTestProfile);
        imageView = findViewById(R.id.imageViewTestProfile);
        mProgressBar = findViewById(R.id.progressBarProfile);

        //get uid
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestoreDb = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        //akhir uid
        mStorageRef = FirebaseStorage.getInstance().getReference("FotoProfile/" + userId); //bisa juga diisi ditambah /sdjdk.jpg
        //db
        final DocumentReference washingtonRef = firebaseFirestoreDb.collection("Users").document(userId);


        textViewChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskChecker != null && taskChecker.isInProgress()) {
                    Toast.makeText(ProfileTestActivity.this, "Progress Sedang Bejalan", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile(washingtonRef);
                    //intent

                }

            }
        });

    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    //ambil file extension image
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    //upload file
    private void uploadFile(final DocumentReference washingtonRef) {
        if (mImageUri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() //bisa juga mStorageRef.child("uploads/" + System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            final UploadTask uploadTask = fileReference.putFile(mImageUri);
            taskChecker = uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();

                            }
                            // Continue with the task to get the download URL

                            //Toast.makeText(ProfileTestActivity.this, "Upload Berhasil", Toast.LENGTH_LONG).show();
                            return fileReference.getDownloadUrl();

                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                //lokasi file disimpan di firebase
                                namaFileMeta = task.getResult().toString();
                                washingtonRef
                                        .update("FotoKey", namaFileMeta)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(ProfileTestActivity.this, "data Foto Berhasil Disimpan", Toast.LENGTH_SHORT).show();
                                                //set progress bar mati
                                                mProgressBar.setVisibility(View.GONE);
                                                //lempar ke home klo udah ke save
                                                Intent halFragmentHome = new Intent(ProfileTestActivity.this, MainActivity.class);
                                                startActivity(halFragmentHome);
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //klo error
                                            }
                                        });
                            }
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileTestActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override //untuk progresBarr
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            });
            //
        } else {
            Toast.makeText(this, "No File Selected", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {

            if (data.getData() != null) {

                mImageUri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);


                    imageView.setImageBitmap(bitmap);
                    Toast.makeText(this, "Okay", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }


}
