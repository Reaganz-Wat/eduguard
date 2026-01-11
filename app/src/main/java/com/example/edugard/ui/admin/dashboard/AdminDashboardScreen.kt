package com.example.edugard.ui.admin.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.edugard.ui.components.BaseButton
import com.example.edugard.ui.components.BaseScreen
import com.example.edugard.ui.theme.BrownPrimary
import com.example.edugard.ui.theme.EduGardTheme
import com.example.edugard.ui.theme.OrangeAccent
import com.example.edugard.ui.theme.StatusAlert
import com.example.edugard.ui.theme.TextPrimary
import com.example.edugard.ui.theme.TextSecondary
import com.example.edugard.ui.theme.WhiteBackground

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
                .verticalScroll(rememberScrollState())
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

            // Stat Cards in a grid layout
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DashboardCard(
                    icon = Icons.Default.AccountCircle,
                    title = "Active Students",
                    value = "156",
                    iconColor = BrownPrimary,
                    modifier = Modifier.weight(1f)
                )
                DashboardCard(
                    icon = Icons.Default.Warning,
                    title = "Violations",
                    value = "12",
                    subtitle = "Today",
                    iconColor = StatusAlert,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DashboardCard(
                    icon = Icons.Default.Refresh,
                    title = "Pending",
                    value = "3",
                    subtitle = "Approvals",
                    iconColor = OrangeAccent,
                    modifier = Modifier.weight(1f)
                )
                DashboardCard(
                    icon = Icons.Default.AccountCircle,
                    title = "Students",
                    value = "245",
                    subtitle = "Total",
                    iconColor = BrownPrimary,
                    modifier = Modifier.weight(1f)
                )
            }

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

@Composable
fun DashboardCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    subtitle: String? = null,
    iconColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = WhiteBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Icon with background
            Card(
                modifier = Modifier.size(48.dp),
                colors = CardDefaults.cardColors(
                    containerColor = iconColor.copy(alpha = 0.15f)
                ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Value
            Text(
                text = value,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Title and subtitle
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
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
