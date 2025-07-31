package dev.arvind.androidtask

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dev.arvind.androidtask.ui.theme.AndroidTaskTheme

class TestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Test Firebase Connection
        val firestore = Firebase.firestore
        val auth = Firebase.auth

        Log.d("Firebase", "onCreate: FireStore - ${firestore.app.name}")
        Log.d("Firebase", "onCreate: Auth - ${auth.app.name}")

        // Test Firebase after a short delay
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            testFirebaseConnection()
        }, 2000)

        setContent {
            AndroidTaskTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun testFirebaseConnection() {
        try {
            val firestore = Firebase.firestore

            val testData = mapOf(
                "message" to "Hello Firebase",
                "timestamp" to System.currentTimeMillis()
            )

            firestore.collection("test")
                .add(testData)
                .addOnSuccessListener { documentReference ->
                    Log.d("Firebase", "✅ SUCCESS: Document written with ID: ${documentReference.id}")
                }
                .addOnFailureListener { exception ->
                    Log.e("Firebase", "❌ FAILED: Error adding document", exception)
                }

        } catch (e: Exception) {
            Log.e("Firebase", "❌ EXCEPTION: ${e.message}", e)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
