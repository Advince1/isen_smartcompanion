package fr.isen.vincenti.isensmartcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.isen.vincenti.isensmartcompanion.ui.theme.IsenSmartCompanionTheme



class EventDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //val eventId = intent.getIntExtra("event_id", -1)
        //val eventTitle = intent.getStringExtra("event_title") ?: "No Title"
        //val eventDescription = intent.getStringExtra("event_description") ?: "No Description"
        //val eventDate = intent.getStringExtra("event_date") ?: "No Date"
        //val eventLocation = intent.getStringExtra("event_location") ?: "No Location"
        //val eventCategory = intent.getStringExtra("event_category") ?: "No Category"
        val event = intent.getSerializableExtra("event") as? Event
        enableEdgeToEdge()
        setContent {
            IsenSmartCompanionTheme {
                EventDetailScreen(
                    eventId = event?.id ?: "Unknown",
                    title = event?.title ?: "No Title",
                    description = event?.description ?: "No Description",
                    date = event?.date ?: "No Date",
                    location = event?.location ?: "No Location",
                    category = event?.category ?: "No Category"
                )
            }
        }
    }
}

@Composable
fun EventDetailScreen(eventId: String, title: String, description: String, date: String, location: String, category: String) {
    val activity = LocalActivity.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF800020),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = Color.Black, thickness = 3.dp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Description: $description", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Color.Black, thickness = 1.dp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Date: $date", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Location: $location", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Category: $category", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Color.Black, thickness = 1.dp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { activity?.finish() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF800020), contentColor = Color.White),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Back")
        }
    }
}
