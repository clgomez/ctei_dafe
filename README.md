________________________________________
📄 README.md
# 🌐 Nombre del Proyecto

Descripción del propósito de la aplicación. 
> Desarrollo de un prototipo de plataforma web para la formulación, gestión y seguimiento de proyectos de investigación dentro de la red de pequeños investigadores en Ciencia, Tecnología e Innovación (CTeI) de Popayán.
> Desarrollada con Spring Boot de Java en el backend y Angular JS en el frontend.

---

## 🛠️ Tecnologías Utilizadas

### Backend:
- Java 21 (Temurin - Adoptium)
- Spring Boot
- IDE: Visual Studio Code

### Frontend:
- Angular JS
- Node.js & npm

### Base de Datos:
- XAMPP (MySQL / MariaDB)

### Sistema de Control de versiones:
- Git

### Sistemas Operativos:
Compatible con: 
- Linux
- Windows

---

## ⚙️ Requisitos Previos

Asegurarse de tener instalados:

### General:
-  Git
- VSCode (o IDE de preferencia) puede ser IntelliJ IDEA Community Edition

### Backend:
- [JDK 21 Temurin (Adoptium)](https://adoptium.net/)
- Maven (si no está incluido en tu configuración de Spring Boot)

### Frontend:
- Node.js (>=14 recomendado)
- npm
- Angular JS

### Base de datos:
- XAMPP (MySQL/MariaDB)

---

## 🚀 Instrucciones de Instalación

### 1. Clona el repositorio
Crea una nueva carpeta y utiliza un bash, terminal, cmd para acceder a esa carpeta: 

D:cd nombre_carpeta
D:\nombre_carpeta>

Clona con comandos el repositorio remoto que se encuentra en github. 

D:\nombre_carpeta> 
git clone https://github.com/TecnologiasVirtuales/Dafe.git

Una vez clonado el repositorio remoto ya se puede acceder a la carpeta del proyecto

D:\nombre_carpeta> cd Dafe
D:\nombre_carpeta\Dafe>

Para acceder a la carpeta del proyecto en Visual Studio Code, se debe abrir el IDE, seleccionar la opción "Explorador" 
en el panel izquierdo y hacer clic en el botón "Abrir carpeta"; luego, en la terminal integrada, se puede verificar que 
el proyecto está ubicado en la ruta: D:\nombre_carpeta\Dafe>.

### 2. Configura la base de datos
•	Inicia XAMPP.
•	Crea una base de datos en phpMyAdmin (enlace: http://localhost/phpmyadmin/) denominada “dafe” con cotejamiento utf8_bin.
### 3. Configura el Backend (Spring Boot)
•	Asegúrate de tener configurado el archivo application.properties:

# Configuración de la base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/dafe?useSSL=false&serverTimezone=UTC&useLegacyDateTimeCode=false
spring.datasource.username=root
spring.datasource.password= 

•	Compila y ejecuta:
En la terminal integrada de visual studio code en la ruta del proyecto compile y ejecute con este comando:
D:\nombre_carpeta\Dafe>./mvnw spring-boot:run

  En Windows: 
    Abra un git bash en la terminal integrada de vscode para poder compilar y ejecutar con este comando: 
    ./mvnw spring-boot:run
	
  En Linux/Mac: ./mvnw spring-boot:run

### 4. Configura el Frontend (AngularJS)
En el terminal integrado de vscode en la ruta del proyecto accede al frontend de la siguiente manera:

D:\nombre_carpeta\Dafe>cd Frontend/sakai-ng-master/
D:\repositorio\Dafe\Frontend\sakai-ng-master>

Ejecuta este comando para instalar los paquetes y dependencias del proyecto en el frontend:
D:\repositorio\Dafe\Frontend\sakai-ng-master>npm install

•	Ejecuta la app:
Compila y ejecuta el frontend con el siguiente comando, el cual también abrirá automáticamente la aplicación en el navegador para su visualización.
     D:\repositorio\Dafe\Frontend\sakai-ng-master>ng serve -o

________________________________________
🌍 Acceso a la Aplicación
Frontend: http://localhost:4200/
Backend (API REST): http://localhost:8080/...
________________________________________
________________________________________
📁 Estructura del Proyecto
/
.
├── Dase de datos
├── Documentacion
├── Frontend
│   ├── Frontend
│   │   ├── public
│   │   └── src
│   └── sakai-ng-master (código del frontend en Angular JS)
│       └── src
└── src
    ├── main
    │   ├── java (código del backend en Spring Boot de Java)
    │   └── resources
    └── test
        └── java
________________________________________
✨ Créditos
•	Desarrollado por:
  Tecnoparque SENA.
  César Luis Gómez Ortega (Unicauca).
  Víctor Alfonso Rincón Ruiz (DAFE).
•	Proyecto creado usando Java + Angular JS.

---


