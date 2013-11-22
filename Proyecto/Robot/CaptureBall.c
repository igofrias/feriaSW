#pragma config(Motor, motorB, clampsMotor, tmotorNXT, PIDControl)
#pragma config(Sensor, S3, colorSensor, sensorCOLORFULL)

/*******************************************************************
 ******************************************************************/
/*
 * Task Statement
 */
task ShowEyes();
task MonitorColor();

/*
 * Function Statement
 */
void leerMensajes();

/*
 * Global Variables Statements
 */
int ColorVal;
int nAccion;
int nMensaje = 0;
int vel = 50;
int mtime = 1500;

/*******************************************************************
 ******************************************************************/
task main(){
	StartTask(ShowEyes);
	leerMensajes();
	return;
}

/*******************************************************************
 ******************************************************************/
/*
 * Task showEyes
 */
task ShowEyes(){
	while(true){
		nxtDisplayRICFile(0, 0, "OpenEyes.ric");
		wait1Msec(2000);
		nxtDisplayRICFile(0, 0, "CloseEyes.ric");
		wait1Msec(400);
	}
}

/*
 * Monitor Color
 */
task MonitorColor(){
	while(true){
		ColorVal = SensorValue[colorSensor];
	}
}

/*******************************************************************
 ******************************************************************/

/*
 * Function leerMensajes
 */
void leerMensajes(){
	int capture=0;
	while (true){
		nMensaje = message;
		if (nMensaje != 0){
			nAccion = messageParm[0];
			switch(nAccion){
				case 1 : //Capture Red Ball
					StopTask(ShowEyes);
					eraseDisplay();
					StartTask(MonitorColor);
					nxtDisplayTextLine(2, "Buscando Pelota");
					nxtDisplayTextLine(3, "Roja");
					wait1Msec(2000);
					while(true){
						switch(ColorVal){
							case REDCOLOR:
								capture = 1;
								motor[clampsMotor] = vel;
								wait1Msec(mtime);
								break;
						}
						if (capture == 1){
							capture = 0;
							break;
						}
					}
					motor[clampsMotor] = 0;

					eraseDisplay();
					StartTask(ShowEyes);
					StopTask(MonitorColor);
					break;
				case 2 : //Capture Blue Ball
					StopTask(ShowEyes);
					eraseDisplay();
					StartTask(MonitorColor);
					nxtDisplayTextLine(2, "Buscando Pelota");
					nxtDisplayTextLine(3, "Azul");
					wait1Msec(2000);
					while(true){
						switch(ColorVal){
							case BLUECOLOR:
								capture = 1;
								motor[clampsMotor] = vel;
								wait1Msec(mtime);
								break;
						}
						if (capture == 1){
							capture = 0;
							break;
						}
					}
					motor[clampsMotor] = 0;

					eraseDisplay();
					StartTask(ShowEyes);
					StopTask(MonitorColor);
					break;
				case 3 : //Capture Green Ball
					StopTask(ShowEyes);
					eraseDisplay();
					StartTask(MonitorColor);
					nxtDisplayTextLine(2, "Buscando Pelota");
					nxtDisplayTextLine(3, "Verde");
					wait1Msec(2000);
					while(true){
						switch(ColorVal){
							case GREENCOLOR:
								capture = 1;
								motor[clampsMotor] = vel;
								wait1Msec(mtime);
								break;
						}
						if (capture == 1){
							capture = 0;
							break;
						}
					}
					motor[clampsMotor] = 0;

					eraseDisplay();
					StartTask(ShowEyes);
					StopTask(MonitorColor);
					break;
			}
			ClearMessage();
		}
	}
}
