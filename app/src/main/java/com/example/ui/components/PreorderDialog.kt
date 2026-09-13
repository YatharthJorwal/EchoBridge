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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ShoppingBag
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.EchoAmber
import com.example.ui.theme.EchoBgElevated
import com.example.ui.theme.EchoBgSurface
import com.example.ui.theme.EchoBlueBright
import com.example.ui.theme.EchoCardBorder
import com.example.ui.theme.EchoPurple
import com.example.ui.theme.EchoTextMuted
import com.example.ui.theme.EchoTextPrimary
import com.example.ui.theme.EchoTextSecondary

@Composable
fun PreorderDialog(
    onDismiss: () -> Unit,
    onConfirmPreorder: (String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedTier by remember { mutableStateOf("Direct Consumer (₹7,499)") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .testTag("preorder_dialog"),
            colors = CardDefaults.cardColors(containerColor = EchoBgSurface),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.Brush.linearGradient(listOf(EchoPurple, EchoBlueBright)))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = EchoPurple, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "EchoRing™ Pre-Order", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = EchoTextPrimary)
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = EchoTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Reserve your titanium biometric ring from the first production batch. Hardware is currently in pilot development.",
                    fontSize = 11.sp,
                    color = EchoTextSecondary,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Price Highlights
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(EchoBgElevated)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Direct Consumer Unit", fontSize = 12.sp, color = EchoTextPrimary)
                        Text(text = "₹7,499", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = EchoBlueBright)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Enterprise / Team Batch (5+ units)", fontSize = 11.sp, color = EchoTextSecondary)
                        Text(text = "₹5,499 / unit", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EchoAmber)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name", fontSize = 11.sp) },
                    placeholder = { Text("Enter your name", fontSize = 11.sp, color = EchoTextMuted) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EchoPurple,
                        unfocusedBorderColor = EchoCardBorder,
                        focusedTextColor = EchoTextPrimary,
                        unfocusedTextColor = EchoTextPrimary
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address", fontSize = 11.sp) },
                    placeholder = { Text("name@example.com", fontSize = 11.sp, color = EchoTextMuted) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EchoPurple,
                        unfocusedBorderColor = EchoCardBorder,
                        focusedTextColor = EchoTextPrimary,
                        unfocusedTextColor = EchoTextPrimary
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onConfirmPreorder(name, email) },
                    modifier = Modifier.fillMaxWidth().height(46.dp).testTag("confirm_preorder_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = EchoPurple),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Reserve Pre-Order Spot →", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Pilot reservation holds priority in hardware batch. No payment processed now.",
                    fontSize = 10.sp,
                    color = EchoTextMuted,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
