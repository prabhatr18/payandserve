package com.digital.payandserve.views.on_boarding;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.digital.payandserve.databinding.ActivityBcStatusBinding;


public class BcStatusActivity extends AppCompatActivity {

    ActivityBcStatusBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBcStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener(v -> {
            finish();
        });


    }
}