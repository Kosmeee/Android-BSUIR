package com.example.lab3am

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.io.File

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
        messageRef = database.getReference("rooms/" + roomName + "/newword")
        messageRef.setValue("hello")
        Toast.makeText(applicationContext,role,Toast.LENGTH_SHORT).show()
        messageRef = database.getReference("rooms/"+roomName+"/message")
        message = role+":Poked!"
        messageRef.setValue(message)
        addTurnEventListener()
        addEnemyNumListener()
        addResultListener()
    }

    fun checkNum(guessword : String, word:String): String {
        var moul = 0
        var cow = 0
        for (i in 0..3) {
            if(guessword[i] == word[i]){
                moul++
            }
        }
        for(i in 0..2){
            for(j in i+1..3){
                if(guessword[i]==word[j]){
                    cow++
                }
            }
        }
        if(moul == 4){
            return "win"
        }
        return "cows: "+cow + " mouls: "+ moul
    }

    fun setupUI(){
        text = findViewById(R.id.textStat)
        editTextWord = findViewById(R.id.editTextNumber)
        btnSetnum = findViewById(R.id.btnSetnum)
        btnSendWord = findViewById(R.id.btnSendWord)
        btnSendWord.setOnClickListener {
            sendNum(editTextWord.text.toString())
        }
        btnSendWord.visibility = View.INVISIBLE
        btnSetnum.setOnClickListener {
            setNum(editTextWord.text.toString())
        }
    }

    fun setNum(num:String){
        if(num.length==4){
            for(i in 0..2){
                for(j in i+1..3){
                    if(num[i]==num[j]){
                        Toast.makeText(applicationContext,"No similar nums",Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            }
            messageRef = database.getReference("rooms/" + roomName + "/" + role + "num")
            messageRef.setValue(num)
            btnSetnum.visibility = View.INVISIBLE
        }
        else{
            Toast.makeText(applicationContext,"Length must be 4, not" + num.length,Toast.LENGTH_SHORT).show()
        }
    }

    fun sendNum(word : String){
        var checkedWord = checkNum(guessWord, word)
        if(checkedWord=="win"){
            var messageNumRef = database.getReference("rooms/" + roomName + "/result")
            messageNumRef.setValue(role+":win")
            Toast.makeText(applicationContext, "Enjoy your victory", Toast.LENGTH_SHORT).show()
            setStastic("win")
        }
        else {
            Toast.makeText(applicationContext, checkedWord, Toast.LENGTH_SHORT).show()
        }
    }

    fun setStastic(result:String){
        var messageNumRef = database.getReference("users/" + user?.displayName)
        messageNumRef.setValue(result)
        Thread.sleep(1_000)
        var intent = Intent(applicationContext, ServersActivity::class.java)
        startActivity(intent)

    }

    fun addTurnEventListener(){
        var messageWordRef = database.getReference("rooms/"+roomName+"/turn")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var newword = dataSnapshot.value.toString()
                btnSendWord.isEnabled = newword != role
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText( applicationContext,"Fail in event listener", Toast.LENGTH_SHORT).show()
            }
        }
        messageWordRef.addValueEventListener(postListener)
    }

    fun addEnemyNumListener(){
        var messageNumRef = database.getReference("rooms/" + roomName + "/" + second_role + "num")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                guessWord = dataSnapshot.value.toString()
                btnSendWord.visibility = View.VISIBLE
                Toast.makeText(applicationContext, "get enemy word", Toast.LENGTH_SHORT).show()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText( applicationContext,"Fail in event listener", Toast.LENGTH_SHORT).show()
            }
        }
        messageNumRef.addValueEventListener(postListener)
    }

    fun addResultListener(){
        var messageNumRef = database.getReference("rooms/" + roomName + "/result")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var result = dataSnapshot.value.toString()
                if(result==second_role+":win") {
                    Toast.makeText(applicationContext, "Enjoy your lose", Toast.LENGTH_SHORT).show()
                    setStastic("lose")

                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText( applicationContext,"Fail in event listener", Toast.LENGTH_SHORT).show()
            }
        }
        messageNumRef.addValueEventListener(postListener)
    }
}

