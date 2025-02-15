package com.example.todolistapp.presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// États possibles pour l'authentification
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    // 🔹 Fonction pour s'inscrire avec Email/Mot de passe
    fun signUp(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    _authState.value = if (task.isSuccessful) {
                        AuthState.Success
                    } else {
                        AuthState.Error(task.exception?.message ?: "Inscription échouée")
                    }
                }
        }
    }

    // 🔹 Fonction pour se connecter avec Email/Mot de passe
    fun signIn(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    _authState.value = if (task.isSuccessful) {
                        AuthState.Success
                    } else {
                        AuthState.Error(task.exception?.message ?: "Erreur de connexion")
                    }
                }
        }
    }
}
