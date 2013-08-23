#pragma config(Motor, motorA, rightMotor, tmotorNXT, PIDControl)
#pragma config(Motor, motorB, leftMotor, tmotorNXT, PIDControl)

task main()
{

	//Synch motors
	nSyncedMotors = synchAB;	//Left motor slaved to Right motor
	nSyncedTurnRatio = 100;	//Left motor turns -100% of right motor

	motor[rightMotor] = 50;
	wait1Msec(3000);

}
