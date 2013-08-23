#pragma config(Motor, motorA, rightMotor, tmotorNXT, PIDControl)
#pragma config(Motor, motorB, leftMotor, tmotorNXT, PIDControl)

task main(){
	int waitTime = 75;
	nSyncedMotors = synchAB;	//Left motor slaved to Right motor
	nSyncedTurnRatio = -100;	//Left motor turns -100% of right motor
	bool asdf = true;

	while(true){
		if(asdf == true){
			motor[rightMotor] = -100;
			asdf = false;
			wait1Msec(waitTime);
		}
		else{
			motor[rightMotor] = 100;
			asdf = true;
			wait1Msec(waitTime);
		}
	}
}
