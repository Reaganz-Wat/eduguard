package com.example.edugard.ui.admin.students

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edugard.domain.model.Student
import com.example.edugard.ui.components.BaseBottomSheet
import com.example.edugard.ui.components.BaseButton
import com.example.edugard.ui.components.BaseScreen
import com.example.edugard.ui.components.BaseTextField
import com.example.edugard.ui.theme.BrownPrimary
import com.example.edugard.ui.theme.TextPrimary
import com.example.edugard.ui.theme.TextSecondary
import com.example.edugard.ui.theme.WhiteBackground
import com.example.edugard.viewmodel.StudentViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentManagementScreen(
    onMenuClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    viewModel: StudentViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    var showAddStudentSheet by remember { mutableStateOf(false) }
    var showStudentActionsSheet by remember { mutableStateOf(false) }
    var selectedStudentForActions by remember { mutableStateOf<Student?>(null) }
    var editingStudent by remember { mutableStateOf<Student?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val actionsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    // Show delete confirmation when in selection mode and items are selected
    val showDeleteConfirm = state.isSelectionMode && state.selectedStudentIds.isNotEmpty()

    Scaffold(
        floatingActionButton = {
            if (!state.isSelectionMode) {
                FloatingActionButton(
                    onClick = {
                        editingStudent = null
                        showAddStudentSheet = true
                    },
                    containerColor = BrownPrimary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Student")
                }
            } else {
                FloatingActionButton(
                    onClick = {
                        viewModel.deleteSelectedStudents()
                    },
                    containerColor = MaterialTheme.colorScheme.error
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Selected")
                }
            }
        }
    ) { padding ->
        BaseScreen(
            title = if (state.isSelectionMode) {
                "${state.selectedStudentIds.size} selected"
            } else {
                "Manage Students"
            },
            showMenuButton = true,
            showProfilePhoto = true,
            onMenuClick = {
                if (state.isSelectionMode) {
                    viewModel.clearSelection()
                } else {
                    onMenuClick()
                }
            },
            onProfileClick = onProfileClick
        ) {
            if (state.students.isEmpty()) {
                // Empty state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.padding(16.dp),
                        tint = TextSecondary
                    )
                    Text(
                        text = "No students registered yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(state.students) { student ->
                        StudentCard(
                            student = student,
                            isSelected = state.selectedStudentIds.contains(student.id),
                            isSelectionMode = state.isSelectionMode,
                            onCardClick = {
                                if (state.isSelectionMode) {
                                    viewModel.toggleStudentSelection(student.id)
                                } else {
                                    selectedStudentForActions = student
                                    showStudentActionsSheet = true
                                }
                            },
                            onLongPress = {
                                viewModel.setSelectionMode(true)
                                viewModel.toggleStudentSelection(student.id)
                            },
                            onDelete = {
                                viewModel.deleteStudent(student.id)
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }

    // Student Actions Bottom Sheet (Edit/Delete)
    if (showStudentActionsSheet && selectedStudentForActions != null) {
        StudentActionsBottomSheet(
            student = selectedStudentForActions!!,
            onDismiss = {
                scope.launch {
                    actionsSheetState.hide()
                    showStudentActionsSheet = false
                    selectedStudentForActions = null
                }
            },
            onEdit = {
                editingStudent = selectedStudentForActions
                scope.launch {
                    actionsSheetState.hide()
                    showStudentActionsSheet = false
                    selectedStudentForActions = null
                    showAddStudentSheet = true
                }
            },
            onDelete = {
                viewModel.deleteStudent(selectedStudentForActions!!.id)
                scope.launch {
                    actionsSheetState.hide()
                    showStudentActionsSheet = false
                    selectedStudentForActions = null
                }
            },
            sheetState = actionsSheetState
        )
    }

    // Add/Edit Student Bottom Sheet
    if (showAddStudentSheet) {
        AddEditStudentBottomSheet(
            student = editingStudent,
            onDismiss = {
                scope.launch {
                    sheetState.hide()
                    showAddStudentSheet = false
                    editingStudent = null
                }
            },
            onSave = { student ->
                if (editingStudent == null) {
                    viewModel.addStudent(student)
                } else {
                    viewModel.updateStudent(student)
                }
                scope.launch {
                    sheetState.hide()
                    showAddStudentSheet = false
                    editingStudent = null
                }
            },
            sheetState = sheetState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun StudentCard(
    student: Student,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onCardClick: () -> Unit,
    onLongPress: () -> Unit,
    onDelete: () -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    val density = LocalDensity.current
    val swipeThresholdDp = 200.dp
    val swipeThreshold = -with(density) { swipeThresholdDp.toPx() }
    val animatedOffset by animateDpAsState(
        targetValue = with(density) { offsetX.toDp() },
        animationSpec = tween(300),
        label = "swipe"
    )

    Box(modifier = Modifier.fillMaxWidth()) {
        // Swipe background (delete indicator)
        if (offsetX < 0) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(MaterialTheme.colorScheme.error)
                    .padding(16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = animatedOffset)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (offsetX < swipeThreshold && !isSelectionMode) {
                                onDelete()
                            }
                            offsetX = 0f
                        }
                    ) { change, dragAmount ->
                        if (!isSelectionMode) {
                            offsetX += dragAmount
                            offsetX = offsetX.coerceAtMost(0f).coerceAtLeast(swipeThreshold * 1.5f)
                        }
                    }
                }
                .combinedClickable(
                    onClick = onCardClick,
                    onLongClick = onLongPress
                ),
            colors = CardDefaults.cardColors(
                containerColor = if (isSelected) BrownPrimary.copy(alpha = 0.2f) else WhiteBackground
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Selection checkbox
                if (isSelectionMode) {
                    Icon(
                        imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.Person,
                        contentDescription = if (isSelected) "Selected" else "Not selected",
                        tint = if (isSelected) BrownPrimary else TextSecondary,
                        modifier = Modifier
                            .size(32.dp)
                            .padding(end = 12.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = student.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "${student.grade} • ${student.studentId}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier
                                .size(16.dp)
                                .padding(end = 4.dp)
                        )
                        Text(
                            text = student.email,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Phone,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier
                                .size(16.dp)
                                .padding(end = 4.dp)
                        )
                        Text(
                            text = student.phoneNumber,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentActionsBottomSheet(
    student: Student,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    sheetState: androidx.compose.material3.SheetState
) {
    BaseBottomSheet(
        title = student.name,
        onDismiss = onDismiss,
        sheetState = sheetState
    ) {
        Column {
            // Student Info
            Text(
                text = "${student.grade} • ${student.studentId}",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Edit Button
            BaseButton(
                text = "EDIT",
                onClick = onEdit,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Delete Button
            BaseButton(
                text = "DELETE",
                onClick = onDelete,
                backgroundColor = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditStudentBottomSheet(
    student: Student?,
    onDismiss: () -> Unit,
    onSave: (Student) -> Unit,
    sheetState: androidx.compose.material3.SheetState
) {
    var name by remember { mutableStateOf(student?.name ?: "") }
    var grade by remember { mutableStateOf(student?.grade ?: "") }
    var studentId by remember { mutableStateOf(student?.studentId ?: "") }
    var email by remember { mutableStateOf(student?.email ?: "") }
    var phoneNumber by remember { mutableStateOf(student?.phoneNumber ?: "") }

    BaseBottomSheet(
        title = if (student == null) "Add Student" else "Edit Student",
        onDismiss = onDismiss,
        sheetState = sheetState
    ) {
        Column {
            BaseTextField(
                value = name,
                onValueChange = { name = it },
                label = "Full Name"
            )

            Spacer(modifier = Modifier.height(12.dp))

            BaseTextField(
                value = grade,
                onValueChange = { grade = it },
                label = "Grade (e.g., Grade 10)"
            )

            Spacer(modifier = Modifier.height(12.dp))

            BaseTextField(
                value = studentId,
                onValueChange = { studentId = it },
                label = "Student ID"
            )

            Spacer(modifier = Modifier.height(12.dp))

            BaseTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(12.dp))

            BaseTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = "Phone Number",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            Spacer(modifier = Modifier.height(24.dp))

            BaseButton(
                text = if (student == null) "ADD STUDENT" else "UPDATE STUDENT",
                onClick = {
                    val newStudent = Student(
                        id = student?.id ?: "",
                        name = name,
                        grade = grade,
                        studentId = studentId,
                        email = email,
                        phoneNumber = phoneNumber
                    )
                    onSave(newStudent)
                },
                enabled = name.isNotEmpty() && grade.isNotEmpty() && studentId.isNotEmpty()
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
