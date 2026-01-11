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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.edugard.ui.components.BaseButton
import com.example.edugard.ui.components.BaseScreen
import com.example.edugard.ui.components.StatCard
import com.example.edugard.ui.theme.EduGardTheme
import com.example.edugard.ui.theme.TextSecondary

@Composable
fun AdminDashboardScreen(
    onMenuClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onViewAnalytics: () -> Unit = {},
    onManageStudents: () -> Unit = {}
) {
    BaseScreen(
        title = "Admin Dashboard",
        showBackButton = false,
        showMenuButton = true,
        showProfilePhoto = true,
        onMenuClick = onMenuClick,
        onProfileClick = onProfileClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Welcome message
            Text(
                text = "Welcome back!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Here's what's happening with your school today.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 24.dp)
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
                text = "MANAGE STUDENTS",
                onClick = onManageStudents
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
