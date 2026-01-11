package com.example.edugard.ui.admin.analytics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.edugard.ui.components.BaseButton
import com.example.edugard.ui.components.BaseScreen
import com.example.edugard.ui.theme.BrownPrimary
import com.example.edugard.ui.theme.EduGardTheme
import com.example.edugard.ui.theme.LightBackground
import com.example.edugard.ui.theme.StatusAlert
import com.example.edugard.ui.theme.StatusOnline
import com.example.edugard.ui.theme.TextPrimary
import com.example.edugard.ui.theme.TextSecondary
import com.example.edugard.ui.theme.WarningYellow
import com.example.edugard.ui.theme.WhiteBackground

@Composable
fun AnalyticsScreen(
    onMenuClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    BaseScreen(
        title = "Usage Analytics",
        showBackButton = true,
        showMenuButton = false,
        showProfilePhoto = true,
        onBackClick = onBack,
        onProfileClick = onProfileClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Chart
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                colors = CardDefaults.cardColors(containerColor = WhiteBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Daily App Usage",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    DailyUsageChart()
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Statistics Cards
            Text(
                text = "Key Metrics",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Row 1: Screen Time & Violations
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    icon = Icons.Default.DateRange,
                    title = "Avg Screen Time",
                    value = "2.5 hrs",
                    color = BrownPrimary,
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    icon = Icons.Default.Warning,
                    title = "Violations",
                    value = "15",
                    subtitle = "this week",
                    color = StatusAlert,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Row 2: Compliance
            MetricCard(
                icon = Icons.Default.CheckCircle,
                title = "Compliance Rate",
                value = "94%",
                subtitle = "overall performance",
                color = StatusOnline,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Additional Stats
            Text(
                text = "Weekly Summary",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            SummaryRow("Total Active Students", "156")
            SummaryRow("Total Devices Monitored", "156")
            SummaryRow("Average Compliance", "94%")
            SummaryRow("Policy Violations", "15")
            SummaryRow("Devices with Alerts", "3")

            Spacer(modifier = Modifier.height(24.dp))

            // Export Button
            BaseButton(
                text = "EXPORT REPORT",
                onClick = { /* TODO: Export functionality */ }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun DailyUsageChart() {
    // Sample data for last 7 days
    val dailyData = listOf(4.2f, 5.1f, 3.8f, 6.2f, 4.9f, 5.5f, 6.8f)
    val maxValue = dailyData.maxOrNull() ?: 1f
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val chartHeight = size.height - 40.dp.toPx()
            val chartWidth = size.width - 40.dp.toPx()
            val barWidth = chartWidth / dailyData.size / 1.5f
            val spacing = chartWidth / dailyData.size

            // Draw grid lines
            drawContext.canvas.save()
            val gridColor = TextSecondary.copy(alpha = 0.2f)
            for (i in 0..4) {
                val y = 20.dp.toPx() + (chartHeight / 4) * i
                drawLine(
                    color = gridColor,
                    start = Offset(20.dp.toPx(), y),
                    end = Offset(size.width - 20.dp.toPx(), y),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f))
                )
            }
            drawContext.canvas.restore()

            // Draw bars
            dailyData.forEachIndexed { index, value ->
                val barHeight = (value / maxValue) * chartHeight
                val x = 20.dp.toPx() + spacing * index + (spacing - barWidth) / 2
                val y = size.height - 20.dp.toPx() - barHeight

                drawRoundRect(
                    color = BrownPrimary,
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barHeight),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx())
                )
            }
        }

        // Draw labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            days.forEach { day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    // Y-axis labels
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "${maxValue.toInt()}h",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
        Text(
            text = "0h",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
    }
}

@Composable
fun MetricCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    subtitle: String? = null,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = WhiteBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
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

@Composable
fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = BrownPrimary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AnalyticsScreenPreview() {
    EduGardTheme {
        AnalyticsScreen()
    }
}
