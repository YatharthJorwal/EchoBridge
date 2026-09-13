package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothConnected
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EchoBgElevated
import com.example.ui.theme.EchoBgSurface
import com.example.ui.theme.EchoBlue
import com.example.ui.theme.EchoBlueBright
import com.example.ui.theme.EchoCardBorder
import com.example.ui.theme.EchoCyan
import com.example.ui.theme.EchoEmerald
import com.example.ui.theme.EchoPurple
import com.example.ui.theme.EchoTextMuted
import com.example.ui.theme.EchoTextPrimary
import com.example.ui.theme.EchoTextSecondary

@Composable
fun EchoBridgeTopBar(
    isRingPaired: Boolean,
    onOpenDevices: () -> Unit,
    onOpenCheckIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(EchoBgSurface)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Brand Title with Gradient
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Echo",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = EchoTextPrimary,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "Bridge",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = EchoBlueBright,
                    letterSpacing = (-0.5).sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Live Indicator
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .scale(pulseScale)
                        .clip(CircleShape)
                        .background(EchoEmerald)
                )
            }
            Text(
                text = "Reclaim Your Mind from the Algorithm",
                fontSize = 11.sp,
                color = EchoTextMuted,
                fontWeight = FontWeight.Medium
            )
        }

        // Action Buttons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // EchoRing Quick Status Chip
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(EchoBgElevated)
                    .border(1.dp, if (isRingPaired) EchoEmerald else EchoCardBorder, RoundedCornerShape(20.dp))
                    .clickable { onOpenDevices() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .testTag("device_status_chip"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isRingPaired) Icons.Default.BluetoothConnected else Icons.Default.Bluetooth,
                    contentDescription = "Bluetooth Device",
                    tint = if (isRingPaired) EchoEmerald else EchoBlueBright,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isRingPaired) "Ring Paired" else "Hardware Ready",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isRingPaired) EchoEmerald else EchoTextSecondary
                )
            }

            // Quick Check-in Button
            IconButton(
                onClick = onOpenCheckIn,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(EchoBgElevated)
                    .border(1.dp, EchoCardBorder, CircleShape)
                    .testTag("quick_checkin_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Mood,
                    contentDescription = "Quick Check-in",
                    tint = EchoPurple,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
