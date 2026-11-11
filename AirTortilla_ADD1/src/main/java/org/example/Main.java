
package org.example;

import java.io.IOException;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.stream.Stream;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;


import org.example.database.ReservaDAO;
import org.example.database.UsuarioDAO;
import org.example.database.HistorialDAO;



public class Main {

    private static final String CARPETA_SALIDA = "ListasReservas";




    public static void main(String[] args) {

        System.out.println("Directorio de trabajo: "
                + System.getProperty("user.dir"));

        Scanner scanner = new Scanner(System.in);

        boolean salir = false;

        while (!salir) {


            System.out.println("\n=== INICIO DE SESION ===");
            System.out.println("(Deja ambos campos vacios para salir)");

            System.out.print("Nombre de usuario: ");
            String nombreUsuario = scanner.nextLine();

            System.out.print("Contraseña: ");
            String contrasena = scanner.nextLine();

            if (nombreUsuario.isEmpty() && contrasena.isEmpty()) {
                System.out.println("Cerrando AirTortilla...");
                salir = true;
                continue;
            }

            if (nombreUsuario.isEmpty() || contrasena.isEmpty()) {
                System.out.println("Debes introducir usuario y contraseña.");
                continue;
            }

            UsuarioDAO usuarioDAO = new UsuarioDAO();


            Usuario usuario = usuarioDAO.iniciarSesion(
                    nombreUsuario, contrasena);

            if (usuario == null) {
                System.out.println("Usuario o contraseña incorrectos.");
            } else {

                System.out.println("\nBienvenido, "
                        + usuario.getNombreUsuario());

                System.out.println("Rol: " + usuario.getRol());

                boolean cerrarSesion = false;

                while (!cerrarSesion) {

                    int opcion;

                    System.out.println("\n=== AIRTORTILLA ===");

                    System.out.println("1. Generar archivos de reservas");
                    System.out.println("2. Consultar todos los archivos");
                    System.out.println("3. Consultar un archivo linea a linea");

                    if (usuario.getRol().equals("ADMINISTRADOR")) {
                        System.out.println("4. Crear reserva");
                        System.out.println("5. Modificar reserva");
                        System.out.println("6. Eliminar reserva");
                        System.out.println("7. Crear usuario");
                        System.out.println("8. Consultar historial de cambios");
                    }

                    System.out.println("0. Cerrar sesion");
                    System.out.print("Selecciona una opcion: ");

                    String entrada = scanner.nextLine();

                    try {
                        opcion = Integer.parseInt(entrada);
                    } catch (NumberFormatException e) {
                        opcion = -1;
                    }

                    try {
                        switch (opcion) {

                            case 1:
                                generarArchivos();
                                break;

                            case 2:
                                consultarTodos();
                                break;

                            case 3:
                                consultarLineaALinea(scanner);
                                break;

                            case 4:
                                if (usuario.getRol().equals("ADMINISTRADOR")) {
                                    crearReserva(scanner, usuario);
                                } else {
                                    System.out.println("No tienes permisos para esta opcion.");
                                }
                                break;

                            case 5:
                                if (usuario.getRol().equals("ADMINISTRADOR")) {
                                    modificarReserva(scanner, usuario);
                                } else {
                                    System.out.println("No tienes permisos para esta opcion.");
                                }
                                break;

                            case 6:
                                if (usuario.getRol().equals("ADMINISTRADOR")) {
                                    eliminarReserva(scanner, usuario);
                                } else {
                                    System.out.println("No tienes permisos para esta opcion.");
                                }
                                break;

                            case 7:
                                if (usuario.getRol().equals("ADMINISTRADOR")) {
                                    crearUsuario(scanner);
                                } else {
                                    System.out.println("No tienes permisos para esta opcion.");
                                }
                                break;

                            case 8:
                                if (usuario.getRol().equals("ADMINISTRADOR")) {
                                    consultarHistorial();
                                } else {
                                    System.out.println("No tienes permisos para esta opcion.");
                                }
                                break;

                            case 0:
                                System.out.println("Cerrando sesion...");
                                cerrarSesion = true;
                                break;

                            default:
                                System.out.println("Opcion no valida.");
                        }

                    } catch (IOException e) {
                        System.out.println("Error al acceder a los archivos: "
                                + e.getMessage());
                    }
                }
            }
        }

        scanner.close();
    }



    private static void generarArchivos() throws IOException {

        ReservaDAO reservaDAO = new ReservaDAO();

        List<Reserva> reservas = reservaDAO.obtenerTodas();

        if (reservas.isEmpty()) {
            System.out.println("No hay reservas en la base de datos.");
            return;
        }

        GenArchivos generador = new GenArchivos();
        generador.generarArchivos(reservas, CARPETA_SALIDA);
    }



    private static void crearReserva(Scanner scanner, Usuario usuario) {

        System.out.println("\n=== CREAR RESERVA ===");

        try {
            System.out.print("Fecha de reserva (AAAA-MM-DD): ");
            LocalDate fecha = LocalDate.parse(scanner.nextLine());

            System.out.print("Pais de origen (2 letras): ");
            String origen = scanner.nextLine().toUpperCase();

            System.out.print("Pais de destino (2 letras): ");
            String destino = scanner.nextLine().toUpperCase();

            System.out.print("Nombre del pasajero: ");
            String nombre = scanner.nextLine();

            if (origen.length() != 2 || destino.length() != 2
                    || nombre.isBlank()) {
                System.out.println("Los datos introducidos no son validos.");
                return;
            }

            Reserva reserva = new Reserva(
                    "0",
                    null,
                    origen,
                    destino,
                    nombre,
                    fecha
            );

            ReservaDAO reservaDAO = new ReservaDAO();

            if (reservaDAO.insertar(reserva)) {

                boolean localizadoresActualizados = reservaDAO.actualizarLocalizadores();

                if (localizadoresActualizados) {
                    System.out.println("Reserva creada correctamente.");
                    System.out.println("Localizadores actualizados correctamente.");
                } else {
                    System.out.println("Reserva creada, pero no se pudieron actualizar los localizadores.");
                }

                HistorialDAO historialDAO = new HistorialDAO();

                String detalles = "Fecha: " + fecha
                        + ", Origen: " + origen
                        + ", Destino: " + destino
                        + ", Pasajero: " + nombre;

                boolean registrado = historialDAO.registrarCambio(
                        usuario.getId(),
                        Integer.parseInt(reserva.getId()),
                        "CREACION",
                        detalles
                );

                if (registrado) {
                    System.out.println("Cambio registrado en el historial.");
                } else {
                    System.out.println("Aviso: no se pudo registrar el cambio en el historial.");
                }

            } else {
                System.out.println("No se ha podido crear la reserva.");
            }

        } catch (DateTimeParseException e) {
            System.out.println("La fecha no tiene un formato valido.");
        }
    }




    private static void modificarReserva(Scanner scanner, Usuario usuario) {

        System.out.println("\n=== MODIFICAR RESERVA ===");

        ReservaDAO reservaDAO = new ReservaDAO();

        List<Reserva> reservas = reservaDAO.obtenerTodas();

        if (reservas.isEmpty()) {
            System.out.println("No hay reservas disponibles.");
            return;
        }

        System.out.print("Introduce el ID de la reserva que quieres modificar: ");
        String id = scanner.nextLine();

        Reserva reservaActual = null;

        for (Reserva reserva : reservas) {
            if (reserva.getId().equals(id)) {
                reservaActual = reserva;
                break;
            }
        }

        if (reservaActual == null) {
            System.out.println("No existe ninguna reserva con ese ID.");
            return;
        }

        System.out.println("\nDatos actuales de la reserva:");
        System.out.println(reservaActual);

        // Guardamos los datos anteriores antes de modificarlos.
        String datosAnteriores = reservaActual.toString();

        System.out.println("\nIntroduce los nuevos datos.");
        System.out.println("Pulsa Enter si quieres mantener el valor actual.");

        System.out.print("Fecha (" + reservaActual.getFechaReserva() + "): ");
        String nuevaFecha = scanner.nextLine();

        LocalDate fecha = reservaActual.getFechaReserva();

        if (!nuevaFecha.isBlank()) {
            try {
                fecha = LocalDate.parse(nuevaFecha);
            } catch (DateTimeParseException e) {
                System.out.println("La fecha no tiene un formato valido.");
                return;
            }
        }

        System.out.print("Origen (" + reservaActual.getLetraOrigen() + "): ");
        String origen = scanner.nextLine();

        if (origen.isBlank()) {
            origen = reservaActual.getLetraOrigen();
        } else {
            origen = origen.toUpperCase();
        }

        System.out.print("Destino (" + reservaActual.getLetraDestino() + "): ");
        String destino = scanner.nextLine();

        if (destino.isBlank()) {
            destino = reservaActual.getLetraDestino();
        } else {
            destino = destino.toUpperCase();
        }

        System.out.print("Pasajero (" + reservaActual.getNombre() + "): ");
        String nombre = scanner.nextLine();

        if (nombre.isBlank()) {
            nombre = reservaActual.getNombre();
        }

        if (origen.length() != 2 || destino.length() != 2) {
            System.out.println("Los codigos de pais deben tener dos letras.");
            return;
        }

        Reserva reservaModificada = new Reserva(
                reservaActual.getId(),
                reservaActual.getLocalizador(),
                origen,
                destino,
                nombre,
                fecha
        );

        if (reservaDAO.actualizar(reservaModificada)) {

            boolean localizadoresActualizados = reservaDAO.actualizarLocalizadores();

            if (localizadoresActualizados) {
                System.out.println("Reserva modificada correctamente.");
                System.out.println("Localizadores actualizados correctamente.");
            } else {
                System.out.println("Reserva modificada, pero no se pudieron actualizar los localizadores.");
            }

            HistorialDAO historialDAO = new HistorialDAO();

            String datosNuevos = reservaModificada.toString();

            String detalles = "Datos anteriores: " + datosAnteriores
                    + " | Datos nuevos: " + datosNuevos;

            boolean registrado = historialDAO.registrarCambio(
                    usuario.getId(),
                    Integer.parseInt(id),
                    "MODIFICACION",
                    detalles
            );

            if (registrado) {
                System.out.println("Cambio registrado en el historial.");
            } else {
                System.out.println("Aviso: no se pudo registrar el cambio en el historial.");
            }

        } else {
            System.out.println("No se ha podido modificar la reserva.");
        }
    }




    private static void eliminarReserva(Scanner scanner, Usuario usuario) {

        System.out.println("\n=== ELIMINAR RESERVA ===");

        ReservaDAO reservaDAO = new ReservaDAO();

        List<Reserva> reservas = reservaDAO.obtenerTodas();

        if (reservas.isEmpty()) {
            System.out.println("No hay reservas disponibles.");
            return;
        }

        System.out.print("Introduce el ID de la reserva que quieres eliminar: ");
        String id = scanner.nextLine();

        Reserva reservaEliminar = null;

        for (Reserva reserva : reservas) {
            if (reserva.getId().equals(id)) {
                reservaEliminar = reserva;
                break;
            }
        }

        if (reservaEliminar == null) {
            System.out.println("No existe ninguna reserva con ese ID.");
            return;
        }

        System.out.println("\nReserva seleccionada:");
        System.out.println(reservaEliminar);

        System.out.print("\n¿Seguro que quieres eliminar esta reserva? (S/N): ");
        String confirmacion = scanner.nextLine();

        if (confirmacion.equalsIgnoreCase("S")) {

            // Guardamos los datos antes de eliminar la reserva.
            String detalles = reservaEliminar.toString();

            if (reservaDAO.eliminar(id)) {

                boolean localizadoresActualizados = reservaDAO.actualizarLocalizadores();

                if (localizadoresActualizados) {
                    System.out.println("Reserva eliminada correctamente.");
                    System.out.println("Localizadores actualizados correctamente.");
                } else {
                    System.out.println("Reserva eliminada, pero no se pudieron actualizar los localizadores.");
                }

                HistorialDAO historialDAO = new HistorialDAO();

                boolean registrado = historialDAO.registrarCambio(
                        usuario.getId(),
                        null,
                        "ELIMINACION",
                        detalles
                );

                if (registrado) {
                    System.out.println("Cambio registrado en el historial.");
                } else {
                    System.out.println("Aviso: no se pudo registrar el cambio en el historial.");
                }

            } else {
                System.out.println("No se ha podido eliminar la reserva.");
            }

        } else {
            System.out.println("Operacion cancelada.");
        }
    }




    private static void crearUsuario(Scanner scanner) {

        System.out.println("\n=== CREAR USUARIO ===");

        System.out.print("Nombre de usuario: ");
        String nombreUsuario = scanner.nextLine();

        if (nombreUsuario.isBlank()) {
            System.out.println("El nombre de usuario no puede estar vacio.");
            return;
        }

        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();

        if (contrasena.isBlank()) {
            System.out.println("La contraseña no puede estar vacia.");
            return;
        }

        System.out.println("\nSelecciona el rol:");
        System.out.println("1. Administrador");
        System.out.println("2. Empleado");
        System.out.print("Opcion: ");

        String opcionRol = scanner.nextLine();

        String rol;

        if (opcionRol.equals("1")) {
            rol = "ADMINISTRADOR";
        } else if (opcionRol.equals("2")) {
            rol = "EMPLEADO";
        } else {
            System.out.println("Opcion de rol no valida.");
            return;
        }

        Usuario nuevoUsuario = new Usuario(
                0,
                nombreUsuario,
                contrasena,
                rol
        );

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        if (usuarioDAO.registrarUsuario(nuevoUsuario)) {
            System.out.println("Usuario creado correctamente.");
        } else {
            System.out.println("No se ha podido crear el usuario.");
            System.out.println("Comprueba que el nombre no este ya registrado.");
        }
    }


    private static void consultarHistorial() {

        System.out.println("\n=== HISTORIAL DE CAMBIOS ===");

        HistorialDAO historialDAO = new HistorialDAO();

        List<String> historial = historialDAO.obtenerHistorial();

        if (historial.isEmpty()) {
            System.out.println("No hay cambios registrados.");
            return;
        }

        for (String cambio : historial) {
            System.out.println("\n" + cambio);
        }
    }






    private static void consultarTodos() throws IOException {

        List<Path> archivos = obtenerArchivos();

        if (archivos.isEmpty()) {
            System.out.println("No hay archivos generados"
                    + "Genera los archivos primero");
            return;
        }

        for (Path archivo : archivos) {
            System.out.println("\n"
                    + archivo.getFileName());

            List<String> lineas = Files.readAllLines(archivo);

            for (String linea : lineas) {
                System.out.println(linea);
            }
        }
    }

    private static void consultarLineaALinea(Scanner scanner)
            throws IOException {

        List<Path> archivos = obtenerArchivos();

        if (archivos.isEmpty()) {
            System.out.println("No hay archivos para consultar");
            return;
        }

        System.out.println("\nArchivos disponibles:");

        for (int i = 0; i < archivos.size(); i++) {
            System.out.println((i + 1) + ". "
                    + archivos.get(i).getFileName());
        }

        System.out.print("Selecciona el numero del archivo: ");

        int seleccion;

        try {
            seleccion = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Debes introducir un numero");
            return;
        }

        if (seleccion < 1 || seleccion > archivos.size()) {
            System.out.println("Seleccion no valida");
            return;
        }

        Path archivo = archivos.get(seleccion - 1);
        List<String> lineas = Files.readAllLines(archivo);

        System.out.println("\nConsultando: " + archivo.getFileName());
        System.out.println("Pulsa Enter para avanzar una linea");

        for (String linea : lineas) {
            System.out.println(linea);
            scanner.nextLine();
        }

        System.out.println("Fin del archivo");
    }

    private static List<Path> obtenerArchivos() throws IOException {

        Path carpeta = Path.of(CARPETA_SALIDA);

        if (!Files.isDirectory(carpeta)) {
            return List.of();
        }

        try (Stream<Path> archivos = Files.list(carpeta)) {
            return archivos
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString()
                            .startsWith("AirTortilla_"))
                    .filter(path -> path.getFileName().toString()
                            .endsWith(".csv"))
                    .sorted()
                    .toList();
        }
    }
}