package com.example.a7minuteworkoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.a7minuteworkoutapp.databinding.ActivityFinishBinding

class finishActivity : AppCompatActivity() {

    private var binding: ActivityFinishBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarFinishActivity)
        if(supportActionBar!=null)
        {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarFinishActivity?.setNavigationOnClickListener{
            onBackPressed()
        }

        binding?.btnFinish?.setOnClickListener{
            finish()
        }
    }
}