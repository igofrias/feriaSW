task Vader();
task AwakeEyes();

task main(){
	StartTask(AwakeEyes);
	StartTask(Vader);
	wait1Msec(5000);
	StopAllTasks();
	return;
}

task Vader(){
	while(true){
		PlaySoundFile("Vader.rso");
		wait1Msec(2500);
	}
}

task AwakeEyes(){
	while(true){
		nxtDisplayRICFile(0, 0, "OpenEyes.ric");
		wait1Msec(1500);
		nxtDisplayRICFile(0, 0, "CloseEyes.ric");
		wait1Msec(200);
	}
}
