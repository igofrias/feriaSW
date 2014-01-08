#pragma config(Motor, 	motorA, rightMotor,  	tmotorNXT, PIDControl)
#pragma config(Motor,		motorB, clampsMotor, 	tmotorNXT, PIDControl)
#pragma config(Motor, 	motorC, leftMotor,   	tmotorNXT, PIDControl)
#pragma config(Sensor, 	S1, 		lightRSensor, sensorLightInactive)
#pragma config(Sensor, 	S2, 		lightLSensor, sensorLightInactive)
#pragma config(Sensor, 	S3, 		colorSensor,  sensorCOLORFULL)
#pragma config(Sensor, 	S4, 		touchSensor, 	sensorTouch)

/*****************************************
 *            Task Statement
 ****************************************/
task ShowNormalEyes();
task ShowAngryEyes();
task ShowBoredEyes();
task ShowCloseEyes();
task ShowHappyEyes();
task ShowShameEyes();
task ShowSadEyes();
task ShowCryEyes();
task ShowDeadEyes();
task MonitorRLight();
task MonitorLLight();
task MonitorColor();
task MonitorTouch();
task Caress();
task Photophobia();

/*****************************************
 *            Function Statement
 ****************************************/
void readMessages();
void calibrate();
void openClamps();
void closeClamps();
void catchBall();
void releaseBall();
void eyesTime();
void stopEyes(int e);
void startEyes(int e);
void playTheme(int t);
void shake();
void monitorInit();
void monitorFinish();

/*****************************************
 *       Global Variable Statement
 ****************************************/
int openE;
int closeE;
int ColorVal;
int LightRVal;
int LightLVal;
int RightLimit;
int LeftLimit;
int TouchVal;
int oClamps = 0; //0 -> Close ; 1 -> Open ; 2 -> Catch
int eyesTask = 0;
int actualEyes;
int showHappiness = 0;
float averRLight = 0/*, devRLight = 0*/;	// create two float variables 'averLight' & 'devLight'
float averLLight = 0/*, devLLight = 0*/; 	// create two float variables 'averLLight' & 'devLLight'
long nAction;
long nMensaje = 0;
ubyte OutGoingMessage[1] = {0};


/*****************************************
 *            Main Task
 ****************************************/
task main(){
	srand(nSysTime);
	StartTask(ShowNormalEyes);
	eyesTask = 1;
	calibrate();
	monitorInit();
	readMessages();
	return;
}

/*****************************************
 *            Task Definition
 ****************************************/
//Show Normal Eyes
task ShowNormalEyes(){
	while(true){
		eyesTime();
		nxtDisplayRICFile(0, 0, "OpenEyes.ric");
		wait1Msec(openE);
		nxtDisplayRICFile(0, 0, "CloseEyes.ric");
		wait1Msec(closeE);
	}
}

//Show Angry Eyes
task ShowAngryEyes(){
	while(true){
		eyesTime();
		nxtDisplayRICFile(0, 0, "AngryEyes.ric");
		wait1Msec(openE);
		nxtDisplayRICFile(0, 0, "CloseEyes.ric");
		wait1Msec(closeE);
	}
}

//Show Bored Eyes
task ShowBoredEyes(){
	while(true){
		eyesTime();
		nxtDisplayRICFile(0, 0, "BoredEyes.ric");
		wait1Msec(openE);
	}
}

//Show Close Eyes
task ShowCloseEyes(){
	while(true){
		eyesTime();
		nxtDisplayRICFile(0, 0, "CloseEyes.ric");
		wait1Msec(openE);
	}
}

//Show Happy Eyes
task ShowHappyEyes(){
	while(true){
		eyesTime();
		nxtDisplayRICFile(0, 0, "HappyEyes.ric");
		wait1Msec(openE);
	}
}

//Show Shame Eyes
task ShowShameEyes(){
	while(true){
		eyesTime();
		nxtDisplayRICFile(0, 0, "ShameEyes.ric");
		wait1Msec(openE);
	}
}

//Show Sad Eyes
task ShowSadEyes(){
	while(true){
		eyesTime();
		nxtDisplayRICFile(0, 0, "SadEyes.ric");
		wait1Msec(openE);
		nxtDisplayRICFile(0, 0, "CloseEyes.ric");
		wait1Msec(closeE);
	}
}

//Show Cry Eyes
task ShowCryEyes(){
	while(true){
		eyesTime();
		nxtDisplayRICFile(0, 0, "CryEyes.ric");
		wait1Msec(openE);
		nxtDisplayRICFile(0, 0, "CloseEyes.ric");
		wait1Msec(closeE);
	}
}

task ShowDeadEyes(){
	while(true){
		eyesTime();
		nxtDisplayRICFile(0, 0, "DeadEyes.ric");
		wait1Msec(openE);
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

//Touch monitoring
task MonitorTouch(){
	while(true){
		TouchVal = SensorValue[touchSensor];
	}
}

//Caress Robot
task Caress(){
	while(true){
		if(TouchVal == 1 && showHappiness == 0){
			actualEyes = eyesTask;
			stopEyes(actualEyes);
			eyesTask = 5;
			startEyes(eyesTask);
			showHappiness = 1;
		}
		else if(TouchVal == 0 && showHappiness == 1){
			stopEyes(eyesTask);
			eyesTask = actualEyes;
			startEyes(eyesTask);
			actualEyes = 0;
			showHappiness = 0;
		}
	}
}

//Photophobic Robot
task Photophobia(){
	while(true){
		RightLimit = averRLight +10;
		LeftLimit = averLLight+10;
		if(LightRVal > RightLimit && LightLVal < LeftLimit){
			motor[leftMotor] = 50;
			motor[rightMotor] = -50;
			wait1Msec(500);
			motor[leftMotor] = 0;
			motor[rightMotor] = 0;
			calibrate();
		}
		if(LightLVal > LeftLimit && LightRVal < RightLimit){
			motor[leftMotor] = -50;
			motor[rightMotor] = 50;
			wait1Msec(500);
			motor[leftMotor] = 0;
			motor[rightMotor] = 0;
			calibrate();
		}
		if(LightLVal > LeftLimit && LightRVal > RightLimit){
			motor[leftMotor] = 50;
			motor[rightMotor] = 50;
			wait1Msec(500);
			motor[leftMotor] = 0;
			motor[rightMotor] = 0;
			calibrate();
		}
	}
	return;
}

/*****************************************
 *          Function Definition
 ****************************************/
//Function: readMessages
void readMessages(){
	while (true){
		nMensaje = message;
		if (nMensaje != 0){
			nAction = messageParm[0];
			switch(nAction){
				case 1 : //Normal Eyes
				case 2 : //Angry Eyes
				case 3 : //Bored Eyes
				case 4 : //Close Eyes
				case 5 : //Happy Eyes
				case 6 : //Shame Eyes
				case 7 : //Sad Eyes
				case 8 : //Cry Eyes
				case 30: //Dead Eyes
					stopEyes(eyesTask);
					eyesTask = nAction;
					startEyes(eyesTask);
					break;
				case 31: //Stop Sensors
					monitorFinish();
					break;
				case 32: //Start Sensors
					monitorInit();
					break;
				case 41: //Open Clamps
					openClamps();
					break;
				case 42: //Close Clamps
					closeClamps();
					break;
				case 43: //Catch Ball
					catchBall();
					break;
				case 44: //Release Ball
					releaseBall();
					break;
				case 47:
					shake();
					break;
				case 61:
				case 62:
				case 63:
					playTheme(nAction);
					break;
				case 200: //Shutdown
					StopAllTasks();
					powerOff();
					return;
					break;
			}
			ClearMessage();
		}
	}
}

//Function: calibrate
void calibrate(){
	//Variables definitions
	int LightRData[10];					  				// create an array 'LightData' for data of light sensor
	int LightLData[10];						  			// create an array 'LightLData' for data of light sensor

	//Initial Value
	averLLight = 0;
	averRLight = 0;

	//Light medition
	for(int i = 0;i<10; i++){
		LightRData[i] = LightRVal;
		LightLData[i] = LightLVal;

		//Obtain Average
		averRLight += (LightRData[i]/10.0);
		averLLight += (LightLData[i]/10.0);
	}
}

//Function: openClamps
void openClamps(){
		if(oClamps == 0){ //Si estan en 'Close', las deja en 'Open'
			motor[clampsMotor] = -50;
			wait1Msec(2000);
			motor[clampsMotor] = 0;
			oClamps = 1;
		}
		if(oClamps == 2){ //Si estan en 'Catch', ejecuta 'releaseBall' para dejarlas en 'Open'
			releaseBall();
			oClamps = 1;
		}
		return;
}

//Function: closeClamps
void closeClamps(){
	if(oClamps == 1){ //Si estan en 'Open', las deja en 'Close'
		motor[clampsMotor] = 50;
		wait1Msec(2000);
		motor[clampsMotor] = 0;
		oClamps = 0;
	}
	if(oClamps == 2){ //Si estan en 'Catch', ejecuta 'openClamps' (para dejar en 'Open') y luego se ejecuta a si mismo
		openClamps();
		closeClamps();
	}
	return;
}

//Function: catchBall
void catchBall(){
	StartTask(MonitorColor);
	if(oClamps == 1){ //Si estan en 'Open', captura, comprueba color y envia mensaje. Deja en 'Catch'
		motor[clampsMotor] = 50;
		wait1Msec(1300);
		motor[clampsMotor] = 0;

		OutGoingMessage[0] = ColorVal;
		cCmdMessageWriteToBluetooth(0,OutGoingMessage,1,mailbox1);
		//nxtDisplayTextLine(2,"%d",ColorVal);
		//wait1Msec(2000);
		oClamps = 2;
	}
	StopTask(MonitorColor);
	return;
}

//Function: releaseBall
void releaseBall(){
	if(oClamps == 2){	//Si esta en 'Catch', deja en 'Open'
		motor[clampsMotor] = -50;
		wait1Msec(1300);
		motor[clampsMotor] = 0;
		oClamps = 1;
	}
	return;
}

//Function: eyesTime
void eyesTime(){
	openE = 0;
	closeE = 0;
	while(openE < 2000)
		openE = rand()%8000 + 2000;
	while(closeE < 300)
		closeE = rand()%100 + 300;
	return;
}

//Function stopEyes
void stopEyes(int e){
	eyesTask = e;
	switch(e){
		case 1:
			StopTask(ShowNormalEyes);
			break;
		case 2:
			StopTask(ShowAngryEyes);
			break;
		case 3:
			StopTask(ShowBoredEyes);
			break;
		case 4:
			StopTask(ShowCloseEyes);
			break;
		case 5:
			StopTask(ShowHappyEyes);
			break;
		case 6:
			StopTask(ShowShameEyes);
			break;
		case 7:
			StopTask(ShowSadEyes);
			break;
		case 8:
			StopTask(ShowCryEyes);
			break;
		case 30:
			StopTask(ShowNormalEyes);
			break;
		default:
			break;
	}
	eraseDisplay();
	return;
}

//Function startEyes
void startEyes(int e){
//	eyesTask = e;
	switch(e){
		case 1:
			StartTask(ShowNormalEyes);
			break;
		case 2:
			StartTask(ShowAngryEyes);
			break;
		case 3:
			StartTask(ShowBoredEyes);
			break;
		case 4:
			StartTask(ShowCloseEyes);
			break;
		case 5:
			StartTask(ShowHappyEyes);
			break;
		case 6:
			StartTask(ShowShameEyes);
			break;
		case 7:
			StartTask(ShowSadEyes);
			break;
		case 8:
			StartTask(ShowCryEyes);
			break;
		case 30:
			StartTask(ShowDeadEyes);
			break;
		default:
			StartTask(ShowNormalEyes);
			break;
	}
	return;
}

//Function: playTheme
void playTheme(int t){
	switch(t){
		case 61:
			PlaySoundFile("YawnSound.rso");
			break;
		case 62:
			PlaySoundFile("SleepSound.rso");
			break;
		case 63:
			PlaySoundFile("EatSound.rso");
			break;
	}
	return;
}

//Function: shake
void shake(){
	for(int i = 0;i<2;i++){
			motor[leftMotor] = 100;
			motor[rightMotor] = -100;
			wait1Msec(75);
			motor[leftMotor] = -100;
			motor[rightMotor] = 100;
			wait1Msec(75);
	}
	motor[leftMotor] = 0;
	motor[rightMotor] = 0;
}

//Function: monitorInit
void monitorInit(){
	StartTask(MonitorTouch);
	StartTask(MonitorLLight);
	StartTask(MonitorRLight);
	StartTask(Photophobia);
	StartTask(Caress);
}

//Function: monitorFinish
void monitorFinish(){
	StopTask(MonitorTouch);
	StopTask(MonitorLLight);
	StopTask(MonitorRLight);
	StopTask(Photophobia);
	StopTask(Caress);
}
