package com.example.drivingapp

import android.app.Application
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.app.ScanFragment
import com.mongodb.MongoException
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.bson.Document
import org.bson.types.ObjectId
import org.json.JSONObject


open class MyApplication: Application() {
    var mongoClient: MongoClient? = null
    val database = null
    open lateinit var  user: Document
    var loggedIn = false
    override fun onCreate() {
        super.onCreate()
        user=Document()
        loggedIn=false
        println("myapplication has been made")
    }
    fun test()
    {
        println("test")
    }
    fun connectToDatabase()
    {
        println("connect to database has been called")
    }


    fun login(username: String, password: String): Int {
        var mongoClient: MongoClient? = null
        try {
            mongoClient = MongoClients.create("mongodb://192.168.0.117:27017")
            // println("A!")
            val database = mongoClient.getDatabase("vaja4")
            // println("B!")
            val collection: MongoCollection<Document> = database.getCollection("users")
            println("C!")
            try {


                val test = collection.find(Filters.eq("username", username)).first()
                val databasePassword = test.getString("password")
                println(databasePassword)

                print(test)
                //print(test[password])
                //   println(collection.find(Filters.eq("username", "tt")).first())
                //  println(collection.find())
                //   println(collection.find().toList())
                //val doc: Document = collection.find(eq("title", "Back to the Future")).first()
                println(
                    BCrypt.verifyer()
                        .verify(password.toCharArray(), databasePassword.toCharArray())
                )
                val checkIfRightPassword = BCrypt.verifyer()
                    .verify(password.toCharArray(), databasePassword.toCharArray())
                if (checkIfRightPassword.verified) {
                    println("it works")
                    if (test != null) {
                        user=test
                    }
                    loggedIn=true
                    return 0
                } else {
                    println("wrong password")
                    return 1
                }
            }
            catch (e: java.lang.NullPointerException)
            {
                e.printStackTrace()
                return 1
            }
           // println("Kotlin is now connected to MongoDB!")
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
            mongoClient = MongoClients.create("mongodb://192.168.0.117:27017")
            val database = mongoClient.getDatabase("vaja4")
            val collection: MongoCollection<Document> = database.getCollection("users")
            try {
                val test = collection.find(Filters.eq("username", username)).first()
                println(test.toList())
                println("name exists")
                return 1
            }
            catch (e: java.lang.NullPointerException)
            {
               val codedPassword= BCrypt.withDefaults().hashToString(12, password.toCharArray());
                collection.insertOne(Document("username",username).append("password",codedPassword).append("email",email).append("_id", ObjectId()) )
                  println("user should have been made")
                val test = collection.find(Filters.eq("username", username)).first()
                user=test
                loggedIn=true
                return 0
                e.printStackTrace()
            }


            println("Kotlin is now connected to MongoDB!")
        } catch (e: MongoException) {
            e.printStackTrace()
            return 2
        } finally {
            mongoClient!!.close()

        }
        return 3
    }
    fun sendRoad (xStart: Double, yStart: Double, xEnd: Double, yEnd: Double, state: Int,)
    {
        println("got to sendroad")


        var mongoClient: MongoClient? = null
        try {
            mongoClient = MongoClients.create("mongodb://192.168.0.117:27017")
            val database = mongoClient.getDatabase("vaja4")
            val collection: MongoCollection<Document> = database.getCollection("roads")
            try {
                val username=user.getString("username")
                println("added road")
                collection.insertOne(Document("xStart",xStart).append("yStart",yStart).append("xEnd",xEnd).append("yEnd",yEnd).append("state",state).append("postedBy",username).append("_id", ObjectId()))
            }
            catch (e: java.lang.NullPointerException)
            {
                e.printStackTrace()
            }


            println("Kotlin is now connected to MongoDB!")
        } catch (e: MongoException) {
            e.printStackTrace()
        } finally {
            mongoClient!!.close()

        }


    }
}