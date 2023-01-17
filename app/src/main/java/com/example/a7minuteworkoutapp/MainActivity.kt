package com.example.a7minuteworkoutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import com.example.a7minuteworkoutapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding : ActivityMainBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

       /* val flStartButton : FrameLayout = findViewById(R.id.fLayout)
        flStartButton.setOnClickListener{
            Toast.makeText(this,
                "Starting the Exercise",
                Toast.LENGTH_SHORT).show()
        }*/

        binding?.fLayout?.setOnClickListener{
           /* Toast.makeText(this,
                "START THE EXERCISE!!! ",
                Toast.LENGTH_SHORT).show() */

            // On clicking start button we will move to the next page ( Exercise-Activity )
            val intent= Intent(this,ExerciseActivity::class.java)
            startActivity(intent)

        }

        binding?.flBMI?.setOnClickListener{
            val intent=Intent(this,BmiActivity::class.java)
            startActivity(intent)
        }

        // START: LEFT ,  END: RIGHT



    }

    // To avoid memory leakage
    // We are assigning null to the viewBinding object (binding)
    override fun onDestroy() {
        super.onDestroy()

        binding=null
    }


}