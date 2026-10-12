
package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

public class LectorCsv {

    public List<Reserva> leerReservas(String rutaArchivo) throws IOException {

        List<Reserva> reservas = new ArrayList<>();

        List<String> lineas = Files.readAllLines(Path.of(rutaArchivo));

        for (int i = 1; i < lineas.size(); i++) {

            String linea = lineas.get(i);

            if (linea.isBlank()) {
                continue;
            }

            String[] campos = linea.split(",", -1);

            if (campos.length != 4) {
                throw new IllegalArgumentException(
                        "Formato incorrecto en la línea " + (i + 1)
                );
            }

            LocalDate fecha = LocalDate.parse(campos[0].trim());
            String origen = campos[1].trim();
            String destino = campos[2].trim();
            String nombre = campos[3].trim();

            Reserva reserva = new Reserva(
                    "RES-" + String.format("%03d", reservas.size() + 1),
                    "",
                    origen,
                    destino,
                    nombre,
                    fecha
            );

            reservas.add(reserva);
        }

        Map<String, String> localizadores = new HashMap<>();
        int siguienteLocalizador = 1;

        for (Reserva reserva : reservas) {

            String clave = reserva.getNombre().trim().toLowerCase(Locale.ROOT)
                    + "|" + reserva.getLetraOrigen().trim().toUpperCase(Locale.ROOT)
                    + "|" + reserva.getLetraDestino().trim().toUpperCase(Locale.ROOT);

            String localizador = localizadores.get(clave);

            if (localizador == null) {
                localizador = "LOC-" + String.format("%03d", siguienteLocalizador);
                localizadores.put(clave, localizador);
                siguienteLocalizador++;
            }

            reserva.setLocalizador(localizador);
        }

        return reservas;
    }
}
