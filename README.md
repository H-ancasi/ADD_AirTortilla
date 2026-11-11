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
- MySQL Server.
- MySQL Workbench, para importar y gestionar la base de datos.

El proyecto utiliza Maven para gestionar sus dependencias, por lo que IntelliJ puede descargarlas automáticamente al abrirlo.

## ¿Cómo puedo instalar y ejecutar el proyecto?

Si se quiere ejecutar el programa desde otro ordenador, hay que seguir estos pasos:

### 1. Descargar el proyecto

Descargar el repositorio desde GitHub y abrir la carpeta `AirTortilla_ADD1` con IntelliJ IDEA.

Una vez abierto, IntelliJ debería reconocer el proyecto Maven a través del archivo `pom.xml`. Si aparece la opción de cargar o sincronizar Maven, hay que aceptarla para que se descarguen las dependencias necesarias.

### 2. Crear la base de datos

Abrir MySQL Workbench y conectarse al servidor MySQL local.

En la carpeta principal del proyecto se encuentra el archivo `air_tortilla.sql`. Hay que abrirlo desde MySQL Workbench y ejecutar el script con el botón del rayo.

Este archivo crea la base de datos `air_tortilla`, sus tres tablas (`reservas`, `usuarios` e `historial_cambios`) y añade algunas reservas de ejemplo.

**Importante:** el script contiene instrucciones para eliminar las tablas existentes antes de crearlas de nuevo. Por tanto, debe ejecutarse sobre una instalación nueva o una base de datos que se pueda reemplazar, ya que se perderán los datos anteriores de esas tablas.

### 3. Crear un usuario para la conexión

Para que el programa pueda conectarse a MySQL, hace falta utilizar una cuenta que tenga permisos sobre la base de datos.

Se puede crear una cuenta específica para el programa ejecutando estas instrucciones en MySQL Workbench:

```sql
CREATE USER 'airtortilla_app'@'localhost'
IDENTIFIED BY 'elige_una_contraseña';

GRANT SELECT, INSERT, UPDATE, DELETE
ON air_tortilla.*
TO 'airtortilla_app'@'localhost';

FLUSH PRIVILEGES;
```

Hay que sustituir `elige_una_contraseña` por una contraseña propia. También se puede utilizar otra cuenta de MySQL que ya exista, siempre que tenga los permisos necesarios.

### 4. Configurar la conexión del programa

Dentro de `src/main/resources` se encuentra el archivo `db.example.properties`, que sirve como plantilla para configurar la conexión.

Hay que hacer una copia de ese archivo y llamar a la copia `db.properties`, dejándola en la misma carpeta.

El contenido tiene este formato:

```properties
db.url=jdbc:mysql://localhost:3304/air_tortilla
db.username=airtortilla_app
db.password=TU_CONTRASEÑA
```

Hay que revisar los tres valores:

- `db.url`: debe contener el puerto que utiliza el servidor MySQL del ordenador. En mi caso utilizo el puerto `3304`, pero puede ser diferente.
- `db.username`: el nombre de usuario de MySQL que se haya creado o elegido.
- `db.password`: la contraseña correspondiente a ese usuario.

Es importante que el archivo se llame exactamente `db.properties`, ya que es el que utiliza el programa para conectarse a la base de datos. Este archivo no se incluye en GitHub porque contiene las credenciales personales de cada ordenador.

### 5. Crear el primer administrador

Como la base de datos de ejemplo no incluye usuarios, primero hay que crear un administrador para poder iniciar sesión.

Para ello, hay que abrir la clase `CrearAdministradorInicial.java` desde IntelliJ y ejecutarla. Esta clase permite registrar el primer usuario administrador en la base de datos.

Hay que introducir el nombre de usuario y la contraseña que se quieran utilizar para acceder al programa.

Una vez creado, ya se puede iniciar sesión desde `Main.java`.

### 6. Ejecutar el programa

Por último, abrir `Main.java` y ejecutar el método `main`.

Al iniciar, aparecerá el menú de inicio de sesión. Hay que introducir las credenciales del administrador creado anteriormente.

A partir de ahí se podrán utilizar las opciones disponibles según el tipo de usuario. Para salir antes de iniciar sesión, se pueden dejar vacíos tanto el usuario como la contraseña.

## ¿Qué he probado?

He probado la conexión con MySQL, el inicio de sesión con usuarios correctos e incorrectos y las diferencias de permisos entre administradores y empleados.

También he comprobado la creación de usuarios, el rechazo de nombres de usuario duplicados y las operaciones de insertar, modificar y eliminar reservas.

Por último, he comprobado que los cambios se registran en el historial, que los localizadores se actualizan correctamente y que los archivos CSV se generan utilizando los datos guardados en la base de datos.