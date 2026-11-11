
package org.example;

import org.example.database.UsuarioDAO;
import java.util.Scanner;

public class CrearEmpleadoPrueba {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== CREAR EMPLEADO ===");

        System.out.print("Nombre de usuario: ");
        String nombreUsuario = scanner.nextLine();

        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();

        Usuario empleado = new Usuario(
                0,
                nombreUsuario,
                contrasena,
                "EMPLEADO"
        );

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        if (usuarioDAO.registrarUsuario(empleado)) {
            System.out.println("Empleado creado correctamente.");
        } else {
            System.out.println("No se ha podido crear el empleado.");
        }

        scanner.close();
    }
}
