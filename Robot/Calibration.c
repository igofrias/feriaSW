#pragma config(Sensor, S1, lightRSensor, sensorLightInactive)
#pragma config(Sensor, S2, lightLSensor, sensorLightInactive)
//#pragma config(Sensor, S3, soundSensor, sensorSoundDB)

//Global Variables
int LightRVal;
int LightLVal;

//TASK
task MonitorRLight();
task MonitorLLight();

//
//MAIN
//
task main(){
	//Variables definitions
	TFileHandle calFile;           				// create a file handle variable 'calFile'
	TFileIOResult IOResult;           	  // create an IO result variable 'IOResult'
	string lightRFile = "lightRCal.dat";	// create and initialize a string variable 'lightCal'
	string lightLFile = "lightLCal.dat";	// create and initialize a string variable 'soundCal'
	int myFileSize = 10;                	// create and initialize an integer variable 'myFileSize'
	int LightRData[10];					  				// create an array 'LightData' for data of light sensor
	int LightLData[10];						  			// create an array 'LightLData' for data of light sensor
	float averRLight = 0, devRLight = 0;	// create two float variables 'averLight' & 'devLight'
	float averLLight = 0, devLLight = 0; 	// create two float variables 'averLLight' & 'devLLight'

	//Start MonitorRLight & MonitorLLight
	StartTask(MonitorRLight);
	StartTask(MonitorLLight);

	//Display message
	nxtDisplayCenteredTextLine(3, "Calibrando...");
	nxtDisplayTextLine(4, "Espere unos");
	nxtDisplayTextLine(5, "momentos");

	//Light medition
	for(int i = 0;i<10; i++){
		LightRData[i] = LightRVal;
		LightLData[i] = LightLVal;
		eraseDisplay();
		nxtDisplayString(4, "Obteniendo Datos");

		//Obtain Average
		averRLight += (LightRData[i]/10.0);
		averLLight += (LightLData[i]/10.0);

		//wait time
		wait1Msec(500);
	}

	//Obtain Standard Deviation
	for(int i = 0;i<10;i++){
		devRLight = pow( (LightRData[i]-averRLight), 2);
		devLLight = pow( (LightLData[i]-averLLight), 2);
	}
	devRLight = sqrt(devRLight/9);
	devLLight = sqrt(devLLight/9);

	//Delete Previous Data & Save New Data
	eraseDisplay();
	Delete("lightRCal.dat", IOResult);
	Delete("lightLCal.dat", IOResult);
	nxtDisplayTextLine(2, "Guardando Datos");
	//Save Light Calibration (Right)
	OpenWrite(calFile, IOResult, lightRFile, myFileSize);	// open for write: "myFile.txt"
	WriteFloat(calFile, IOResult, averRLight);							// writes "average" value to the file handled by 'calFile'
	WriteFloat(calFile, IOResult, devRLight);							// writes "deviation" value to the file handled by 'calFile'
	Close(calFile, IOResult);															// close the file (DON'T FORGET THIS STEP!)

	//Save Light Calibration (Left)
	OpenWrite(calFile, IOResult, lightLFile, myFileSize);	// open for write: "myFile.txt"
	WriteFloat(calFile, IOResult, averLLight);							// writes "average" value to the file handled by 'calFile'
	WriteFloat(calFile, IOResult, devLLight);							// writes "deviation" value to the file handled by 'calFile'
	Close(calFile, IOResult);															// close the file (DON'T FORGET THIS STEP!)
	wait1Msec(1000);

	//Exit Message
	eraseDisplay();
	nxtDisplayTextLine(2, "Sensor Derecho");
	nxtDisplayTextLine(3, "Nivel Luz: %3d", averRLight);
	nxtDisplayTextLine(4, "Error Luz: %3d", devRLight);
	wait10Msec(500);

	nxtDisplayTextLine(2, "Sensor Izquierdo");
	nxtDisplayTextLine(3, "Nivel Luz: %3d", averLLight);
	nxtDisplayTextLine(4, "Error Luz: %3d", devLLight);
	wait10Msec(500);

	eraseDisplay();
	nxtDisplayCenteredTextLine(2, "Calibracion");
	nxtDisplayCenteredTextLine(3, "Terminada");
	wait10Msec(100);
	eraseDisplay();

	//Finish Running Task
	StopAllTasks();
}

//Light level monitoring (Right)
task MonitorRLight(){
	while(true){
		LightRVal = SensorValue[lightRSensor];
	}
}

//Light level monitoring (Left)
task MonitorLLight(){
	while(true){
		LightLVal = SensorValue[lightLSensor];
	}
}
