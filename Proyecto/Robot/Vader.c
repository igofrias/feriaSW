task Vader();
task OpenCloseEyes();

task main(){
	StartTask(OpenCloseEyes);
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

task OpenCloseEyes(){
	while(true){
		nxtDisplayRICFile(0, 0, "OpenEyes.ric");
		wait1Msec(1500);
		nxtDisplayRICFile(0, 0, "CloseEyes.ric");
		wait1Msec(200);
	}
}
