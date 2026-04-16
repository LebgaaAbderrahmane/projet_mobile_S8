package com.queuemanager.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.queuemanager.app.domain.model.UserRole
import com.queuemanager.app.ui.auth.ForgotPasswordScreen
import com.queuemanager.app.ui.auth.LoginScreen
import com.queuemanager.app.ui.auth.SignUpScreen
import com.queuemanager.app.ui.navigation.MainScreen
import com.queuemanager.app.ui.navigation.SplashViewModel
import com.queuemanager.app.ui.profile.ChangePasswordScreen
import com.queuemanager.app.ui.profile.PersonalInfoScreen

@Composable
fun NavGraph(
    startDestination: String = "splash"
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("splash") {
            val viewModel: SplashViewModel = hiltViewModel()
            val authState by viewModel.authState.collectAsState()

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = com.queuemanager.app.ui.theme.PrimaryTeal)
            }

            LaunchedEffect(authState) {
                when (val state = authState) {
                    is SplashViewModel.AuthState.Authenticated -> {
                        navController.navigate("main/${state.role.name}") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                    is SplashViewModel.AuthState.Unauthenticated -> {
                        navController.navigate("welcome") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                    else -> Unit
                }
            }
        }

        composable("welcome") {
            com.queuemanager.app.ui.auth.WelcomeScreen(
                onLoginClick = { navController.navigate("login") },
                onSignUpClick = { navController.navigate("signup") }
            )
        }

        composable("login") {
            LoginScreen(
                onSignUpClick = { navController.navigate("signup") },
                onLoginSuccess = { role ->
                    navController.navigate("main/${role.name}") {
                        popUpTo("auth") { inclusive = true }
                    }
                },
                onForgotPasswordClick = { navController.navigate("forgot_password") }
            )
        }

        composable("forgot_password") {
            ForgotPasswordScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("signup") {
            SignUpScreen(
                onLoginClick = { navController.popBackStack() },
                onSignUpSuccess = { role ->
                    navController.navigate("main/${role.name}") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }

        composable("onboarding") {
            com.queuemanager.app.ui.onboarding.OnboardingWizardScreen(
                onFinish = {
                    navController.navigate("main/${UserRole.BUSINESS_OWNER.name}") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "main/{role}",
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val roleName = backStackEntry.arguments?.getString("role")
            val role = UserRole.valueOf(roleName ?: UserRole.CUSTOMER.name)
            
            MainScreen(
                userRole = role,
                onLogout = {
                    navController.navigate("splash") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToPersonalInfo = { navController.navigate("profile/personal_info") },
                onNavigateToChangePassword = { navController.navigate("profile/change_password") }
            )
        }
        
        composable("profile/personal_info") {
            PersonalInfoScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable("profile/change_password") {
            ChangePasswordScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
