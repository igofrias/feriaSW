#pragma config(Motor, motorA, rightMotor, tmotorNXT, PIDControl)
#pragma config(Motor, motorB,  headMotor, tmotorNXT, PIDControl)
#pragma config(Motor, motorC,  leftMotor, tmotorNXT, PIDControl)

task main(){
	int waitTime = 75;
	nSyncedMotors = synchBC;	//Left motor slaved to Right motor
	nSyncedTurnRatio = -100;	//Left motor turns -100% of right
	int totalShake = 10;

	for(int i = 0;i<totalShake;i++){
			motor[rightMotor] = -100;
			motor[headMotor] = -75;
			wait1Msec(waitTime);
			motor[rightMotor] = 100;
			motor[headMotor] = 75;
			wait1Msec(waitTime);
	}
}
