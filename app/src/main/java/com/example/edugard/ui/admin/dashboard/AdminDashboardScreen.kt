package com.example.edugard.ui.admin.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.edugard.ui.components.BaseButton
import com.example.edugard.ui.components.BaseScreen
import com.example.edugard.ui.components.StatCard
import com.example.edugard.ui.theme.EduGardTheme

@Composable
fun AdminDashboardScreen(
    onBackClick: () -> Unit = {},
    onViewAnalytics: () -> Unit = {},
    onManageDevices: () -> Unit = {}
) {
    BaseScreen(
        title = "Admin Dashboard",
        showBackButton = false
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Subtitle
            Text(
                text = "EduGuard AI",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Stat Cards with dummy data
            StatCard(
                title = "Active Devices",
                value = "156"
            )

            Spacer(modifier = Modifier.height(12.dp))

            StatCard(
                title = "Violations Today",
                value = "12"
            )

            Spacer(modifier = Modifier.height(12.dp))

            StatCard(
                title = "Pending Approvals",
                value = "3"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            BaseButton(
                text = "VIEW ANALYTICS",
                onClick = onViewAnalytics
            )

            Spacer(modifier = Modifier.height(12.dp))

            BaseButton(
                text = "MANAGE DEVICES",
                onClick = onManageDevices
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminDashboardScreenPreview() {
    EduGardTheme {
        AdminDashboardScreen()
    }
}
