package fr.isen.vincenti.isensmartcompanion.gemini

import com.google.ai.client.generativeai.GenerativeModel

object GeminiService {
    val generativeModel = GenerativeModel("gemini-1.5-flash", "AIzaSyBIGzu564ZviiAEBKUPqW2x8exHVRvyW5I")

    suspend fun generateResponse(input: String): String {
        return try {
            val response = generativeModel.generateContent(input)
            response.text ?: "No response"
        } catch (e: Exception) {
            "Error generating response: ${e.message}"
        }
    }
}