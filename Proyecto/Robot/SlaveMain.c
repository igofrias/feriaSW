#pragma config(Motor, motorB,  tailMotor, tmotorNXT, PIDControl)

int nMessage;
int nAction;

void ReadMessages();
void checkConnection();

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
 * Task ReadMessages
 *
 */
void ReadMessages(){
	while(true){
		nMessage = message;
		if (nMessage != 0){
			nAction = messageParm[0];
			switch(nAction){
				case 1:	//Move Tail
					for (int i=0;i<3;i++){
						nMotorEncoder[tailMotor] = 0;
						nMotorEncoderTarget[tailMotor] = 50;
						motor[tailMotor] = 75;
						while(nMotorRunState[tailMotor] != runStateIdle){
						}
						nMotorEncoder[tailMotor] = 0;
						nMotorEncoderTarget[tailMotor] = -50;
						motor[tailMotor] = -75;
						while(nMotorRunState[tailMotor] != runStateIdle){
						}
					}
					break;
				case 2: //Stop Tail
					break;
				case 3: //Shutdown
					powerOff();
					break;
			}
		}
	}
}
