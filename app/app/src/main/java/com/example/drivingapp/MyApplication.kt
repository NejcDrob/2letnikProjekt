package com.example.drivingapp

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import at.favre.lib.crypto.bcrypt.BCrypt
import com.mongodb.MongoException
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import org.bson.Document
import org.bson.types.ObjectId
import java.time.LocalDateTime
import java.time.LocalTime


open class MyApplication: Application() {
    var mongoClient: MongoClient? = null
    val database = null
    open lateinit var  user: Document
    var loggedIn = false
    var mongoDBIP= "mongodb://164.8.162.186:27017"

    override fun onCreate() {
        super.onCreate()
        user=Document()
        loggedIn=false
    }


    fun login(username: String, password: String): Int {
        var mongoClient: MongoClient? = null
        try {
            println("got to login")
            mongoClient = MongoClients.create(mongoDBIP)
            val database = mongoClient.getDatabase("vaja4")
            val collection: MongoCollection<Document> = database.getCollection("users")
            try {


                val test = collection.find(Filters.eq("username", username)).first()
                val databasePassword = test.getString("password")

                val checkIfRightPassword = BCrypt.verifyer()
                    .verify(password.toCharArray(), databasePassword.toCharArray())
                if (checkIfRightPassword.verified) {
                    if (test != null) {
                        user=test
                    }
                    loggedIn=true
                    return 0
                } else {
                    return 1
                }
            }
            catch (e: java.lang.NullPointerException)
            {
                e.printStackTrace()
                return 1
            }
        } catch (e: MongoException) {
            e.printStackTrace()
            return 3
        } finally {
            mongoClient!!.close()

        }
    }
    fun signUp(username: String, password: String, email: String): Int {
        var mongoClient: MongoClient? = null
        try {
            mongoClient = MongoClients.create(mongoDBIP)
            val database = mongoClient.getDatabase("vaja4")
            val collection: MongoCollection<Document> = database.getCollection("users")
            try {
                val test = collection.find(Filters.eq("username", username)).first()
                return 1
            }
            catch (e: java.lang.NullPointerException)
            {
               val codedPassword= BCrypt.withDefaults().hashToString(12, password.toCharArray());
                collection.insertOne(Document("username",username).append("password",codedPassword).append("email",email).append("_id", ObjectId()) )
                val test = collection.find(Filters.eq("username", username)).first()
                user=test
                loggedIn=true
                return 0
                e.printStackTrace()
            }


        } catch (e: MongoException) {
            e.printStackTrace()
            return 2
        } finally {
            mongoClient!!.close()

        }
        return 3
    }
    public fun sendRoad (xStart: Double, yStart: Double, xEnd: Double, yEnd: Double, state: Int,)
    {


        var mongoClient: MongoClient? = null
        try {
            mongoClient = MongoClients.create(mongoDBIP)
            val database = mongoClient.getDatabase("vaja4")
            val collection: MongoCollection<Document> = database.getCollection("roads")
            try {
                val username=user.getString("username")
                collection.insertOne(Document("xStart",xStart).append("yStart",yStart).append("xEnd",xEnd).append("yEnd",yEnd).append("state",state).append("postedBy",username).append("_id", ObjectId()))
            }
            catch (e: java.lang.NullPointerException)
            {
                e.printStackTrace()
            }


        } catch (e: MongoException) {
            e.printStackTrace()
        } finally {
            mongoClient!!.close()

        }


    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun sendRawData(location: String, speed: String, accelerometer:String, score: Double)
    {
        data class SensorData(
            var location: String,
            var speed: String,
            var accelerometer: String,
            var user: String,
            var score: Double
        )
        var mongoClient: MongoClient? = null
        try {
            mongoClient = MongoClients.create(mongoDBIP)
            val database = mongoClient.getDatabase("vaja4")
            val collection: MongoCollection<Document> = database.getCollection("rawData")
            try {
                val currentTime = LocalDateTime.now()
                val username=user.getString("username")
                collection.insertOne(Document("location",location).append("speed",speed).append("accelerometer",accelerometer).append("user",username).append("score",score).append("_id", ObjectId()).append("time",currentTime))
            }
            catch (e: java.lang.NullPointerException)
            {
                e.printStackTrace()
            }


        } catch (e: MongoException) {
            e.printStackTrace()
        } finally {
            mongoClient!!.close()

        }
    }
}