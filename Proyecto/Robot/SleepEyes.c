task CloseEyes();
task Yawn();

task main()
{
	StartTask(CloseEyes);
	StartTask(Yawn);
	wait1Msec(5000);
	StopAllTasks();
	return;
}

task CloseEyes(){
	while(true){
		nxtDisplayRICFile(0, 0, "CloseEyes.ric");
		wait1Msec(200);
	}
}

task Yawn(){
	PlaySoundFile("yawning.rso");
	while(true){
		PlaySoundFile("Vader.rso");
		wait1Msec(1500);
	}
}
