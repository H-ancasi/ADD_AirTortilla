
package org.example;

import org.example.database.ReservaDAO;

import java.util.List;

public class PruebaReservas {

    public static void main(String[] args) {

        ReservaDAO reservaDAO = new ReservaDAO();

        List<Reserva> reservas = reservaDAO.obtenerTodas();

        System.out.println("RESERVAS ALMACENADAS EN MYSQL");
        System.out.println("-----------------------------");

        for (Reserva reserva : reservas) {
            System.out.println(reserva);
        }

        System.out.println("-----------------------------");
        System.out.println("Total de reservas: " + reservas.size());
    }
}
