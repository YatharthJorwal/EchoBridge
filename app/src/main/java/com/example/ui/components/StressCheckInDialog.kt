package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.EchoAmber
import com.example.ui.theme.EchoBgElevated
import com.example.ui.theme.EchoBgSurface
import com.example.ui.theme.EchoCardBorder
import com.example.ui.theme.EchoCoral
import com.example.ui.theme.EchoCyan
import com.example.ui.theme.EchoEmerald
import com.example.ui.theme.EchoPurple
import com.example.ui.theme.EchoTextMuted
import com.example.ui.theme.EchoTextPrimary
import com.example.ui.theme.EchoTextSecondary

@Composable
fun StressCheckInDialog(
    onDismiss: () -> Unit,
    onSubmit: (level: Int, label: String, note: String) -> Unit
) {
    var selectedLevel by remember { mutableIntStateOf(2) }
    var noteInput by remember { mutableStateOf("") }

    val levels = listOf(
        Triple(1, "Calm", EchoEmerald),
        Triple(2, "Balanced", EchoCyan),
        Triple(3, "Fatigued", EchoAmber),
        Triple(4, "Elevated", EchoCoral),
        Triple(5, "Acute Stress", EchoPurple)
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .testTag("stress_checkin_dialog"),
            colors = CardDefaults.cardColors(containerColor = EchoBgSurface),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(EchoPurple, EchoCyan)))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Favorite, contentDescription = null, tint = EchoCoral, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Subjective Stress Check-In", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = EchoTextPrimary)
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = EchoTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Self-assess your current cognitive and emotional state. Physical telemetry is recorded separately via compatible wearable sensors.",
                    fontSize = 11.sp,
                    color = EchoTextSecondary,
                    lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(14.dp))
                Text(text = "Perceived Stress Level:", fontSize = 12.sp, color = EchoTextSecondary, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))

                // Stress Level selector buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    levels.forEach { (lvl, label, color) ->
                        val isSelected = selectedLevel == lvl
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) color.copy(alpha = 0.25f) else EchoBgElevated)
                                .border(1.dp, if (isSelected) color else EchoCardBorder, RoundedCornerShape(10.dp))
                                .clickable { selectedLevel = lvl }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) color else EchoTextSecondary,
                                maxLines = 1
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = noteInput,
                    onValueChange = { noteInput = it },
                    label = { Text("Context or Triggers", fontSize = 11.sp) },
                    placeholder = { Text("e.g. Social media debate, research deadline, post-walk calm", fontSize = 11.sp, color = EchoTextMuted) },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EchoCoral,
                        unfocusedBorderColor = EchoCardBorder,
                        focusedTextColor = EchoTextPrimary,
                        unfocusedTextColor = EchoTextPrimary
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        val label = levels.firstOrNull { it.first == selectedLevel }?.second ?: "Balanced"
                        onSubmit(selectedLevel, label, noteInput)
                    },
                    modifier = Modifier.fillMaxWidth().height(46.dp).testTag("save_stress_checkin_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = EchoPurple),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Save Stress Assessment", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
