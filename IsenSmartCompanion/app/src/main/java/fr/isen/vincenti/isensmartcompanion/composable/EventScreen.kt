package fr.isen.vincenti.isensmartcompanion.composable

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.isen.vincenti.isensmartcompanion.Event
import fr.isen.vincenti.isensmartcompanion.EventDetailActivity
import fr.isen.vincenti.isensmartcompanion.api.RetrofitInstance.eventService

@Composable
fun EventsScreen() {
    val context = LocalContext.current

    var eventList by remember { mutableStateOf<List<Event>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            eventList = eventService.getEvents()
        } catch (e: Exception) {
            Toast.makeText(context, "Error fetching events: ${e.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(eventList) { event ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF800020)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = event.title,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White
                    )
                    HorizontalDivider(color = Color.White)
                    Button(
                        onClick = {
                            val intent = Intent(context, EventDetailActivity::class.java).apply {
                                putExtra("event", event)
                            }
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF800020)
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .height(35.dp)
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(0.9f)
                    ) {
                        Text(text = "View Details", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}