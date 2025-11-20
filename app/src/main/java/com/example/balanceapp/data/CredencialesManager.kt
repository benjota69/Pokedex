package com.example.balanceapp.data

// Objeto que administra las credenciales registradas en la app
object CredencialesManager {
    // Mapa que guarda pares: (email y contraseña)
    // Es private para que solo este objeto pueda modificarlo
    private var credencialesRegistradas: MutableMap<String, String> = mutableMapOf()

    // Registrar un usuario
    fun registrar(email: String, clave: String) {
        // Guarda el email en minúsculas para evitar duplicados.
        credencialesRegistradas[email.lowercase()] = clave
    }

    // Validar que las credenciales ingresadas estén correctas
    fun validarCredenciales(email: String, clave: String): Boolean {
        // Accede a la contraseña guardada para ese email (en minúsculas)
        // y compara si coincide con la ingresada
        return credencialesRegistradas[email.lowercase()] == clave
    }

    // Verificar si un email ya fue registrado
    fun existeEmail(email: String): Boolean {
        // Devuelve true si el email existe dentro del mapa de credenciales
        return credencialesRegistradas.containsKey(email.lowercase())
    }
}

