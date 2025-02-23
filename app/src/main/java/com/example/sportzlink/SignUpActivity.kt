package com.example.sportzlink

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.HomeActivity
import com.example.sportzlink.Models.User
import com.example.sportzlink.databinding.ActivitySignUpBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class SignUpActivity : AppCompatActivity() {
    // Syntax to use binding
    private val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
lateinit var user: User
//launcher variable to launch gallery of phone - for uploading image
private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
    uri->
    uri?.let{ // uri = uniform resource identifier (thing of android)

    }
}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)  // Use binding.root to set the content view
        user  = User()
        // Handle sign-up button click
        binding.signUpBtn.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill up the required details.", Toast.LENGTH_SHORT).show()
            } else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { result ->
                        if (result.isSuccessful) {
                            user.name = binding.nameEditText.text.toString()
                            user.password = binding.passwordEditText.text.toString()
                            user.email = binding.emailEditText.text.toString()
                            Firebase.firestore.collection("Users").document(Firebase.auth.currentUser!!.uid).set(user)
                                .addOnSuccessListener { startActivity(Intent(this@SignUpActivity, HomeActivity::class.java)) }
                            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, result.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        // Set up window insets listener
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
