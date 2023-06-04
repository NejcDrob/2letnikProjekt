package com.example.app
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
class CamFragment:Fragment(R.layout.fragment_cam) {
    lateinit var myApplication: MyApplication
    private lateinit var binding: FragmentCamBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myApplication = requireContext().applicationContext as MyApplication    }

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



    }

 private fun goToScan(){
     val scanFragment = ScanFragment()
     val fragmentManager = requireActivity().supportFragmentManager
     fragmentManager.beginTransaction()
         .replace(R.id.nav_host_fragment, scanFragment)
         .addToBackStack(null)
         .commit()
 }

    private fun serverResult(result: String) {
        val username = myApplication.user.getString("username")
        println("Before ups")
        if (result.contains("Nejc")) {
            if (username.equals("nejc"))
                goToScan()
            else
                println("ups")
        } else if (result.contains("Nik")) {
            if (username.equals("nik"))
                goToScan()
            else
                println("ups")
        } else if (result.contains("Martin")) {
            if (username.equals("martin"))
                goToScan()
            else
                println("ups")
        } else
            println("ups")
    }
    private suspend fun sendData(base64String: String): String {
        val json = JSONObject()
        json.put("image", base64String)
        val requestBody = RequestBody.create("faceID/json; charset=utf-8".toMediaTypeOrNull(), json.toString())
        val request: Request = Request.Builder()
            .url("http://ip:5000/predict")
            .post(requestBody)
            .build()

        val client = OkHttpClient()
        val response = client.newCall(request).execute()

        return response.body?.string() ?: ""
    }

}