void SendMessages(int nData);
void checkConnection();

task main(){
	checkConnection();
	eraseDisplay();
	bNxtLCDStatusDisplay = true;
	wait1Msec(500);
	SendMessages(3);
	powerOff();
}

/*
 * Task SendMessages
 */
void SendMessages(int nData){
	sendMessageWithParm(nData);
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
