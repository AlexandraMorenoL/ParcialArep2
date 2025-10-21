### Parcial Segundo Tercio AREP
## Alexandra Moreno Latorre
## Profesor: Luis Daniel Benavides Navarro



Para este parcial uso Spring Initializer para obtener toda la estructura del proyecto.

![alt text](image.png)

Al revisar la estructura ya tengo el Servicio de la aplicación y ahora debo crear el controlador 

![alt text](image-1.png)

Tengo unos errores para devolver la respuesta como se pide en el enunciado 
```bash 
Ejemplo 1 de un llamado:
 
https://amazonxxx.x.xxx.x.xxx:{port}/linearsearch?list=10,20,13,40,60&value=13
 
Salida. El formato de la salida y la respuesta debe ser un JSON con el siguiente formato
 
{
 "operation": "linearSearch",
 "inputlist": "10,20,13,40,60",
 "value": "13"
 "output":  "2"
}

```


Para la parte del proxy, creé el controlador, la aplicación del proxy el algotirmo round-robin

![alt text](image-2.png)

![alt text](image-3.png)

![alt text](image-4.png)

Para desplegar en AWS Academy creamos las dos instancias EC2

![alt text](image-5.png)

![alt text](image-6.png)

