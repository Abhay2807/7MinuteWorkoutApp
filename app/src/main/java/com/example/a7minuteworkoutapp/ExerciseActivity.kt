package com.example.a7minuteworkoutapp

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minuteworkoutapp.databinding.ActivityExerciseBinding
import com.example.a7minuteworkoutapp.databinding.CustomDialogBackpressBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var bindingEA:ActivityExerciseBinding?=null

    // restTimer : Time to rest/relax
    private var restTimer: CountDownTimer?=null
    // restProgress : How far we have come
    private var restProgress:Int=0

    private var restViewTime:Long=1000// 10 sec
    private var onTickTime:Long=1000 // 1 sec
    private var exerciseViewTime:Long=3000 // 30 sec

    private var ExerciseTimer:CountDownTimer?=null
    private var ExerciseProgress:Int=0

    private var exerciseList:ArrayList<ExerciseModel>?=null
    private var currentExercisePos:Int=-1

    private var tts: TextToSpeech? = null // Variable for TextToSpeech
    private var player:MediaPlayer?=null

    private var exrAdapter: ExerStatusAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingEA=ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(bindingEA?.root)

        setSupportActionBar(bindingEA?.tbExercise)

        if(supportActionBar!=null)
        {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        bindingEA?.tbExercise?.setNavigationOnClickListener{
            onBackPressed()
        }

        exerciseList=Constants.defaultExerciseList()

        tts= TextToSpeech(this,this)

      setupRestView()
        setupExrStatusRecyclerView()
    }

    override fun onBackPressed() {
        customDialogforBackbtn()
        //super.onBackPressed()
    }

    private fun customDialogforBackbtn(){
        val customDialog=Dialog(this)
        val dialogBinding=CustomDialogBackpressBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        customDialog.show()

        dialogBinding.btnYes.setOnClickListener{
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }

        dialogBinding.btnNo.setOnClickListener{
            customDialog.dismiss()
        }



    }

    private fun setupExrStatusRecyclerView(){
        bindingEA?.rvExrStatus?.layoutManager=
            LinearLayoutManager(this@ExerciseActivity,
            LinearLayoutManager.HORIZONTAL,false)
        exrAdapter=ExerStatusAdapter(exerciseList!!)
        bindingEA?.rvExrStatus?.adapter=exrAdapter
    }

    private fun setupRestView(){

        try{

            val soundURI=Uri.parse(
                "android.resource://com.example.a7minuteworkoutapp/" +
                        R.raw.press_start
            )

            player=MediaPlayer.create(applicationContext,soundURI)
            player?.isLooping=false
            player?.start()


        }catch(e:Exception)
        {
            e.printStackTrace()
        }


        bindingEA?.flProgressBarRestview?.visibility= View.VISIBLE
        bindingEA?.tvReady?.visibility=View.VISIBLE
        bindingEA?.tvExerciseName?.visibility=View.INVISIBLE
        bindingEA?.flProgressBarExercise?.visibility=View.INVISIBLE
        bindingEA?.ivExerciseView?.visibility=View.INVISIBLE
        bindingEA?.tvUpcomingExercise?.visibility=View.VISIBLE
        bindingEA?.tvUpcomingLabel?.visibility=View.VISIBLE

        if(restTimer!=null)
        {
            restTimer?.cancel()
            restProgress=0
        }

        bindingEA?.tvUpcomingExercise?.text=exerciseList!![currentExercisePos+1].getName()

        //speakOut("Now you can rest for ${(restViewTime/onTickTime).toInt()} seconds ")

        setRestProgressBar()
    }

    private fun setupExerciseView(){
        // flProgressBar or can use ProgressBar
        bindingEA?.flProgressBarRestview?.visibility= View.INVISIBLE
        bindingEA?.tvReady?.visibility=View.INVISIBLE
        bindingEA?.tvExerciseName?.visibility=View.VISIBLE
        bindingEA?.flProgressBarExercise?.visibility=View.VISIBLE
        bindingEA?.ivExerciseView?.visibility=View.VISIBLE
        bindingEA?.tvUpcomingExercise?.visibility=View.INVISIBLE
        bindingEA?.tvUpcomingLabel?.visibility=View.INVISIBLE

        if(ExerciseTimer!=null)
        {
            ExerciseTimer?.cancel()
            ExerciseProgress = 0
        }

        speakOut(exerciseList!![currentExercisePos].getName())

        bindingEA?.ivExerciseView?.setImageResource(
                exerciseList!![currentExercisePos].getImage())
        bindingEA?.tvExerciseName?.text=
            exerciseList!![currentExercisePos].getName()

        setExerciseProgressBar()
    }

    private fun setRestProgressBar(){
        bindingEA?.progressBar?.progress=restProgress

        restTimer=object: CountDownTimer(restViewTime,onTickTime){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                bindingEA?.progressBar?.progress=(restViewTime/onTickTime).toInt() - restProgress
                bindingEA?.tvTimer?.text=( (restViewTime/onTickTime).toInt()
                        -restProgress).toString()
            }

            override fun onFinish() {

              /* Toast.makeText(this@ExerciseActivity,
               "Starting the Exercise",
               Toast.LENGTH_SHORT).show()*/
                currentExercisePos++

                exerciseList!![currentExercisePos].setIsSelected(true)
                exrAdapter!!.notifyDataSetChanged()

                setupExerciseView()

            }

        }.start()
    }

    private fun setExerciseProgressBar(){
        bindingEA?.progressBarExercise?.progress=ExerciseProgress

        ExerciseTimer=object: CountDownTimer(exerciseViewTime,onTickTime){
            override fun onTick(millisUntilFinished: Long) {
                ExerciseProgress++
                bindingEA?.progressBarExercise?.progress=(exerciseViewTime/onTickTime)
                    .toInt()- ExerciseProgress
                bindingEA?.tvTimerExercise?.text=( (exerciseViewTime/onTickTime)
                    .toInt() -ExerciseProgress).toString()
            }

            override fun onFinish() {





                /*Toast.makeText(this@ExerciseActivity,
                    "Lets go back to the Rest view",
                    Toast.LENGTH_SHORT).show()*/

                if(currentExercisePos < exerciseList?.size!!-1)
                {
                    exerciseList!![currentExercisePos].setIsSelected(false)
                    exerciseList!![currentExercisePos].setIsCompleted(true)
                    exrAdapter!!.notifyDataSetChanged()
                    setupRestView()
                }
                else
                { /*
                    Toast.makeText(this@ExerciseActivity,
                    "Congratulations Your 7 Minutes workout is completed!!!",
                    Toast.LENGTH_SHORT).show()*/

                    finish()
                    val intent= Intent(this@ExerciseActivity,finishActivity::class.java)
                    startActivity(intent)
                }

            }

        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(restTimer!=null)
        {
            restTimer?.cancel()
            restProgress=0
        }

        if(ExerciseTimer!=null)
        {
            ExerciseTimer?.cancel()
            ExerciseProgress = 0
        }

        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
        }

        if(player!=null)
        {
            player!!.stop()
        }

        bindingEA=null
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!")
            }

        } else {
            Log.e("TTS", "Initialization Failed!")
        }
    }


    private fun speakOut(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

}