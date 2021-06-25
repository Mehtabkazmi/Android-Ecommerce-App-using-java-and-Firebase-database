package com.example.ecommerceproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.ecommerceproject.R;
import com.example.ecommerceproject.models.NewProductModel;
import com.example.ecommerceproject.models.ShowAllModel;
import com.example.ecommerceproject.models.popularProductModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {

    ImageView detail_img,addItems,removeItems;
    TextView name,rating, description,price,quantity;
    Button addToCart,buyNow;

    Toolbar toolbar;

    int totalQnty=1, totalPrice=0;
    //new product
    NewProductModel newProductModel= null;
    // popular product
    popularProductModel popularProductModels=null;

    //show all
    ShowAllModel showAllModel=null;

    FirebaseAuth auth;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //toolbar
        toolbar=findViewById(R.id.toolbar_det);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //firebase
        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        final Object obj= getIntent().getSerializableExtra("Detailed");

        if (obj instanceof NewProductModel){
            newProductModel=(NewProductModel) obj;
        }else if (obj instanceof popularProductModel){
            popularProductModels=(popularProductModel) obj;
        }else if (obj instanceof ShowAllModel){
            showAllModel=(ShowAllModel) obj;
        }

        quantity=findViewById(R.id.quantity);

        detail_img=findViewById(R.id.detail_img);
        name=findViewById(R.id.detail_name);
        rating=findViewById(R.id.rating);
        description=findViewById(R.id.detail_descrip);
        price=findViewById(R.id.detail_price);

        addToCart=findViewById(R.id.add_to_cart);
        buyNow=findViewById(R.id.buy_now);

        addItems=findViewById(R.id.add_items);
        removeItems=findViewById(R.id.remove_items);

        // new product

        if (newProductModel !=null){
            Glide.with(getApplicationContext()).load(newProductModel.getImg_url()).into(detail_img);
            name.setText(newProductModel.getName());
            rating.setText(newProductModel.getRating());
            description.setText(newProductModel.getDescription());
            price.setText(String.valueOf(newProductModel.getPrice()));
            name.setText(newProductModel.getName());

            totalPrice=newProductModel.getPrice()*totalQnty;
        }

        if (popularProductModels !=null){
            Glide.with(getApplicationContext()).load(popularProductModels.getImg_url()).into(detail_img);
            name.setText(popularProductModels.getName());
            rating.setText(popularProductModels.getRating());
            description.setText(popularProductModels.getDescription());
            price.setText(String.valueOf(popularProductModels.getPrice()));
            name.setText(popularProductModels.getName());

            totalPrice=popularProductModels.getPrice()*totalQnty;
        }

        if (showAllModel !=null){
            Glide.with(getApplicationContext()).load(showAllModel.getImg_url()).into(detail_img);
            name.setText(showAllModel.getName());
            rating.setText(showAllModel.getRating());
            description.setText(showAllModel.getDescription());
            price.setText(String.valueOf(showAllModel.getPrice()));
            name.setText(showAllModel.getName());

            totalPrice=showAllModel.getPrice()*totalQnty;
        }
        // Buy Now
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DetailActivity.this,AddressActivity.class);
                if (newProductModel!=null){
                    intent.putExtra("item",newProductModel);
                }
                if (popularProductModels!=null){
                    intent.putExtra("item",popularProductModels);
                }
                if (showAllModel!=null){
                    intent.putExtra("item",showAllModel);
                }
                startActivity(intent);
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtoCart();
            }

        });

        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQnty<=5){
                    totalQnty++;
                    quantity.setText(String.valueOf(totalQnty));

                    if (newProductModel !=null){
                        totalPrice=newProductModel.getPrice()*totalQnty;
                    }
                    if (popularProductModels !=null){
                        totalPrice=popularProductModels.getPrice()*totalQnty;
                    }
                    if (showAllModel !=null){
                        totalPrice=showAllModel.getPrice()*totalQnty;
                    }
                }
            }
        });
        removeItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQnty>1){
                    totalQnty--;
                    quantity.setText(String.valueOf(totalQnty));
                }
            }
        });
    }
    public void addtoCart() {

        String saveCurrentTime,saveCurrentDate;

        Calendar calForDate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate=currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calForDate.getTime());

        final HashMap<String,Object> cartMap=new HashMap<>();

        cartMap.put("productName",name.getText().toString().trim());
        cartMap.put("productPrice",price.getText().toString().trim());
        cartMap.put("currentTime",saveCurrentTime);
        cartMap.put("currentDate",saveCurrentDate);
        cartMap.put("TotalPrice",totalPrice);
        cartMap.put("totalQuantity",quantity.getText().toString());

            firestore.collection("AddToCart").add(cartMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(DetailActivity.this, "added to cart", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DetailActivity.this, "fail to add", Toast.LENGTH_SHORT).show();
                }
            });

//        Iterator myVeryOwnIterator = cartMap.keySet().iterator();
//        while(myVeryOwnIterator.hasNext()) {
//            String key=(String)myVeryOwnIterator.next();
//            String value=(String)cartMap.get(key);
//            Toast.makeText(this, "Key: "+key+" Value: "+value, Toast.LENGTH_LONG).show();
//        }

//        firestore.collection("AddToCart").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentReference> task) {
//                Toast.makeText(DetailActivity.this, "Added to a cart", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        });

    }
}