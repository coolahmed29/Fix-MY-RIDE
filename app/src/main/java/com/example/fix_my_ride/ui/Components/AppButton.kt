package com.example.fix_my_ride.ui.Components
// ui/components/AppButton.kt


import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fix_my_ride.ui.theme.Primary
import com.example.fix_my_ride.ui.theme.PrimaryDark
import com.example.fix_my_ride.ui.theme.Roboto

@Composable
fun AppButton(
    text        : String,
    onClick     : () -> Unit,
    modifier    : Modifier = Modifier,
    isLoading   : Boolean  = false,
    loadingText : String   = "Please wait...",
    enabled     : Boolean  = true
) {
    val gradient = Brush.horizontalGradient(
        colors = listOf(Primary, PrimaryDark)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .shadow(
                elevation    = 8.dp,
                shape        = RoundedCornerShape(30.dp),
                ambientColor = Primary.copy(alpha = 0.3f),
                spotColor    = Primary.copy(alpha = 0.4f)
            )
            .clip(RoundedCornerShape(30.dp))
            .background(
                brush  = if (enabled && !isLoading) gradient
                else Brush.horizontalGradient(
                    listOf(
                        Color.Gray.copy(alpha = 0.5f),
                        Color.Gray.copy(alpha = 0.5f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick  = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            enabled  = enabled && !isLoading,
            shape    = RoundedCornerShape(30.dp),
            colors   = ButtonDefaults.buttonColors(
                containerColor         = Color.Transparent,
                disabledContainerColor = Color.Transparent
            ),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            AnimatedContent(
                targetState = isLoading,
                label       = "btn_anim"
            ) { loading ->
                if (loading) {
                    Row(
                        verticalAlignment    = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier    = Modifier.size(18.dp),
                            color       = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text       = loadingText,
                            color      = Color.White,
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Medium,
                            fontSize   = 15.sp
                        )
                    }
                } else {
                    Text(
                        text       = text,
                        color      = Color.White,
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Medium,
                        fontSize   = 15.sp
                    )
                }
            }
        }
    }
}