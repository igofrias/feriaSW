#pragma config(Sensor, S1, sonarSensor, sensorSONAR)
#pragma config(Sensor, S2, lightSensor, sensorLightInactive)
#pragma config(Sensor, S3, soundSensor, sensorSoundDB)
#pragma config(Motor, motorA, rightMotor, tmotorNXT, PIDControl)
#pragma config(Motor, motorB, leftMotor, tmotorNXT, PIDControl)
#define MAX_DIST 15 //distancia maxima de 10cm
#define MIN_DIST 10  //distancia minima de 5cm

//Global Variables
int SonarValue;
int LightValue;
//int SoundValue;

//Light level monitoring
task MonitorLight(){
	while(true){
		LightValue = SensorValue[lightSensor];
	}
}

//Distance monitoring (with Sonar)
task MonitorSonar(){
	while(true){
		SonarValue = SensorValue[sonarSensor];
	}
}
/*
//Sound level monitoring
task MonitorSound(){
	while(true){
		SoundValue = SensorValue[soundSensor];
	}
}
*/

//
//MAIN
//
task main(){
	//Variables definitions
	TFileHandle calFile;         				// create a file handle variable 'calFile'
	TFileIOResult IOResult;           	// create an IO result variable 'IOResult'
	string lightFile = "lightCal.dat";	// create and initialize a string variable 'lightCal'
	string soundFile = "soundCal.dat";	// create and initialize a string variable 'soundCal'
	int myFileSize = 10;              	// create and initialize an integer variable 'myFileSize'
	int LightData[10];									// create an array 'LightData' for data of light sensor
//	int SoundData[10];									// create an array 'SoundData' for data of light sensor
	float averLight = 0, devLight = 0;		// create two float variables 'averLight' & 'devLight'
//	float averSound = 0, devSound = 0;		// create two float variables 'averSound' & 'devSound'

	//Start MonitorSonar & MonitorLight
	StartTask(MonitorSonar);
	StartTask(MonitorLight);
//	StartTask(MonitorSound);

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
				LightData[i] = LightValue;
//				SoundData[i] = SoundValue;
				eraseDisplay();
				nxtDisplayString(4, "Obteniendo Datos");

				//Obtain Average
				averLight += (LightData[i]/10);
//				averSound += (SoundData[i]/10);

				//wait time
				wait1Msec(500);
			}

			//Stop motors
			motor[rightMotor] = 0;
			break;
		}
	}

	//Obtain Standard Deviation
	for(int i = 0;i<10;i++){
		devLight = pow( (LightData[i]-averLight), 2);
//		devSound = pow( (SoundData[i]-averSound), 2);
	}
	devLight = sqrt(devLight/9);
//	debSound = sqrt(devSound/9);

	//Delete Previous Data & Save New Data
	eraseDisplay();
	Delete("lightCal.dat", IOResult);
//	Delete("soundCal.dat", IOResult);
	nxtDisplayTextLine(2, "Guardando Datos");
	//Save Light Calibration
	OpenWrite(calFile, IOResult, lightFile, myFileSize);	// open for write: "myFile.txt"
	WriteFloat(calFile, IOResult, averLight);							// writes "average" value to the file handled by 'calFile'
	WriteFloat(calFile, IOResult, devLight);							// writes "deviation" value to the file handled by 'calFile'
	Close(calFile, IOResult);															// close the file (DON'T FORGET THIS STEP!)
/*
	//Save Sound Calibration
	OpenWrite(calFile, IOResult, soundFile, myFileSize);	// open for write: "myFile.txt"
	WriteFloat(calFile, IOResult, averSound);							// writes "average" value to the file handled by 'calFile'
	WriteFloat(calFile, IOResult, devSound);							// writes "deviation" value to the file handled by 'calFile'
	Close(calFile, IOResult);															// close the file (DON'T FORGET THIS STEP!)
*/	wait1Msec(500);

	//Finish Running Task
	StopAllTasks();

	//Exit Message
	eraseDisplay();
	nxtDisplayCenteredTextLine(2, "Calibracion");
	nxtDisplayCenteredTextLine(3, "Terminada");
	wait10Msec(1000);
	eraseDisplay();

}
