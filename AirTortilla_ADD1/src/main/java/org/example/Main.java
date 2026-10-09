
package org.example;

import java.io.IOException;
import java.util.List;





public class Main {



    public static void main(String[] args) {

        System.out.println("Directorio de trabajo: "
                + System.getProperty("user.dir"));

        LectorCsv lector = new LectorCsv();

        try {

            List<Reserva> reservas = lector.leerReservas("data/reservas.csv");

            for (Reserva reserva : reservas) {
                System.out.println(reserva);
                System.out.println("--------------------");
            }

            System.out.println("Total de reservas: " + reservas.size());

        } catch (IOException e) {
            System.out.println("No se pudo leer el archivo CSV.");
            e.printStackTrace();
        }
    }
}