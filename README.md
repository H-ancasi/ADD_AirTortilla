# AirTortilla

Este proyecto consiste en un programa en Java para organizar las reservas de AirTortilla. Empecé trabajando con archivos CSV y, para esta segunda parte, he adaptado el programa para utilizar una base de datos relacional MySQL.

El programa funciona desde la consola y permite gestionar las reservas, generar archivos CSV y controlar el acceso mediante usuarios con diferentes permisos.

## ¿Cómo he hecho el programa?

Primero creé la clase `Reserva.java`, donde guardo los datos de cada reserva, como el origen, el destino, el nombre del pasajero y la fecha. También tiene un ID para diferenciar cada reserva y un localizador para los casos en los que coinciden el nombre, el origen y el destino.

Después hice `LectorCsv.java`, que se encarga de leer archivos CSV y utilizar los datos de cada fila para crear las reservas. Esta clase formaba parte de la primera versión del proyecto.

Luego pasé a crear `GenArchivos.java`. Esta clase se encarga de separar las reservas por destino y fecha y guardarlas en archivos CSV diferentes. Por ejemplo, las reservas con destino a México y fecha 15 de marzo de 2026 se guardan en un archivo llamado `AirTortilla_MX_2026_03_15.csv`.

Para la segunda entrega he añadido una conexión con MySQL. He creado las clases `ConexionBD.java`, `ReservaDAO.java`, `UsuarioDAO.java` e `HistorialDAO.java` para trabajar con la base de datos.

También he añadido un sistema de inicio de sesión con contraseñas cifradas mediante BCrypt y dos tipos de usuario: administrador y empleado. El administrador puede gestionar las reservas, crear usuarios y consultar el historial de cambios, mientras que el empleado tiene acceso a las opciones de consulta y generación de archivos.

Por último, en `Main.java` he mantenido la interfaz por consola. Desde el menú se puede iniciar sesión, generar archivos, consultar reservas y realizar las operaciones permitidas según el tipo de usuario.

## ¿Cómo tengo organizado el proyecto?

He separado el código en diferentes clases para tenerlo más ordenado y no mezclar todas las funciones en un solo archivo.

Dentro de `database` están las clases que se encargan de conectar con MySQL y realizar las operaciones sobre las tablas. El resto de clases contienen la lógica de las reservas, los usuarios, la lectura de CSV y la generación de archivos.

También tengo una carpeta llamada `ListasReservas`, donde se guardan los archivos CSV que genera el programa. Esta carpeta se crea o se rellena al utilizar la opción correspondiente del menú.

## ¿Qué necesito para ejecutar el proyecto?

Para poder ejecutar AirTortilla en otro ordenador hace falta tener instalado:

- Java JDK 21.
- IntelliJ IDEA (o un entorno compatible con proyectos Maven).
- Docker Desktop.

El proyecto utiliza Maven para gestionar sus dependencias, por lo que IntelliJ puede descargarlas automáticamente al abrirlo.

Docker Compose se encarga de iniciar MySQL, crear la base de datos y sus tablas e importar las reservas de ejemplo. Así no hace falta instalar MySQL Server ni configurar manualmente la base de datos.

## ¿Cómo puedo instalar y ejecutar el proyecto?

Si se quiere ejecutar el programa desde otro ordenador, hay que seguir estos pasos:

### 1. Descargar el proyecto

Descargar el repositorio desde GitHub y abrir la carpeta `AirTortilla_ADD1` con IntelliJ IDEA.

Una vez abierto, IntelliJ debería reconocer el proyecto Maven a través del archivo `pom.xml`. Si aparece la opción de cargar o sincronizar Maven, hay que aceptarla para que se descarguen las dependencias necesarias.

### 2. Iniciar la base de datos con Docker

Abrir Docker Desktop y comprobar que está funcionando.

Después, abrir una terminal dentro de la carpeta `AirTortilla_ADD1`, donde se encuentra el archivo `compose.yaml`, y ejecutar:

```bash
docker compose up -d
```

Este comando descarga la imagen de MySQL si todavía no está disponible y crea el contenedor con la configuración necesaria.

Al iniciarse por primera vez, Docker Compose prepara automáticamente la base de datos `air_tortilla`, crea las tablas `reservas`, `usuarios` e `historial_cambios` e importa las reservas de ejemplo del archivo `air_tortilla_completo.sql`.

No es necesario importar el archivo SQL manualmente desde MySQL Workbench.

**Importante:** la importación automática del SQL solamente se realiza cuando se inicializa la base de datos por primera vez. Si se vuelve a iniciar el contenedor posteriormente, se conservan los datos que ya existan.

### 3. Configurar la conexión del programa

Dentro de `src/main/resources` se encuentra el archivo `db.example.properties`, que sirve como plantilla para configurar la conexión.

Hay que hacer una copia de ese archivo y llamar a la copia `db.properties`, dejándola en la misma carpeta.

Con la configuración de Docker Compose incluida en el proyecto, el contenido debe ser:

```properties
db.url=jdbc:mysql://localhost:3307/air_tortilla
db.username=airtortilla_app
db.password=airtortilla_local
```

Estos datos corresponden a la configuración local incluida en `compose.yaml`. Si se modifica esa configuración, también habrá que actualizar los datos de conexión.

Es importante que el archivo se llame exactamente `db.properties`, ya que es el que utiliza el programa para conectarse a la base de datos. Este archivo no se incluye en GitHub porque contiene las credenciales de conexión de cada ordenador.

### 4. Crear el primer administrador

Como la base de datos de ejemplo no incluye usuarios, primero hay que crear un administrador para poder iniciar sesión.

Para ello, hay que abrir la clase `CrearAdministradorInicial.java` desde IntelliJ y ejecutarla. Esta clase permite registrar el primer usuario administrador en la base de datos.

Hay que introducir el nombre de usuario y la contraseña que se quieran utilizar para acceder al programa.

Una vez creado, ya se puede iniciar sesión desde `Main.java`.

### 5. Ejecutar el programa

Por último, abrir `Main.java` y ejecutar el método `main`.

Al iniciar, aparecerá el menú de inicio de sesión. Hay que introducir las credenciales del administrador creado anteriormente.

A partir de ahí se podrán utilizar las opciones disponibles según el tipo de usuario. Para salir antes de iniciar sesión, se pueden dejar vacíos tanto el usuario como la contraseña.

### 6. Detener Docker

Cuando se termine de utilizar el programa, se puede abrir una terminal en la carpeta `AirTortilla_ADD1` y ejecutar:

```bash
docker compose down
```

Esto detiene y elimina el contenedor, pero conserva el volumen donde se guardan los datos de MySQL. Para volver a utilizar el programa, basta con ejecutar otra vez `docker compose up -d`.

## ¿Qué he probado?

He probado la conexión con MySQL, el inicio de sesión con usuarios correctos e incorrectos y las diferencias de permisos entre administradores y empleados.

También he comprobado la creación de usuarios, el rechazo de nombres de usuario duplicados y las operaciones de insertar, modificar y eliminar reservas.

Por último, he comprobado que los cambios se registran en el historial, que los localizadores se actualizan correctamente y que los archivos CSV se generan utilizando los datos guardados en la base de datos.

Además, he probado el funcionamiento de Docker Compose, comprobando que se crea la base de datos, se importan las reservas de ejemplo y el programa Java puede conectarse al contenedor MySQL.