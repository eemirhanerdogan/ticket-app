package com.example.ticketapp.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ticketapp.viewmodel.TicketDetailViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketDetailScreen(
    ticketId: String,
    onBackClick: () -> Unit,
    viewModel: TicketDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(ticketId) {
        viewModel.loadTicket(ticketId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bilet Detayı") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isLoading -> CircularProgressIndicator()
                state.error != null -> Text(text = state.error!!, color = MaterialTheme.colorScheme.error)
                state.ticket != null -> {
                    val ticket = state.ticket!!
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        DetailItem(label = "Bilet ID", value = ticket.id)
                        DetailItem(label = "Etkinlik Adı", value = ticket.eventName ?: "Bilinmeyen Etkinlik")
                        DetailItem(label = "Bilet Türü", value = ticket.ticketTypeName ?: "Bilinmeyen Bilet Türü")
                        DetailItem(label = "Tarih/Saat", value = ticket.eventStartsAt ?: "Tarih bilgisi yok")
                        DetailItem(label = "Lokasyon", value = ticket.eventPlace ?: "Lokasyon bilgisi yok")
                        DetailItem(label = "Durum", value = ticket.status)
                        DetailItem(label = "QR Kod", value = ticket.qrCode)
                        
                        ticket.priceCents?.let {
                            DetailItem(label = "Fiyat", value = "${it / 100.0} TL")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
    }
}
