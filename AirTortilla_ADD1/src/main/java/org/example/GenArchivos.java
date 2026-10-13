package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GenArchivos {

    public void generarArchivos(List<Reserva> reservas, String carpetaSalida)
            throws IOException {

        Path directorio = Path.of(carpetaSalida);
        Files.createDirectories(directorio);

        Map<String, List<Reserva>> grupos = new LinkedHashMap<>();

        for (Reserva reserva : reservas) {

            String destino = reserva.getLetraDestino()
                    .trim().toUpperCase(Locale.ROOT);

            LocalDate fecha = reserva.getFechaReserva();

            String clave = destino + "|" + fecha;

            grupos.computeIfAbsent(clave, k -> new ArrayList<>())
                    .add(reserva);
        }

        for (List<Reserva> grupo : grupos.values()) {

            Reserva primera = grupo.get(0);

            String destino = primera.getLetraDestino()
                    .trim().toUpperCase(Locale.ROOT);

            LocalDate fecha = primera.getFechaReserva();

            String nombreArchivo = String.format(
                    "AirTortilla_%s_%04d_%02d_%02d.csv",
                    destino,
                    fecha.getYear(),
                    fecha.getMonthValue(),
                    fecha.getDayOfMonth()
            );

            Path archivo = directorio.resolve(nombreArchivo);

            List<String> lineas = new ArrayList<>();

            lineas.add(
                    "id,localizador,origen,destino,nombrePasajero,fechaReserva"
            );

            for (Reserva reserva : grupo) {

                String linea = reserva.getId() + ","
                        + reserva.getLocalizador() + ","
                        + reserva.getLetraOrigen() + ","
                        + reserva.getLetraDestino() + ","
                        + reserva.getNombre() + ","
                        + reserva.getFechaReserva();

                lineas.add(linea);
            }

            Files.write(archivo, lineas);

            System.out.println("Archivo generado: " + nombreArchivo);
        }

        System.out.println("Total de archivos generados: " + grupos.size());
    }
}
