package com.example.edugard.ui.admin.students

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.AccountBox
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
    var editingStudent by remember { mutableStateOf<Student?>(null) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingStudent = null
                    showAddStudentSheet = true
                },
                containerColor = BrownPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Student")
            }
        }
    ) { padding ->
        BaseScreen(
            title = "Manage Students",
            showMenuButton = true,
            showProfilePhoto = true,
            onMenuClick = onMenuClick,
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
                            onEdit = {
                                editingStudent = student
                                showAddStudentSheet = true
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

@Composable
fun StudentCard(
    student: Student,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = WhiteBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
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
                }

                Row {
                    IconButton(onClick = onEdit) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = BrownPrimary
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Email,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.padding(end = 8.dp)
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
                    modifier = Modifier.padding(end = 8.dp)
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
