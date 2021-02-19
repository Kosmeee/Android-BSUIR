package com.example.lab3am

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ServersActivity : AppCompatActivity() {
    lateinit var listViewOfRooms : ListView
    lateinit var btnCreateRoom : Button
    var roomList = mutableListOf<String>()
    lateinit var roomName : String
    var user = FirebaseAuth.getInstance().currentUser
    var playerName = user?.displayName
    lateinit var database : FirebaseDatabase
    lateinit var roomRef : DatabaseReference
    lateinit var roomsRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servers)
        database = FirebaseDatabase.getInstance()
        Toast.makeText(applicationContext,playerName, Toast.LENGTH_SHORT).show()
        roomName = playerName.toString()
        listViewOfRooms = findViewById(R.id.listView)
        btnCreateRoom = findViewById(R.id.createRoom)
        roomList = mutableListOf<String>()

        btnCreateRoom.setOnClickListener {
            btnCreateRoom.setText("CREATING ROOM")
            btnCreateRoom.isEnabled = false
            roomName = playerName.toString()
            roomRef = database.getReference("rooms/" + roomName + "/player1")
            addRoomEventListener()
            roomRef.setValue(playerName)
        }
        listViewOfRooms.setOnItemClickListener { parent, view, position, id ->
            roomName = roomList.get(position)
            roomRef = database.getReference("room/"+roomName+"/player2")
            addRoomEventListener()
            roomRef.setValue(playerName)
        }
        addRoomsEventListener()
    }
    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, ServersActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }
    @SuppressLint("ResourceType")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.action_settings) {
            val intent = Intent(this, AccountActivity::class.java)
            intent.putExtra("roomName", item.title)
            startActivityForResult(intent, 1)
        }
        return super.onOptionsItemSelected(item)

    }
    private fun addRoomEventListener(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                //val post = dataSnapshot.getValue<Post>()
                // ...
                btnCreateRoom.setText("CREATE ROOM")
                btnCreateRoom.isEnabled = true
                var intent = Intent(applicationContext, PlayActivity::class.java)
                intent.putExtra("roomName", roomName)
                startActivity(intent)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                btnCreateRoom.setText("CREATE ROOM")
                btnCreateRoom.isEnabled = true
                Toast.makeText(applicationContext, "Error!", Toast.LENGTH_SHORT).show()
                // ...
            }
        }
        roomRef.addValueEventListener(postListener)
    }

    private fun addRoomsEventListener(){
        roomsRef = database.getReference("rooms")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                //val post = dataSnapshot.getValue<Post>()
                // ...
                roomList.clear()
                // Toast.makeText(applicationContext, "Error in ondata!", Toast.LENGTH_SHORT).show()
                var rooms = dataSnapshot.children//tut
                for(snapshot in rooms){
                    roomList.add(snapshot.key.toString())
                    var adapter = ArrayAdapter<String>(applicationContext, android.R.layout.simple_list_item_1, roomList)//tut
                    listViewOfRooms.adapter = adapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Toast.makeText(applicationContext, "Error rooms!", Toast.LENGTH_SHORT).show()
                // Getting Post failed, log a message
                // ...
            }
        }
        roomsRef.addValueEventListener(postListener)
    }
}