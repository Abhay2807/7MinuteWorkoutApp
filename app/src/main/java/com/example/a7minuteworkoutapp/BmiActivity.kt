package com.example.a7minuteworkoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.a7minuteworkoutapp.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BmiActivity : AppCompatActivity() {

    private var binding:ActivityBmiBinding?=null
    private var currentVisibleView: String =
        METRIC_UNITS_VIEW

    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW" // Metric Unit View
        private const val US_UNITS_VIEW = "US_UNIT_VIEW" // US Unit View
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Setting up the toolbar

        setSupportActionBar(binding?.tbBmiActivity)

        if(supportActionBar!=null)
        {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title="CALCULATE BMI"
        }

        binding?.tbBmiActivity?.setNavigationOnClickListener{
            onBackPressed()
        }

        makeVisibleMetricUnitsView()

        binding?.rgBmi?.setOnCheckedChangeListener{_,checkedId:Int ->

            if(checkedId==R.id.rbMetricUnits)
            {
                makeVisibleMetricUnitsView()
            }
            else
            {
                makeVisibleUsUnitsView()
            }

        }

        binding?.flBMICalculator?.setOnClickListener{

            calculateUnits()
        }


    }

    private fun makeVisibleMetricUnitsView(){

        currentVisibleView= METRIC_UNITS_VIEW
        binding?.tilWeight?.visibility=View.VISIBLE
        binding?.tilHeight?.visibility=View.VISIBLE
        binding?.tilUsMetricUnitWeight?.visibility = View.GONE
        binding?.tilMetricUsUnitHeightFeet?.visibility = View.GONE
        binding?.tilMetricUsUnitHeightInch?.visibility = View.GONE

        binding?.etHeight?.text!!.clear()
        binding?.etWeight?.text!!.clear()

        binding?.llBmi?.visibility=View.INVISIBLE

    }

    private fun makeVisibleUsUnitsView(){

        currentVisibleView= US_UNITS_VIEW
        binding?.tilWeight?.visibility=View.INVISIBLE
        binding?.tilHeight?.visibility=View.INVISIBLE
        binding?.tilUsMetricUnitWeight?.visibility = View.VISIBLE
        binding?.tilMetricUsUnitHeightFeet?.visibility = View.VISIBLE
        binding?.tilMetricUsUnitHeightInch?.visibility = View.VISIBLE

        binding?.etUsMetricUnitWeight?.text!!.clear() // weight value is cleared.
        binding?.etUsMetricUnitHeightFeet?.text!!.clear() // height feet value is cleared.
        binding?.etUsMetricUnitHeightInch?.text!!.clear() // height inch is cleared.

        binding?.llBmi?.visibility = View.INVISIBLE

    }

    private fun displayBMIResult(bmi:Float){

        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (java.lang.Float.compare(bmi, 25f) > 0 && java.lang.Float.compare(
                bmi,
                30f
            ) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        binding?.llBmi?.visibility= View.VISIBLE

        val bmiRoundoffValue=BigDecimal(bmi.toDouble())
            .setScale(2,RoundingMode.HALF_EVEN).toString()

        binding?.tvBMIValue?.text=bmiRoundoffValue
        binding?.tvBMIType?.text=bmiLabel
        binding?.tvBMIDescription?.text=bmiDescription

    }

    private fun validateValues():Boolean{

        var isValid:Boolean=true

        if(binding?.etHeight?.text.toString().isEmpty())
        {
            isValid=false
        }
        else if(binding?.etWeight?.text.toString().isEmpty()){
            isValid=false
        }
        return isValid
    }

    private fun validateUsUnits(): Boolean {
        var isValid = true

        when {
            binding?.etUsMetricUnitWeight?.text.toString().isEmpty() -> {
                isValid = false
            }
            binding?.etUsMetricUnitHeightFeet?.text.toString().isEmpty() -> {
                isValid = false
            }
            binding?.etUsMetricUnitHeightInch?.text.toString().isEmpty() -> {
                isValid = false
            }
        }

        return isValid
    }

    private fun calculateUnits() {

        if (currentVisibleView == METRIC_UNITS_VIEW) {

            if(validateValues()){
                var heightVal:Float=
                    binding?.etHeight?.text.toString().toFloat()/100 // to get in m
                var weightVal:Float=
                    binding?.etWeight?.text.toString().toFloat()

                val bmi:Float=weightVal/(heightVal*heightVal) // in Float

                displayBMIResult(bmi)

            }
            else
            {
                Toast.makeText(
                    this@BmiActivity,
                    "Please enter valid Height and Weight Values!! ",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        else
        {

            if (validateUsUnits()) {

                val usUnitHeightValueFeet: String =
                    binding?.etUsMetricUnitHeightFeet?.text.toString() // Height Feet value entered in EditText component.
                val usUnitHeightValueInch: String =
                    binding?.etUsMetricUnitHeightInch?.text.toString() // Height Inch value entered in EditText component.
                val usUnitWeightValue: Float = binding?.etUsMetricUnitWeight?.text.toString()
                    .toFloat() // Weight value entered in EditText component.

                // Here the Height Feet and Inch values are merged and multiplied by 12 for converting it to inches.
                val heightValue =
                    usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12

                // This is the Formula for US UNITS result.

                val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))

                displayBMIResult(bmi)// Displaying the result into UI
            } else {
                Toast.makeText(
                    this@BmiActivity,
                    "Please enter valid Height and Weight Values!!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        }
    }

}