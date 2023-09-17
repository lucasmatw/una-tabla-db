# Una Tabla DB

Base de datos relacional simple y limitada a una única tabla.

## Tecnologías

- Java 17
- maven
- Docker

## ⚡️ Quick start

⚠️ `Se necesita Docker levantado`

Ejecución de REPL:

1. Clonar repositorio

```bash
git clone git@github.com:lucasmatw/una-tabla-db.git
```

2. Ejecutar el `run.sh` en el root de proyecto

```bash
cd una-tabla-db
chmod +x run.sh
./run.sh
```

3. Opcional: `run.sh` puede recibir un argumento que representa el archivo de la base de datos.

    ```bash
    ./run.sh /path/to/db/file
    ```
   
Aclaración: se hace una copia del archivo y se lleva a la imagen de docker. El archivo original no se modifica.

El `run.sh` hace el build de la imagen y la corre en un container.