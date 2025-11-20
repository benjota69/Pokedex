package com.example.balanceapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.balanceapp.ui.screens.PokemonListScreen
import com.example.balanceapp.ui.screens.LoginScreen
import com.example.balanceapp.ui.screens.RegistroScreen
import com.example.balanceapp.ui.screens.WelcomeScreen

// Objeto donde guardamos las rutas de cada pantalla.
object Routes {
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val WELCOME = "welcome"
    const val POKEMON_LIST = "pokemon_list"
}

// Esta función arma toda la navegación de la app.
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    // Controlador de navegación.
    val navController = rememberNavController()

    // NavHost: aquí definimos la pantalla inicial y cada ruta de la app.
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN,
        modifier = modifier
    ) {
        // pantalla login
        composable(Routes.LOGIN) {
            // Llamamos al composable LoginScreen
            LoginScreen(
                onLoginExitoso = {
                    navController.navigate(Routes.WELCOME) {
                        popUpTo(0) // limpia el historial para que no se pueda volver atrás al login.
                    }
                },
                onIrARegistro = {
                    // Si el usuario no tiene cuenta, lo mandamos a Registro.
                    navController.navigate(Routes.REGISTRO)
                }
            )
        }
        // pantalla de registro
        composable(Routes.REGISTRO) {

            RegistroScreen(
                // Si el registro es correcto, lo mandamos a Welcome
                onRegistroExitoso = {
                    navController.navigate(Routes.WELCOME) {
                        popUpTo(0)
                    }
                },
                onVolverALogin = {
                    // Vuelve a la pantalla anterior
                    navController.navigateUp()
                }
            )
        }// pantalla de bienvenida
        composable(Routes.WELCOME) {
            //pantalla de bienvenida, solo tiene un botón para continuar.
            WelcomeScreen(onGetStartedClick = {
                // Cuando aprieta "Get Started", manda a la lista de Pokémon.
                navController.navigate(Routes.POKEMON_LIST) })
        }
        composable(Routes.POKEMON_LIST) {
            PokemonListScreen(onBack = {
                // Si queremos volver atrás desde la lista
                navController.navigateUp() })
        }
    }
}


