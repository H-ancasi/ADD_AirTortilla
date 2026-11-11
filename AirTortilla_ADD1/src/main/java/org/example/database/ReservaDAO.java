
package org.example.database;

import org.example.Reserva;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

public class ReservaDAO {

    public List<Reserva> obtenerTodas() {

        List<Reserva> reservas = new ArrayList<>();

        String sql = "SELECT * FROM reservas ORDER BY id";

        Connection conexion = ConexionBD.conectar();

        if (conexion == null) {
            return reservas;
        }

        try (
                conexion;
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()
        ) {

            while (resultado.next()) {

                Reserva reserva = new Reserva(
                        String.valueOf(resultado.getInt("id")),
                        resultado.getString("localizador"),
                        resultado.getString("origen"),
                        resultado.getString("destino"),
                        resultado.getString("nombrePasajero"),
                        resultado.getDate("fechaReserva").toLocalDate()
                );

                reservas.add(reserva);
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar las reservas: " + e.getMessage());
        }

        return reservas;
    }




    public boolean insertar(Reserva reserva) {

        String sql = "INSERT INTO reservas (localizador, fechaReserva, origen, destino, nombrePasajero) "
                + "VALUES (?, ?, ?, ?, ?)";

        Connection conexion = ConexionBD.conectar();

        if (conexion == null) {
            return false;
        }

        try (
                conexion;
                PreparedStatement sentencia = conexion.prepareStatement(
                        sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            sentencia.setString(1, reserva.getLocalizador());
            sentencia.setDate(2, java.sql.Date.valueOf(reserva.getFechaReserva()));
            sentencia.setString(3, reserva.getLetraOrigen());
            sentencia.setString(4, reserva.getLetraDestino());
            sentencia.setString(5, reserva.getNombre());

            int filasInsertadas = sentencia.executeUpdate();

            if (filasInsertadas > 0) {

                try (ResultSet resultado = sentencia.getGeneratedKeys()) {

                    if (resultado.next()) {
                        reserva.setId(String.valueOf(resultado.getInt(1)));
                        return true;
                    }
                }
            }

            return false;

        } catch (SQLException e) {
            System.out.println("Error al insertar la reserva: " + e.getMessage());
            return false;
        }
    }



    public boolean actualizar(Reserva reserva) {

        String sql = "UPDATE reservas SET localizador = ?, fechaReserva = ?, "
                + "origen = ?, destino = ?, nombrePasajero = ? WHERE id = ?";

        Connection conexion = ConexionBD.conectar();

        if (conexion == null) {
            return false;
        }

        try (
                conexion;
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {

            sentencia.setString(1, reserva.getLocalizador());
            sentencia.setDate(2, java.sql.Date.valueOf(reserva.getFechaReserva()));
            sentencia.setString(3, reserva.getLetraOrigen());
            sentencia.setString(4, reserva.getLetraDestino());
            sentencia.setString(5, reserva.getNombre());
            sentencia.setInt(6, Integer.parseInt(reserva.getId()));

            int filasModificadas = sentencia.executeUpdate();

            return filasModificadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar la reserva: " + e.getMessage());
            return false;
        }
    }


    public boolean eliminar(String id) {

        String sql = "DELETE FROM reservas WHERE id = ?";

        Connection conexion = ConexionBD.conectar();

        if (conexion == null) {
            return false;
        }

        try (
                conexion;
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(1, Integer.parseInt(id));

            int filasEliminadas = sentencia.executeUpdate();

            return filasEliminadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar la reserva: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarLocalizadores() {

        String sqlConsulta = "SELECT id, nombrePasajero, origen, destino FROM reservas ORDER BY id";
        String sqlActualizar = "UPDATE reservas SET localizador = ? WHERE id = ?";

        Connection conexion = ConexionBD.conectar();

        if (conexion == null) {
            return false;
        }

        Map<String, String> localizadores = new HashMap<>();
        Map<Integer, String> localizadoresPorId = new HashMap<>();

        int siguienteLocalizador = 1;

        try (conexion) {

            try (PreparedStatement consulta = conexion.prepareStatement(sqlConsulta);
                 ResultSet resultado = consulta.executeQuery()) {

                while (resultado.next()) {

                    int id = resultado.getInt("id");

                    String nombre = resultado.getString("nombrePasajero")
                            .trim().toLowerCase(Locale.ROOT);

                    String origen = resultado.getString("origen")
                            .trim().toUpperCase(Locale.ROOT);

                    String destino = resultado.getString("destino")
                            .trim().toUpperCase(Locale.ROOT);

                    String clave = nombre + "|" + origen + "|" + destino;

                    String localizador = localizadores.get(clave);

                    if (localizador == null) {
                        localizador = "LOC-" + String.format("%03d", siguienteLocalizador);
                        localizadores.put(clave, localizador);
                        siguienteLocalizador++;
                    }

                    localizadoresPorId.put(id, localizador);
                }
            }

            try (PreparedStatement actualizacion = conexion.prepareStatement(sqlActualizar)) {

                for (Map.Entry<Integer, String> entrada : localizadoresPorId.entrySet()) {

                    actualizacion.setString(1, entrada.getValue());
                    actualizacion.setInt(2, entrada.getKey());

                    actualizacion.executeUpdate();
                }
            }

            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar los localizadores: " + e.getMessage());
            return false;
        }
    }



}
