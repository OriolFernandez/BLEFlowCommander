package com.uriolus.btlecommander.features.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.uriolus.btlecommander.features.detail.ui.DetailScreen

class DetailActivity : ComponentActivity() {
    companion object {
        const val MAC_PARAMETER = "mac"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mac: String = intent.getStringExtra(MAC_PARAMETER)
            ?: throw java.lang.Exception("Parameter mac not found")
        setContent {
            DetailScreen(mac = mac, navigateUp = { finish() })
        }
    }
}