
package org.example.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class HistorialDAO {

    public boolean registrarCambio(int usuarioId, Integer reservaId,
                                   String accion, String detalles) {

        String sql = "INSERT INTO historial_cambios " +
                "(usuario_id, reserva_id, accion, detalles) VALUES (?, ?, ?, ?)";

        Connection conexion = ConexionBD.conectar();

        if (conexion == null) return false;

        try (conexion; PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, usuarioId);

            if (reservaId == null) {
                sentencia.setNull(2, Types.INTEGER);
            } else {
                sentencia.setInt(2, reservaId);
            }

            sentencia.setString(3, accion);
            sentencia.setString(4, detalles);

            int filasInsertadas = sentencia.executeUpdate();

            return filasInsertadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al registrar el cambio: " + e.getMessage());
            return false;
        }
    }

    public List<String> obtenerHistorial() {

        List<String> historial = new ArrayList<>();

        String sql = "SELECT h.id, u.nombreUsuario, h.reserva_id, " +
                "h.accion, h.detalles, h.fechaHora " +
                "FROM historial_cambios h " +
                "INNER JOIN usuarios u ON h.usuario_id = u.id " +
                "ORDER BY h.fechaHora DESC, h.id DESC";

        Connection conexion = ConexionBD.conectar();

        if (conexion == null) return historial;

        try (conexion; PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {

                String reservaId = resultado.getString("reserva_id");

                if (reservaId == null) {
                    reservaId = "Reserva eliminada";
                }

                String cambio = "ID: " + resultado.getInt("id") +
                        " | Usuario: " + resultado.getString("nombreUsuario") +
                        " | Reserva: " + reservaId +
                        " | Acción: " + resultado.getString("accion") +
                        " | Detalles: " + resultado.getString("detalles") +
                        " | Fecha: " + resultado.getTimestamp("fechaHora");

                historial.add(cambio);
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar el historial: " + e.getMessage());
        }

        return historial;
    }
}
