package com.example.renner;

import android.Manifest; // nomes das permissões do Android
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.os.Looper;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import java.util.Map;
public class MainActivity4 extends AppCompatActivity {
    private TextView textGps;
    private WebView webViewMapa;
    private FusedLocationProviderClient fusedLocationClient;
    private ActivityResultLauncher<String[]> localizacaoLauncher;
    private LocationCallback locationCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main4);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                (v, insets) -> {
                    Insets systemBars =
                            insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top,

                            systemBars.right, systemBars.bottom);
                    return insets;
                });
        textGps = findViewById(R.id.textGps);
        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);
// Carrega o mapa.html e permite que ele execute JavaScript
        webViewMapa = findViewById(R.id.webViewMapa);
        WebSettings webSettings = webViewMapa.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true); // ESSENCIAL: sem isso o mapa fica em branco
// Captura erros de JavaScript do mapa.html e manda pro Logcat
// (filtre por "MapaJS" no Logcat para diagnosticar problemas)
        webViewMapa.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("MapaJS", consoleMessage.message()
                        + " -- linha " + consoleMessage.lineNumber());
                return true;
            }
        });
        webViewMapa.loadUrl("file:///android_asset/mapa.html");
// Ouvinte que recebe a localização e atualiza o texto + o mapa
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;
                for (android.location.Location location :
                        locationResult.getLocations()) {
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();

                    textGps.setText("Lat: " + lat + " | Long: " + lng);
// Chama updateLocation() dentro do mapa.html
                    webViewMapa.evaluateJavascript(
                            "updateLocation(" + lat + ", " + lng + ")", null);
                }
                fusedLocationClient.removeLocationUpdates(locationCallback);
            }
        };
        localizacaoLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                (Map<String, Boolean> resultado) -> {
                    Boolean fineConcedida =
                            resultado.get(Manifest.permission.ACCESS_FINE_LOCATION);
                    Boolean coarseConcedida =
                            resultado.get(Manifest.permission.ACCESS_COARSE_LOCATION);
                    if (Boolean.TRUE.equals(fineConcedida)
                            || Boolean.TRUE.equals(coarseConcedida)) {
                        solicitarAtualizacaoLocalizacao();
                    } else {
                        textGps.setText("Permissão de localização negada");
                    }
                }
        );
        Button botaoLocalizacao = findViewById(R.id.btnPegarLocalizacao);
        botaoLocalizacao.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(this,

                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                solicitarAtualizacaoLocalizacao();
            } else {
                localizacaoLauncher.launch(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                });
            }
        });
        Button button = findViewById(R.id.button15);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity4.this, MainActivity.class);
            startActivity(intent);
        });
        Button button2 = findViewById(R.id.button18);
        button2.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity4.this, MainActivity2.class);
            startActivity(intent);
        });
        Button button3 = findViewById(R.id.button19);
        button3.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity4.this, sacola.class);
            startActivity(intent);
        });
        Button button4 = findViewById(R.id.button14);
        button4.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity4.this, sacola.class);
            startActivity(intent);
        });
        Button button5 = findViewById(R.id.button20);
        button5.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity4.this, sacola.class);
            startActivity(intent);
        });
    }
    private void solicitarAtualizacaoLocalizacao() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        textGps.setText("Buscando localização...");
        LocationRequest locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 2000)
                .setMaxUpdates(1)
                .build();
        fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
        );
    }
}