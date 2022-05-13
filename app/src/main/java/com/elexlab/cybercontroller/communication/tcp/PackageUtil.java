package com.elexlab.cybercontroller.communication.tcp;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PackageUtil {
    public static byte[] pack(byte[] bytes,int type){
        DataOutputStream dOutput = null;
        try {
            ByteArrayOutputStream bOutput = new ByteArrayOutputStream();
            dOutput = new DataOutputStream(bOutput);
            dOutput.writeInt(type);
            dOutput.writeInt(bytes.length);
            dOutput.write(bytes);
            byte[] dataPackage = bOutput.toByteArray();
            return dataPackage;
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(dOutput!=null){
                try {
                    dOutput.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
