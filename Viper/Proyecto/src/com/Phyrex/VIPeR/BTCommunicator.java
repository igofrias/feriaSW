package com.Phyrex.VIPeR;

/**
 *   (Changes from original are) Copyright 2010 Guenther Hoelzl, Shawn Brown
 * 
 * (original work is) Copyright (C) 2009 The Android Open Source Project
**/

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.UUID;


/**
 * This class is for talking to a LEGO NXT robot via bluetooth.
 * The communciation to the robot is done via LCP (LEGO communication protocol).
 * Objects of this class can either be run as standalone thread or controlled
 * by the owners, i.e. calling the send/receive methods by themselves.
 */

//Nos servir�a manejo de un motor, desconectar, estados de conexi�n y errores.
public class BTCommunicator extends Thread {
    public static final int MOTOR_A = 0;
    public static final int MOTOR_B = 1;
    public static final int MOTOR_C = 2;
    public static final int MOTOR_B_ACTION = 40;
    public static final int MOTOR_RESET = 10;
    public static final int DO_BEEP = 51;
    public static final int DO_ACTION = 52;    
    public static final int READ_MOTOR_STATE = 60;
    public static final int GET_FIRMWARE_VERSION = 70;
    public static final int DISCONNECT = 99;

    public static final int DISPLAY_TOAST = 1000;
    public static final int STATE_CONNECTED = 1001;
    public static final int STATE_CONNECTERROR = 1002;
    public static final int STATE_CONNECTERROR_PAIRING = 1022;
    public static final int MOTOR_STATE = 1003;
    public static final int STATE_RECEIVEERROR = 1004;
    public static final int STATE_SENDERROR = 1005;
    public static final int FIRMWARE_VERSION = 1006;
    public static final int FIND_FILES = 1007;
    public static final int START_PROGRAM = 1008;
    public static final int STOP_PROGRAM = 1009;
    public static final int GET_PROGRAM_NAME = 1010;
    public static final int PROGRAM_NAME = 1011;
    public static final int SAY_TEXT = 1030;
    public static final int VIBRATE_PHONE = 1031;
    public static final int RECEIVE_INT_MESSAGE = 1032;
    public static final int SEND_PET_MESSAGE = 1033;
    public static final int NO_DELAY = 0;

    private static final UUID SERIAL_PORT_SERVICE_CLASS_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // this is the only OUI registered by LEGO, see http://standards.ieee.org/regauth/oui/index.shtml
    public static final String OUI_LEGO = "00:16:53";
	

    private Resources mResources;
    private BluetoothAdapter btAdapter;
    private BluetoothSocket nxtBTsocket = null;
    private OutputStream nxtOutputStream = null;
    private InputStream nxtInputStream = null;
    private boolean connected = false;

    private Handler uiHandler;
    private String mMACaddress;

	private BTConnectable myOwner;

    private byte[] returnMessage;

    public BTCommunicator(BTConnectable myOwner, Handler uiHandler, BluetoothAdapter btAdapter, Resources resources) {
        this.myOwner = myOwner;
        this.uiHandler = uiHandler;
        this.btAdapter = btAdapter;
        this.mResources = resources;
    }

    public Handler getHandler() {
        return myHandler;
    }

    public byte[] getReturnMessage() {
        return returnMessage;
    }

    public void setMACAddress(String mMACaddress) {
        this.mMACaddress = mMACaddress;
    }

    /**
     * @return The current status of the connection
     */            
    public boolean isConnected() {
        return connected;
    }

    /**
     * Creates the connection, waits for incoming messages and dispatches them. The thread will be terminated
     * on closing of the connection.
     */
    @Override
    public void run() {

        try {        
            createNXTconnection();
        }
        catch (IOException e) { }

        while (connected) {
            try {
                returnMessage = receiveMessage();
                if ((returnMessage.length >= 2) && ((returnMessage[0] == LCPMessage.REPLY_COMMAND) ||
                    (returnMessage[0] == LCPMessage.DIRECT_COMMAND_NOREPLY)))
                    dispatchMessage(returnMessage);

            } catch (IOException e) {
                // don't inform the user when connection is already closed
                if (connected)
                    sendState(STATE_RECEIVEERROR);
                return;
            }
        }
    }

    /**
     * Create a bluetooth connection with SerialPortServiceClass_UUID
     * @see <a href=
     *      "http://lejos.sourceforge.net/forum/viewtopic.php?t=1991&highlight=android"
     *      />
     * On error the method either sends a message to it's owner or creates an exception in the
     * case of no message handler.
     */
    public void createNXTconnection() throws IOException {
        try {
            BluetoothSocket nxtBTSocketTemporary;
            BluetoothDevice nxtDevice = null;
            nxtDevice = btAdapter.getRemoteDevice(mMACaddress);
            if (nxtDevice == null) {
                if (uiHandler == null)
                    throw new IOException();
                else {
                    sendToast(mResources.getString(R.string.no_paired_nxt));
                    sendState(STATE_CONNECTERROR);
                    return;
                }
            }
            nxtBTSocketTemporary = nxtDevice.createRfcommSocketToServiceRecord(SERIAL_PORT_SERVICE_CLASS_UUID);
            try {
                nxtBTSocketTemporary.connect();
            }
            catch (IOException e) {  
                if (myOwner.isPairing()) {
                    if (uiHandler != null) {
                        sendToast(mResources.getString(R.string.pairing_message));
                        sendState(STATE_CONNECTERROR_PAIRING);
                        //run();
                    }
                    else
                        throw e;
                    return;
                }

                // try another method for connection, this should work on the HTC desire, credits to Michael Biermann
                try {
                    Method mMethod = nxtDevice.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
                    nxtBTSocketTemporary = (BluetoothSocket) mMethod.invoke(nxtDevice, Integer.valueOf(1));            
                    nxtBTSocketTemporary.connect();
                }
                catch (Exception e1){
                    if (uiHandler == null)
                        throw new IOException();
                    else
                        sendState(STATE_CONNECTERROR);
                    return;
                }
            }
            nxtBTsocket = nxtBTSocketTemporary;
            nxtInputStream = nxtBTsocket.getInputStream();
            nxtOutputStream = nxtBTsocket.getOutputStream();
            connected = true;
        } catch (IOException e) {
            if (uiHandler == null)
                throw e;
            else {
                if (myOwner.isPairing())
                    sendToast(mResources.getString(R.string.pairing_message));
                sendState(STATE_CONNECTERROR);
                return;
            }
        }
        // everything was OK
        if (uiHandler != null)
            sendState(STATE_CONNECTED);
    }

    /**
     * Closes the bluetooth connection. On error the method either sends a message
     * to it's owner or creates an exception in the case of no message handler.
     */
    public void destroyNXTconnection() throws IOException {
        try {
            if (nxtBTsocket != null) {
                connected = false;
                nxtBTsocket.close();
                nxtBTsocket = null;
            }

            nxtInputStream = null;
            nxtOutputStream = null;

        } catch (IOException e) {
            if (uiHandler == null)
                throw e;
            else
                sendToast(mResources.getString(R.string.problem_at_closing));
        }
    }

    /**
     * Sends a message on the opened OutputStream
     * @param message, the message as a byte array
     */
    public void sendMessage(byte[] message) throws IOException {
        if (nxtOutputStream == null)
            throw new IOException();

        // send message length
        int messageLength = message.length;
        nxtOutputStream.write(messageLength);
        nxtOutputStream.write(messageLength >> 8);
        nxtOutputStream.write(message, 0, message.length);
    }  

    /**
     * Receives a message on the opened InputStream
     * @return the message
     */                
    public byte[] receiveMessage() throws IOException {
        if (nxtInputStream == null)
            throw new IOException();
     
        int length = nxtInputStream.read();
        length = (nxtInputStream.read() << 8) + length;
        byte[] returnMessage = new byte[length];
        nxtInputStream.read(returnMessage);
        return returnMessage;
    }    
    
    /**
     * Sends a message on the opened OutputStream. In case of 
     * an error the state is sent to the handler.
     * @param message, the message as a byte array
     */
    void sendMessageAndState(byte[] message) {
        if (nxtOutputStream == null){
            return;
        }
        try {
            sendMessage(message);
        }
        catch (IOException e) {
            sendState(STATE_SENDERROR);
        }
    }

    private void dispatchMessage(byte[] message) {
        switch (message[1]) {

            case LCPMessage.GET_OUTPUT_STATE:

                if (message.length >= 25)
                    sendState(MOTOR_STATE);

                break;

            case LCPMessage.GET_FIRMWARE_VERSION:

                if (message.length >= 7)
                    sendState(FIRMWARE_VERSION);

                break;
                
            case LCPMessage.MESSAGE_WRITE:

               if (message.length >= 4)
                   sendState(RECEIVE_INT_MESSAGE);

                break;
                //TODO
        }
        
        	
    }

    private void startProgram(String programName) {
        byte[] message = LCPMessage.getStartProgramMessage(programName);
        sendMessageAndState(message);
        
    }

    private void stopProgram() {
        byte[] message = LCPMessage.getStopProgramMessage();
        sendMessageAndState(message);
    }
    
    private void getProgramName() {
        byte[] message = LCPMessage.getProgramNameMessage();
        sendMessageAndState(message);
    }
    private void doBeep(int frequency, int duration) {
        byte[] message = LCPMessage.getBeepMessage(frequency, duration);
        sendMessageAndState(message);
        waitSomeTime(20);
    }
    
    private void doAction(int actionNr) {
        byte[] message = LCPMessage.getActionMessage(actionNr);
        sendMessageAndState(message);
    }
    private void sendPetMessage(int inbox, int petmessage)
    {
    	byte[] int_message = ByteBuffer.allocate(4).putInt(petmessage).array();
    	byte[] message = new byte[8];
    	message[0] = LCPMessage.DIRECT_COMMAND_NOREPLY;
    	message[1] = LCPMessage.MESSAGE_WRITE;
    	message[2] = (byte)inbox;
    	message[3] = 1;
//    	for(int i = 0; i < 4; i++)
//    	{
//    		message[4+i] = int_message[i];
//    	}
    	message[4] = (byte)petmessage;
    	sendMessageAndState(message);
    }
    
    private void changeMotorSpeed(int motor, int speed) {
        if (speed > 100)
            speed = 100;

        else if (speed < -100)
            speed = -100;

        byte[] message = LCPMessage.getMotorMessage(motor, speed);
        sendMessageAndState(message);
    }

    private void rotateTo(int motor, int end) {
        byte[] message = LCPMessage.getMotorMessage(motor, -80, end);
        sendMessageAndState(message);
    }
    

    private void reset(int motor) {
        byte[] message = LCPMessage.getResetMessage(motor);
        sendMessageAndState(message);
    }

    private void readMotorState(int motor) {
        byte[] message = LCPMessage.getOutputStateMessage(motor);
        sendMessageAndState(message);
    }

    private void getFirmwareVersion() {
        byte[] message = LCPMessage.getFirmwareVersionMessage();
        sendMessageAndState(message);
    }

    private void waitSomeTime(int millis) {
        try {
            Thread.sleep(millis);

        } catch (InterruptedException e) {
        }
    }

    private void sendToast(String toastText) {
        Bundle myBundle = new Bundle();
        myBundle.putInt("message", DISPLAY_TOAST);
        myBundle.putString("toastText", toastText);
        sendBundle(myBundle);
    }

    void sendState(int message) {
        Bundle myBundle = new Bundle();
        myBundle.putInt("message", message);
        sendBundle(myBundle);
    }

    private void sendBundle(Bundle myBundle) {
        Message myMessage = myHandler.obtainMessage();
        myMessage.setData(myBundle);
        uiHandler.sendMessage(myMessage);
    }

    // receive messages from the UI
    final Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message myMessage) {

            int message;

            switch (message = myMessage.getData().getInt("message")) {
                case MOTOR_A:
                case MOTOR_B:
                case MOTOR_C:
                    changeMotorSpeed(message, myMessage.getData().getInt("value1"));
                    break;
                case MOTOR_B_ACTION:
                    rotateTo(MOTOR_B, myMessage.getData().getInt("value1"));
                    break;
                    
                case MOTOR_RESET:
                    reset(myMessage.getData().getInt("value1"));
                    break;
                case DO_BEEP:
                    doBeep(myMessage.getData().getInt("value1"), myMessage.getData().getInt("value2"));
                    break;
                case START_PROGRAM:
                    startProgram(myMessage.getData().getString("name"));
                    break;
                case STOP_PROGRAM:
                	stopProgram();
                    break;
                case GET_PROGRAM_NAME:
                    getProgramName();
                    break;    
                case DO_ACTION:
                    doAction(myMessage.getData().getInt("value1"));
                    break;
                case READ_MOTOR_STATE:
                    readMotorState(myMessage.getData().getInt("value1"));
                    break;
                case SEND_PET_MESSAGE:
                	sendPetMessage(myMessage.getData().getInt("value1"),myMessage.getData().getInt("value2"));
                	break;
                case GET_FIRMWARE_VERSION:
                    getFirmwareVersion();
                    break;
                case DISCONNECT:
                    // send stop messages before closing
                    changeMotorSpeed(MOTOR_A, 0);
                    changeMotorSpeed(MOTOR_B, 0);
                    changeMotorSpeed(MOTOR_C, 0);
                    waitSomeTime(500);
                    try {
                        destroyNXTconnection();
                    }
                    catch (IOException e) { }
                    break;
            }
        }
    };
}
