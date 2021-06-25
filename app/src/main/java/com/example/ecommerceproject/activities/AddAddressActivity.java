package com.example.ecommerceproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ecommerceproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddAddressActivity extends AppCompatActivity {

    EditText name,addr,city,postalCode,phone;
    Toolbar toolbar;
    Button AddAddrBtn;

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();

        toolbar=findViewById(R.id.add_address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name=findViewById(R.id.ad_name);
        addr=findViewById(R.id.ad_address);
        city=findViewById(R.id.ad_city);
        postalCode=findViewById(R.id.ad_code);
        phone=findViewById(R.id.ad_phone);
        AddAddrBtn=findViewById(R.id.ad_add_address);

        AddAddrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName=name.getText().toString();
                String userAddr=addr.getText().toString();
                String userCity=city.getText().toString();
                String userCode=postalCode.getText().toString();
                String userPhone=phone.getText().toString();

                String final_addr="";
                if (!userName.isEmpty()){
                    final_addr +=userName +", ";
                }
                if (!userAddr.isEmpty()){
                    final_addr +=userAddr+", ";
                }
                if (!userCity.isEmpty()){
                    final_addr +=userCity+", ";
                }
                if (!userCode.isEmpty()){
                    final_addr +=userCode +", ";
                }
                if (!userPhone.isEmpty()){
                    final_addr +=userPhone;
                }
                if (!userName.isEmpty() && !userAddr.isEmpty() && !userCity.isEmpty() && !userCode.isEmpty() && !userPhone.isEmpty()){
                    HashMap<String,String> map=new HashMap<>();

                    map.put("userAddress",final_addr);

                    firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                            .collection("Address").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(AddAddressActivity.this, "Address Added", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddAddressActivity.this,DetailActivity.class));
                                finish();
                            }else{
                                Toast.makeText(AddAddressActivity.this, "kindly fill all field", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }
}