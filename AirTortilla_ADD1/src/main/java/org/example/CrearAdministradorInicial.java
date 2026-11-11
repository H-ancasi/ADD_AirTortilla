
package org.example;

import org.example.database.UsuarioDAO;

import java.util.Scanner;

public class CrearAdministradorInicial {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        System.out.println("=== CREAR ADMINISTRADOR INICIAL ===");

        System.out.print("Nombre de usuario: ");
        String nombreUsuario = teclado.nextLine();

        System.out.print("Contraseña: ");
        String contrasena = teclado.nextLine();

        Usuario administrador = new Usuario(
                0,
                nombreUsuario,
                contrasena,
                "ADMINISTRADOR"
        );

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        boolean registrado = usuarioDAO.registrarUsuario(administrador);

        if (registrado) {
            System.out.println("Administrador creado correctamente.");
        } else {
            System.out.println("No se pudo crear el administrador.");
        }

        teclado.close();
    }
}
