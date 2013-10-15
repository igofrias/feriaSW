task SendMessages();
void checkConnection();

task main(){
	checkConnection();
	eraseDisplay();
	bNxtLCDStatusDisplay = true;
	wait1Msec(500);
	StartTask(SendMessages);
	powerOff();
}

/*
 * Task SendMessages
 */
task SendMessages(){
//	while (true){
	int nButton = 3; //Shutdown Slave Brick
//		if(nButton > -1)
			sendMessageWithParm(nButton);
//	}
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
