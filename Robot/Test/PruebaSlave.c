#pragma config(Motor, motorB,  tailMotor, tmotorNXT, PIDControl)

void MoveTail1();
void MoveTail2();
task tail1();
task tail2();

task main(){
	for(int i = 0; i < 2; i++){
		StartTask(tail1);
		wait1Msec(2000);
		StopTask(tail1);
		wait1Msec(2000);
		StartTask(tail2);
		wait1Msec(2000);
		StopTask(tail2);
		wait1Msec(2000);
	}
	return;
}

void MoveTail1(){
	int n;
	while(n < 4){
		nMotorEncoder[tailMotor] = 0;
		nMotorEncoderTarget[tailMotor] = 50;
		motor[tailMotor] = 20;
		while(nMotorRunState[tailMotor] != runStateIdle){
		}
		nMotorEncoder[tailMotor] = 0;
		nMotorEncoderTarget[tailMotor] = -50;
		motor[tailMotor] = -20;
		while(nMotorRunState[tailMotor] != runStateIdle){
		}
		n++;
	}
	nMotorEncoder[tailMotor] = 0;
	nMotorEncoderTarget[tailMotor] = 50;
	motor[tailMotor] = 20;
	while(nMotorRunState[tailMotor] != runStateIdle){
	}
	return;
}

void MoveTail2(){
	int n;
	while(n < 4){
		nMotorEncoder[tailMotor] = 0;
		nMotorEncoderTarget[tailMotor] = -50;
		motor[tailMotor] = -20;
		while(nMotorRunState[tailMotor] != runStateIdle){
		}
		nMotorEncoder[tailMotor] = 0;
		nMotorEncoderTarget[tailMotor] = 50;
		motor[tailMotor] = 20;
		while(nMotorRunState[tailMotor] != runStateIdle){
		}
		n++;
	}
	nMotorEncoder[tailMotor] = 0;
	nMotorEncoderTarget[tailMotor] = -50;
	motor[tailMotor] = -20;
	while(nMotorRunState[tailMotor] != runStateIdle){
	}
	return;
}

task tail1(){
	MoveTail1();
}

task tail2(){
	MoveTail2();
}
