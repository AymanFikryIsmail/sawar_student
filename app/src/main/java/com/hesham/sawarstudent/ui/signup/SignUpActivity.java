package com.hesham.sawarstudent.ui.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.hesham.sawarstudent.R;
import com.hesham.sawarstudent.adapter.CustomSpinnerAdapter;
import com.hesham.sawarstudent.adapter.FacultySelectAdapter;
import com.hesham.sawarstudent.data.model.DepartmentPojo;
import com.hesham.sawarstudent.data.model.FacultyPojo;
import com.hesham.sawarstudent.data.model.UserPojo;
import com.hesham.sawarstudent.data.response.DepartmentResponse;
import com.hesham.sawarstudent.data.response.FacultyResponse;
import com.hesham.sawarstudent.data.response.ImageResponse;
import com.hesham.sawarstudent.data.response.SignUpResponse;
import com.hesham.sawarstudent.networkmodule.Apiservice;
import com.hesham.sawarstudent.networkmodule.NetworkUtilities;
import com.hesham.sawarstudent.ui.login.LoginActivity;
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

public class SignUpActivity extends AppCompatActivity implements OnRequestImageIntentListener,   FacultySelectAdapter.EventListener{


    private Spinner universitySpinner, facultySpinner;

    private Button nextBtn;
    private List<FacultyPojo> universityPojos;
    private List<FacultyPojo> facultyPojos , allFacultyPojos ;
    private FrameLayout progress_view;

    PrefManager prefManager;
    private EditText nameEdit, emailedit, passwordedit;
    private int universityId, facultyId;
    private RadioGroup genderRadioGroup;
    String gender = "male";//prefManager.getLanguage();
    private File file;
    private File imageFile;
    String logo = "";
    private ImageView addImage;
    List<String> univlist , facultyList;
    CustomSpinnerAdapter facultyCustomSpinnerAdapter ,  universityCustomSpinnerAdapter;
    private boolean isPasswordVisible;
    ImageView eyeId;

    private List<DepartmentPojo> departmentsPojos , newDepartmentsPojos;
    private RecyclerView departmentRecyclerView;
    private FacultySelectAdapter departmentSelectAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ////////////////////////////////////////
        departmentsPojos = new ArrayList<>();
        newDepartmentsPojos = new ArrayList<>();
        departmentRecyclerView = findViewById(R.id.departmentsRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        departmentRecyclerView.setLayoutManager(linearLayoutManager);
        departmentSelectAdapter = new FacultySelectAdapter(this, this, departmentsPojos);
        departmentRecyclerView.setAdapter(departmentSelectAdapter);
        //////////////
        universityPojos = new ArrayList<>();
        facultyPojos = new ArrayList<>();
        allFacultyPojos = new ArrayList<>();
        univlist = new ArrayList<>();
        facultyList = new ArrayList<>();
        univlist.add(0, "  ");
        facultyList.add(0, "  ");
        eyeId = findViewById(R.id.eyeId);
        eyeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePassVisability();
            }
        });
        eyeId.setImageResource(R.drawable.icon_visibility_outlined);

        initView();
        nextBtn = findViewById(R.id.nextBtn);
        progress_view = findViewById(R.id.progress_view);
        addImage = findViewById(R.id.addImage);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkGalleryOrCameraPermissions(10);

            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtilities.isOnline(SignUpActivity.this)) {
                    if (NetworkUtilities.isFast(SignUpActivity.this)) {
                        signup();
                    }else {
                        Toast.makeText(SignUpActivity.this, "Poor network connection , please try again", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "Please , check your network connection", Toast.LENGTH_LONG).show();
                }
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
        isPasswordVisible= !isPasswordVisible;
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
                        universityPojos.clear();
                        universityPojos.addAll(response.body().cc_id);
                        for (int i = 1; i <= universityPojos.size(); i++) {
                            univlist.add(universityPojos.get(i - 1).getName());
                        }
                        universityCustomSpinnerAdapter = new CustomSpinnerAdapter(SignUpActivity.this, univlist, "choose university");
                        universitySpinner.setAdapter(universityCustomSpinnerAdapter);
                        universitySpinner.setSelection(0, false);
                        universitySpinner.setAdapter(universityCustomSpinnerAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<FacultyResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                getUniversities();
            }
        });
    }

    public void getFaculties() {
        Call<FacultyResponse> call = Apiservice.getInstance().apiRequest.
                getFaculties();
        call.enqueue(new Callback<FacultyResponse>() {
            @Override
            public void onResponse(Call<FacultyResponse> call, Response<FacultyResponse> response) {
                if (response.body() != null) {
                    if (response.body().status && response.body().cc_id != null) {
                    Log.d("tag", "articles total result:: " + response.body().getMessage());

                    facultyPojos.addAll(response.body().cc_id);
//                    setFaculties();
                }
            }}

            @Override
            public void onFailure(Call<FacultyResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }   // add items into spinner dynamically

    void setFaculties(){
        facultyList = new ArrayList<>();
        facultyList.add(0, "  ");
        for (int i=0 ; i <facultyPojos.size(); i++ ){
            if (facultyPojos.get(i).getUniv_id()==prefManager.getUniversityId()){
//                facultyPojos.add(facultyPojos.get(i));
                facultyList.add(facultyPojos.get(i).getName());

            }
        }
//        for (int i = 1; i <= facultyPojos.size(); i++) {
//            facultyList.add(facultyPojos.get(i-1).getName());
//        }
        facultyCustomSpinnerAdapter = new CustomSpinnerAdapter(SignUpActivity.this, facultyList, "Choose faculty");
        facultySpinner.setAdapter(facultyCustomSpinnerAdapter);
        facultySpinner.setSelection(0, false);
        facultySpinner.setAdapter(facultyCustomSpinnerAdapter);
    }
    public void addItemsOnSpinner2() {
        universitySpinner = (Spinner) findViewById(R.id.universitySpinner);
        facultySpinner = (Spinner) findViewById(R.id.facultySpinner);
        universityCustomSpinnerAdapter = new CustomSpinnerAdapter(SignUpActivity.this, facultyList, "Choose  university");
        facultyCustomSpinnerAdapter = new CustomSpinnerAdapter(SignUpActivity.this, facultyList, "Choose faculty");

        universitySpinner.setAdapter(universityCustomSpinnerAdapter);
        universitySpinner.setSelection(0, false);

        universitySpinner.setAdapter(universityCustomSpinnerAdapter);
        facultySpinner.setAdapter(facultyCustomSpinnerAdapter);


        universitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                universityId = i;
                if (universityId!=0){
                    prefManager.setUniversityId(universityPojos.get(universityId-1).getId());
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
                if (facultyId!=0)
                    getAllDepartments( facultyPojos.get(facultyId-1).getId());
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
        genderRadioGroup = findViewById(R.id.genderId);
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.maleId:
                        gender = "male";
                        break;
                    case R.id.femaleId:
                        gender = "female";
                        break;
                }
            }
        });
    }

    public void signup() {
        if (nameEdit.getText().toString().trim().isEmpty()) {
            nameEdit.setError("Please enter name");
        }  else if (emailedit.getText().toString().trim().isEmpty() || !validateEmail(emailedit.getText().toString())) {
            emailedit.setError("Please enter a valid email");
        }else if (passwordedit.getText().toString().trim().isEmpty()) {
            passwordedit.setError("Please enter password");
        } else if (!validatePassword(passwordedit.getText().toString())) {
            passwordedit.setError("Password must be more than 8 ");
        }
        else if (universityId == 0) {
            Toast.makeText(SignUpActivity.this, "please choose university ", Toast.LENGTH_LONG).show();
        }   else if (facultyId == 0) {
            Toast.makeText(SignUpActivity.this, "please choose faculty ", Toast.LENGTH_LONG).show();
        }
        else {
            final UserPojo userPojo = new UserPojo(nameEdit.getText().toString(), emailedit.getText().toString(), passwordedit.getText().toString(),
                    gender, universityPojos.get(universityId-1).getId(), facultyPojos.get(facultyId-1).getId(), logo , prefManager.getNotificationToken(), departmetnID);
            progress_view.setVisibility(View.VISIBLE);

            Call<SignUpResponse> call = Apiservice.getInstance().apiRequest.
                    signup(userPojo);
            call.enqueue(new Callback<SignUpResponse>() {
                @Override
                public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                    if (response.body() != null) {
                        if (response.body().status && response.body().data != null) {
                            userPojo.setId(response.body().data.getInsertId());
                            prefManager.setStudentData(userPojo);
//                        prefManager.setToken("registered");
                            Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(SignUpActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    progress_view.setVisibility(View.GONE);
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
                    Glide.with(SignUpActivity.this).load(imageFile)
                            .apply(requestOptions)
                            .into(addImage);
                    prefManager.setImageProfile(logo);

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


        private boolean validateEmail(String email) {
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

            boolean isValidEmail = email.matches(emailPattern);
            return isValidEmail;
        }

        private boolean validatePassword(String password1) {
            return password1.length() > 7;
        }

        private boolean validatePhoneNumber(String phoneNumber) {

            String phonePattern = "^01[0-2|5]{1}[0-9]{8}";
            boolean isValidPhone = phoneNumber.matches(phonePattern);

            return isValidPhone;

        }


    public void getAllDepartments(int facID) {//prefManager.getCenterId()
        Call<DepartmentResponse> call = Apiservice.getInstance().apiRequest.
                getAllDepartments(facID);
        call.enqueue(new Callback<DepartmentResponse>() {
            @Override
            public void onResponse(Call<DepartmentResponse> call, Response<DepartmentResponse> response) {
                if (response.body() != null) {
                    if (response.body().status && response.body().cc_id != null) {
                        Log.d("tag", "articles total result:: " + response.body().getMessage());
                        departmentsPojos.clear();
                        departmentsPojos.addAll(response.body().cc_id);
                        departmentSelectAdapter.updateList(departmentsPojos);
                    }
                }
            }
            @Override
            public void onFailure(Call<DepartmentResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                Toast.makeText(SignUpActivity.this, "Something went wrong , please try again", Toast.LENGTH_LONG).show();

            }
        });
    }
    ArrayList<Integer> departmetnID = new ArrayList<>();

    @Override
    public void onChange(int facultyId, boolean check) {
        if (check) {
            departmetnID.add(facultyId);
        } else {
            departmetnID.remove((Integer) facultyId);
        }
    }
}
