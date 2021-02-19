package com.example.lab3am

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class GameViewModel: ViewModel() {
    var roomName = ""
    var role = ""
    var second_role =""
    lateinit var user: FirebaseUser
    lateinit var database : FirebaseDatabase
    lateinit var messageRef : DatabaseReference
    lateinit var btnSendWord: Button
    lateinit var guessWord : String
    lateinit var btnSetnum: Button
    lateinit var imageReference : StorageReference
    public fun GameViewModelConstr(
        user : FirebaseUser?,
        roomName: String,
        role: String,
        second_role: String,
        database : FirebaseDatabase,
        messageRef : DatabaseReference,
        btnSendWord: Button,
        guessWord : String,
        btnSetnum: Button,
    ) {
        if (user != null) {
            this.user = user
        }
        this.role = role
        this.second_role = second_role
        this.database = database
        this.roomName = roomName
        this.messageRef = messageRef
        this.btnSendWord = btnSendWord
        this.guessWord = guessWord
        this.btnSetnum = btnSetnum
    }


    public fun setupUI(){
        database = FirebaseDatabase.getInstance()
    }

    public fun setNum(num:String, context: Context){
        if(num.length==4){
            for(i in 0..2){
                for(j in i+1..3){
                    if(num[i]==num[j]){
                        Toast.makeText(context,"No similar nums",Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            }
            messageRef = database.getReference("rooms/" + roomName + "/" + role + "num")
            messageRef.setValue(num)
            btnSetnum.visibility = View.INVISIBLE
        }
        else{
            Toast.makeText(context,"Length must be 4, not" + num.length,Toast.LENGTH_SHORT).show()
        }
    }


    public fun checkNum(guessword : String, word:String): String {
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

    public fun onStart(context: Context){
        messageRef = database.getReference("rooms/" + roomName + "/newword")
        messageRef.setValue("hello")
        messageRef = database.getReference("rooms/"+roomName+"/message")
        var message = role+":Poked!"
        messageRef.setValue(message)

        addTurnEventListener(context)
        addEnemyNumListener(context)
        addResultListener(context)
    }

   public fun sendNum(word : String, context: Context){
        var checkedWord = checkNum(guessWord, word)
        if(checkedWord=="win"){
            var messageNumRef = database.getReference("rooms/" + roomName + "/result")
            messageNumRef.setValue(role+":win")
            Toast.makeText(context, "Enjoy your victory", Toast.LENGTH_SHORT).show()
            ModelSetStatistic("win")
        }
        else {
            Toast.makeText(context, checkedWord, Toast.LENGTH_SHORT).show()
        }
    }

   public fun ModelSetStatistic(result:String){
        var messageNumRef = database.getReference("users/" + user?.displayName)
        messageNumRef.setValue(result)
        Thread.sleep(1_000)

    }

    public fun addTurnEventListener(context: Context){
        var messageWordRef = database.getReference("rooms/"+roomName+"/turn")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var newword = dataSnapshot.value.toString()
                btnSendWord.isEnabled = newword != role
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context,"Fail in event listener", Toast.LENGTH_SHORT).show()
            }
        }
        messageWordRef.addValueEventListener(postListener)
    }

    public fun addEnemyNumListener(context: Context){
        var messageNumRef = database.getReference("rooms/" + roomName + "/" + second_role + "num")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                guessWord = dataSnapshot.value.toString()
                btnSendWord.visibility = View.VISIBLE
                Toast.makeText(context, "get enemy word", Toast.LENGTH_SHORT).show()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context,"Fail in event listener", Toast.LENGTH_SHORT).show()
            }
        }
        messageNumRef.addValueEventListener(postListener)
    }

    public fun addResultListener(context: Context){
        var messageNumRef = database.getReference("rooms/" + roomName + "/result")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var result = dataSnapshot.value.toString()
                if(result==second_role+":win") {
                    Toast.makeText(context, "Enjoy your lose", Toast.LENGTH_SHORT).show()
                    ModelSetStatistic("lose")

                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context,"Fail in event listener", Toast.LENGTH_SHORT).show()
            }
        }
        messageNumRef.addValueEventListener(postListener)
    }

}
