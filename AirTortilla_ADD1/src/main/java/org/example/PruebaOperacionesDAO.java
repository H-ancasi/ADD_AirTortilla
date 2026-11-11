
package org.example;

import org.example.database.ReservaDAO;

import java.time.LocalDate;
import java.util.List;

public class PruebaOperacionesDAO {

    public static void main(String[] args) {

        ReservaDAO reservaDAO = new ReservaDAO();


        Reserva nuevaReserva = new Reserva(
                "0",
                null,
                "ES",
                "FR",
                "PRUEBA DAO AIR TORTILLA",
                LocalDate.of(2026, 11, 9)
        );

        boolean insertada = reservaDAO.insertar(nuevaReserva);

        if (insertada) {
            System.out.println("Reserva insertada correctamente.");
            System.out.println("ID generado: " + nuevaReserva.getId());
        } else {
            System.out.println("No se pudo insertar la reserva.");
            return;
        }


        List<Reserva> reservas = reservaDAO.obtenerTodas();

        String idReserva = null;

        for (Reserva reserva : reservas) {
            if (reserva.getNombre().equals("PRUEBA DAO AIR TORTILLA")) {
                idReserva = reserva.getId();
                break;
            }
        }

        if (idReserva == null) {
            System.out.println("No se ha encontrado la reserva de prueba.");
            return;
        }

        System.out.println("ID de la reserva: " + idReserva);


        Reserva reservaModificada = new Reserva(
                idReserva,
                null,
                "ES",
                "IT",
                "PRUEBA DAO MODIFICADA",
                LocalDate.of(2026, 11, 10)
        );

        boolean actualizada = reservaDAO.actualizar(reservaModificada);

        if (actualizada) {
            System.out.println("Reserva modificada correctamente.");
        } else {
            System.out.println("No se pudo modificar la reserva.");
        }


        boolean eliminada = reservaDAO.eliminar(idReserva);

        if (eliminada) {
            System.out.println("Reserva eliminada correctamente.");
        } else {
            System.out.println("No se pudo eliminar la reserva.");
        }
    }
}
