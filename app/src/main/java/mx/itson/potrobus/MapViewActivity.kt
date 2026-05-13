package mx.itson.potrobus

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class MapViewActivity : AppCompatActivity() {
    private var map: GoogleMap? = null
    private var socket: Socket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        //mapFragment.getMapAsync(this)

        // Socket.IO para obtener la información en tiempo real desde el backend
        socket = IO.socket("IP del BACKEND")
        socket?.connect()
        socket?.on("gps_live") { args ->
            val data = args[0] as JSONObject
            val lat = data.getDouble("lat")
            val lng = data.getDouble("lng")
            val marker = MarkerOptions().position(LatLng(lat, lng)).title("PotroBus Live")
            map?.clear()  // Limpiar marker previos
            map?.addMarker(marker)
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 15f))
        }
    }

    /**
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(27.9269, -110.8992), 14f))  // Centro Guaymas
    }

    override fun onDestroy() {
        super.onDestroy()
        socket?.disconnect()
    }
    **/
}