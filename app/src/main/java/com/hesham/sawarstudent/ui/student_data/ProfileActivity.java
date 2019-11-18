package com.hesham.sawarstudent.ui.student_data;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.hesham.sawarstudent.R;
import com.hesham.sawarstudent.adapter.CustomSpinnerAdapter;
import com.hesham.sawarstudent.data.model.FacultyPojo;
import com.hesham.sawarstudent.data.model.UserPojo;
import com.hesham.sawarstudent.data.response.FacultyResponse;
import com.hesham.sawarstudent.data.response.ImageResponse;
import com.hesham.sawarstudent.data.response.SignUpResponse;
import com.hesham.sawarstudent.networkmodule.Apiservice;
import com.hesham.sawarstudent.ui.home.HomeActivity;
import com.hesham.sawarstudent.ui.signup.SignUpActivity;
import com.hesham.sawarstudent.utils.AddImageUtilities;
import com.hesham.sawarstudent.utils.OnRequestImageIntentListener;
import com.hesham.sawarstudent.utils.PrefManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hesham.sawarstudent.networkmodule.NetworkManager.BASE_URL;

public class ProfileActivity extends AppCompatActivity implements OnRequestImageIntentListener {


    private Spinner universitySpinner, facultySpinner;

    private Button nextBtn;
    private List<FacultyPojo> universityPojos;
    private List<FacultyPojo> facultyPojos;
    private List<FacultyPojo> newFacultyPojos;

    private FrameLayout progress_view;

    PrefManager prefManager;
    private EditText nameEdit, passwordedit;
    TextView emailedit;
    private int universityId, facultyId;
    private File file;
    private File imageFile;
    String logo = "";
    private ImageView addImage;

    private boolean isPasswordVisible;
    ImageView eyeId;
    List<String> univlist, facultyList;
    CustomSpinnerAdapter facultyCustomSpinnerAdapter, universityCustomSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        universityPojos = new ArrayList<>();
        facultyPojos = new ArrayList<>();
        univlist = new ArrayList<>();
        facultyList = new ArrayList<>();
        newFacultyPojos = new ArrayList<>();
        univlist.add(0, "  ");
        facultyList.add(0, "  ");
        initView();
        eyeId = findViewById(R.id.eyeId);
        eyeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePassVisability();
            }
        });

        nextBtn = findViewById(R.id.nextBtn);
        progress_view = findViewById(R.id.progress_view);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkGalleryOrCameraPermissions(10);

            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });
        addItemsOnSpinner2();
        getUniversities();
        getFaculties();
    }

    private void togglePassVisability() {
        if (isPasswordVisible) {
            String pass = passwordedit.getText().toString();
            passwordedit.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordedit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordedit.setText(pass);
            passwordedit.setSelection(pass.length());
            eyeId.setImageResource(R.drawable.icon_visibility_outlined);
        } else {
            String pass = passwordedit.getText().toString();
            passwordedit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            passwordedit.setInputType(InputType.TYPE_CLASS_TEXT);
            passwordedit.setText(pass);
            passwordedit.setSelection(pass.length());
            eyeId.setImageResource(R.drawable.icon_visibility_outlined_blue);
        }
        isPasswordVisible = !isPasswordVisible;
    }

    public void getUniversities() {
        Call<FacultyResponse> call = Apiservice.getInstance().apiRequest.
                getUniversities();
        call.enqueue(new Callback<FacultyResponse>() {
            @Override
            public void onResponse(Call<FacultyResponse> call, Response<FacultyResponse> response) {
                if (response.body() != null) {
                    if (response.body().status && response.body().cc_id != null) {
                        Log.d("tag", "articles total result:: " + response.body().getMessage());
                        Log.d("tag", "articles total result:: " + response.body().getMessage());
                        universityPojos.addAll(response.body().cc_id);
                        int position = 0;
                        int selectedUnivId = prefManager.getStudentData().getUniv();
                        for (int i = 0; i < universityPojos.size(); i++) {
                            univlist.add(universityPojos.get(i).getName());
                            if (universityPojos.get(i).getId() == selectedUnivId) {
                                position = univlist.size()-1;

                            }
                        }

                        universityCustomSpinnerAdapter = new CustomSpinnerAdapter(ProfileActivity.this, univlist, "choose university");
                        universitySpinner.setAdapter(universityCustomSpinnerAdapter);
                        universitySpinner.setSelection(position, true);
                    }
                }

            }

            @Override
            public void onFailure(Call<FacultyResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                Toast.makeText(ProfileActivity.this, "Something went wrong , please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    void setFaculties() {
        int position = 0;
        int selectedFacId = prefManager.getStudentData().getFacultyId();
        facultyList = new ArrayList<>();
        newFacultyPojos = new ArrayList<>();
        facultyList.add(0, "  ");
        for (int i = 0; i < facultyPojos.size(); i++) {
            if (facultyPojos.get(i).getUniv_id() == prefManager.getUniversityId()) {
//                facultyPojos.add(facultyPojos.get(i));
                facultyList.add(facultyPojos.get(i).getName());
                newFacultyPojos.add(facultyPojos.get(i));
                if (facultyPojos.get(i).getId() == selectedFacId) {
                    position = newFacultyPojos.size();
                }
            }
        }
//        for (int i = 1; i <= facultyPojos.size(); i++) {
//            facultyList.add(facultyPojos.get(i-1).getName());
//        }
        facultyCustomSpinnerAdapter = new CustomSpinnerAdapter(this, facultyList, "Choose faculty");
        facultySpinner.setAdapter(facultyCustomSpinnerAdapter);
        facultySpinner.setSelection(position, true);
    }

    public void getFaculties() {
        Call<FacultyResponse> call = Apiservice.getInstance().apiRequest.
                getFaculties();
        progress_view.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<FacultyResponse>() {
            @Override
            public void onResponse(Call<FacultyResponse> call, Response<FacultyResponse> response) {
                if (response.body() != null) {
                    if (response.body().status && response.body().cc_id != null) {
                        Log.d("tag", "articles total result:: " + response.body().getMessage());
//                    for (int i=0 ; i <response.body().cc_id.size(); i++ ){
//                        if (response.body().cc_id.get(i).getUniv_id()==prefManager.getUniversityId()){
//                            facultyPojos.addAll(response.body().cc_id);
//                        }
//                    }
                        facultyPojos.addAll(response.body().cc_id);
                        prefManager.setUniversityId(prefManager.getStudentData().getUniv());
                        setFaculties();
                        progress_view.setVisibility(View.GONE);

                    }
                }
            }
            @Override
            public void onFailure(Call<FacultyResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                progress_view.setVisibility(View.GONE);

            }
        });
    }   // add items into spinner dynamically

    public void addItemsOnSpinner2() {
        universitySpinner = (Spinner) findViewById(R.id.universitySpinner);
        facultySpinner = (Spinner) findViewById(R.id.facultySpinner);

        List<String> list = new ArrayList<String>();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        universitySpinner.setAdapter(dataAdapter);
        facultySpinner.setAdapter(dataAdapter);

        universitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                universityId = i;
                if (universityId != 0) {
                    prefManager.setUniversityId(universityPojos.get(universityId - 1).getId());
                    setFaculties();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        facultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                facultyId = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    void initView() {
        prefManager = new PrefManager(this);
        nameEdit = findViewById(R.id.nameedit);
        passwordedit = findViewById(R.id.passwordedit);
        emailedit = findViewById(R.id.emailedit);
        nameEdit.setText(prefManager.getStudentData().getName());
        emailedit.setText(prefManager.getStudentData().getEmail());
        passwordedit.setText(prefManager.getStudentData().getPassword());
          logo=prefManager.getStudentData().getPhoto();
        addImage = findViewById(R.id.addImage);

        RequestOptions requestOptions = new RequestOptions();

        requestOptions = requestOptions.placeholder(R.drawable.ellipse_9)
                .transforms(new CenterCrop(), new CircleCrop()).dontAnimate();
        Glide.with(ProfileActivity.this).load(BASE_URL + prefManager.getStudentData().getPhoto())
                .apply(requestOptions)
                .into(addImage);
    }

    public void signup() {
        if (nameEdit.getText().toString().trim().isEmpty()) {
            nameEdit.setError("Please enter name");
        } else if (passwordedit.getText().toString().trim().isEmpty()) {
            passwordedit.setError("Please enter password");
        } else if (!validatePassword(passwordedit.getText().toString())) {
            passwordedit.setError("Password must be more than 8 ");
        } else if (universityId == 0) {
            Toast.makeText(this, "please choose university ", Toast.LENGTH_LONG).show();
        } else if (facultyId == 0) {
            Toast.makeText(this, "please choose faculty ", Toast.LENGTH_LONG).show();
        } else {
            final UserPojo userPojo = new UserPojo(prefManager.getStudentData().getId(), nameEdit.getText().toString(), emailedit.getText().toString(), passwordedit.getText().toString(),
                    universityPojos.get(universityId - 1).getId(), newFacultyPojos.get(facultyId - 1).getId() ,logo);
            progress_view.setVisibility(View.VISIBLE);

            Call<SignUpResponse> call = Apiservice.getInstance().apiRequest.
                    updateProfile(userPojo);
            call.enqueue(new Callback<SignUpResponse>() {
                @Override
                public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                    if (response.body() != null) {
                        if (response.body().status) {
                            prefManager.setStudentData(userPojo);
                            prefManager.setToken("registered");
                            progress_view.setVisibility(View.GONE);
                            Intent i = new Intent(ProfileActivity.this, HomeActivity.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(ProfileActivity.this, response.message(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<SignUpResponse> call, Throwable t) {
                    Log.d("tag", "articles total result:: " + t.getMessage());
                    progress_view.setVisibility(View.GONE);

                }
            });
        }
    }

    public void uploadImage() {
        RequestBody imageFileRb = null;
        if (imageFile != null) {
            imageFileRb = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
        }
//        MultipartBody.Part pictureFileToUpload = MultipartBody.Part.createFormData("file", file.getName(), imageFileRb);
        Call<ImageResponse> call = Apiservice.getInstance().apiRequest.
                uploadProfileImages(imageFileRb);
        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("tag", "articles total result:: " + response);
                    logo = response.body().data;
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions = requestOptions.placeholder(R.drawable.ellipse_9)
                            .transforms(new CenterCrop(), new CircleCrop()).dontAnimate();
                    Glide.with(ProfileActivity.this).load(imageFile)
                            .apply(requestOptions)
                            .into(addImage);
                    prefManager.setImageProfile(logo);
                    prefManager.getStudentData().setPhoto(logo);

                }

            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                progress_view.setVisibility(View.GONE);

            }
        });

    }

    private void checkGalleryOrCameraPermissions(int galleryOrCamera) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 5);
        } else {
            AddImageUtilities.openGalleryOrCameraIntent(10, this, this);
        }
    }

    @Override
    public void onRequestGallery(Intent gallery) {
        startActivityForResult(gallery, 10);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("result", "onactivityresult");
        if (resultCode == RESULT_OK) {
            if (requestCode == 10) {
                if (data != null) {
                    Uri selectedImage = data.getData();
                    String imagePath = AddImageUtilities.getImagePath(selectedImage, this);
                    File file = new File(imagePath);

                    this.imageFile = file;
                    Log.e("file", this.imageFile.toString());
                    uploadImage();
//                    }
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                AddImageUtilities.openGalleryOrCameraIntent(10, this, this);
            }

        }
    }


    private boolean validatePassword(String password1) {
        return password1.length() > 7;
    }
}
