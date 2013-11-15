#pragma platform(NXT)

int nBoton;
int nAccion;
int nMensaje = 0;

task enviarMensajes();
void leerMensajes();
void comprobarConexion();

/*
 * Task Main
 */
task main(){
	comprobarConexion();
	eraseDisplay();
	bNxtLCDStatusDisplay = true;
	wait1Msec(500);
	StartTask(enviarMensajes);
	leerMensajes();
	return;
}

/*
 * Task enviarMensajes
 */
task enviarMensajes(){
	while (true){
		nBoton = nNxtButtonPressed;
		if(nBoton > -1)
			sendMessageWithParm(nBoton);
	}
	return;
}

/*
 * Function leerMensajes
 */
void leerMensajes(){
	while (true){
		nMensaje = message;
		if (nMensaje != 0){
			nAccion = messageParm[0];
			switch(nAccion){
				case 1 : //Left Bottom
					motor[motorA] = -30;
					motor[motorC] = -10;
					break;
				case 2 : //Right Bottom
					motor[motorA] = -10;
					motor[motorC] = -30;
					break;
				case 3 : //Center Bottom
					motor[motorA] = -30;
					motor[motorC] = -30;
					break;
			}
			ClearMessage();
		}
	}
}

/*
 * Function comprobarConexion
 */
void comprobarConexion(){
	if (nBTCurrentStreamIndex >= 0)
		return;
	PlaySound(soundLowBuzz);
	PlaySound(soundLowBuzz);
	eraseDisplay();
	nxtDisplayCenteredTextLine(3, "No esta");
	nxtDisplayCenteredTextLine(4, "Conectado");
	wait1Msec(3000);
	StopAllTasks();
}
