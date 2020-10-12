package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Registration extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    DatePickerDialog.OnDateSetListener onDateSetListener;

    StorageReference idStorageReference;
    Uri fileUri;
    ImageView id_front;
    String currentDateString;
    TextView date_of_birth_text;
    FirebaseAuth fAuth;
    DatabaseReference userRef;
    Button button_register,date_btn,uploadId_profile_image;
    EditText editText_fullName,editText_contactNo,editText_address,editText_email,editText_password,editText_confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        Initialization();
        ClickListeners();
        Processes();
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void Initialization() {
        idStorageReference = FirebaseStorage.getInstance().getReference().child("Customer_Profile_image");

        id_front = (ImageView) findViewById(R.id.id_front);

        date_of_birth_text = (TextView) findViewById(R.id.date_of_birth_text);

        userRef = FirebaseDatabase.getInstance().getReference("Users_data");

        fAuth = FirebaseAuth.getInstance();

        uploadId_profile_image = (Button) findViewById(R.id.uploadId_profile_image);
        date_btn = (Button) findViewById(R.id.date_btn);
        button_register = (Button) findViewById(R.id.button_register);

        editText_fullName = (EditText)findViewById(R.id.editText_fullName);
        editText_contactNo = (EditText)findViewById(R.id.editText_contactNo);
        editText_email = (EditText)findViewById(R.id.editText_email);
        editText_password = (EditText)findViewById(R.id.editText_password);
        editText_address = (EditText)findViewById(R.id.editText_address);
        editText_confirmPassword = (EditText)findViewById(R.id.editText_confirmPassword);
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void ClickListeners() {
        registerButtonClick();
        date_btnClisckListner();
        takeProfileImage();
    }

    private void takeProfileImage() {
        uploadId_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(Registration.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    private void registerButtonClick() {
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }

            private void register() {
                final String fullname = editText_fullName.getText().toString();
                final String contactno = editText_contactNo.getText().toString();
                final String dob = date_of_birth_text.getText().toString();
                final String address = editText_address.getText().toString();
                final String email = editText_email.getText().toString();
                String password = editText_password.getText().toString();
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String userID = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            Toast.makeText(Registration.this, "Registered!", Toast.LENGTH_SHORT).show();
                            userRef.child(userID).child("type").setValue("customer");
                            userRef.child(userID).child("fullname").setValue(fullname);
                            userRef.child(userID).child("contactno").setValue(contactno);
                            userRef.child(userID).child("dob").setValue(dob);
                            userRef.child(userID).child("address").setValue(address);
                            userRef.child(userID).child("email").setValue(email);

                            idStorageReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ID").putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(Registration.this, "Uploading finished!", Toast.LENGTH_SHORT).show();

                                    idStorageReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ID").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            finish();
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Registration.this, "Not Submitted", Toast.LENGTH_SHORT).show();
                                }
                            });

                            Intent intent = new Intent(Registration.this, BuyLocal_main.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
                fAuth.createUserWithEmailAndPassword(email,password).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Registration.this, String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void date_btnClisckListner() {
        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int year= (calendar.get(Calendar.YEAR))-18;
                int month= 0;
                int day= 1;

                DatePickerDialog dialog = new DatePickerDialog(Registration.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,onDateSetListener,year,month,day);
                currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void Processes() {
        onDatesetListner();
    }

    private void onDatesetListner() {
        onDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String date = dayOfMonth+"/"+ month + "/"+ year;

                SimpleDateFormat input = new SimpleDateFormat("d/M/yyyy");
                SimpleDateFormat output = new SimpleDateFormat("EEEE, d MMMM yyyy");

                try {
                    date_of_birth_text.setText(output.format(input.parse(date)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        date_of_birth_text.setText(currentDateString);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            fileUri = data.getData();
            id_front.setImageURI(fileUri);

            //You can get File object from intent
//            val file:File = ImagePicker.getFile(data)!!

                    //You can also get File Path from intent
//                    val filePath:String = ImagePicker.getFilePath(data)!!
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
//            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}