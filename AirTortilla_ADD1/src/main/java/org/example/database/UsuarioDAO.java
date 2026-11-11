
package org.example.database;

import org.example.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public boolean registrarUsuario(Usuario usuario) {

        String sql = "INSERT INTO usuarios (nombreUsuario, password, rol) VALUES (?, ?, ?)";

        String contrasenaHash = BCrypt.hashpw(
                usuario.getContrasena(),
                BCrypt.gensalt()
        );

        Connection conexion = ConexionBD.conectar();

        if (conexion == null) {
            return false;
        }

        try (
                conexion;
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {

            sentencia.setString(1, usuario.getNombreUsuario());
            sentencia.setString(2, contrasenaHash);
            sentencia.setString(3, usuario.getRol());

            int filasInsertadas = sentencia.executeUpdate();

            return filasInsertadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al registrar el usuario: " + e.getMessage());
            return false;
        }
    }

    public Usuario iniciarSesion(String nombreUsuario, String contrasena) {

        String sql = "SELECT * FROM usuarios WHERE nombreUsuario = ?";

        Connection conexion = ConexionBD.conectar();

        if (conexion == null) {
            return null;
        }

        try (
                conexion;
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {

            sentencia.setString(1, nombreUsuario);

            try (ResultSet resultado = sentencia.executeQuery()) {

                if (resultado.next()) {

                    String contrasenaHash = resultado.getString("password");

                    // Comprobamos la contraseña introducida
                    if (BCrypt.checkpw(contrasena, contrasenaHash)) {

                        return new Usuario(
                                resultado.getInt("id"),
                                resultado.getString("nombreUsuario"),
                                contrasenaHash,
                                resultado.getString("rol")
                        );
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al iniciar sesión: " + e.getMessage());
        }

        return null;
    }
}
