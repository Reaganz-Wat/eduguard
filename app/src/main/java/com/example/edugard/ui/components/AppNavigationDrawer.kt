package com.example.edugard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.edugard.ui.theme.BrownPrimary
import com.example.edugard.ui.theme.BrownSecondary
import com.example.edugard.ui.theme.LightBackground
import com.example.edugard.ui.theme.TextPrimary
import com.example.edugard.ui.theme.TextWhite
import com.example.edugard.ui.theme.WhiteBackground

data class DrawerMenuItem(
    val icon: ImageVector,
    val title: String,
    val route: String,
    val isSelected: Boolean = false
)

@Composable
fun AppNavigationDrawer(
    drawerState: DrawerState,
    currentRoute: String,
    userName: String = "Admin User",
    userEmail: String = "admin@edugard.ai",
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit,
    content: @Composable () -> Unit
) {
    val menuItems = listOf(
        DrawerMenuItem(Icons.Default.Home, "Dashboard", "admin_dashboard"),
        DrawerMenuItem(Icons.Default.AccountCircle, "Manage Students", "manage_students"),
        DrawerMenuItem(Icons.Default.List, "Analytics", "analytics"),
        DrawerMenuItem(Icons.Default.Settings, "Settings", "settings")
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = WhiteBackground
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(300.dp)
                ) {
                    // Header Section
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BrownPrimary)
                            .statusBarsPadding()
                            .padding(24.dp)
                    ) {
                        Column {
                            // Profile Photo
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(WhiteBackground.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile",
                                    tint = TextWhite,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = userName,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )

                            Text(
                                text = userEmail,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextWhite.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Menu Items
                    menuItems.forEach { item ->
                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            selected = currentRoute == item.route,
                            onClick = { onNavigate(item.route) },
                            modifier = Modifier.padding(horizontal = 12.dp),
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = BrownPrimary.copy(alpha = 0.1f),
                                selectedIconColor = BrownPrimary,
                                selectedTextColor = BrownPrimary,
                                unselectedIconColor = TextPrimary,
                                unselectedTextColor = TextPrimary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Divider(modifier = Modifier.padding(horizontal = 16.dp))

                    // Logout Item
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "Logout"
                            )
                        },
                        label = {
                            Text(
                                text = "Logout",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        selected = false,
                        onClick = onLogout,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedIconColor = MaterialTheme.colorScheme.error,
                            unselectedTextColor = MaterialTheme.colorScheme.error
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        },
        content = content
    )
}
