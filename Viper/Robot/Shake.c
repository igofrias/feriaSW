#pragma config(Motor, motorA, rightMotor, tmotorNXT, PIDControl)
#pragma config(Motor, motorB, leftMotor, tmotorNXT, PIDControl)

task main(){
	int waitTime = 75;
	nSyncedMotors = synchAB;	//Left motor slaved to Right motor
	nSyncedTurnRatio = -100;	//Left motor turns -100% of right
	int totalShake = 10;

	for(int i = 0;i<totalShake;i++){
			motor[rightMotor] = -100;
			wait1Msec(waitTime);
			motor[rightMotor] = 100;
			wait1Msec(waitTime);
	}
}
