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

![Descripción de la imagen](src/resources/BFS-DFS.png)

``DFS PathFinder:`` 
El algoritmo ``DFS`` explora el grafo avanzando lo más posible por una rama antes de retroceder utilizando ``(LIFO)`` o recursividad. A diferencia de ``BFS``, ``DFS`` no garantiza el camino más corto, pero resulta útil para explorar todas las rutas posibles o verificar la conexión entre nodos.

``BFS PathFinder:`` 
El algoritmo ``BFS`` explora el grafo nivel por nivel, visitando primero todos los nodos vecinos del nodo actual antes ir a los siguientes, utiliza ``(FIFO)`` para gestionar el orden de visita. Cuando todas las conexiones tienen el mismo costo, ``BFS`` garantiza encontrar el camino más corto entre el nodo de inicio y el nodo de destino.
 
## **Resultados obtenidos:**

*Tabla #1: Comparación de ``BFS`` y ``DFS``*

| Caso | Algoritmo | Inicio | Destino | Nodos Visitados | Cantidad Aristas | Tiempo |
| ---- |-----------|--------|---------|-----------------|------------------|--------|
|1     |     BFS   |  axa   | loni    |    10           |  2               | 4 ms   |
|1     |     DFS   |  axa   | loni    |    37           |   23             | 0 ms   |
|2     |      BFS  |A007    | A001    |    17           |  3               | 1 ms   | 
|2     |      DFS  |A007    | A001    |    19           |  4               | 0 ms   |       
|3     |      BFS  |xi      | haya    |    14           |  2               | 2 ms   |
|3     |    DFS    |xi      | haya    |    23           |  15              | 0 ms   |

![BFS_Caso1](src/resources/BFS_Caso1.png)
_Figura 1. Exploración BFS: Caso 1 (axa a loni)_

![DFS_Caso1](src/resources/DFS_Caso1.png)
_Figura 2. Exploración DFS: Caso 1 (axa a loni)_

![BFS_Caso2](src/resources/BFS_Caso2.jpeg)
_Figura 3. Exploración BFS: Caso 2 (A007 a A001)_

![DFS_Caso2](src/resources/DFS_Caso2.jpeg)
_Figura 4. Exploración DFS: Caso 2 (A007 a A001)_

![BFS_Caso3](src/resources/BFS_Caso3.jpeg)
_Figura 5. Exploración BFS — Caso 3 (xi a haya)_

![DFS_Caso3](src/resources/DFS_Caso3.jpeg)
_Figura 6. Exploración DFS — Caso 3 (xi a haya)_

## **Grafica UML del proyecto :**
![Diagrama UML](src\resources\diagramaUML.png)
_Figura 7. UML_

## **Ánalisis Requerido: Preguntas hechas en base a las pruebas:**

``Costo``: No hace referencia a dinero, si no, cuan caro es realmente recorrer la conexión establecida (tiempo, distancia o dificultad/restricción del paso)

- ``¿Qué diferencias se observaron en el orden de exploración de BFS y DFS?``
En las tres pruebas, BFS exploró por anchura y llegó al destino visitando entre 10 y 17 nodos. mientras que DFS se comprometió con una rama profunda antes de retroceder, visitando siempre más nodos que BFS: 19 a 37.

- ``¿BFS encontró una ruta con menor cantidad de aristas en todos los casos evaluados?``
Sí, en los tres casos: 
Caso #1 (2 vs 23 aristas). 
Caso #2 (3 vs 4 aristas). 
Caso #3: 2 vs 15 aristas. 
BFS encontró consistentemente la ruta más corta o igual de corta.

- ``¿DFS encontró rutas diferentes a las obtenidas con BFS?``
Sí, en las tres pruebas ejecutadas, las rutas fueron distintas, el recorrido en los casos Nro. 1 y Nro. 3 fue más largo, mientras que en el caso Nro. 2 la diferencia fue menor (4 vs 3 conexiones), lo que muestra que la ventaja de BFS es más marcada cuando el destino está lejos en la estructura de exploración de DFS.

- ``¿Qué algoritmo visitó más nods en cada caso?`` 
El algoritmo DFS visitó, en los tres casos: 
37 vs 10, 
19 vs 17, 
23 vs 14.

- ``¿Los tiempos de ejecución fueron suficientes para determinar cuál algortimo es mejor?``
No, los tiempos son de 0 a 4 ms, prácticamente inapreciables porque el grafo es pequeño. No sirven para comparar rendimiento, pero lo que sí podemos considerar como un respaldo en los tres casos, es la cantidad de nodos visitados y conexiones en la ruta, donde BFS fue mejor en los tres.

- ``¿Cómo influyó la estructura del grafo en el comportamiento de cada algoritmo?``
Cuando el destino estaba "cerca" en términos de niveles de conexión (los Casos Nro. 1 y Nro. 3), BFS lo encontró casi de inmediato mientras DFS se desviaba por otras calles. Cuando el destino estaba en una zona más densamente conectada cerca del punto de inicio (el Caso 2), la diferencia entre ambos algoritmos se estabilizó, o sea que fue más fácil ver como trabajaban.

- ``¿Qué ventajas aporta separar la lógica del algoritmo de la visualización?``
Separar la lógica de búsqueda: PathFinder, BFSPathFinder, DFSPathFinder de la interfaz gráfica: MapPanel y MainFrame, permite que cada algoritmo se pueda probar, modificar o reemplazar sin tocar el código que dibuja en el mapa, por ejemplo, para agregar un nuevo algoritmo de búsqueda, solo tendríamos que crear una nueva clase que implemente la interfaz ``PathFinder``. Además, esta separación facilita las pruebas: se puede ejecutar y verificar la lógica de un algoritmo sobre el ``Graph``, también hace el código más legible y mantenible, ya que cada clase tiene asignada una única responsabilidad.

- ``¿Qué mejoras podrían implementarse para trabajar con calles ponderadas?``
Actualmente las conexiones del grafo no tienen un costo, solo indican si existe conexión uni o bidireccionales. Para trabajar con calles ponderadas, representando distancia real, tiempo de recorrido o tráfico, se necesitaría:
    1. Agregar un atributo de peso a la clase ``Edge``.

    2. Modificar ``Graph`` para almacenar y mostrar ese peso al construir o consultar las conexiones.

    3. Reemplazar o complementar ``BFS/DFS``, que no consideran pesos, con un algoritmo que sí los use como ``A*`` ya que ``BFS`` solo garantiza el camino más corto en número de saltos, no en costo real.
    4. Actualizar la interfaz gráfica para permitir ingresar el peso al crear una conexión, por ejemplo, un txtField, y opcionalmente mostrarlo visualmente sobre el mapa, ya sea con el grosor o color de línea según el peso.  

## Conclusiones Individuales          

``Est. Nataly Jiménez Salazar``: El modelo ``MVC`` facilitó la separación de responsabilidades entre la lógica de búsqueda de caminos, el manejo de datos del mapa y la interfaz gráfica, favoreciendo el mantenimiento y la escalabilidad del
proyecto.


``Est. Michelle Marca``: La representación de un mapa urbano como un grafo de nodos y conexiones que nos permiten aplicar de forma práctica los conceptos teóricos de estructuras de datos no lineales.

``Est. Evelyn Mayancela:`` ``BFS`` encuentra siempre el camino más corto en saltos, mientras que ``DFS``, al profundizar por una sola rama, visitó muchos más nodos antes de llegar al destino. Separar los algoritmos de la interfaz permitió animar y visualizar claramente esta diferencia.

## Recomendaciones: 

- Incorporar pesos en las conexiones para permitir, en trabajos futuros, la implementación de algoritmos
como ``Dijkstra`` o ``A*`` y comparar sus resultados frente a ``BFS`` y ``DFS``.
- Agregar validaciones visuales que impidan crear conexiones duplicadas o nodos superpuestos sobre el
mapa.

