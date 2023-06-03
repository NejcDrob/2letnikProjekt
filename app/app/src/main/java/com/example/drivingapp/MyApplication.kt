package com.example.drivingapp

import android.app.Application
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import at.favre.lib.crypto.bcrypt.BCrypt
import com.mongodb.MongoException
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import org.bson.Document

class MyApplication: Application() {
    var mongoClient: MongoClient? = null
    val database = null

    var username = null
    var loggedIn = false
    override fun onCreate() {
        super.onCreate()
        println("myapplication has been made")
    }
    fun test()
    {
        println("test")
    }
    fun connectToDatabase()
    {

    }
    fun login(username: String, password: String) {
        var mongoClient: MongoClient? = null
        try {
            mongoClient = MongoClients.create("mongodb://192.168.0.105:27017")
            // println("A!")
            val database = mongoClient.getDatabase("vaja4")
            // println("B!")
            val collection: MongoCollection<Document> = database.getCollection("users")
            println("C!")
            try {


                val test = collection.find(Filters.eq("username", username)).first()
                val databasePassword = test.getString("password")
                println(databasePassword)
                //     print(test)
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
                } else {
                    println("wrong password")
                }
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
    private fun signUp(username: String, password: String, email: String) {
        var mongoClient: MongoClient? = null
        try {
            mongoClient = MongoClients.create("mongodb://192.168.0.105:27017")
            // println("A!")
            val database = mongoClient.getDatabase("vaja4")
            // println("B!")
            val collection: MongoCollection<Document> = database.getCollection("users")
            println("C!")
            try {


                val test = collection.find(Filters.eq("username", username)).first()
                val databasePassword = test.getString("password")
                println(databasePassword)
                //     print(test)
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
                } else {
                    println("wrong password")
                }
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