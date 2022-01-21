package com.example.test1;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.content.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import static com.example.test1.MyApplication.sendcomp;

public class MyBluetooth {
    public static BluetoothAdapter mBtAdapter;
    private static ConnectThread mConnectThread;
    public static ConnectedThread nanami;
    private static boolean isBlueToothConnected = false;
    public static final String TAG = "ContentValues";
    public void printLog(String str) {
        Log.e("print", str);
    }
    public void connect(BluetoothDevice device) {
        printLog("connect to: " + device);
        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
    }
    public class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            } catch (IOException e) {
                printLog("create() failed" + e);
            }
            mmSocket = tmp;
        }
        public void run() {
            if (Thread.interrupted())
                return;
            printLog("BEGIN mConnectThread");
            setName("ConnectThread");

            // Always cancel discovery because it will slow down a connection
            mBtAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                //isBlueToothConnected = true;
                mmSocket.connect();
                sendcomp.what=1551;
                isBlueToothConnected = true;
            } catch (IOException e) {
                sendcomp.what=773;
                printLog("T unable to connect() socket " + e);
                //handler.sendEmptyMessage(NOT_CONNECT);
                isBlueToothConnected = false;
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    printLog("unable to close() socket during connection failure" + e2);
                }
                return;
            }

            //mConnectThread = null;

            isBlueToothConnected = true;

            // Start the connected thread
            // Start the thread to manage the connection and perform
            // transmissions
            //handler.sendEmptyMessage(CONNECT_SUCCESS);

             nanami = new ConnectedThread(mmSocket);
            //blccmp=true;
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                printLog("close() of connect socket failed" + e);
            }
        }
    }
}
class ConnectedThread extends Thread {
    public static final String TAG = "ContentValues";

    public void printLog(String str) {
        Log.e("print", str);
    }

    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;

    public ConnectedThread(BluetoothSocket socket) {

        printLog("create ConnectedThread");
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the BluetoothSocket input and output streams
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            printLog("temp sockets not created" + e);
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        if (Thread.interrupted()) {
            printLog("return");
            return;
        }
        printLog("BEGIN mConnectedThread");
        byte[] buffer = new byte[256];
        int bytes;

        // Keep listening to the InputStream while connected
        while (true) {
            synchronized (this) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    printLog(bytes + "bytes");
                    //returnres=(int)buffer[0];
                    //printLog(String.valueOf(returnres));
                    //if(returnres==67){warning="得到数据";}
                    // Message msg = new Message();
                    // msg.what = GET_DATA;
                    // Bundle bundle = new Bundle();
                    //bundle.putInt("data", buffer[0]);
                    //msg.setData(bundle);
                    //handler.sendMessage(msg);
                    sendcomp.what=1551;
                    printLog("handle: "+String.valueOf(sendcomp.what));

                } catch (IOException e) {
                    printLog("disconnected " + e);
                    sendcomp.what=773;
                    printLog("handle: "+String.valueOf(sendcomp.what));
//                        handler.sendEmptyMessage(OUT_OF_CONNECTED);
                    break;
                }
            }
        }
    }

    /**
     * Write to the connected OutStream.
     *
     * @param buffer The bytes to write
     */
    public void write(byte[] buffer) {
        try {
            sendcomp.what=1551;
            mmOutStream.write(buffer); //java.io.IOException: Broken pipe
        } catch (IOException e) {
            sendcomp.what=773;
            Log.e(TAG, "Exception during write", e);
        }
        //blccmp=false;
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "close() of connect socket failed", e);
        }
    }
}
