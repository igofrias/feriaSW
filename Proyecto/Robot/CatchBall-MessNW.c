#pragma config(Motor, motorB, clampsMotor, tmotorNXT, PIDControl)
#pragma config(sensor, S3, colorSensor, sensorCOLORFULL);

task MonitorColor();

ubyte OutGoingMessage[1] = {0};
int ColorVal;

task main(){
	StartTask(MonitorColor);

	motor[clampsMotor] = 50;
	wait1Msec(1300);
	motor[clampsMotor] = 0;

	OutGoingMessage[0] = ColorVal;
	nxtDisplayTextLine(2,"%d",ColorVal);
	cCmdMessageWriteToBluetooth(0,OutGoingMessage,1,mailbox1);
	wait1Msec(2000);

	StopAllTasks();
}

//Type Color monitoring
task MonitorColor(){
	while(true){
		ColorVal = SensorValue[colorSensor];
	}
}
