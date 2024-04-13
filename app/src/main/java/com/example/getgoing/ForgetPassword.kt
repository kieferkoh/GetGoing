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


class ForgetPassword : AppCompatActivity() {

    private lateinit var edtPhone: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtName: EditText
    private lateinit var edtConfirmPass: EditText
    private lateinit var btnDone: Button
    private lateinit var btnBack: ImageButton
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forget_password)
        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()
        edtPhone = findViewById(R.id.edt_phone)
        edtPassword = findViewById(R.id.edt_password)
        edtConfirmPass = findViewById(R.id.edt_confirm_pass)
        edtName = findViewById(R.id.edt_name)
        btnBack = findViewById(R.id.backToLoginPage)
        btnDone = findViewById(R.id.edt_btnDone)

        btnBack.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            finish()
            startActivity(intent)
        }

        btnDone.setOnClickListener {
            val phone = edtPhone.text.toString()
            val password = edtPassword.text.toString()
            val confirm = edtConfirmPass.text.toString()
            val name = edtName.text.toString()

            CoroutineScope(Dispatchers.Main).launch {
                if (phone.isEmpty() || name.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                    Toast.makeText(this@ForgetPassword, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                if (CurrentUserManager.getUserByPhone(phone) == null) {
                    Toast.makeText(
                        this@ForgetPassword,
                        "User does not exist",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }
                else {
                    if (CurrentUserManager.getUserByPhone(phone)?.name != name){
                        Toast.makeText(
                            this@ForgetPassword,
                            "Name does not match user",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@launch
                    }

                }
                if (password != confirm) {
                    Toast.makeText(this@ForgetPassword, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                DatabaseManager.createDataFirebase(password, mDbRef.child("User").child(phone).child("password"))
                val intent = Intent(this@ForgetPassword, LogIn::class.java)
                finish()
                startActivity(intent)
            }


        }

    }
}

