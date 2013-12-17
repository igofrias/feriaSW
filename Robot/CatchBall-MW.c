#pragma config(Motor, motorB, clampsMotor, tmotorNXT, PIDControl)
#pragma config(sensor, S3, colorSensor, sensorCOLORFULL);

task MonitorColor();
task readMessages();

ubyte OutGoingMessage[1] = {0};
int ColorVal;
int nAction;
int nMensaje = 0;
int StopFunc = 0;

task main(){
	StartTask(MonitorColor);
	StartTask(readMessages);

	motor[clampsMotor] = 50;
	wait1Msec(1300);
	motor[clampsMotor] = 0;

	while(true){
		OutGoingMessage[0] = ColorVal;
		nxtDisplayTextLine(2,"%d",ColorVal);
		cCmdMessageWriteToBluetooth(0,OutGoingMessage,1,mailbox1);
		wait1Msec(100);
		if(StopFunc)
			break;
	}
	StopAllTasks();
}

//Type Color monitoring
task MonitorColor(){
	while(true){
		ColorVal = SensorValue[colorSensor];
	}
}

task readMessages(){
	while (true){
		nMensaje = message;
		if (nMensaje != 0){
			nAction = messageParm[0];
			if(nAction == 201){
				StopFunc = 1;
			}
			ClearMessage();
		}
	}
}
