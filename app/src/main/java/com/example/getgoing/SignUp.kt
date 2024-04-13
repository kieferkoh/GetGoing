package com.example.getgoing


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SignUp : AppCompatActivity() {

    private lateinit var edtPhone: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtName: EditText
    private lateinit var edtConfirmPass: EditText
    private lateinit var btnSignUp: Button
    private lateinit var btnBack: ImageButton
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()
        edtPhone = findViewById(R.id.edt_phone)
        edtPassword = findViewById(R.id.edt_password)
        edtConfirmPass = findViewById(R.id.edt_confirm_pass)
        edtName = findViewById(R.id.edt_name)
        btnBack = findViewById(R.id.backToLoginPage)
        btnSignUp = findViewById(R.id.edt_btnSignUp)


        btnBack.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            finish()
            startActivity(intent)
        }
        btnSignUp.setOnClickListener {
            val phone = edtPhone.text.toString()
            val password = edtPassword.text.toString()
            val confirm = edtConfirmPass.text.toString()
            val name = edtName.text.toString()

            if (phone.length != 8){
                Toast.makeText(this, "Please enter a valid 8 digit phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
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
        CoroutineScope(Dispatchers.Main).launch {
            if (CurrentUserManager.getUserByPhone(phone) == null) {
                addUserToDatabase(name, phone, password)
                val intent = Intent(this@SignUp, MainScreen::class.java)
                finish()
                startActivity(intent)
            }
            else {
                Toast.makeText(this@SignUp, "Phone number in use", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addUserToDatabase(name: String, phone: String, password: String){
        val newUser = User(name,phone,password)
        CurrentUserManager.currentUser = newUser
        mDbRef.child("User").child(phone).setValue(newUser)
    }




}

