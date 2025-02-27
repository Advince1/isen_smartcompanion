package fr.isen.vincenti.isensmartcompanion.composable

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.isen.vincenti.isensmartcompanion.R
import fr.isen.vincenti.isensmartcompanion.gemini.GeminiService
import kotlinx.coroutines.launch
import fr.isen.vincenti.isensmartcompanion.db.*

@Composable
fun MainScreen() {
    val context = LocalContext.current
    var input by remember { mutableStateOf("") }
    var savedinput by remember { mutableStateOf("") }
    var analyzedResponse by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var gemList by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    val chatDao = DBInstance.getChatDao(context)
    val chatHistory by chatDao.getAllMessages().collectAsState(initial = emptyList())
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = stringResource(id = R.string.logo_desc),
        )
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(gemList){ (question,answer) ->
                Column{
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 40.dp, end = 10.dp, top = 10.dp, bottom = 10.dp)
                            .background(Color(0xFFEFEFEF), shape = RoundedCornerShape(5.dp))
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
                            .background(Color(0xFFD9D9D9), shape = RoundedCornerShape(5.dp))
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
                            input = ""
                            analyzedResponse = GeminiService.generateResponse(savedinput).trim()
                            gemList = gemList + listOf(Pair(savedinput, analyzedResponse))
                            chatDao.insertMessage(
                                ChatMessage(
                                    question = savedinput,
                                    answer = analyzedResponse
                                )
                            )
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