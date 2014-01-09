#pragma config(Motor, motorA, headMotor,  tmotorNXT, PIDControl)
#pragma config(Motor, motorB, tailMotor, tmotorNXT, PIDControl)
#pragma config(Sensor, S3, touchSensor, sensorTouch)

/*****************************************
 *            Task Statement
 ****************************************/
task MonitorTouch();
task Caress();
task moveHead();
task taskMoveTail();

/*****************************************
 *            Function Statement
 ****************************************/
void readMessages();
void moveTail();
void playTheme(int t);
void monitorInit();
void monitorFinish();

/*****************************************
 *       Global Variable Statement
 ****************************************/
int TouchVal;
int nAction;
int nMessage = 0;
ubyte OutGoingMessage[1] = {0};

/*****************************************
 *            Main Task
 ****************************************/
task main(){
	eraseDisplay();
	motor[tailMotor] = 50;
	wait1Msec(300);
	monitorInit();
	readMessages();
	return;
}

/*****************************************
 *            Task Definition
 ****************************************/


//Touch monitoring
task MonitorTouch(){
	while(true){
		TouchVal = SensorValue[touchSensor];
	}
}

//Caress Robot
task Caress(){
	int showHappiness = 0;
	while(true){
		if(TouchVal == 1 && showHappiness == 0){
			StartTask(taskMoveTail);
			showHappiness = 1;
		}
		else if(TouchVal == 0 && showHappiness == 1){
			StopTask(taskMoveTail);
			motor[tailMotor] = 50;
			wait1Msec(120);
			motor[tailMotor] = 0;
			showHappiness = 0;
		}
	}
}

//Move Head
task moveHead(){
	while(true){
		motor[headMotor] = 40;
		wait1Msec(200);
		motor[headMotor] = -40;
		wait1Msec(200);
	}
}

//Move Tail
task taskMoveTail(){
	while(true){
		motor[tailMotor] = -50;
		wait1Msec(80);
		motor[tailMotor] = 50;
		wait1Msec(120);
	}
}

/*****************************************
 *          Function Definition
 ****************************************/
//Function: readMessages
void readMessages(){
	while(true){
		nMessage = message;
		if (nMessage != 0){
			nAction = messageParm[0];
			switch(nAction){
				case 31: //Stop Sensors
					monitorFinish();
					break;
				case 32: //Start Sensors
					monitorInit();
					break;
				case 45: //Start Move Head
					StartTask(moveHead);
					break;
				case 46: //Move Tail
					moveTail();
					break;
				case 48: //Stop Move Head
					StopTask(moveHead);
					motor[headMotor] = -40;
					wait1Msec(200);
					motor[headMotor] = 0;
					break;
				case 64: //Fart Sound
				case 90: //Fanfare Sound
					playTheme(nAction);
					break;
				case 100: //Shutdown
					powerOff();
					return;
					break;
				case 101: //Send Level Battery
					break;
			}
			ClearMessage();
			nMessage = 0;
		}
	}
}

//Function: moveTail
void moveTail(){
	for(int i=0;i<5;i++){
		motor[tailMotor] = -50;
		wait1Msec(80);
		motor[tailMotor] = 50;
		wait1Msec(120);
	}
	motor[tailMotor] = 0;
}

//Function: playTheme
void playTheme(int t){
	switch(t){
		case 64:
			PlaySoundFile("FartSound.rso");
			break;
		case 90:
			PlaySoundFile("FanfareSound.rso");
			break;
	}
	return;
}

//Function: monitorInit
void monitorInit(){
	StartTask(MonitorTouch);
	StartTask(Caress);
}

//Function: monitor Finish
void monitorFinish(){
	StopTask(MonitorTouch);
	StopTask(Caress);
}
