package com.project.mapdistancecalculator

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.math.log

class SignInActivity : AppCompatActivity() {
    lateinit var loginBtn:AppCompatButton
    lateinit var forgetBtn:TextView
    lateinit var googleSigninBtn: CardView
    lateinit var signupBtn:TextView
    lateinit var email:EditText
    lateinit var pasw:EditText
    private lateinit var auth:FirebaseAuth
    private lateinit var googleSignin:GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        loginBtn=findViewById(R.id.btnSignIn)
        forgetBtn=findViewById(R.id.tvForgotPassword)
        googleSigninBtn=findViewById(R.id.btnSignInWithGoogle)
        signupBtn=findViewById(R.id.tvRegister)
        email=findViewById(R.id.etSignInEmail)
        pasw=findViewById(R.id.etSignInPassword)

//        Log.d("MyTag", "onCreate: "+Firebase.auth)

        auth=Firebase.auth
        val gso= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()
        googleSignin=GoogleSignIn.getClient(this,gso)

        //check whether the user is already logged in
        if(auth.currentUser!=null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        forgetBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,ForgetPasswordActivity::class.java))
        })

        signupBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        })

        loginBtn.setOnClickListener(View.OnClickListener {
            signIn()
        })

        googleSigninBtn.setOnClickListener(View.OnClickListener {
            signInWithGoogle()
        })

    }

    private fun signIn(){
        val emailS=email.text.toString()
        val password=pasw.text.toString()

        if(emailS.isNotEmpty() && password.isNotEmpty()){
            auth.signInWithEmailAndPassword(emailS, password)
                .addOnCompleteListener{
                    task->
                    if(task.isSuccessful){
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }
                    else
                        Toast.makeText(this, "User Credential In-correct!!", Toast.LENGTH_SHORT).show()
                }
        }

    }

    private fun signInWithGoogle(){
        val signinIntent= googleSignin.signInIntent
        launcher.launch(signinIntent)
    }
    private val launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result->
        if(result.resultCode== Activity.RESULT_OK){
            val task=GoogleSignIn.getSignedInAccountFromIntent(result.data)

            if(task.isSuccessful){
                val account:GoogleSignInAccount ?=task.result

                if(account!=null)
                    updateUI(account)
            }
            else{
                Toast.makeText(this, "SignIn Failed, Try again later", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(account:GoogleSignInAccount){
        val credential =GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            it->
            if(it.isSuccessful){
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
            else{
                Toast.makeText(this, "Can't login currently. Try after sometime", Toast.LENGTH_SHORT).show()
            }
        }
    }
}