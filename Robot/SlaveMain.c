#pragma config(Motor, motorA, headMotor,  tmotorNXT, PIDControl)
#pragma config(Motor, motorB, tailMotor, tmotorNXT, PIDControl)

/*****************************************
 *            Task Statement
 ****************************************/
//task moveTail();

/*****************************************
 *            Function Statement
 ****************************************/
void readMessages();
void moveHead();
void moveTail();
//void checkConnection();

/*****************************************
 *       Global Variable Statement
 ****************************************/
int nAction;
int nMessage = 0;
ubyte OutGoingMessage[1] = {0};

/*****************************************
 *            Main Task
 ****************************************/
task main(){
//	checkConnection();
	eraseDisplay();
	motor[tailMotor] = 50;
	wait1Msec(300);
//	bNxtLCDStatusDisplay = true;
//	wait1Msec(500);
	readMessages();
	return;
}

/*****************************************
 *            Task Definition
 ****************************************/
/*//Function: moveTail
task moveTail(){
	while(true){
		nMotorEncoder[tailMotor] = 0;
		nMotorEncoderTarget[tailMotor] = 40;
		motor[tailMotor] = 60;
		wait1Msec(150);
		motor[tailMotor] = -60;
		wait1Msec(150);
	}
}

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
*/

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
				case 45: //Move Head
					moveHead();
					break;
				case 46: //Move Tail
					moveTail();
					break;
				case 90: //Victory Fanfare
					PlaySoundFile("VictoryFanfare.rso");
					break;
				case 200: //Shutdown
					StopAllTasks();
					powerOff();
					return;
					break;
			}
			ClearMessage();
			nMessage = 0;
		}
	}
}

//Function: moveHead
void moveHead(){
	for(int i=0;i<3;i++){
		motor[headMotor] = 50;
		wait1Msec(100);
		motor[headMotor] = -50;
		wait1Msec(100);
	}
	motor[headMotor] = 0;
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
