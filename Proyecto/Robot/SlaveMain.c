#pragma config(Motor, motorB,  tailMotor, tmotorNXT, PIDControl)

int nMessage = 0;
int nAction;

void ReadMessages();
void checkConnection();
task MoveTail();

task main(){
	checkConnection();
	eraseDisplay();
	bNxtLCDStatusDisplay = true;
	wait1Msec(500);
	ReadMessages();
	return;
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

/*
 * void ReadMessages
 *
 */
void ReadMessages(){
	while(true){
		nMessage = message;
		if (nMessage != 0){
			nAction = messageParm[0];
			switch(nAction){
				case 1:	//Move Tail
					StartTask(MoveTail);
					break;
				case 2: //Stop Tail
					StopTask(MoveTail);
					motor[tailMotor] = 0;
					break;
				case 90: //Victory Fanfare
					PlaySoundFile("VictoryFanfare.rso");
					break;
				case 200: //Shutdown
					powerOff();
					break;
			}
			ClearMessage();
			nMessage = 0;
		}
	}
}

task MoveTail(){
	while(true){
		nMotorEncoder[tailMotor] = 0;
		nMotorEncoderTarget[tailMotor] = 40;
		motor[tailMotor] = 60;
		wait1Msec(150);
		motor[tailMotor] = -60;
		wait1Msec(150);
	}
}
