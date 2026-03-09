package com.sifu.passdatascreen.locatioin

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen() {
    // --- State ---
    val defaultLocation = LatLng(37.7749, -122.4194)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 10f)
    }

    var selectedMapType by remember { mutableStateOf(MapType.NORMAL) }
    var showTraffic    by remember { mutableStateOf(false) }
    var showBuildings  by remember { mutableStateOf(true) }
    var selectedMarker by remember { mutableStateOf<MapMarker?>(null) }
    var showControls   by remember { mutableStateOf(true) }

    val uiSettings = rememberUpdatedState(
        MapUiSettings(
            zoomControlsEnabled    = true,
            compassEnabled         = true,
            myLocationButtonEnabled = false,
            mapToolbarEnabled      = true,
            rotationGesturesEnabled = true,
            tiltGesturesEnabled    = true,
            scrollGesturesEnabled  = true,
            zoomGesturesEnabled    = true,
        )
    )

    val mapProperties = rememberUpdatedState(
        MapProperties(
            mapType          = selectedMapType,
            isTrafficEnabled = showTraffic,
            isBuildingEnabled = showBuildings,
            isMyLocationEnabled = false,
            minZoomPreference = 5f,
            maxZoomPreference = 20f,
        )
    )

    // --- UI ---
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "🗺️  Google Maps – Compose",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                actions = {
                    IconButton(onClick = { showControls = !showControls }) {
                        Icon(
                            imageVector = if (showControls) Icons.Default.Email else Icons.Default.MailOutline,
                            contentDescription = "Toggle controls"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor    = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White,
                )
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // ── Map ──────────────────────────────────────────────────────────
            GoogleMap(
                modifier              = Modifier.fillMaxSize(),
                cameraPositionState   = cameraPositionState,
                uiSettings            = uiSettings.value,
                properties            = mapProperties.value,
                onMapClick            = { selectedMarker = null },
            ) {
                sampleMarkers.forEach { marker ->
                    Marker(
                        state   = MarkerState(position = marker.position),
                        title   = marker.title,
                        snippet = marker.snippet,
                        icon    = BitmapDescriptorFactory.defaultMarker(marker.hue),
                        onClick = {
                            selectedMarker = marker
                            false   // return false → show default info window
                        }
                    )
                }
            }

            // ── Control Panel ────────────────────────────────────────────────
            if (showControls) {
                ControlPanel(
                    modifier          = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp),
                    selectedMapType   = selectedMapType,
                    showTraffic       = showTraffic,
                    showBuildings     = showBuildings,
                    onMapTypeChange   = { selectedMapType = it },
                    onTrafficToggle   = { showTraffic = !showTraffic },
                    onBuildingsToggle = { showBuildings = !showBuildings },
                )
            }

            // ── Zoom buttons ─────────────────────────────────────────────────
            ZoomButtons(
                modifier    = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 12.dp, bottom = if (selectedMarker != null) 160.dp else 24.dp),
                onZoomIn    = { cameraPositionState.move(CameraUpdateFactory.zoomIn()) },
                onZoomOut   = { cameraPositionState.move(CameraUpdateFactory.zoomOut()) },
                onReset     = {
                    cameraPositionState.move(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition.fromLatLngZoom(defaultLocation, 10f)
                        )
                    )
                }
            )

            // ── Marker Info Card ─────────────────────────────────────────────
            selectedMarker?.let { marker ->
                MarkerInfoCard(
                    marker     = marker,
                    modifier   = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    onDismiss  = { selectedMarker = null },
                    onNavigate = {
                        cameraPositionState.move(
                            CameraUpdateFactory.newCameraPosition(
                                CameraPosition.fromLatLngZoom(marker.position, 14f)
                            )
                        )
                    }
                )
            }
        }
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// Control Panel
// ──────────────────────────────────────────────────────────────────────────────

@Composable
private fun ControlPanel(
    modifier          : Modifier,
    selectedMapType   : MapType,
    showTraffic       : Boolean,
    showBuildings     : Boolean,
    onMapTypeChange   : (MapType) -> Unit,
    onTrafficToggle   : () -> Unit,
    onBuildingsToggle : () -> Unit,
) {
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Map Type", fontWeight = FontWeight.Bold, fontSize = 13.sp)

            mapTypeOptions.forEach { (label, type) ->
                MapTypeChip(
                    label    = label,
                    selected = selectedMapType == type,
                    onClick  = { onMapTypeChange(type) }
                )
            }

            Divider(color = Color.LightGray, thickness = 0.5.dp)

            Text("Layers", fontWeight = FontWeight.Bold, fontSize = 13.sp)

            LayerToggleRow("Traffic",   showTraffic,   Icons.Default.CheckCircle,  onTrafficToggle)
            LayerToggleRow("Buildings", showBuildings, Icons.Default.Add, onBuildingsToggle)
        }
    }
}

private val mapTypeOptions = listOf(
    "Normal"    to MapType.NORMAL,
    "Satellite" to MapType.SATELLITE,
    "Hybrid"    to MapType.HYBRID,
    "Terrain"   to MapType.TERRAIN,
)

@Composable
private fun MapTypeChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick  = onClick,
        label    = { Text(label, fontSize = 12.sp) },
        modifier = Modifier.fillMaxWidth(),
        colors   = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor     = Color.White,
        )
    )
}

@Composable
private fun LayerToggleRow(label: String, checked: Boolean, icon: androidx.compose.ui.graphics.vector.ImageVector, onToggle: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier          = Modifier.fillMaxWidth()
    ) {
        Icon(icon, contentDescription = label, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(6.dp))
        Text(label, fontSize = 12.sp, modifier = Modifier.weight(1f))
        Switch(
            checked         = checked,
            onCheckedChange = { onToggle() },
            modifier        = Modifier.size(36.dp),
        )
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// Zoom Buttons
// ──────────────────────────────────────────────────────────────────────────────

@Composable
private fun ZoomButtons(
    modifier  : Modifier,
    onZoomIn  : () -> Unit,
    onZoomOut : () -> Unit,
    onReset   : () -> Unit,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        FloatingActionButton(onClick = onZoomIn,  containerColor = MaterialTheme.colorScheme.primary) {
            Icon(Icons.Default.Add, contentDescription = "Zoom In", tint = Color.White)
        }
        FloatingActionButton(onClick = onZoomOut, containerColor = MaterialTheme.colorScheme.primary) {
            Icon(Icons.Default.Delete, contentDescription = "Zoom Out", tint = Color.White)
        }
        FloatingActionButton(onClick = onReset,   containerColor = Color.White) {
            Icon(Icons.Default.ThumbUp, contentDescription = "Reset", tint = MaterialTheme.colorScheme.primary)
        }
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// Marker Info Card
// ──────────────────────────────────────────────────────────────────────────────

@Composable
private fun MarkerInfoCard(
    marker     : MapMarker,
    modifier   : Modifier,
    onDismiss  : () -> Unit,
    onNavigate : () -> Unit,
) {
    Card(
        modifier  = modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(12.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Row(
            modifier            = Modifier.padding(16.dp),
            verticalAlignment   = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                tint               = MaterialTheme.colorScheme.primary,
                modifier           = Modifier.size(36.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(marker.title,   fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(marker.snippet, fontSize = 13.sp, color = Color.Gray)
                Text(
                    "%.4f, %.4f".format(marker.position.latitude, marker.position.longitude),
                    fontSize = 11.sp, color = Color.LightGray
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                IconButton(onClick = onNavigate) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Fly to", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                }
            }
        }
    }
}
