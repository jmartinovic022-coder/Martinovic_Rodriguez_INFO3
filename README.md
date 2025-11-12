# Proyecto Final - Estructuras de Datos (INFO3)
## Cátedra: Alejandro Silvestre

Este repositorio contiene el proyecto final consolidado de la materia, el cual implementa un conjunto de Estructuras de Datos genéricas y su aplicación en diversos ejercicios prácticos, cumpliendo con los requisitos de modularidad y código único.

---

### 👨‍💻 Integrantes del Grupo

* Jovan Martinovic -
* Mateo Rodriguez 

---

### 📂 Estructura del Proyecto

El proyecto está organizado en dos paquetes principales, siguiendo un diseño modular:

* `/estructuras`
    Contiene la "biblioteca" central del proyecto. Cada estructura de datos (TDA) está implementada como una clase genérica (`<T>`) en su propio paquete para ser reutilizada.
    * `estructuras.lista_enlazada`
    * `estructuras.pila`
    * `estructuras.cola`
    * `estructuras.arbol_AVL`
    * `estructuras.arbol_rojinegro`
    * `estructuras.monticulo_binario`
    * `estructuras.tabla_hash`

* `/practicos`
    Contiene los diferentes prácticos (ejercicios) de la cursada. **Ningún práctico implementa su propia estructura**, sino que `importan` y consumen las clases genéricas definidas en `/estructuras`.
    * `src.practico_01_gestor_tareas`
    * `src.practico_02_recursividad`
    * `... (y así)`
    * `src.practico_09_integrador` 

---

    ### 📚 Documentación (JavaDoc)

    Las clases implementadas dentro de la carpeta `estructuras/` están documentadas
    con JavaDoc en las cabeceras de clase y en los métodos públicos principales.
    Esto facilita su comprensión y permite generar la documentación HTML estándar.

    Para generar la documentación en formato HTML desde la raíz del proyecto:

    ```bash
    javadoc -d docs $(find estructuras -name "*.java")
    ```

    El resultado se ubicará en la carpeta `docs/`.


### 🚀 Cómo Compilar y Ejecutar

**Requisitos:**
* Tener instalado Java (JDK 11 o superior).

**Pasos:**

1.  Clonar el repositorio:
    ```bash
    git clone [https://www.youtube.com/watch?v=GtN6N11qSgA](https://www.youtube.com/watch?v=GtN6N11qSgA)
    ```

2.  Navegar a la carpeta raíz del proyecto:
    ```bash
    cd INFO3_MARTINOVIC_RODRIGUEZ-M...
    ```

3.  **Compilar:**
    Crearemos un directorio `bin` para los archivos `.class` compilados y luego compilaremos todo el código fuente.

    ```bash
    mkdir bin
    javac -d bin $(find . -name "*.java")
    ```

4.  **Ejecutar:**
    Para ejecutar el proyecto, se debe invocar el `Main` del práctico integrador, el cual presenta un menú para navegar a todas las funcionalidades.

    **IMPORTANTE:** (¡Acá tenés que poner el comando correcto! Si tu Main está en `src/practico_09_integrador/src/Main.java`, el comando es este)
    ```bash
    java -cp bin src.practico_09_integrador.src.Main
    ```