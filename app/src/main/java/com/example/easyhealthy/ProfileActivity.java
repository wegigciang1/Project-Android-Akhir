package com.example.easyhealthy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity {


    private static final int CHOOSE_IMAGE_REQUEST_CODE = 1100;
    @BindView(R.id.userProfilePictureId)
    ImageView userProfilePictureId;
    @BindView(R.id.profileUserNameId)
    EditText profileUserNameId;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private FirebaseUser user;

    private Uri imageUri;
    private String profilePictureUri, oldProfilePicUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        setTitle("Profile");

        init();
    } // end of onCreate


    private void init() {

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("PROFILE_PICTURE");

    }


    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null) {

            finish();
            startActivity(new Intent(this, MainActivity.class));
        } else {

            loadProfileInfo();
        }
    }


    private void loadProfileInfo() {

        user = mAuth.getCurrentUser();
        String profileUserName = null, profilePictureUri = null;
        if (user != null) {

            if (user.getDisplayName() != null) {
                profileUserName = user.getDisplayName();
            }

            if (user.getPhotoUrl() != null) {
                profilePictureUri = user.getPhotoUrl().toString();
            }
            profileUserNameId.setText(profileUserName);

            if (profilePictureUri != null) {
                Glide.with(this).load(profilePictureUri).into(userProfilePictureId);
            }
        }

    }


    @OnClick(R.id.userProfilePictureId)
    public void onUserProfilePictureIdClicked() {

        chooseImage();
    }

    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a profile picture"), CHOOSE_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            if (data.getData() != null) {

                imageUri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                    userProfilePictureId.setImageBitmap(bitmap);
                    Toast.makeText(this, "Okay", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }


    @OnClick(R.id.updateProfileInfoButton)
    public void onUpdateProfileInfoButtonClicked() {

        storageReference.child(System.currentTimeMillis() + ".jpg");

        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //kemungkinan salah disini
                profilePictureUri = taskSnapshot.getStorage().getDownloadUrl().toString();

                saveProfileInfo();

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {


            }
        });

    }


    public void saveProfileInfo() {

        if (mAuth.getCurrentUser() != null) {

            user = mAuth.getCurrentUser();

            String name = profileUserNameId.getText().toString().trim();
            if (name.isEmpty()) {
                profileUserNameId.setError("Please enter a name");
                profileUserNameId.requestFocus();
                return;
            }

            if (user != null && profilePictureUri != null) {
                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .setPhotoUri(Uri.parse(profilePictureUri)).build();


                user.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(ProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(ProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }

    }


    @OnClick(R.id.log_outButton)
    public void onLogOutButtonClicked() {

        mAuth.signOut();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}
