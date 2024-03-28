package com.example.getgoing


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.ActionCodeEmailInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignUp : AppCompatActivity() {

    private lateinit var edtPhone: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtName: EditText
    private lateinit var edtConfirmPass: EditText
    private lateinit var btnSignUp: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()
        edtPhone = findViewById(R.id.edt_phone)
        edtPassword = findViewById(R.id.edt_password)
        edtConfirmPass = findViewById(R.id.edt_confirm_pass)
        edtName = findViewById(R.id.edt_name)

        btnSignUp = findViewById(R.id.edt_btnSignUp)

        btnSignUp.setOnClickListener {
            val phone = edtPhone.text.toString()
            val password = edtPassword.text.toString()
            val confirm = edtConfirmPass.text.toString()
            val name = edtName.text.toString()

            if (phone.isEmpty() || name.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirm) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signup(name, phone, password)
        }

    }

    private fun signup(name: String, phone: String, password: String) {

        // Get a reference to the "User" node in the database
        mDbRef = FirebaseDatabase.getInstance().getReference()

        // Check if the phone number already exists in the database
        mDbRef.child("User").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var exist = 0
                for(postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)
                    if(phone == currentUser?.phone){
                        exist = 1
                    }
                }
                if (exist==1){
                    //Toast.makeText(this@SignUp, "User Exists", Toast.LENGTH_SHORT).show()
                    }
                else {

                    addUserToDatabase(name, phone, password)
                    val intent = Intent(this@SignUp, MainScreen::class.java)
                    finish()
                    startActivity(intent)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun addUserToDatabase(name: String, phone: String, password: String){
        val newUser = User(name,phone,password)
        CurrentUserManager.setCurrentUser(newUser)
        mDbRef.child("User").child(phone).setValue(newUser)
    }




}

