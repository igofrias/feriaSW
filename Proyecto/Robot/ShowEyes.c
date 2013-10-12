#define X_BigCircleSup	5
#define Y_BigCircleSup	51
#define X_BigCircleInf	44
#define Y_BigCircleInf	12
#define X_SmallCircleSup	25
#define Y_SmallCircleSup	46
#define X_SmallCircleInf	39
#define Y_SmallCircleInf	32
#define X_EllipseSup		9
#define	Y_EllipseSup		26
#define X_EllipseInf		19
#define	Y_EllipseInf		19
#define	DistanceLR		50

task ShowEyes(){
	int X_BCS,X_BCI;
	int X_SCS,X_SCI;
	int X_ES,X_EI;

	//Position for Right Eyes
	//Right Big Circle
	X_BCS=X_BigCircleSup+DistanceLR;
	X_BCI=X_BigCircleInf+DistanceLR;

	//Right Small Circle
	X_SCS=X_SmallCircleSup+DistanceLR;
	X_SCI=X_SmallCircleInf+DistanceLR;

	//Right Ellipse
	X_ES=X_EllipseSup+DistanceLR;
	X_EI=X_EllipseInf+DistanceLR;

	//Display Right Eye
	nxtFillEllipse(X_BCS,Y_BigCircleSup,X_BCI,Y_BigCircleInf);
	nxtEraseEllipse(X_SCS,Y_SmallCircleSup,X_SCI,Y_SmallCircleInf);
	nxtEraseEllipse(X_ES,Y_EllipseSup,X_EI,Y_EllipseInf);
	//Display Left Eye
	nxtFillEllipse(X_BigCircleSup,Y_BigCircleSup,X_BigCircleInf,Y_BigCircleInf);
	nxtEraseEllipse(X_SmallCircleSup,Y_SmallCircleSup,X_SmallCircleInf,Y_SmallCircleInf);
	nxtEraseEllipse(X_EllipseSup,Y_EllipseSup,X_EllipseInf,Y_EllipseInf);
}

task main(){
	StartTask(ShowEyes);
	wait10Msec(500);
	StopTask(ShowEyes);
}
