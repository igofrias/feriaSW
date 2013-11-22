#pragma config(Motor, motorB, clampsMotor, tmotorNXT, PIDControl)
#pragma config(sensor, S3, colorSensor, sensorCOLORFULL);

task MonitorColor();

ubyte OutGoingMessage[1] = {0};
int ColorVal;
int i=0;

task main(){
	StartTask(MonitorColor);

	motor[clampsMotor] = 50;
	wait1Msec(1300);
	motor[clampsMotor] = 0;

	while(true){
		OutGoingMessage[0] = ColorVal;
		if(ColorVal > 1 && ColorVal < 7){
			nxtDisplayTextLine(2,"%d",ColorVal);
			cCmdMessageWriteToBluetooth(0,OutGoingMessage,1,mailbox1);
			i++;
		}
		if(i == 20){
			eraseDisplay();
			break;
		}
	}
	StopAllTasks();
}

//Type Color monitoring
task MonitorColor(){
	while(true){
		ColorVal = SensorValue[colorSensor];
	}
}
