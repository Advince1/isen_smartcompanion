package fr.isen.vincenti.isensmartcompanion

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import fr.isen.vincenti.isensmartcompanion.RetrofitInstance.eventService
import fr.isen.vincenti.isensmartcompanion.gemini.GeminiService
import fr.isen.vincenti.isensmartcompanion.ui.theme.IsenSmartCompanionTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            IsenSmartCompanionTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavigationBar(navController) }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavigationGraph(navController)
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = navController.currentDestination?.route == "home",
            onClick = { navController.navigate("home") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Events") },
            label = { Text("Events") },
            selected = navController.currentDestination?.route == "events",
            onClick = { navController.navigate("events") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "History") },
            label = { Text("History") },
            selected = navController.currentDestination?.route == "history",
            onClick = { navController.navigate("history") }
        )
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "home") {
        composable("home") { MainScreen() }
        composable("events") { EventsScreen() }
        composable("history") { HistoryScreen() }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    var input by remember { mutableStateOf("") }
    var savedinput by remember { mutableStateOf("") }
    var analyzedResponse by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var GemList by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = stringResource(id = R.string.logo_desc),
        )
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(GemList){ (question,answer) ->
                Column{
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 40.dp, end = 10.dp, top = 10.dp, bottom = 10.dp)
                            .background(Color(0xFFF3F3F3), shape = RoundedCornerShape(3.dp))
                    ){
                        Text(
                            text = question,
                            modifier = Modifier
                                .padding(10.dp)
                        )
                    }
                    Box(

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 40.dp, top = 10.dp, bottom = 10.dp)
                            .background(Color.LightGray, shape = RoundedCornerShape(3.dp))
                    ){
                        Text(
                            text = answer,
                            modifier = Modifier
                                .padding(10.dp)
                        )
                    }
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(bottom = 10.dp, top = 10.dp)
                .background(Color(0xFFDCDCDC), shape = RoundedCornerShape(50))
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f),
                value = input,
                singleLine = true,
                onValueChange = { input = it },
                placeholder = { Text(text = stringResource(id = R.string.main_placeholder)) },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )

            )
            IconButton(
                onClick = {
                    if (input.isNotEmpty()) {
                        coroutineScope.launch {
                            savedinput = input
                            analyzedResponse = GeminiService.generateResponse(input)
                            input = ""
                            GemList = GemList + listOf(Pair(savedinput, analyzedResponse))
                        }
                    } else {
                        Toast.makeText(context, "Please enter text", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .background(Color(0xFF800020), shape = RoundedCornerShape(50))
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Send",
                    tint = Color.White
                )
            }
        }
    }
}

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

@Composable
fun HistoryScreen() {

}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    IsenSmartCompanionTheme {
        MainScreen()
    }
}
