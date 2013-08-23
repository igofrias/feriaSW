#pragma config(Sensor, S1, sonarSensor, sensorSONAR)
#pragma config(Sensor, S2, lightSensor, sensorLightInactive)
#pragma config(Motor, motorA, rightMotor, tmotorNXT, PIDControl)
#pragma config(Motor, motorB, leftMotor, tmotorNXT, PIDControl)
#define MAX_DIST 15 //distancia maxima de 10cm
#define MIN_DIST 10  //distancia minima de 5cm

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
	TFileHandle lightCal;         			// create a file handle variable 'lightCal'
	TFileIOResult IOResult;           	// create an IO result variable 'IOResult'
	string lightFile = "lightCal.dat";	// create and initialize a string variable 'myFileName'
	int myFileSize = 10;              	// create and initialize an integer variable 'myFileSize'
	int LightData[10];									// create an array 'LightData' for data of light sensor
	float average = 0, deviation = 0;						// create two float variables 'average' & 'deviation'

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
			motor[rightMotor] = 15;				//Right motor moves at 50% power
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
				LightData[i] = LightValue;
				eraseDisplay();
				nxtDisplayString(4, "Obteniendo Datos");
//				nxtDisplayString(4, "Luz: %3d", LightValue);

				//Obtain Average
				average += (LightData[i]/10);

				//wait time
				wait1Msec(500);
			}

			//Stop motors
			motor[rightMotor] = 0;
			break;
		}
	}

	for(int i = 0;i<10;i++){
		deviation = pow( (LightData[i]-average), 2);
	}
	deviation = sqrt(deviation/9);
/*
	eraseDisplay();
	nxtDisplayString(3, "Promedio  : %3d", average);
	nxtDisplayString(4, "Desviacion: %3d", deviation);
	wait1Msec(2000);
*/
	//Delete Previous Data & Save New Data
	eraseDisplay();
	Delete("lightCal.dat", IOResult);
	nxtDisplayTextLine(2, "Guardando Datos");
	OpenWrite(lightCal, IOResult, lightFile, myFileSize);	// open for write: "myFile.txt"
	WriteFloat(lightCal, IOResult, average);							// writes "average" value to the file handled by 'lightCal'
	WriteFloat(lightCal, IOResult, deviation);						// writes "deviation" value to the file handled by 'lightCal'
	Close(lightCal, IOResult);														// close the file (DON'T FORGET THIS STEP!)

	wait1Msec(500);

	//Finish Running Task
	StopAllTasks();

	//Exit Message
	eraseDisplay();
	nxtDisplayCenteredTextLine(2, "Calibracion");
	nxtDisplayCenteredTextLine(3, "Terminada");
	wait10Msec(1000);
	eraseDisplay();

}
