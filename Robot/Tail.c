#pragma config(Motor, motorA, tailMotor, tmotorNXT, PIDControl)

task main()
{
	int i=0;
	while(i<5){
		motor[tailMotor] = -50;
		wait1Msec(80);
		motor[tailMotor] = 50;
		wait1Msec(120);
		i++;
	}
}
