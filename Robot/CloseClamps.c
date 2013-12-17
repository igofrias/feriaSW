#pragma config(Motor, motorB, clampsMotor, tmotorNXT, PIDControl)

task main(){
	motor[clampsMotor] = 50;
	wait1Msec(2000);

}
