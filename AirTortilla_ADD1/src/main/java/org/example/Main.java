
package org.example;

import java.io.IOException;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.stream.Stream;





public class Main {

    private static final String RUTA_CSV = "data/reservas.csv";
    private static final String CARPETA_SALIDA = "ListasReservas";



    public static void main(String[] args) {

        System.out.println("Directorio de trabajo: "
                + System.getProperty("user.dir"));

        LectorCsv lector = new LectorCsv();



        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\nAIRTORTILLA");
            System.out.println("1. Generar archivos de reservas");
            System.out.println("2. Consultar todos los archivos");
            System.out.println("3. Consultar un archivo línea a línea");
            System.out.println("0. Salir");
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

                    case 0:
                        System.out.println("FIN");
                        break;

                    default:
                        System.out.println("Opcion no valida");
                }
            } catch (IOException e) {
                System.out.println("Error al acceder a los archivos: "
                        + e.getMessage());
            }

        } while (opcion != 0);

        scanner.close();
    }

    private static void generarArchivos() throws IOException {

        LectorCsv lector = new LectorCsv();
        List<Reserva> reservas = lector.leerReservas(RUTA_CSV);

        GenArchivos generador = new GenArchivos();
        generador.generarArchivos(reservas, CARPETA_SALIDA);
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




        /*

        //Codigo de prueba
        try {

            List<Reserva> reservas = lector.leerReservas("data/reservas.csv");

            for (Reserva reserva : reservas) {
                System.out.println(reserva);
                System.out.println("--------------------");
            }

            System.out.println("Total de reservas: " + reservas.size());


            GenArchivos generador = new GenArchivos();
            generador.generarArchivos(reservas, "ListasReservas");


        } catch (IOException e) {
            System.out.println("No se pudo leer el archivo CSV.");
            e.printStackTrace();
        }

         */
    }
}