#pragma config(Motor, motorB,  headMotor, tmotorNXT, PIDControl)
#define VELOCITY 25

/*
 * Move Head for indicate success connection
 */
task main(){
	PlaySoundFile("Woops.rso");
	motor[headMotor]=VELOCITY;
	wait10Msec(40);
	motor[headMotor]=-VELOCITY;
	wait10Msec(40);
}
