Logros - Estadísticas

readme.txt

/*********
Clases Nuevas
**********/

Achievement.java
Achievement_Activity.java
Achievement_Cursor Adapter.java
DB_Updater.java
Init.java
Statistics.java
Statistics_Activity.java
Statistics_CursorAdapter.java

/*******
Clases Modificadas
*******/

CreateActivity.java
Database_Helper.java
MainActivity.java




Achievement.java

Clase que contiene datos generales de un achievement.
Por ahora tiene: Nombre, Descripción, Hecho
	Hecho: Entero que revela si el logro está completado en la Base de Datos.
	0: No logrado
	1: Logrado


Achievement_Activity.java && Statistics_Activity.java
Fragmentos que contendrá el GridView de los Logros y Estadísticas respectivamente

Achievement_CursorAdapter.java && Statistics_CursorAdapter.java
Clase que rellenará cada item del GridView.
Tener en consideración que las imagenes para cada logro se ubican de manera estática
(manual). Para esto, existe el switch, el cual se debe rellenar por cada caso,
que en total son todos los achievements que se quieran poner

DB_Updater.java
Trabaja 100% con argumento Database_Helper para modificar cumplimiento de logros
y modificación de estadísticas.

Init.java
Su función es insertar en la base de datos la lista de Logros y estadísticas
que luego se mostrarán en Achievement_Acitivity y Statistics_Activity

Statistics.java

Clase que contiene datos generales de un indicador estadístico.
Por ahora tiene: Nombre, Descripción, Cantidad
	Cantidad: Veces que se  ejecuta una instrucción (Jugar, dormir, comer)

--------------------------------------------

CreateActivity.java
Las modificaciones realizadas fueron las siguientes:
113-116: Inicialización de lista de logros y estadísticas
 y confirmación de logro "Creó su primera mascota".
------------------
IMPORTANTE!!!!!!!!!!!!!!!!
------------------
Dado que la inicialización se encuentra acá (temporal), para versiones anteriores
no van a existir la lista de estadísticas y logros.


Database_Helper.java
Creación de Tablas y Funciones que corresponden a Logros y Estadísticas.


MainActivity.java
Se habilitó en el menu del sherlock el gridview de los logros.
Es análogo estadísticas, pero como no sabía si lo iban a poner en el menu
opté por poner esa numa.
------------------
DATO!!!!!!!!!!!!!!!!
------------------
LA condición en el menu par que la muestre la cambié para probar funcionalidad
En vez de isConnected() está !isConnected()



	

