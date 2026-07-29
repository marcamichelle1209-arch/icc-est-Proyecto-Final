# **Informe Del Proyecto: Implementación y Visualización de rutas en un mapa de calles mediante BFS y DFS**


## _**Nombre de Integrantes:**_

- Nataly Jiménez Salazar 
- Evelyn Mayancela
- Michelle Marca

## **Fecha Inicio:** 17/17/2026

## **Fecha Entrega:** 29/07/2026

## _**Objetivo General:**_

_Diseñar e implementar un sistema en Java que permita representar un mapa urbano mediante un grafo de nodos y
encontrar el mejor camino entre dos puntos utilizando los algoritmos ``BFS`` y ``DFS``, aplicando los principios de la
programación orientada a objetos y las estructuras de datos estudiadas en la asignatura._

## **_Objetivos Específicos:_**
1. Modelar los elementos del mapa (puntos, nodos y conexiones) mediante clases y estructuras de datos
adecuadas.
2. Implementar los algoritmos de búsqueda ``BFS`` y ``DFS`` sobre una estructura de grafo genérica.
3. Permitir la creación interactiva de nodos y conexiones (unidireccionales o bidireccionales) sobre la imagen
de un mapa.
4. Desarrollar una interfaz gráfica que permita visualizar el mapa, los nodos, las conexiones y el camino
resultante.
5. Persistir la información del grafo generado para que pueda reutilizarse en sesiones posteriores.

## Descripción de los métodos 

``DFS PathFinder:`` 
El algoritmo ``DFS`` explora el grafo avanzando lo más posible por una rama antes de retroceder, utilizando una estructura de pila ``(LIFO)`` o recursividad. A diferencia de BFS, DFS no garantiza el camino más corto, pero resulta útil para explorar todas las rutas posibles o verificar la conectividad entre nodos.


``BFS PathFinder:`` 
El algoritmo ``BFS`` explora el grafo nivel por nivel, visitando primero todos los nodos vecinos del nodo actual antes de avanzar a los siguientes niveles. Utiliza una estructura de cola ``(FIFO)`` para gestionar el orden de visita. Cuando todas las conexiones tienen el mismo costo, BFS garantiza encontrar el camino más corto en número de saltos entre el nodo de origen y el nodo de destino.
 
## **Resultados obtenidos:**

*Tabla #1: Comparación de ``BFS`` y ``DFS``*

| Caso | Algoritmo | Inicio | Destino | Nodos Visitados | Cantidad Aristas | Tiempo |
| ---- |-----------|--------|---------|-----------------|------------------|--------|
|1     |   BFS     |   vvvv |jeuuy    |       9         |    2             |  2ms   |
|1     |   DFS     |  vvvv  | jeuuy   |       35        |     33           |  0ms    |
|2     |   BFS     |        |         |             10    |                 |  4ms      | 
|2     |   DFS     |        |         |                 |                  |        |       
|3     |   BFS     |        |         |                 |                  |        |
|3     |   DFS     |        |         |                 |                  |        |


## **Ánalisis Requerido: Preguntas hechas en base a las pruebas:**

- ¿Qué diferencias se observaron en el orden de exploración de BFS y DFS?
- ¿BFS encontró una ruta con menor cantidad de aristas en todos los casos evaluados?
- ¿DFS encontró rutas diferentes a las obtenidas con BFS?
- ¿Qué algoritmo visitó más nods en cada caso?
- ¿Los tiempos de ejecución fueron suficientes para determinar cuál algortimo es mejor?
- ¿Cómo influyó la estructura del grafo en el comportamiento de cada algoritmo?
- ¿Qué ventajas aporta separar la lógica del algoritmo de la visualización?
- ¿Qué mejoras podrían implementarse para trabajar con calles ponderadas?



## Conclusiones 

Est. Nataly Jiménez Salazar: La representación de un mapa urbano como un grafo de nodos y conexiones permite aplicar de forma
práctica los conceptos teóricos de estructuras de datos no lineales.

Est. Michelle Marca: 

Est. Evelyn Mayancela:BFS encuentra siempre el camino más corto en saltos, mientras que DFS, al profundizar por una sola rama, visitó muchos más nodos antes de llegar al destino. Separar los algoritmos de la interfaz permitió animar y visualizar claramente esta diferencia.


