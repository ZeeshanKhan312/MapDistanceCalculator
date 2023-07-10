package com.project.mapdistancecalculator

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

class ForgetPasswordActivity : AppCompatActivity() {
    lateinit var emailId:EditText
    lateinit var textMsg:TextView
    lateinit var errorMsg:TextView
    lateinit var submit:AppCompatButton
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        emailId=findViewById(R.id.etForgotPasswordEmail)
        submit=findViewById(R.id.btnForgotPasswordSubmit)
        textMsg=findViewById(R.id.tvSubmitMsg)
        errorMsg=findViewById(R.id.errorMsg)
        auth=Firebase.auth

        submit.setOnClickListener{
            resetPasw()
        }
    }

    private fun resetPasw(){
       val email=emailId.text.toString()
        if(email.isNotEmpty() ){
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener{
                    task->
                    if(task.isSuccessful){
                        textMsg.visibility= View.VISIBLE //to make a text view visible or invisible
                    }
                    else{
                        errorMsg.visibility=View.VISIBLE //to make a text view visible and "View.Gone"
                    }
                }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}