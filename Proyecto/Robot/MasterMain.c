#pragma config(Motor, motorA, rightMotor,  tmotorNXT, PIDControl)
#pragma config(Motor, motorB, clampsMotor, tmotorNXT, PIDControl)
#pragma config(Motor, motorC,  leftMotor,  tmotorNXT, PIDControl)
#pragma config(Sensor, S1, lightRSensor, sensorLightInactive)
#pragma config(Sensor, S2, lightLSensor, sensorLightInactive)
#pragma config(Sensor, S3, colorSensor,  sensorCOLORFULL)


/*****************************************
 *            Task Statement
 ****************************************/
task ShowEyes();
task MonitorRLight();
task MonitorLLight();
task MonitorColor();

/*****************************************
 *            Function Statement
 ****************************************/
void sendMess(int nData);
void checkConnection(); //Not used.... yet...
void readMessages();
void calibrate();
//void captureBall(int iColor);

/*****************************************
 *       Global Variable Statement
 ****************************************/
int ColorVal;
int LightRVal;
int LightLVal;
int nAction;
int nMensaje = 0;




/*****************************************
 *            Main Task
 ****************************************/
task main(){
	StartTask(ShowEyes);
	readMessages();
	return;
}

/*****************************************
 *            Task Definition
 ****************************************/
//Show Normal Eyes
task ShowEyes(){
	while(true){
		nxtDisplayRICFile(0, 0, "OpenEyes.ric");
		wait1Msec(2000);
		nxtDisplayRICFile(0, 0, "CloseEyes.ric");
		wait1Msec(400);
	}
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

//Type Color monitoring
task MonitorColor(){
	while(true){
		ColorVal = SensorValue[colorSensor];
	}
}


/*****************************************
 *          Function Definition
 ****************************************/
//Function: sendMess
void sendMess(int nData){
	sendMessageWithParm(nData);
	return;
}

//Function: checkConnection
void checkConnection(){
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

//Function: readMessages
void readMessages(){
	while (true){
		nMensaje = message;
		if (nMensaje != 0){
			nAction = messageParm[0];
			switch(nAction){
				case 1 : //Normal Eyes Case!!
					break;
				case 2 :
					StopTask(ShowEyes);
					eraseDisplay();
					nxtDisplayRICFile(0, 0, "AngryEyes.ric");
					wait1Msec(4000);
					eraseDisplay();
					StartTask(ShowEyes);
					break;
				case 3 :
					StopTask(ShowEyes);
					eraseDisplay();
					nxtDisplayRICFile(0, 0, "BoredEyes.ric");
					wait1Msec(4000);
					eraseDisplay();
					StartTask(ShowEyes);
					break;
				case 4 :
					StopTask(ShowEyes);
					eraseDisplay();
					nxtDisplayRICFile(0, 0, "CloseEyes.ric");
					wait1Msec(4000);
					eraseDisplay();
					StartTask(ShowEyes);
					break;
				case 5 :
					StopTask(ShowEyes);
					eraseDisplay();
					nxtDisplayRICFile(0, 0, "HappyEyes.ric");
					wait1Msec(4000);
					eraseDisplay();
					StartTask(ShowEyes);
					break;
				case 6 :
					StopTask(ShowEyes);
					eraseDisplay();
					nxtDisplayRICFile(0, 0, "ShameEyes.ric");
					wait1Msec(4000);
					eraseDisplay();
					StartTask(ShowEyes);
					break;
				case 30:
					StopTask(ShowEyes);
					eraseDisplay();
					nxtDisplayRICFile(0, 0, "DeadEyes.ric");
					wait1Msec(4000);
					eraseDisplay();
					StartTask(ShowEyes);
					break;
				case 31: //Calibrate Case
					StopTask(ShowEyes);
					eraseDisplay();
					calibrate();
					eraseDisplay();
					StartTask(ShowEyes);
					break;
			}
			ClearMessage();
		}
	}
}

//Function: calibrate
void calibrate(){
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

	//Finish Light Monitor Task
	StopTask(MonitorRLight);
	StopTask(MonitorLLight);
}

/*
//Function: captureBall
void captureBall(int iColor){
	int capture = 0;
	switch(iColor){
		case 1:
			iColor = BLACKCOLOR;
			break;
		case 2:
			iColor = BLUECOLOR;
			break;
		case 3:
			iColor = GREENCOLOR;
			break;
		case 4:
			iColor = YELLOWCOLOR;
			break;
		case 5:
			iColor = REDCOLOR;
			break;
		case 6:
			iColor = WHITECOLOR;
			break;
	}

	StopTask(ShowEyes);
	eraseDisplay();
	StartTask(MonitorColor);

	nxtDisplayTextLine(2, "Buscando Pelota");
	nxtDisplayTextLine(3, "Verde");
	wait1Msec(2000);
	while(true){
		switch(ColorVal){
			case iColor:
				capture = 1;
				motor[clampsMotor] = 30;
				wait1Msec(500);
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
}
*/
