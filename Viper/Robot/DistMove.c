#pragma config(Sensor, S1, sonarSensor, sensorSONAR)
#pragma config(Sensor, S2, lightSensor, sensorLightInactive)
#pragma config(Motor, motorA, rightMotor, tmotorNXT, PIDControl)
#pragma config(Motor, motorB, leftMotor, tmotorNXT, PIDControl)
#define MAX_DIST 10 //distancia maxima de 10cm
#define MIN_DIST 5  //distancia minima de 5cm

//variables globales
int SonarValue;
int LightValue;

//Medicion de nivel de luz con Sensor correspondiente
task MonitorLight(){
	while(true){
		LightValue = SensorValue[lightSensor];
	}
}

//Medicion de distancias con Sonar
task MonitorSonar(){
	while(true){
		SonarValue = SensorValue[sonarSensor];
	}
}

/*
 *	Tarea principal
 */
task main(){
	//Variables definitions
	TFileHandle myFileHandle;          // create a file handle variable 'myFileHandle'
	TFileIOResult IOResult;            // create an IO result variable 'IOResult'
	string lightFile = "lightCal.txt"; // create and initialize a string variable 'myFileName'
	int myFileSize = 10;               // create and initialize an integer variable 'myFileSize'

	//Start MonitorSonar & MonitorLight
	StartTask(MonitorSonar);
	StartTask(MonitorLight);

	//Synch Motors
	nSyncedMotors = synchAB;	//Left motor slaved to Right motor
	nSyncedTurnRatio = -100;	//Left motor turns -100% of right motor

	//Display message
	nxtDisplayTextLine(2, "Para iniciar,");
	nxtDisplayTextLine(3, "coloque su mano");
	nxtDisplayTextLine(4, "entre 10 y 5cm");
	nxtDisplayTextLine(5, "del sonar.");

	//Wait condition
	while(true){
		//Detect distance range
		if( (SonarValue<=MAX_DIST)&&(SonarValue>=MIN_DIST) ){
			eraseDisplay();
			motor[rightMotor] = 20;				//Right motor moves at 50% power
																		//Left motor automatically moves at -50%
																		//because of synch and synch ratio.

			//Display message
			nxtDisplayCenteredTextLine(3, "Calibrando...");
			nxtDisplayTextLine(4, "Espere unos");
			nxtDisplayTextLine(5, "momentos");

			//Light medition
			for(int i = 0;i<10; i++){
				/*
				 *
				 *	CODIGO DE SENSOR!!!!!
				 *
				 */
				nxtDisplayString(3, "Medicion %2d", i);
				nxtDisplayString(4, "Sonar: %3d", SonarValue);

				//wait time
				wait1Msec(500);
			}

			//Stop motors
			motor[rightMotor] = 0;
			break;
		}
	}

	/*
	 *
	 * ANALIZAR DATOS!!!!!!
	 *
	 */

	//Save data
	eraseDisplay();
	nxtDisplayTextLine(2, "Guardando Datos");
	OpenWrite(myFileHandle, IOResult, lightFile, myFileSize);  // open for write: "myFile.txt"
	wait1Msec(500);

	//Finish Running Task
	StopAllTasks();

	//Exit Message
	eraseDisplay();
	nxtDisplayCenteredTextLine(2, "Calibracion");
	nxtDisplayCenteredTextLine(3, "Terminada");
	wait10Msec(100);
	eraseDisplay();

}
