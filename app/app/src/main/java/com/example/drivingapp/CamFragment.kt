package com.example.app
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.content.Context
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.app.databinding.FragmentCamBinding
import com.example.app.databinding.FragmentProfileBinding
import com.example.drivingapp.LogInFragment
import com.example.drivingapp.MyApplication
import com.example.drivingapp.RegisterFragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.bson.Document
import org.json.JSONObject
import okhttp3.Request
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

import android.util.Base64
import androidx.camera.core.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Date

class CamFragment:Fragment(R.layout.fragment_cam) {
    lateinit var myApplication: MyApplication
    private lateinit var binding: FragmentCamBinding
    private lateinit var photoFile: File
    private lateinit var photoPath: String
    private lateinit var uri: Uri
    private var imageData: ByteArray? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myApplication = requireContext().applicationContext as MyApplication
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCamBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        captureImage()
    }

    private fun captureImage()
    {
        photoFile = createImageFile()
        try {
                uri = FileProvider.getUriForFile(requireContext(), "com.example.drivingapp.fileprovider", photoFile)
        } catch (e: java.lang.Exception){
        }
        getcameraImage.launch(uri)
    }
    private val getcameraImage = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if(success){
            GlobalScope.launch {
            try {
            createImageData(uri)
            val res=sendDataToPythonServer(Base64.encodeToString(photoFile.readBytes(), Base64.DEFAULT).replace("\n", ""))
            handleServerResult(res)
            } catch (e: Exception){
                    println("error:$e")
                }
            }
        }

    }
    private fun createImageData(uri: Uri){
        val inputStream = context?.contentResolver?.openInputStream(uri)
        inputStream?.buffered()?.use{
            imageData = it.readBytes()
        }
    }
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDirectory= context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG${timeStamp}_",
            ".jpg",
            storageDirectory
        ).apply {
            photoPath = absolutePath
        }
    }

 private fun goToScan(){
     val scanFragment = ScanFragment()
     val fragmentManager = requireActivity().supportFragmentManager
     fragmentManager.beginTransaction()
         .replace(R.id.nav_host_fragment, scanFragment)
         .addToBackStack(null)
         .commit()
 }



    private suspend fun sendDataToPythonServer(base64String: String): String {
        val json = JSONObject()
        json.put("image", base64String)
        val requestBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json.toString())
        val request: Request = Request.Builder()
            .url("http://IP:5000/predict")
            .post(requestBody)
            .build()

        val client = OkHttpClient()
        val response = client.newCall(request).execute()
        return response.body?.string() ?: ""
    }

    private fun handleServerResult(result: String) {
        if (result.contains("nik")) {
            myApplication.login("Nik","")
            goToScan()
        } else if (result.contains("njc")) {
            myApplication.login("Nejc","")
            goToScan()
        } else if(result.contains("mrt")) {
            myApplication.login("Martin","")
            goToScan()
        } else
            println("ni usera")
    }
}