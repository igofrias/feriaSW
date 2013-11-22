#pragma config(Motor, motorB, clampsMotor, tmotorNXT, PIDControl)
#pragma config(Sensor, S3, colorSensor,  sensorCOLORFULL)

ubyte OutcommingMessage[1] = {20};
int timer = 0;
int count = 0;

task main(){
	while(true){
		if(timer==0){
			cCmdMessageWriteToBluetooth(0,OutcommingMessage,1,mailbox1);
			timer=5;
			count++;
		}
		else{
			timer--;
			if(count == 5){
				break;
			}
		}
		wait1Msec(2);
	}
}
