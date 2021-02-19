package com.example.lab3am

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class PlayActivity : AppCompatActivity() {
    var user = FirebaseAuth.getInstance().currentUser
    lateinit var editTextWord : EditText
    lateinit var text : TextView
    var playerName = user?.displayName
    var roomName = ""
    var role = ""
    var second_role =""
    var message = ""
    lateinit var database : FirebaseDatabase
    lateinit var messageRef : DatabaseReference
    lateinit var btnSendWord: Button
    lateinit var guessWord : String
    lateinit var gameStateModel : GameViewModel
    lateinit var btnSetnum:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        database = FirebaseDatabase.getInstance()
        setupUI()
        var extras = intent.extras
        if(extras!= null){
            roomName = extras.getString("roomName").toString()
            if(roomName.equals(playerName)){
                role = "host"
                second_role = "guest"
            }
            else{
                role="guest"
                second_role = "host"
            }
        }
        gameStateModel = ViewModelProviders.of(this)[GameViewModel::class.java]
        gameStateModel!!.GameViewModelConstr(user,roomName, role.toString(), second_role.toString(), database!!,messageRef,btnSendWord,guessWord,btnSetnum)
       gameStateModel.onStart()
    }


    fun setupUI(){
        text = findViewById(R.id.textStat)
        editTextWord = findViewById(R.id.editTextNumber)
        btnSetnum = findViewById(R.id.btnSetnum)
        btnSendWord = findViewById(R.id.btnSendWord)
        btnSendWord.setOnClickListener {
            gameStateModel.sendNum(editTextWord.text.toString(), applicationContext)
        }
        btnSendWord.visibility = View.INVISIBLE
        btnSetnum.setOnClickListener {
            gameStateModel.setNum(editTextWord.text.toString(),applicationContext)
        }
    }

        }




