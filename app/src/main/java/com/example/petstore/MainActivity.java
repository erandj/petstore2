package com.example.petstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView mGETTextView;
    ImageView mGETImageView;
    EditText mGETEditTextId;
    Button mGETBtn;
    TextView mGETError;

    EditText mPOSTEditTextId;
    EditText mPOSTEditTextName;
    EditText mPOSTEditTextPhotoUrl;
    Button mPOSTBtn;
    TextView mPOSTError;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getBaseContext();

        mGETTextView = (TextView) findViewById(R.id.GETTextView);
        mGETImageView = (ImageView) findViewById(R.id.GETImageView);
        mGETEditTextId = (EditText) findViewById(R.id.GETEditTextId);
        mGETBtn = (Button) findViewById(R.id.GETBtn);
        mGETError = (TextView) findViewById(R.id.GETError);

        mPOSTEditTextId = (EditText) findViewById(R.id.POSTEditTextId);
        mPOSTEditTextName = (EditText) findViewById(R.id.POSTEditTextName);
        mPOSTEditTextPhotoUrl = (EditText) findViewById(R.id.POSTEditTextPhotoUrl);
        mPOSTBtn = (Button) findViewById(R.id.POSTBtn);
        mPOSTError = (TextView) findViewById(R.id.POSTError);
    }

    public void GETOnClick(View view){
        String convertable = mGETEditTextId.getText().toString();
        if (convertable == null && !convertable.matches("[0-9.]+")){
            mGETTextView.setText("id is required");
            return;
        }

        int id = Integer.parseInt(convertable);
        PetStoreAPI petStoreAPI = PetStoreAPI.retrofit.create(PetStoreAPI.class);

        final Call<PetStore> call = petStoreAPI.getData(id);

        call.enqueue(new Callback<PetStore>() {
            @Override
            public void onResponse(Call<PetStore> call, Response<PetStore> response) {
                if (response.isSuccessful()) {
                    PetStore pet = response.body();

                    mGETTextView.setText(pet.getName());
                    Picasso.with(mContext)
                            .load(pet.getPhotoUrls().get(0))
                            .error(R.drawable.ic_launcher_foreground)
                            .into(mGETImageView);
                }else {
                    ResponseBody errorBody = response.errorBody();

                    try {
                        mGETError.setText(errorBody.string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PetStore> call, Throwable throwable) {
                mGETError.setText("Что-то пошло не так: " + throwable.getMessage());
            }
        });
    }

    public void POSTOnClick(View view){
        String convertable = mPOSTEditTextId.getText().toString();
        if (convertable == null && !convertable.matches("[0-9.]+")){
            mGETTextView.setText("id is required");
            return;
        }

        int id = Integer.parseInt(convertable);

        String name = mPOSTEditTextName.getText().toString();

        Category category = new Category();
        category.setId(0);
        category.setName("null");

        ArrayList<String> photoUrls = new ArrayList<>();
        String photoUrl = mPOSTEditTextPhotoUrl.getText().toString();
        photoUrls.add(photoUrl);

        ArrayList<Tag> tags = new ArrayList<>();
        Tag tag = new Tag();
        tag.setId(0);
        tag.setName("null");
        tags.add(tag);

        String status = "available";


        PetStore pet = new PetStore();
        pet.setId(id);
        pet.setCategory(category);
        pet.setName(name);
        pet.setPhotoUrls(photoUrls);
        pet.setTags(tags);
        pet.setStatus(status);

        PetStoreAPI petStoreAPI = PetStoreAPI.retrofit.create(PetStoreAPI.class);

        final Call<PetStore> call = petStoreAPI.postPet(pet);

        call.enqueue(new Callback<PetStore>() {
            @Override
            public void onResponse(Call<PetStore> call, Response<PetStore> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(mContext, "Pet " + response.body().getName() + " created", Toast.LENGTH_SHORT).show();

                }else {
                    ResponseBody errorBody = response.errorBody();

                    try {
                        mGETError.setText(errorBody.string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PetStore> call, Throwable throwable) {
                mGETError.setText("Что-то пошло не так: " + throwable.getMessage());
            }
        });
    }
}