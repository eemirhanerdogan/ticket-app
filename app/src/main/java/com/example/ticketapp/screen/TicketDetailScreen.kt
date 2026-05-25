package com.example.ticketapp.screen

import android.app.Activity
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.util.DateFormatter
import com.example.ticketapp.R
import com.example.ticketapp.viewmodel.TicketDetailViewModel
import org.koin.androidx.compose.koinViewModel
import qrcode.QRCode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketDetailScreen(
    ticketId: String,
    onBackClick: () -> Unit,
    viewModel: TicketDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context as? Activity

    // Brightness Control
    DisposableEffect(Unit) {
        val window = activity?.window
        val layoutParams = window?.attributes
        val originalBrightness = layoutParams?.screenBrightness ?: -1f
        
        layoutParams?.let {
            it.screenBrightness = 1.0f
            window.attributes = it
        }

        onDispose {
            layoutParams?.let {
                it.screenBrightness = originalBrightness
                window?.attributes = it
            }
        }
    }

    LaunchedEffect(ticketId) {
        viewModel.loadTicket(ticketId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.ticket_detail)) },
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
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // QR Code Section
                        val qrBitmap = remember(ticket.qrCode) {
                            try {
                                QRCode(ticket.qrCode).render().nativeImage() as Bitmap
                            } catch (e: Exception) {
                                null
                            }
                        }

                        qrBitmap?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = "QR Code",
                                modifier = Modifier.size(200.dp)
                            )
                        }
                        
                        Text(
                            text = ticket.qrCode,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        DetailItem(label = stringResource(R.string.ticket_id), value = ticket.id)
                        DetailItem(label = stringResource(R.string.event_name), value = ticket.eventName ?: stringResource(R.string.unknown_event))
                        DetailItem(label = stringResource(R.string.ticket_type), value = ticket.ticketTypeName ?: stringResource(R.string.unknown_ticket_type))
                        DetailItem(label = stringResource(R.string.date_time), value = DateFormatter.format(ticket.eventStartsAt))
                        DetailItem(label = stringResource(R.string.location), value = ticket.eventPlace ?: stringResource(R.string.no_location_info))
                        DetailItem(label = stringResource(R.string.status), value = ticket.status)
                        
                        ticket.priceCents?.let {
                            DetailItem(label = stringResource(R.string.price), value = "${it / 100.0} TL")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
    }
}
