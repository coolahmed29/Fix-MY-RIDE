package com.example.fix_my_ride

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.fix_my_ride.ui.theme.FixMYrideTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShowCardSample()

            }
        }
    }

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ShowCardSample() {
    Card(
        elevation = 5.dp,
        modifier = Modifier
            .padding(5.dp)
            .wrapContentHeight()
    ) {
        Text(text = "Text inside Card", Modifier.padding(15.dp))
    }
}