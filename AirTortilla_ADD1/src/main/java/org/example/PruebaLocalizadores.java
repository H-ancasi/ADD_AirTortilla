package org.example;

import org.example.database.ReservaDAO;

public class PruebaLocalizadores {

    public static void main(String[] args) {

        ReservaDAO reservaDAO = new ReservaDAO();

        boolean resultado = reservaDAO.actualizarLocalizadores();

        if (resultado) {
            System.out.println("Localizadores actualizados correctamente.");
        } else {
            System.out.println("No se pudieron actualizar los localizadores.");
        }
    }
}