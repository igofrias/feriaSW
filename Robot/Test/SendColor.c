#pragma config(sensor, S3, colorSensor, sensorCOLORFULL);

task MonitorColor();

ubyte OutGoingMessage[1] = {0};
int ColorVal;

task main(){
	StartTask(MonitorColor);
	while(true){
		OutGoingMessage[0] = ColorVal;
		if(ColorVal > 1 && ColorVal < 7)
			nxtDisplayTextLine(2,"%d",ColorVal);
			cCmdMessageWriteToBluetooth(0,OutGoingMessage,1,mailbox1);
	}
}

//Type Color monitoring
task MonitorColor(){
	while(true){
		ColorVal = SensorValue[colorSensor];
	}
}
