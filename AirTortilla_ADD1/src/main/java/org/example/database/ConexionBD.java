
package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

public class ConexionBD {

    public static Connection conectar() {
        Properties propiedades = new Properties();

        try {
            InputStream archivo = ConexionBD.class.getClassLoader()
                    .getResourceAsStream("db.properties");

            if (archivo == null) {
                throw new IOException("No se encuentra db.properties");
            }

            propiedades.load(archivo);
            archivo.close();

            String url = propiedades.getProperty("db.url");
            String usuario = propiedades.getProperty("db.username");
            String contrasena = propiedades.getProperty("db.password");

            Connection conexion = DriverManager.getConnection(url, usuario, contrasena);

            System.out.println("Conexion con MySQL establecida correctamente.");

            return conexion;

        } catch (IOException | SQLException e) {
            System.out.println("Error al conectar con MySQL: " + e.getMessage());
            return null;
        }
    }
}
