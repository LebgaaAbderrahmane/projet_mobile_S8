package com.queuemanager.app.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.queuemanager.app.domain.model.UserRole
import com.queuemanager.app.ui.theme.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.queuemanager.app.ui.theme.*

@Composable
fun SignUpScreen(
    onLoginClick: () -> Unit,
    onSignUpSuccess: (com.queuemanager.app.domain.model.UserRole) -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val firstName by viewModel.firstName
    val lastName by viewModel.lastName
    val email by viewModel.email
    val phone by viewModel.phone
    val password by viewModel.password
    val role by viewModel.role
    val isLoading by viewModel.isLoading
    val error by viewModel.error
    
    var passwordVisible by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .imePadding()
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
            ScreenHeader(
                title = "Create your account",
                subtitle = "Begin your journey to effortless queuing."
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(32.dp),
                color = SurfaceColor,
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RoleSwitcher(
                        selectedRole = role,
                        onRoleSelected = viewModel::onRoleChange
                    )
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    AppTextField(
                        value = firstName,
                        onValueChange = viewModel::onFirstNameChange,
                        label = "First Name",
                        placeholder = "Julian"
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    AppTextField(
                        value = lastName,
                        onValueChange = viewModel::onLastNameChange,
                        label = "Last Name",
                        placeholder = "Vance"
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    AppTextField(
                        value = email,
                        onValueChange = viewModel::onEmailChange,
                        label = "Email Address",
                        placeholder = "julian@email.com",
                        leadingIcon = Icons.Default.Email
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    AppTextField(
                        value = phone,
                        onValueChange = viewModel::onPhoneChange,
                        label = "Phone Number",
                        placeholder = "+1 (555) 000-0000",
                        leadingIcon = Icons.Default.Phone
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    AppTextField(
                        value = password,
                        onValueChange = viewModel::onPasswordChange,
                        label = "Secure Password",
                        placeholder = "••••••••••••",
                        leadingIcon = Icons.Default.Lock,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = SlateGrey
                                )
                            }
                        }
                    )
                    
                    if (error != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = error!!, color = MaterialTheme.colorScheme.error)
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    AppButton(
                        text = if (isLoading) "Creating Account..." else "Create Account",
                        onClick = { viewModel.signUp(onSignUpSuccess) },
                        enabled = !isLoading,
                        showArrow = true
                    )
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Already have an account? ", color = SlateGrey)
                        Text(
                            text = "Sign In",
                            color = PrimaryTeal,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onLoginClick() }
                        )
                    }
                }
            }
            
            // Extra dynamic spacer for keyboard
            Spacer(modifier = Modifier.padding(bottom = WindowInsets.ime.asPaddingValues().calculateBottomPadding()).height(24.dp))
        }
    }
}
