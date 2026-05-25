package com.example.ticketapp.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.domain.event.TicketType
import com.example.core.util.DateFormatter
import com.example.ticketapp.R
import com.example.ticketapp.viewmodel.EventDetailViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: String,
    onBackClick: () -> Unit,
    onPurchasePaid: () -> Unit,
    viewModel: EventDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(eventId) {
        viewModel.loadEvent(eventId)
    }

    LaunchedEffect(state.paymentSuccess) {
        if (state.paymentSuccess) {
            onPurchasePaid()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.event?.name ?: stringResource(R.string.event_name)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                    }
                }
            )
        },
        bottomBar = {
            if (state.event != null) {
                BottomAppBar {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Toplam: ₺${state.totalCents / 100.0}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = viewModel::createPurchase,
                            enabled = state.canPurchase
                        ) {
                            if (state.isCreatingPurchase) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                            } else {
                                Text("Satın Al")
                            }
                        }
                    }
                }
            }
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
                state.errorMessage != null && state.event == null -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = state.errorMessage!!, color = MaterialTheme.colorScheme.error)
                        Button(onClick = { viewModel.loadEvent(eventId) }) {
                            Text("Tekrar Dene")
                        }
                    }
                }
                state.event != null -> {
                    val event = state.event!!
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Text(text = event.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                            Text(text = event.venue, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)
                            Spacer(Modifier.height(8.dp))
                            Text(text = event.description, style = MaterialTheme.typography.bodyLarge)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Tarih: ${DateFormatter.format(event.startsAt)}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        item {
                            Text(text = "Bilet Türleri", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        }

                        // Added key to items for correct state association
                        items(
                            items = event.ticketTypes,
                            key = { it.id }
                        ) { ticketType ->
                            TicketTypeItem(
                                ticketType = ticketType,
                                quantity = state.selectedQuantities[ticketType.id] ?: 0,
                                onIncrease = { viewModel.increase(ticketType.id) },
                                onDecrease = { viewModel.decrease(ticketType.id) }
                            )
                        }
                        
                        item { Spacer(Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }

    if (state.pendingPurchase != null) {
        AlertDialog(
            onDismissRequest = viewModel::dismissPaymentDialog,
            title = { Text("Ödeme Onayı") },
            text = { Text("Toplam tutar ₺${state.pendingPurchase!!.totalCents / 100.0} ödenecektir. Onaylıyor musunuz?") },
            confirmButton = {
                TextButton(onClick = viewModel::confirmPayment) {
                    Text("Ödemeyi Tamamla")
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::dismissPaymentDialog) {
                    Text("Vazgeç")
                }
            }
        )
    }
}

@Composable
fun TicketTypeItem(
    ticketType: TicketType,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = ticketType.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = "₺${ticketType.priceCents / 100.0}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Kalan: ${ticketType.remaining} / ${ticketType.capacity}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (ticketType.remaining < 10) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDecrease, enabled = quantity > 0) {
                    Text("-", style = MaterialTheme.typography.headlineSmall)
                }
                Text(
                    text = quantity.toString(),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                // Corrected max allowed check
                IconButton(onClick = onIncrease, enabled = quantity < minOf(20L, ticketType.remaining)) {
                    Text("+", style = MaterialTheme.typography.headlineSmall)
                }
            }
        }
    }
}
