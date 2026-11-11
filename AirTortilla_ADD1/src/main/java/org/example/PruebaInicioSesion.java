
package org.example;

import org.example.database.UsuarioDAO;

import java.util.Scanner;

public class PruebaInicioSesion {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        System.out.println("=== INICIO DE SESION AIR TORTILLA ===");

        System.out.print("Nombre de usuario: ");
        String nombreUsuario = teclado.nextLine();

        System.out.print("Contraseña: ");
        String contrasena = teclado.nextLine();

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        Usuario usuario = usuarioDAO.iniciarSesion(nombreUsuario, contrasena);

        if (usuario != null) {

            System.out.println("\nInicio de sesion correcto.");
            System.out.println("Bienvenido, " + usuario.getNombreUsuario());
            System.out.println("Rol: " + usuario.getRol());

        } else {

            System.out.println("\nNombre de usuario o contraseña incorrectos.");

        }

        teclado.close();
    }
}
