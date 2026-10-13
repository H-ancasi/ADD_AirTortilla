# ADD_AirTortilla

# AirTortilla

Este proyecto consiste en hacer un programa en Java para organizar las reservas de AirTortilla.

## ¿Cómo he hecho el programa?

Primero creé la clase `Reserva.java`, donde guardo los datos de cada reserva, como el origen, el destino, el nombre del pasajero y la fecha. También tiene un ID para poder diferenciar cada reserva y un localizador para los casos en los que coinciden el nombre, el origen y el destino.

Después hice `LectorCsv.java`, que se encarga de leer el archivo CSV y utilizar los datos de cada fila para crear las reservas. 

Luego, pasé a crear `GenArchivos.java`. Esta clase se encarga de separar las reservas por destino y fecha y guardarlas en archivos CSV diferentes. Por ejemplo, las reservas con destino a México y fecha 15 de marzo de 2026 se guardan en un archivo llamado `AirTortilla_MX_2026_03_15.csv`.

Y para acabar, en el `Main.java` he decicido hacer la interfaz en la consola, desde ahí puedo generar los archivos, consultar todos los que se han creado o elegir uno para verlo línea a línea, pulsando Enter para avanzar.

## ¿Cómo tengo organizado el proyecto?

He separado el código en cuatro clases para tenerlo más ordenado y no mezclarlo todo en un solo archivo. `Main.java` controla el menú, `Reserva.java` guarda los datos de cada reserva, `LectorCsv.java` lee el archivo de entrada y `GenArchivos.java` crea los archivos de salida.

También tengo una carpeta llamada `data`, donde tengo el archivo `reservas.csv` que sin querer subí al Github pero logré borrarlo. Los archivos que se generan se guardan en otra carpeta llamada `ListasReservas`.

El archivo CSV de entrada tiene esta cabecera:

```csv
fechaReserva,origen,destino,nombrePasajero
```

Las fechas tienen que estar escritas con el formato `YYYY-MM-DD`, por ejemplo `2026-03-15`. El archivo que utilizo para las pruebas se queda en mi ordenador y no lo subo a GitHub.

He probado el programa con varias reservas, incluyendo casos en los que coinciden el nombre, el origen y el destino.

También he comprobado que los archivos se generan con el nombre correcto, que las reservas se guardan en el archivo que les corresponde y que las opciones del menú funcionan. Además, he vuelto a generar los archivos para comprobar que siguen saliendo correctamente.

