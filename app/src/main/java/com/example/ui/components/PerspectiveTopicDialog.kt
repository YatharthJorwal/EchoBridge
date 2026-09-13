package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.PerspectiveTopic
import com.example.ui.theme.EchoAmber
import com.example.ui.theme.EchoBgElevated
import com.example.ui.theme.EchoBgSurface
import com.example.ui.theme.EchoBlue
import com.example.ui.theme.EchoBlueBright
import com.example.ui.theme.EchoCyan
import com.example.ui.theme.EchoEmerald
import com.example.ui.theme.EchoPurple
import com.example.ui.theme.EchoTextMuted
import com.example.ui.theme.EchoTextPrimary
import com.example.ui.theme.EchoTextSecondary

@Composable
fun PerspectiveTopicDialog(
    topic: PerspectiveTopic,
    onDismiss: () -> Unit,
    onAcknowledge: (PerspectiveTopic) -> Unit
) {
    val scrollState = rememberScrollState()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .testTag("perspective_topic_dialog"),
            colors = CardDefaults.cardColors(containerColor = EchoBgSurface),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(EchoCyan, EchoPurple)))
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(scrollState)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.CompassCalibration, contentDescription = null, tint = EchoCyan, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Perspective Balancing", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = EchoTextPrimary)
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = EchoTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(text = topic.title, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = EchoTextPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "${topic.category} · Alert: ${topic.biasAlert}", fontSize = 11.sp, color = EchoAmber)

                Spacer(modifier = Modifier.height(14.dp))

                // Viewpoint A Card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(EchoBgElevated)
                        .padding(12.dp)
                ) {
                    Text(text = "VIEWPOINT A (Dominant Narrative)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EchoBlueBright)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = topic.viewA, fontSize = 12.sp, color = EchoTextPrimary, lineHeight = 16.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Viewpoint B Card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(EchoBgElevated)
                        .padding(12.dp)
                ) {
                    Text(text = "VIEWPOINT B (Counter-Perspective)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EchoPurple)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = topic.viewB, fontSize = 12.sp, color = EchoTextPrimary, lineHeight = 16.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Dialectic Synthesis
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(EchoEmerald.copy(alpha = 0.1f))
                        .padding(12.dp)
                ) {
                    Text(text = "SYNTHESIS & CRITICAL INSIGHT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EchoEmerald)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = topic.balancedInsight, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = EchoTextPrimary, lineHeight = 16.sp)
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = { onAcknowledge(topic) },
                    modifier = Modifier.fillMaxWidth().height(46.dp).testTag("earn_oms_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = EchoBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (topic.isExplored) "Close Viewpoint" else "Acknowledge & Earn +${topic.omsBonus} OMS →",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
