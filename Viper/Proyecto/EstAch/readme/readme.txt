Logros - Estad�sticas

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
Por ahora tiene: Nombre, Descripci�n, Hecho
	Hecho: Entero que revela si el logro est� completado en la Base de Datos.
	0: No logrado
	1: Logrado


Achievement_Activity.java && Statistics_Activity.java
Fragmentos que contendr� el GridView de los Logros y Estad�sticas respectivamente

Achievement_CursorAdapter.java && Statistics_CursorAdapter.java
Clase que rellenar� cada item del GridView.
Tener en consideraci�n que las imagenes para cada logro se ubican de manera est�tica
(manual). Para esto, existe el switch, el cual se debe rellenar por cada caso,
que en total son todos los achievements que se quieran poner

DB_Updater.java
Trabaja 100% con argumento Database_Helper para modificar cumplimiento de logros
y modificaci�n de estad�sticas.

Init.java
Su funci�n es insertar en la base de datos la lista de Logros y estad�sticas
que luego se mostrar�n en Achievement_Acitivity y Statistics_Activity

Statistics.java

Clase que contiene datos generales de un indicador estad�stico.
Por ahora tiene: Nombre, Descripci�n, Cantidad
	Cantidad: Veces que se  ejecuta una instrucci�n (Jugar, dormir, comer)

--------------------------------------------

CreateActivity.java
Las modificaciones realizadas fueron las siguientes:
113-116: Inicializaci�n de lista de logros y estad�sticas
 y confirmaci�n de logro "Cre� su primera mascota".
------------------
IMPORTANTE!!!!!!!!!!!!!!!!
------------------
Dado que la inicializaci�n se encuentra ac� (temporal), para versiones anteriores
no van a existir la lista de estad�sticas y logros.


Database_Helper.java
Creaci�n de Tablas y Funciones que corresponden a Logros y Estad�sticas.


MainActivity.java
Se habilit� en el menu del sherlock el gridview de los logros.
Es an�logo estad�sticas, pero como no sab�a si lo iban a poner en el menu
opt� por poner esa numa.
------------------
DATO!!!!!!!!!!!!!!!!
------------------
LA condici�n en el menu par que la muestre la cambi� para probar funcionalidad
En vez de isConnected() est� !isConnected()



	

