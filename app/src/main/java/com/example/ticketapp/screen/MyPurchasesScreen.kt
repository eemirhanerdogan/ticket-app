package com.example.ticketapp.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.domain.purchase.Purchase
import com.example.core.domain.purchase.PurchaseStatus
import com.example.core.util.DateFormatter
import com.example.ticketapp.R
import com.example.ticketapp.viewmodel.MyPurchasesViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPurchasesScreen(
    onBackClick: () -> Unit,
    onContinuePaymentSuccess: () -> Unit,
    viewModel: MyPurchasesViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.paymentSuccess) {
        if (state.paymentSuccess) {
            onContinuePaymentSuccess()
            viewModel.consumePaymentSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.my_purchases)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::refreshPurchases) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
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
                state.isLoading && !state.isRefreshing -> CircularProgressIndicator()
                state.errorMessage != null && state.purchases.isEmpty() -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = state.errorMessage!!, color = MaterialTheme.colorScheme.error)
                        Button(onClick = viewModel::loadPurchases) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }
                state.purchases.isEmpty() -> {
                    Text(text = stringResource(R.string.no_purchases_yet))
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.purchases, key = { it.id }) { purchase ->
                            PurchaseCard(
                                purchase = purchase,
                                isPaying = state.payingPurchaseId == purchase.id,
                                onContinuePayment = { viewModel.continuePayment(purchase.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PurchaseCard(
    purchase: Purchase,
    isPaying: Boolean,
    onContinuePayment: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${stringResource(R.string.ticket_id)}: ${purchase.id.take(8)}...",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                StatusChip(status = purchase.status)
            }
            
            Spacer(Modifier.height(8.dp))
            
            Text(
                text = "${stringResource(R.string.total)}: ₺${purchase.totalCents / 100.0}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            if (purchase.paidAt != null) {
                Text(
                    text = DateFormatter.format(purchase.paidAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Spacer(Modifier.height(12.dp))

            if (purchase.status == PurchaseStatus.PENDING) {
                Button(
                    onClick = onContinuePayment,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isPaying
                ) {
                    if (isPaying) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    } else {
                        Text(stringResource(R.string.continue_payment))
                    }
                }
            } else if (purchase.status == PurchaseStatus.PAID) {
                Text(
                    text = stringResource(R.string.paid),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
fun StatusChip(status: PurchaseStatus) {
    val color = when (status) {
        PurchaseStatus.PAID -> Color(0xFF4CAF50)
        PurchaseStatus.PENDING -> Color(0xFFFFA000)
        PurchaseStatus.UNKNOWN -> Color.Gray
    }
    
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color)
    ) {
        Text(
            text = status.name,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}
