package com.example.app

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.mongodb.MongoException
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.bson.Document

    fun login(username: String, password: String) {
            var mongoClient: MongoClient? = null
            try {
                mongoClient = MongoClients.create("mongodb://192.168.0.105:27017")
                // println("A!")
                val database = mongoClient.getDatabase("vaja4")
                // println("B!")
                val collection: MongoCollection<Document> = database.getCollection("users")
                println("C!")
                println(collection.find(Filters.eq("username", "tt")).first())
                println(collection.find())
                // if(BCrypt.checkpw(()))
                //   println(collection.find().toList())
                //val doc: Document = collection.find(eq("title", "Back to the Future")).first()
                println("Kotlin is now connected to MongoDB!")
            } catch (e: MongoException) {
                e.printStackTrace()
            } finally {
                mongoClient!!.close()

            }
        }


