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
int RandNum;
int vel = 50;
int mtime = 1300;

/*******************************************************************
 ******************************************************************/
task main(){
	srand(nSysTime);
	RandNum = 2 + random(3);
	//StartTask(ShowEyes);
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
		switch(RandNum){
			case 2 : //Capture Blue Ball
				StopTask(ShowEyes);
				eraseDisplay();
				StartTask(MonitorColor);
				nxtDisplayTextLine(2, "Buscando Pelota");
				nxtDisplayTextLine(3, "Azul");
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
				nxtDisplayTextLine(2, "Pelota Atrapada!");
				wait1Msec(2000);
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
				nxtDisplayTextLine(2, "Pelota Atrapada!");
				wait1Msec(2000);
				eraseDisplay();
				StartTask(ShowEyes);
				StopTask(MonitorColor);
				break;
			case 4 : //Capture Yellow Ball
				StopTask(ShowEyes);
				eraseDisplay();
				StartTask(MonitorColor);
				nxtDisplayTextLine(2, "Buscando Pelota");
				nxtDisplayTextLine(3, "Amarilla");
				while(true){
					switch(ColorVal){
						case YELLOWCOLOR:
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
				nxtDisplayTextLine(2, "Pelota Atrapada!");
				wait1Msec(2000);
				eraseDisplay();
				StartTask(ShowEyes);
				StopTask(MonitorColor);
				break;
			case 5 : //Capture Roja Ball
				StopTask(ShowEyes);
				eraseDisplay();
				StartTask(MonitorColor);
				nxtDisplayTextLine(2, "Buscando Pelota");
				nxtDisplayTextLine(3, "Roja");
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
				nxtDisplayTextLine(2, "Pelota Atrapada!");
				wait1Msec(2000);
				eraseDisplay();
				StartTask(ShowEyes);
				StopTask(MonitorColor);
				break;
		}
		if(capture==1){
			break;
		}
	}
}
