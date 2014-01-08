#pragma config(Motor, motorA, rightMotor, tmotorNXT, PIDControl)
#pragma config(Motor, motorB,  headMotor, tmotorNXT, PIDControl)
#pragma config(Motor, motorC,  leftMotor, tmotorNXT, PIDControl)

task main(){
	int waitTime = 75;
	nSyncedMotors = synchBC;	//Left motor slaved to Right motor
	nSyncedTurnRatio = -100;	//Left motor turns -100% of right

	for(int i = 0;i<10;i++){
			motor[leftMotor] = 100;
			motor[rightMotor] = -100;
			//motor[headMotor] = -75;
			wait1Msec(75);
			motor[leftMotor] = -100;
			motor[rightMotor] = 100;
			//motor[headMotor] = 75;
			wait1Msec(75);
	}
}
