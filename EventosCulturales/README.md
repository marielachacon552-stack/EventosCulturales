# Sistema de Reservas para Eventos Culturales

## Objetivo:
Desarrollar un sistema para eventos culturales integral basado en la web que permita la gestión de obras de teatro, conciertos y exposiciones, la compra y reserva de boletos en linea con control estricto de aforos y asientos y un panel administrativo completo para organizadores con estadisticas de asistencia y ventas en tiempo real.

## Definicion de pantallas y modulos:
El sistema contara con las siguientes interfaces de usuario utilizando estrictamente controles de Primefaces:

### Pantalla de Login:
- Autenticacion de usuarios contra la base de datos SQLite.
- Soporte de Roles (Administrador, Organizador, Cliente/Asistente).

### Pantalla principal (Dashboard/Menu de navegacion):
- Acceso a las diferentes secciones del sistema según el rol del usuario.
- Menu de navegacion estructurado (MenuBar o SlideMenu de Primefaces) para acceder a las funcionalidades del sistema.
- Resumen visual de eventos próximos, boletos vendidos y total de ingresos generados.

### Modulo de Gestión de Eventos (obras de teatro, conciertos, exposiciones):
- Pantalla CRUD completa con p:dataTable para inserción/edición y validaciones con AJAX (p:ajax) para eventos culturales (teatro, conciertos, exposiciones).
- Campos: Título del evento, categoría (teatro, concierto, exposición), fecha y hora (p:calendar), duración, descripción, precio del boleto, aforo máximo y ubicación.

### Modulo de Compra y reserva de Boletos:
- Interfaz para cliente donde puede seleccionar un evento, elegir asientos disponibles (p:selectOneMenu o p:selectCheckboxMenu) y realizar la compra o reserva de boletos en linea.
- Validación de disponibilidad de asientos en tiempo real y control estricto de aforos.
- Integración con pasarela de pago (simulada) para procesar pagos de boletos.
- Confirmación de compra y envío de boletos electrónicos al correo del cliente.
- Estadísticas de ventas y asistencia en tiempo real para organizadores, incluyendo gráficos y reportes descargables (p:chart).
- Registro del historial de compras y en la base de datos para futuras referencias y análisis.

### Pantalla de control de aforos y asientos:
- Visualización en tiempo real de los espacios disponibles frente a los reservados o vendidos por evento.
- Representación gráfica de la distribución de asientos (p:diagram o p:panelGrid) para facilitar la selección de asientos por parte del cliente.
- Validación de aforos máximos permitidos por evento y control de reservas simultáneas para evitar sobreventa.
- Integración con el módulo de compra y reserva de boletos para actualizar la disponibilidad de asientos en tiempo real.

### Panel para organizadores (Reportes y Estadísticas):
- Graficos estadisticos (p:chart - barras y pastel) provistos por primefaces para mostrar la asistencia por tipo de evento y el reporte de ventas acumuladas.
- Opciones de exporación de reportes.

## Esquema de base de datos:
El sistema utilizará una base de datos SQLite con patron Singleton, estructurando de la siguiente manera:
- Roles: Tabla para definir los roles de usuario (Administrador, Organizador, Cliente/Asistente).
- Usuarios: Almacena credenciales (nombre, correo electrónico, contraseña hasheada) y ID del rol asignado.
- Eventos: Tabla para almacenar información de eventos culturales (ID, título, categoría, fecha y hora, duración, descripción, precio del boleto, aforo máximo y ubicación).
- Asientos_aforos: control de capacidad y estado de evento.
- Boletos: Registro de boletos vendidos o reservados (ID, ID del evento, ID del usuario, número de asiento, estado del boleto, fecha de compra/reserva).
- Reservas: Registro de compras de boletos asociadas al usuario y al evento.

## Librerias y Tecnologias:
- Frontend: JSF (JavaServer Faces) con Primefaces 15.0.16 (Uso exclusivo de componentes de Primefaces para UI sin frameworks externos).
-Backend: Java 25, Arquitectura MVC (Model-View-Controller) para separar la lógica de negocio, la presentación y el acceso a datos, utilizando ManagegBeans.
- Base de datos: SQLite mediante JDBC con patrón Singleton para la gestión de conexiones.
- Interactividad: Uso intensivo de AJAX (p:ajax con atributos update y process) y componentes avanzados (p:dataTable, p:dialog, p:chart, p:calendar, p:selectOneMenu, p:selectCheckboxMenu) para mejorar la experiencia del usuario.
- Seguridad: Implementación de autenticación y autorización basada en roles, con almacenamiento seguro de contraseñas mediante hashing (p.ej., BCrypt) y control de acceso a funcionalidades según el rol del usuario.
- Validaciones: Validaciones de formularios y entradas de datos utilizando las capacidades de
- Primefaces y JSF, incluyendo mensajes de error y confirmación para mejorar la experiencia del usuario.

## Estructura de carpetas:
- src/main/java: Contendrá el codigo fuente de la aplicación, incluyendo los ManagedBeans, controladores y clases de modelo.
 - carpeta beans para los ManagedBeans.
 - carpeta data para los objetos java.
 - carpeta database para la conexion a la base de datos y las operaciones CRUD.
 - carpeta services para la logica de negocio y servicios adicionales.
- src/main/resources: Contendrá los archivos de configuración, propiedades y recursos estáticos.
- src/main/webapp: Contendrá las páginas JSF (XHTML) y recursos web como imagenes y hojas de estilo CSS.

