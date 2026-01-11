package com.example.edugard.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.edugard.domain.model.Student

data class StudentState(
    val students: List<Student> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedStudent: Student? = null
)

class StudentViewModel : ViewModel() {
    private val _state = MutableStateFlow(StudentState())
    val state: StateFlow<StudentState> = _state.asStateFlow()

    init {
        loadDummyStudents()
    }

    private fun loadDummyStudents() {
        val dummyStudents = listOf(
            Student(
                name = "John Doe",
                grade = "Grade 10",
                studentId = "STU001",
                email = "john.doe@school.com",
                phoneNumber = "+256700000001"
            ),
            Student(
                name = "Jane Smith",
                grade = "Grade 11",
                studentId = "STU002",
                email = "jane.smith@school.com",
                phoneNumber = "+256700000002"
            ),
            Student(
                name = "Bob Johnson",
                grade = "Grade 10",
                studentId = "STU003",
                email = "bob.johnson@school.com",
                phoneNumber = "+256700000003"
            ),
            Student(
                name = "Alice Williams",
                grade = "Grade 12",
                studentId = "STU004",
                email = "alice.williams@school.com",
                phoneNumber = "+256700000004"
            ),
            Student(
                name = "Charlie Brown",
                grade = "Grade 11",
                studentId = "STU005",
                email = "charlie.brown@school.com",
                phoneNumber = "+256700000005"
            )
        )

        _state.update { it.copy(students = dummyStudents) }
    }

    fun addStudent(student: Student) {
        _state.update { currentState ->
            currentState.copy(
                students = currentState.students + student
            )
        }
    }

    fun updateStudent(student: Student) {
        _state.update { currentState ->
            currentState.copy(
                students = currentState.students.map {
                    if (it.id == student.id) student else it
                }
            )
        }
    }

    fun deleteStudent(studentId: String) {
        _state.update { currentState ->
            currentState.copy(
                students = currentState.students.filter { it.id != studentId }
            )
        }
    }

    fun selectStudent(student: Student?) {
        _state.update { it.copy(selectedStudent = student) }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
