# 🎭 SISTEMA DE RESERVAS PARA EVENTOS CULTURALES

## 📋 Descripción General

Sistema integral basado en web para la gestión de eventos culturales (teatro, conciertos, exposiciones) con compra y reserva de boletos online, control de aforos y panel administrativo con estadísticas en tiempo real.

**Estado:** ✅ **COMPLETADO Y FUNCIONAL**

---

## 🎯 Características Principales

### ✨ Módulos Funcionales

- **🔐 Autenticación de Usuarios**: Login seguro con roles (Admin, Organizador, Cliente)
- **📅 Gestión de Eventos**: CRUD completo con validaciones y categorías
- **🎫 Compra de Boletos**: Sistema interactivo con validación de aforo
- **💺 Control de Asientos**: Visualización gráfica con estados actualizados
- **📊 Reportes y Estadísticas**: Gráficos Primefaces de ventas y asistencia
- **👥 Panel Administrativo**: Gestión de usuarios y eventos

---

## 🏗️ Stack Tecnológico

| Componente | Tecnología |
|-----------|-----------|
| **Frontend** | JSF + Primefaces 15.0.16 |
| **Backend** | Java 21 |
| **Arquitectura** | MVC con ManagedBeans CDI |
| **Base de Datos** | SQLite (Singleton Pattern) |
| **Seguridad** | BCrypt + Control de Roles |
| **Servidor** | Apache Tomcat 11 |
| **Build Tool** | Maven |

---

## 📁 Estructura del Proyecto

```
EventosCulturales/
├── src/main/java/
│   ├── beans/           # ManagedBeans CDI (6 beans)
│   ├── data/            # POJOs (6 modelos)
│   ├── database/        # DAO Layer + Conexión (8 clases)
│   ├── services/        # Lógica de Negocio (5 servicios)
│   └── utils/           # Utilidades (2 clases)
├── src/main/webapp/
│   ├── *.xhtml          # 7 páginas JSF
│   ├── css/principal.css # Estilos responsive
│   └── WEB-INF/         # Configuración JSF
└── pom.xml             # Dependencias Maven
```

---

## 🚀 Instalación y Ejecución

### Requisitos Previos

- Java 21 o superior
- Maven 3.6+
- Tomcat 10+

### Pasos de Instalación

1. **Clonar o descargar el proyecto**
   ```bash
   cd EventosCulturales
   ```

2. **Compilar el proyecto**
   ```bash
   mvnw.cmd clean compile
   ```

3. **Construir el WAR**
   ```bash
   mvnw.cmd clean package -DskipTests
   ```

4. **Desplegar en Tomcat**
   ```bash
   # Copiar el archivo WAR
   copy target\EventosCulturales-1.0-SNAPSHOT.war ^
        C:\apache-tomcat-11.0.24\webapps\EventosCulturales.war
   ```

5. **Iniciar Tomcat**
   ```bash
   C:\apache-tomcat-11.0.24\bin\catalina.bat run
   ```

6. **Acceder a la Aplicación**
   ```
   http://localhost:8080/EventosCulturales/login.xhtml
   ```

---

## 👤 Credenciales de Prueba

### Administrador
```
Correo: admin@eventos.hn
Contraseña: admin123
```

### Organizador
```
Correo: organizador@eventos.hn
Contraseña: admin123
```

### Cliente
```
Correo: carlos@cliente.hn
Contraseña: admin123
```

---

## 🎨 Pantallas Principales

### 1. Login (login.xhtml)
- Autenticación segura con validación
- Diseño moderno y responsive
- Credenciales de prueba visibles

### 2. Dashboard (dashboard.xhtml)
- Resumen visual de estadísticas
- Lista de eventos próximos
- Navegación según rol

### 3. Gestión de Eventos (eventos-admin.xhtml)
- CRUD con p:dataTable
- Diálogos para crear/editar
- Validaciones AJAX

### 4. Compra de Boletos (compra-boletos.xhtml)
- Selección interactiva de eventos
- Grid de asientos disponibles
- Resumen de compra con total

### 5. Reportes (reportes.xhtml)
- Gráficos de ventas y asistencia
- Exportación de datos (simulada)
- Filtros por categoría

### 6. Administración de Usuarios (usuarios-admin.xhtml)
- Gestión de usuarios del sistema
- Asignación de roles
- CRUD completo

---

## 🔒 Seguridad Implementada

✅ **Autenticación**
- Sistema de login con sesiones CDI
- Contraseñas hasheadas con BCrypt

✅ **Autorización**
- Control de roles en cada operación
- Redireccionamiento automático sin permisos
- Protección de URLs sensibles

✅ **Validaciones**
- Lado cliente: Primefaces
- Lado servidor: Java
- Validación de aforos y duplicados

✅ **Base de Datos**
- Queries parametrizadas (PreparedStatement)
- Índices en campos frecuentes
- Constraints e integridad referencial

---

## 📊 Base de Datos

### Tablas SQLite

| Tabla | Descripción |
|-------|------------|
| `roles` | Definición de roles (Admin, Organizador, Cliente) |
| `usuarios` | Datos de usuarios con contraseña hasheada |
| `eventos` | Información de eventos culturales |
| `asientos_aforos` | Control de asientos por evento |
| `boletos` | Registro de boletos vendidos |
| `reservas` | Registro de compras/reservas |

### Datos Iniciales

- 1 Admin, 1 Organizador, 2 Clientes
- 5 Eventos de prueba (teatro, concierto, exposición)
- Asientos generados automáticamente por evento

---

## 🎯 Flujos Principales

### Flujo de Autenticación
```
1. Usuario ingresa correo y contraseña
2. LoginBean.login() valida credenciales
3. AuthenticationService usa BCrypt para verificar
4. Se crea sesión CDI
5. Redirecciona a dashboard
```

### Flujo de Compra de Boletos
```
1. Cliente selecciona evento
2. Sistema muestra asientos disponibles
3. Cliente selecciona asientos
4. CompraService valida aforo
5. Crea boletos y reserva
6. Actualiza estado de asientos
7. Envía confirmación
```

### Flujo de Reportes
```
1. ReportService consulta datos
2. Calcula totales y por categoría
3. ReportesBean renderiza gráficos
4. Primefaces p:chart visualiza
```

---

## 🧪 Pruebas Realizadas

✅ Compilación sin errores  
✅ Construcción WAR exitosa  
✅ Despliegue en Tomcat  
✅ Autenticación de usuarios  
✅ CRUD de eventos  
✅ Compra de boletos  
✅ Validación de aforos  
✅ Generación de reportes  
✅ Diseño responsive  
✅ Seguridad de roles  

---

## 📈 Componentes Utilizados

### Primefaces
- `p:menubar` - Navegación
- `p:dataTable` - Tablas con paginación
- `p:dialog` - Diálogos modales
- `p:calendar` - Selector de fecha/hora
- `p:selectOneMenu` - Dropdown
- `p:selectCheckboxMenu` - Selección múltiple
- `p:chart` - Gráficos (pie, bar, line)
- `p:inputText`, `p:password` - Campos validados
- `p:messages` - Sistema de notificaciones
- `p:tag` - Etiquetas de estado

### CDI
- `@Named` - Inyección de dependencias
- `@SessionScoped` - Alcance de sesión
- Gestión automática del ciclo de vida

---

## 🎨 Diseño UI/UX

### Características de Diseño

✅ **Colores Profesionales**
- Púrpura (#667eea) - Color principal
- Azul marino (#2c3e50) - Textos oscuros
- Tonos neutros para fondo

✅ **Tipografía Coherente**
- Segoe UI, sans-serif
- Espaciado consistente
- Jerarquía visual clara

✅ **Responsive Design**
- Media queries para móvil, tablet, desktop
- Tablas adaptables
- Navegación responsive

✅ **Animaciones**
- Fade-in suave
- Hover effects
- Transiciones de color

---

## 🔍 Estructura de Código

### Separación de Capas

**Presentación (JSF/XHTML)**
- Formularios y formularios
- Validaciones lado cliente
- Visualización de datos

**Lógica de Negocio (Services)**
- AuthenticationService
- EventService
- CompraService
- ReportService
- EmailService

**Acceso a Datos (DAO)**
- RoleDAO, UsuarioDAO, EventoDAO
- AsientoDAO, BoletoDAO, ReservaDAO

**Modelos (POJOs)**
- Role, Usuario, Evento, Asiento, Boleto, Reserva

---

## 📝 Archivos Principales

| Archivo | Descripción |
|---------|------------|
| `DatabaseConnection.java` | Conexión SQLite (Singleton) |
| `AuthenticationService.java` | Autenticación con BCrypt |
| `CompraService.java` | Lógica de compra y validación |
| `EventoCrudBean.java` | Controller de eventos |
| `CompraBoletosBean.java` | Controller de compra |
| `eventos-admin.xhtml` | Gestión de eventos |
| `compra-boletos.xhtml` | Compra de boletos |
| `principal.css` | Estilos principales |

---

## 🐛 Solución de Problemas

### Si Tomcat no inicia
- Verificar puerto 8080 disponible
- Revisar logs en `CATALINA_HOME/logs/`
- Verificar JAVA_HOME configurado

### Si la base de datos no se crea
- Verificar permisos en carpeta `src/main/resources/`
- Eliminar `eventosculturales.db` si existe
- Reiniciar la aplicación

### Si las páginas no cargan
- Limpiar caché del navegador
- Verificar Tomcat está ejecutándose
- Revisar URL: `http://localhost:8080/EventosCulturales/login.xhtml`

---

## 🔮 Futuras Mejoras

1. Integración real con pasarela de pago
2. Envío real de emails
3. Exportación a PDF y Excel real
4. Búsqueda y filtros avanzados
5. Historial de cambios
6. Sistema de descuentos
7. Notificaciones por email
8. APIs REST

---

## 📞 Información de Contacto

Para preguntas o sugerencias contactar al equipo de desarrollo.

---

## 📄 Licencia

Proyecto educativo - 2026

---

**🎉 ¡Proyecto Completado y Funcional!**

**Versión:** 1.0  
**Estado:** ✅ Producción  
**Última actualización:** 2026-08-08
