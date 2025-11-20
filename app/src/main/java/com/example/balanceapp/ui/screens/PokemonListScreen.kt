package com.example.balanceapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
// Pantalla PRINCIPAL de la Pokédex: lista de Pokémon + detalle en diálogo.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    onBack: () -> Unit,
    viewModel: PokemonListViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    // Cada vez que cambia, la UI se recompone.
    val estado by viewModel.estado.collectAsState()
    // Estados de búsqueda en la propia pantalla.
    var buscando by remember { mutableStateOf(false) } // para mostrar/ocultar el buscador
    var consulta by remember { mutableStateOf("") }

    // Lista de pokémon a mostrar, filtrada según la consulta.
    val itemsParaMostrar = remember(estado.pokemones, consulta) {
        if (consulta.isBlank()) estado.pokemones else estado.pokemones.filter { it.name.contains(consulta, true) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pokedex") },
                actions = {
                    // Icono de búsqueda en la barra superior.
                    IconButton(onClick = { buscando = !buscando }) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "Buscar")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            // Si está activada la búsqueda, mostramos el campo de texto.
            if (buscando) {
                OutlinedTextField(
                    value = consulta,
                    onValueChange = { consulta = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    placeholder = { Text("Buscar Pokémon...") }
                )
            }
            // Distintos estados de la pantalla
            when {
                // Modo cargando: muestra el spinner al centro.
                estado.cargando -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }

                // estado error, mostrar el mensaje.
                estado.error != null -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { Text(estado.error ?: "Error") }

                // estado normal mostrar la lista de pokemon
                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // items() recorre la lista de Pokémon y dibuja cada fila
                    items(itemsParaMostrar) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                                .clickable {
                                    // Al hacer clic en un Pokémon, pedimos el detalle al ViewModel
                                    viewModel.cargarDetalle(item.id) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // imagen del pokemon
                            AsyncImage(
                                model = item.imageUrl,
                                contentDescription = item.name,
                                modifier = Modifier.height(56.dp),
                                contentScale = ContentScale.Fit
                            )
                            // Un pequeño espacio entre imagen y texto
                            Spacer(modifier = Modifier.height(0.dp).weight(0.05f))

                            // Texto con id y nombre del Pokémon.
                            Text(
                                text = "${item.id}. ${item.name}",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
        // Si el detalle se está cargando, mostramos otro spinner encima
        if (estado.detalleCargando) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        // Si hay un detalle seleccionado, mostramos el AlertDialog con la info del pokemon
        estado.detalleSeleccionado?.let { detail ->
            AlertDialog(
                onDismissRequest = { viewModel.cerrarDetalle() },
                confirmButton = {
                    TextButton(onClick = {
                        // al cerrar el diálogo, limpiamos el detalle en el ViewModel
                        viewModel.cerrarDetalle() }) { Text("Cerrar") }
                },
                title = { Text(text = "${detail.id}. ${detail.name}") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        // imagen más grande del pokemon
                        AsyncImage(
                            model = detail.imageUrl,
                            contentDescription = detail.name,
                            modifier = Modifier.height(120.dp).fillMaxWidth(),
                            contentScale = ContentScale.Fit
                        )
                        Text("Altura: ${detail.height}")
                        Text("Peso: ${detail.weight}")

                        // tipos del pokemon como chips
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            detail.types.forEach { t ->
                                AssistChip(onClick = {}, label = { Text(t) })
                            }
                        }
                        // estadisticas básicas: Ataque, Defensa, etc
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            detail.stats.forEach { (name, value) ->
                                // Capitalizamos la primera letra del nombre de la stat.
                                Text("${name.replaceFirstChar { it.titlecase() }}: ${value}")
                            }
                        }
                    }
                }
            )
        }
    }
}


