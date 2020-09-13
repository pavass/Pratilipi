package com.example.pratilip

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       button_signup.setOnClickListener {
           performRegister()
       }
        textView_already_have_acc.setOnClickListener {
            val intent=Intent(this,LoginActivity::class.java)
startActivity(intent)
        }
        button_photo_select_register.setOnClickListener {
            Log.d("MainActivity","try to show")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)
        }
    }
    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==0 && resultCode == Activity.RESULT_OK  && data!=null){
            //proceed and check the selected image

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)
            //val bitmapDrawable = BitmapDrawable(bitmap)
            selectphoto_imageview_register.setImageBitmap(bitmap)
button_photo_select_register.alpha=0f
            //button_photo_select_register.alpha = 0f


//            button_photo_select_register.setBackgroundDrawable(bitmapDrawable)

        }


    }
    private fun performRegister(){
        val email=editText_email.text.toString()
        val password=editText_password.text.toString()
        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,"please enter text in email/psw",Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(!it.isSuccessful) return@addOnCompleteListener
                Log.d("Main","Successful:${it.result?.user?.uid}")
                uploadImagetoFirebaseStorage()
            }
            .addOnFailureListener {
                Log.d("Main","Failed to create user: ${it.message}")
                Toast.makeText(this,"Failed to create user: ${it.message}",Toast.LENGTH_SHORT).show()
            }

    }
    private fun uploadImagetoFirebaseStorage(){
        if(selectedPhotoUri == null) return

        val filename: String = UUID.randomUUID().toString()

        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)

            .addOnSuccessListener {

                ref.downloadUrl.addOnSuccessListener {
                    it.toString()
                    saveUserToFirebaseDatabase(it.toString())

                }

            }
    }
    private fun saveUserToFirebaseDatabase(image_url: String){

        val uid = FirebaseAuth.getInstance().uid ?:""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, editText_username.text.toString(),image_url)
        ref.setValue(user)
            .addOnSuccessListener {
                val intent = Intent(this,view_story::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

    }
}


class User(val uid:String,val username:String,val profileImageUrl:String)