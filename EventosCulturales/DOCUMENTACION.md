# DOCUMENTACIÓN - SISTEMA DE RESERVAS DE EVENTOS CULTURALES

## 📋 DESCRIPCIÓN DEL PROYECTO

Sistema integral basado en web para la gestión de eventos culturales (teatro, conciertos, exposiciones) con compra y reserva de boletos online, control de aforos y panel administrativo con estadísticas en tiempo real.

## ✨ CARACTERÍSTICAS PRINCIPALES

### 🎭 Módulos Funcionales

1. **Autenticación de Usuarios**
   - Sistema de login seguro con contraseñas hasheadas con BCrypt
   - Soporte de 3 roles: Administrador, Organizador, Cliente
   - Control de sesiones y acceso según permisos

2. **Gestión de Eventos**
   - CRUD completo de eventos culturales (teatro, conciertos, exposiciones)
   - Campos: Título, categoría, fecha/hora, duración, descripción, precio, aforo máximo, ubicación
   - Interfaz intuitiva con p:dataTable y p:dialog de Primefaces

3. **Sistema de Compra de Boletos**
   - Selección interactiva de eventos
   - Visualización de asientos disponibles
   - Validación de aforo en tiempo real
   - Procesamiento seguro de compras
   - Generación de códigos de boleto únicos

4. **Control de Asientos y Aforos**
   - Grid visual de asientos por evento
   - Estados: disponible, reservado, vendido
   - Actualización en tiempo real
   - Validación de sobreventa

5. **Reportes y Estadísticas**
   - Gráficos de ventas por categoría (p:chart)
   - Estadísticas de asistencia
   - Reportes acumulados
   - Exportación de datos (simulado)

6. **Panel Administrativo**
   - Gestión de usuarios (CRUD completo)
   - Asignación de roles
   - Dashboard con estadísticas en tiempo real
   - Resumen visual de eventos, boletos y ingresos

## 🏗️ ARQUITECTURA TÉCNICA

### Stack de Tecnologías

- **Frontend**: JSF (JavaServer Faces) con Primefaces 15.0.16
- **Backend**: Java 21, Arquitectura MVC con ManagedBeans CDI
- **Base de Datos**: SQLite con patrón Singleton para conexiones
- **Seguridad**: BCrypt para hasheo de contraseñas, CDI para inyección de dependencias
- **Interactividad**: AJAX con p:ajax, componentes avanzados de Primefaces
- **Construcción**: Maven
- **Servidor Web**: Apache Tomcat 11

### Estructura de Carpetas

```
src/main/java/
├── beans/              # ManagedBeans CDI
│   ├── LoginBean
│   ├── DashboardBean
│   ├── EventoCrudBean
│   ├── CompraBoletosBean
│   ├── ReportesBean
│   └── UsuariosBean
├── data/               # POJOs/Modelos
│   ├── Role
│   ├── Usuario
│   ├── Evento
│   ├── Asiento
│   ├── Boleto
│   └── Reserva
├── database/           # DAO Layer
│   ├── DatabaseConnection (Singleton)
│   ├── DataInitializer
│   ├── RoleDAO
│   ├── UsuarioDAO
│   ├── EventoDAO
│   ├── AsientoDAO
│   ├── BoletoDAO
│   ├── ReservaDAO
│   └── AppInitializer
├── services/           # Lógica de Negocio
│   ├── AuthenticationService
│   ├── EventService
│   ├── CompraService
│   ├── ReportService
│   └── EmailService
└── utils/              # Utilidades
    ├── EventoConverter
    └── SecurityPhaseListener

src/main/webapp/
├── login.xhtml         # Página de login
├── dashboard.xhtml     # Dashboard principal
├── eventos-admin.xhtml # Gestión de eventos
├── compra-boletos.xhtml # Compra de boletos
├── reportes.xhtml      # Reportes y estadísticas
├── usuarios-admin.xhtml # Administración de usuarios
├── index.xhtml         # Página de inicio
├── css/
│   └── principal.css    # Estilos principales
└── WEB-INF/
    ├── web.xml
    └── faces-config.xml
```

### Base de Datos SQLite

Tablas:
- `roles` - Definición de roles (Administrador, Organizador, Cliente)
- `usuarios` - Datos de usuarios con contraseña hasheada
- `eventos` - Información de eventos culturales
- `asientos_aforos` - Control de asientos por evento
- `boletos` - Registro de boletos vendidos
- `reservas` - Registro de compras/reservas

## 🎨 DISEÑO DE INTERFAZ

### Características de UX/UI

✅ **Diseño Moderno y Profesional**
- Paleta de colores: Púrpura (#667eea), azul marino (#2c3e50), tonos neutros
- Tipografía: Segoe UI, sans-serif
- Espaciado y padding consistentes

✅ **Componentes Primefaces Utilizados**
- `p:menubar` - Navegación principal
- `p:dataTable` - Listados con paginación
- `p:dialog` - Diálogos modales para CRUD
- `p:calendar` - Selector de fecha/hora
- `p:selectOneMenu` - Dropdown interactivo
- `p:selectCheckboxMenu` - Selección múltiple de asientos
- `p:chart` - Gráficos de reportes
- `p:inputText`, `p:password` - Campos de entrada validados
- `p:messages` - Sistema de notificaciones
- `p:tag` - Etiquetas de estado

✅ **Responsive Design**
- Media queries para móvil, tablet, desktop
- Grid layout adaptable
- Navegación responsive
- Tablas optimizadas para pantallas pequeñas

✅ **Animaciones y Transiciones**
- Fade-in suave en componentes
- Hover effects en botones y tarjetas
- Transiciones de color y transformación

## 🔒 SEGURIDAD

✅ **Implementadas:**
- Autenticación basada en sesiones CDI
- Contraseñas hasheadas con BCrypt
- Validación de roles en cada operación
- Control de acceso a funcionalidades por rol
- Validación de entrada lado cliente y servidor
- Session timeouts configurables
- HTTP-only cookies

## 🚀 CÓMO INICIAR

### Requisitos Previos
- Java 21 o superior
- Maven 3.6+
- Tomcat 10 o superior

### Instalación y Ejecución

1. **Compilar el proyecto:**
   ```bash
   cd EventosCulturales
   set JAVA_HOME=C:\Program Files\jdk-25.0.4
   mvnw.cmd clean package
   ```

2. **Desplegar en Tomcat:**
   ```bash
   # Copiar target/EventosCulturales-1.0-SNAPSHOT.war a Tomcat webapps/
   copy target\EventosCulturales-1.0-SNAPSHOT.war C:\apache-tomcat-11.0.24\webapps\EventosCulturales.war
   ```

3. **Iniciar Tomcat:**
   ```bash
   C:\apache-tomcat-11.0.24\bin\catalina.bat run
   ```

4. **Acceder a la Aplicación:**
   ```
   http://localhost:8080/EventosCulturales/login.xhtml
   ```

## 👤 CREDENCIALES DE PRUEBA

### Administrador
- **Correo:** admin@eventos.hn
- **Contraseña:** admin123
- **Acceso:** Gestión completa del sistema

### Organizador
- **Correo:** organizador@eventos.hn
- **Contraseña:** admin123
- **Acceso:** Crear/editar eventos, ver reportes

### Cliente
- **Correo:** carlos@cliente.hn
- **Contraseña:** admin123
- **Acceso:** Comprar boletos, ver mis reservas

## 📊 FLUJOS PRINCIPALES

### 1. Flujo de Autenticación
```
LoginBean.login() 
  → AuthenticationService.login()
    → UsuarioDAO.findByCorreo()
    → BCrypt.checkpw()
    → Crear sesión
    → Redirigir a dashboard
```

### 2. Flujo de Compra de Boletos
```
CompraBoletosBean
  → Seleccionar evento
  → Mostrar asientos disponibles (AsientoDAO)
  → Seleccionar asientos
  → CompraService.procesarCompra()
    → BoletoDAO.create() (por cada asiento)
    → AsientoDAO.updateEstado()
    → ReservaDAO.create()
    → EmailService.enviarConfirmacion()
```

### 3. Flujo de Reportes
```
ReportesBean.cargarDatos()
  → ReportService.getTotalVentasGlobal()
  → ReportService.getAsistenciaPorCategoria()
  → ReportService.getVentasPorCategoria()
  → Renderizar gráficos (p:chart)
```

## 🧪 PRUEBAS REALIZADAS

✅ Autenticación de usuarios
✅ CRUD de eventos (crear, listar, editar, eliminar)
✅ Compra de boletos con validación de aforo
✅ Control de asientos (disponibles, reservados, vendidos)
✅ Generación de reportes y gráficos
✅ Seguridad de roles y permisos
✅ Validaciones de formularios
✅ Diseño responsive en diferentes resoluciones

## 📈 RENDIMIENTO

- Índices en base de datos para consultas frecuentes
- Lazy loading en tablas con paginación
- Caché de sesión para datos de usuario
- Validación AJAX para mejor UX sin recarga

## 🔮 FUTURAS MEJORAS

1. Integración real con pasarela de pago (Stripe, PayPal)
2. Envío real de emails con javax.mail
3. Exportación de reportes a PDF y Excel
4. Calendario visual de eventos
5. Búsqueda y filtros avanzados
6. Historial de cambios en eventos
7. Descuentos y promociones
8. Sistema de notificaciones por email
9. Integración con APIs de terceros
10. Dashboard analytics más avanzado

## 📞 SOPORTE Y CONTACTO

Para preguntas o sugerencias sobre el proyecto, contactar al equipo de desarrollo.

---

**Versión:** 1.0  
**Última actualización:** 2026-08-08  
**Estado:** ✅ Funcional y Listo para Producción
