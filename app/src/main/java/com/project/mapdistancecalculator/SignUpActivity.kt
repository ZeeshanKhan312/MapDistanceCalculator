package com.project.mapdistancecalculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    lateinit var name:EditText
    lateinit var email:EditText
    lateinit var pasw:EditText
    lateinit var signUpBtn:AppCompatButton
    lateinit var loginPage:TextView

    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        name=findViewById(R.id.etSignUpName)
        email=findViewById(R.id.etSignUpEmail)
        pasw=findViewById(R.id.etSignUpPassword)
        signUpBtn=findViewById(R.id.btnSignUp)
        loginPage=findViewById(R.id.tvLoginPage)
        auth= Firebase.auth

        loginPage.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
            finish()
        })

        signUpBtn.setOnClickListener(View.OnClickListener {
            registerUser()
        })

    }

    private fun registerUser(){
        val nameS=name.text.toString()
        val emailS=email.text.toString()
        val password=pasw.text.toString()

        if(nameS.isNotEmpty() && emailS.isNotEmpty() && password.isNotEmpty() ){
            if(!Patterns.EMAIL_ADDRESS.matcher(emailS).matches())
                Toast.makeText(this, "Enter a valid email address!!", Toast.LENGTH_SHORT).show()
            else{
                auth.createUserWithEmailAndPassword(emailS,password)
                    .addOnCompleteListener{
                        task->
                        if(task.isSuccessful){
                            Toast.makeText(this, "User registered successfully.", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@SignUpActivity,SignInActivity::class.java))
                            finish()
                        }
                        else
                            Toast.makeText(this, "User Id not crated. Try again later", Toast.LENGTH_SHORT).show()
                    }
            }

        }
        else
            Toast.makeText(this, "Please fill all the required details!!", Toast.LENGTH_SHORT).show()
    }
}