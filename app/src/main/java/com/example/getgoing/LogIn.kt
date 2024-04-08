package com.example.getgoing


import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LogIn : AppCompatActivity() {

    private lateinit var edtPhone: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_log_in)

        //hiding action bar first
        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()

        edtPhone = findViewById(R.id.edt_phone)
        edtPassword = findViewById(R.id.edt_password)
        btnLogin = findViewById(R.id.btnLogin)
        btnSignUp = findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
        btnLogin.setOnClickListener {
            val phone = edtPhone.text.toString()
            val password = edtPassword.text.toString()

            login(phone, password);
        }


    }


    private fun login(phone: String, password: String) {
        // login logic
        mDbRef = FirebaseDatabase.getInstance().getReference()

        // Check if the phone number already exists in the database
        CoroutineScope(Dispatchers.Main).launch {
            if (CurrentUserManager.getUserByPhone(phone) != null) {
                // Call the suspend function within the coroutine scope
                var myUser = CurrentUserManager.getUserByPhone(phone)
                CurrentUserManager.currentUser = myUser
                if (CurrentUserManager.currentUser != null) {
                    if (password == myUser?.password) {
                        val intent = Intent(this@LogIn, MainScreen::class.java)
                        finish()
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@LogIn, "Wrong Password", Toast.LENGTH_SHORT).show()
                    }
                }

            }
            else {
                Toast.makeText(this@LogIn, "User does not exist", Toast.LENGTH_SHORT).show()
            }

        }


    }

}




