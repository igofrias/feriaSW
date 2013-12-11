#pragma config(Motor, motorB,  headMotor, tmotorNXT, PIDControl)

/*
 * Move Head for indicate success connection
 */
task main(){
	PlaySoundFile("Woops.rso");
	motor[headMotor]=25;
	wait10Msec(40);
	motor[headMotor]=-25;
	wait10Msec(40);
}
