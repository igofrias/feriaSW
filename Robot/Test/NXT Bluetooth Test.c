////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//                                    Bluetooth Operational Tests
//
// ROBOTC provides access and control over the NXT Bluetooth link. This program contains many samples
// to illustrate this functionality.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//                                  waitForBTCommandToComplete
//
// Most BT commands take several "time slices" to complete. The NXT CPU sends a command request to the
// Bluetooth chip. The chip processes the command and then sends a reply message back to the NXT CPU
// containing the results.
//
// The NXT firmware internally does a 'time out' test to check if the command has completed so a user
// application program can rely on this and not do its own testing. There are three different timeout
// values used by the firmware
//    0.5 second for commands that only operate on the internal variables in the bluetooth chip
//    2.0 seconds for commands that manipulate the bluetooth radio
//   30.0 seconds for commands like 'search', 'connect', etc that are setting up or tearing down links
//
////////////////////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//                                     bWaitForBluetoothIdle
//
// Wait up to three seconds for Bluetooth module to become idle. Return failure if module is busy after
// the end of the waiting period.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////

bool bWaitForBluetoothIdle(int kWaitDuration = 3000)
{
  int nWait;

  ClearMessage();
  for (nWait = 0; nWait < kWaitDuration; nWait += 10)
  {
    if (!bBTBusy)
      return true;
    wait1Msec(10);
  }
  return false;  // Still Busy
}


////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//                                     btFactoryReset
//
// This function restores the NXT's Bluetooth Module to the "factory" default settings.
//   - All existing connections are dropped.
//   - The table of paired connections is emptied.
//   - Configuration parameters are restored to the default values.
//
// NOTE:
//   - the firmeware in the Bluetooth module is not erased.
//   - If you have renamed the NXT, then the new name is preserved.
//   - the "factory reset" command can take 3 to 5 seconds.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////

bool FactoryReset()
{
  if (!bWaitForBluetoothIdle())
    return false;
  btFactoryReset();
  bWaitForBluetoothIdle();
  return true;
}


////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//                        BluetoothOff() / BluetoothOn()
//
// These functions are used to turn the Bluetooth module on or off.
//
// It is fine to leave the Bluetooth module always on. The NXT will draw slightly more power than when
// the Bluetooth module is turned off. When BT is powered on then its wireless receiver is continuously
// checking for incoming BT messages/commands to process.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////

bool BluetoothOff()
{
  if (!bWaitForBluetoothIdle())
    return false;
  setBluetoothOff();
  bWaitForBluetoothIdle();
  return true;
}


bool BluetoothOn()
{
  if (!bWaitForBluetoothIdle())
    return false;
  setBluetoothOn();
  bWaitForBluetoothIdle();
  return true;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//                               Turn BlueTooth Visibility On and Off
//
// Function is used to turn the Bluetooth visibility on or off.
//
// When device is "invisible" then it will not respond to "search" commands. However, it will still
// continue to accept connection requests as long as the far end already knows the address of this
// device.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////

bool BluetoothVisible(const bool bVisible)
{
  if (!bWaitForBluetoothIdle())
    return false;
  setBluetoothVisibility(bVisible);
  bWaitForBluetoothIdle();
  return true;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//                                   SetBrickName
//
// Used to modify the "friendly" name used to identify a Bluetooth device.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////


bool SetBrickName(string& sName)
{
  if (!bWaitForBluetoothIdle())
    return false;
  setFriendlyName(sName);
  bWaitForBluetoothIdle();
  return true;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//                       Connect  Bluetooth Devices
//
// Make a Bluetooth connection between this NXT and another device with the specified "friendly name".
//
// The NXT firmware will look up the "sName" in the My Contacts list as well as the list generated by a
// search command. If it cannot find the name in these lists then the request will fail.
//
// The request will also fail if
////////////////////////////////////////////////////////////////////////////////////////////////////////

const int kConnectionIndex = 1; // range is 1 to 3

bool ConnectPort(const string& sName)
{
  TFileIOResult nConnStatus;

  if (!bWaitForBluetoothIdle())
    return false;
  nConnStatus = btConnect(kConnectionIndex, sName);
  if (nConnStatus != ioRsltInProgress)
  {
    // Connection failed!!!
    PlaySound(soundLowBuzz);
    return false;
  }

  bWaitForBluetoothIdle(15000);
  nConnStatus = nBluetoothCmdStatus;
  if (nBTCurrentStreamIndex < 0)
  {
    // Connection failed!!!
    PlaySound(soundLowBuzz);
  }
  else
  {
    int nTemp;
    nTemp = nBTCurrentStreamIndex;
    if (nTemp < 0)
    {
      // Connection failed!!!
      PlaySound(soundLowBuzz);
    }
  }
  return nBluetoothCmdStatus == ioRsltSuccess;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//                       Connect / Disconnect Bluetooth Devices
//
////////////////////////////////////////////////////////////////////////////////////////////////////////

bool DisconnectPort(const int nPort = nBTCurrentStreamIndex)
{
  if (!bWaitForBluetoothIdle())
    return false;
  if (btDisconnect(nPort) != ioRsltCommPending)
    return false;
  bWaitForBluetoothIdle();
  return nBluetoothCmdStatus == ioRsltSuccess;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//                                    Bluetooth Search
//
// This function initiates a bluetooth search. It takes 20 to 25 seconds to perform the search!
//
// Generally end users will not want to use this command. However, here is an excample of where you might
// use it:
//   - Your program tries to make a connection to a specific Bluetooth device by name using the
//     appropriate function for this.
//   - Above request failed because the requested device was not in the contacts list.
//   - Performing a search will add the device to the contacts list.
//   - Repeat the original request and it should succeed.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////

bool BluetoothSearch()
{
  if (!bWaitForBluetoothIdle())
    return false;
  btSearch();
  bWaitForBluetoothIdle();
  return true;
}


bool BluetoothStopSearch()
{
  btStopSearch();
  bWaitForBluetoothIdle();
  return true;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//                                        Main Task
//
////////////////////////////////////////////////////////////////////////////////////////////////////////

void startCommandMessage(const string sMessage)
{
  string sTemp;

  StringFormat(sTemp, "\r%6.3f >>>>>>>>> ", ((float) nSysTime) / 1000.0);
  writeDebugStream(sTemp);
  writeDebugStream(sMessage);
  writeDebugStream(" <<<<<<<<<\r\r");
  return;
}

void delayBetweenCommands()
{
  long nStartDelayTime;
  long nDelayTime;

  nStartDelayTime = nPgmTime;
  while (true)
  {
    // wait up to 15 seconds for current command to complete

    if ((long)nPgmTime >= (nStartDelayTime + 15000))
      break;

    if (!bBTBusy)
      break;
    wait1Msec(5);
  }
  // minimum of  500 millisecond wait

  nDelayTime = (nStartDelayTime + 500) - nPgmTime;
  if (nDelayTime >= 0)
    wait1Msec(nDelayTime);
  else
    wait1Msec(1);

  if (bBTBusy)
  {
    PlaySound(soundFastUpwardTones);
    writeDebugStream("\r*ERR: Timeout*");
  }
  else if (nBluetoothCmdStatus != ioRsltSuccess)
  {
    PlaySound(soundFastUpwardTones);
    writeDebugStream("\r*ERR: Failed*");
  }
  return;
}

task main()
{
	string& fName = "Bender";
	string file = "lightCal.dat";

  bNxtLCDStatusDisplay = true;
  startCommandMessage("Running ....");
  wait1Msec(2000);

  //
  // Setup some useful BT parameters.
  //
  bBTSkipPswdPrompt    = true;          // Simply use default password. No need for user entry.
  bBTHasProgressSounds = true;          // Play speaker tones when BT connected / disconnected
  bBTDebugTrace        = true;          // Generate messages on internal BT operation to "debug stream" output

  while (true)
  {
    startCommandMessage("BT 'Off'");		      BluetoothOff();         delayBetweenCommands();
    startCommandMessage("BT 'On'");		        BluetoothOn();          delayBetweenCommands();
    startCommandMessage("Set Brick Name");		SetBrickName(fName);  delayBetweenCommands();
    startCommandMessage("BT Invisible");		  BluetoothVisible(false); delayBetweenCommands();
    startCommandMessage("BT Visible");		    BluetoothVisible(true); delayBetweenCommands();
    //
    // Connect and Disconnect
    //
    startCommandMessage("BT Connect");		    ConnectPort("Ben");		 delayBetweenCommands();
    startCommandMessage("BT Disconnect");		  DisconnectPort();		     delayBetweenCommands();
  }
  return;
}
