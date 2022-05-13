package com.elexlab.cybercontroller.communication;

import android.content.Context;
import android.view.KeyEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class BluetoothKeyboard {
    public static Map<String,Byte> KEY2BYTE = new HashMap<String,Byte> ();
    public static Map<String,Boolean> SHITBYTE = new HashMap<String,Boolean> ();
    static {
        KEY2BYTE.put("A", (byte) 4);
        KEY2BYTE.put("B",(byte)5);
        KEY2BYTE.put("C",(byte)6);
        KEY2BYTE.put("D",(byte)7);
        KEY2BYTE.put("E",(byte)8);
        KEY2BYTE.put("F",(byte)9);
        KEY2BYTE.put("G",(byte)0);
        KEY2BYTE.put("H",(byte)11);
        KEY2BYTE.put("I",(byte)12);
        KEY2BYTE.put("J",(byte)13);
        KEY2BYTE.put("K",(byte)14);
        KEY2BYTE.put("L",(byte)15);
        KEY2BYTE.put("M",(byte)16);
        KEY2BYTE.put("N",(byte)17);
        KEY2BYTE.put("O",(byte)18);
        KEY2BYTE.put("P",(byte)19);
        KEY2BYTE.put("Q",(byte)20);
        KEY2BYTE.put("R",(byte)21);
        KEY2BYTE.put("S",(byte)22);
        KEY2BYTE.put("T",(byte)23);
        KEY2BYTE.put("U",(byte)24);
        KEY2BYTE.put("V",(byte)25);
        KEY2BYTE.put("W",(byte)26);
        KEY2BYTE.put("X",(byte)27);
        KEY2BYTE.put("Y",(byte)28);
        KEY2BYTE.put("Z",(byte)29);

        KEY2BYTE.put("1",(byte)30);
        KEY2BYTE.put("2",(byte)31);
        KEY2BYTE.put("3",(byte)32);
        KEY2BYTE.put("4",(byte)33);
        KEY2BYTE.put("5",(byte)34);
        KEY2BYTE.put("6",(byte)35);
        KEY2BYTE.put("7",(byte)36);
        KEY2BYTE.put("8",(byte)37);
        KEY2BYTE.put("9",(byte)38);
        KEY2BYTE.put("0",(byte)39);

        KEY2BYTE.put("ENTER",(byte)40);
        KEY2BYTE.put("ESC",(byte)41);
        KEY2BYTE.put("BACK_SPACE",(byte)42);
        KEY2BYTE.put("TAB",(byte)43);
        KEY2BYTE.put("SPACE",(byte)44);
        KEY2BYTE.put("-",(byte)45);
        KEY2BYTE.put("=",(byte)46);
        KEY2BYTE.put("[",(byte)47);
        KEY2BYTE.put("]",(byte)48);
        KEY2BYTE.put("\\",(byte)49);
        KEY2BYTE.put(";",(byte)51);
        KEY2BYTE.put("'",(byte)52);
        KEY2BYTE.put("`",(byte)53);
        KEY2BYTE.put(",",(byte)54);
        KEY2BYTE.put(".",(byte)55);
        KEY2BYTE.put("/",(byte)56);
        KEY2BYTE.put("SCROLL_LOCK",(byte)71);
        KEY2BYTE.put("INSERT ",(byte)73);
        KEY2BYTE.put("HOME ",(byte)74);
        KEY2BYTE.put("PAGE_UP  ",(byte)75);
        KEY2BYTE.put("DELETE ",(byte)76);
        KEY2BYTE.put("END ",(byte)77);
        KEY2BYTE.put("PAGE_DOWN ",(byte)78);
        KEY2BYTE.put("DPAD_RIGHT ",(byte)79);
        KEY2BYTE.put("KEYCODE_DPAD_LEFT ",(byte)80);
        KEY2BYTE.put("KEYCODE_DPAD_DOWN ",(byte)81);
        KEY2BYTE.put("KEYCODE_DPAD_UP ",(byte)82);
        KEY2BYTE.put("NUM_LOCK ",(byte)83);




        KEY2BYTE.put("!",(byte)30);
        SHITBYTE.put("!",true);

        KEY2BYTE.put("@",(byte)31);
        SHITBYTE.put("@",true);

        KEY2BYTE.put("#",(byte)32);
        SHITBYTE.put("#",true);

        KEY2BYTE.put("$",(byte)33);
        SHITBYTE.put("$",true);

        KEY2BYTE.put("%",(byte)34);
        SHITBYTE.put("%",true);

        KEY2BYTE.put("^",(byte)35);
        SHITBYTE.put("^",true);

        KEY2BYTE.put("&",(byte)36);
        SHITBYTE.put("&",true);

        KEY2BYTE.put("*",(byte)37);
        SHITBYTE.put("*",true);

        KEY2BYTE.put("(",(byte)38);
        SHITBYTE.put("(",true);

        KEY2BYTE.put(")",(byte)39);
        SHITBYTE.put(")",true);


        KEY2BYTE.put("_",(byte)45);
        SHITBYTE.put("_",true);

        KEY2BYTE.put("+",(byte)46);
        SHITBYTE.put("+",true);


        KEY2BYTE.put("{",(byte)47);
        SHITBYTE.put("{",true);

        KEY2BYTE.put("}",(byte)48);
        SHITBYTE.put("}",true);

        KEY2BYTE.put("|",(byte)49);
        SHITBYTE.put("|",true);

        KEY2BYTE.put(":",(byte)51);
        SHITBYTE.put(":",true);

        KEY2BYTE.put("\"",(byte)52);
        SHITBYTE.put("\"",true);

        KEY2BYTE.put("<",(byte)54);
        SHITBYTE.put("<",true);

        KEY2BYTE.put(">",(byte)55);
        SHITBYTE.put(">",true);

        KEY2BYTE.put("?",(byte)56);
        SHITBYTE.put("?",true);



    }

    private BluetoothClient bluetoothClient;
    public BluetoothKeyboard(Context context){
        bluetoothClient = BluetoothClient.getInstance();
        bluetoothClient.setListener(new BluetoothClient.Listener() {
            @Override
            public void onConnected() {
            }

            @Override
            public void onDisConnected() {
                bluetoothClient.active();

            }
        });
    }

    public void sendKey(String key){
        byte b1 = 0;

        if(key.length()<=1){
            char keyChar =  key.charAt(0);
            if((keyChar>=65)&&(keyChar<=90)){
                b1 = 2;
            }
        }


        if(BluetoothKeyboard.SHITBYTE.containsKey(key)){
            b1 = 2;
        }

        bluetoothClient.sendData(8, new byte[]{b1,0,BluetoothKeyboard.KEY2BYTE.get(key.toUpperCase())});
        bluetoothClient.sendData( 8, new byte[]{0,0,0});

    }

    public void active(){
        bluetoothClient.active();
    }
    public void stop(){
        bluetoothClient.stop();
    }



}
