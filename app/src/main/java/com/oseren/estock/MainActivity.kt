package com.oseren.estock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.oseren.estock.navigation.controller.NavController
import com.oseren.estock.ui.theme.EStockTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EStockTheme {
                Surface {
                    NavController()
                }
            }
        }
    }
}
