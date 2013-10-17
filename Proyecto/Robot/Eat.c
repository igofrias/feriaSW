task EatSound();

task main(){
	StartTask(EatSound);
	wait1Msec(5000);
	StopTask(EatSound);
	return;
}

task EatSound(){
	while(true){
		PlaySoundFile("nom1.rso");
		wait1Msec(1500);
	}
}
