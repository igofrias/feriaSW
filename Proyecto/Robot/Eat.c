#pragma config(Motor, motorB,  headMotor, tmotorNXT, PIDControl)

task EatSound();
task HappyEyes();
task MoveHead();

task main(){
	StartTask(HappyEyes);
	StartTask(EatSound);
	StartTask(MoveHead);
	wait1Msec(5000);

	StopAllTasks();
	return;
}

task EatSound(){
	while(true){
		PlaySoundFile("nom1.rso");
		wait1Msec(1500);
	}
}

task HappyEyes(){
	while(true){
		nxtDisplayRICFile(0, 0, "HappyEyes.ric");
	}
}

task MoveHead(){
	motor[headMotor]=25;
	wait10Msec(40);
	motor[headMotor]=-25;
	wait10Msec(40);
}
