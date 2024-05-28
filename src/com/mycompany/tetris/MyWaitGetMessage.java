/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tetris;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mycompany.tetris.TETRIS;

/**
 *
 * @author kekos
 */
public class MyWaitGetMessage extends Thread{
    BufferedReader bufIn;
    public boolean Game=true;

    MyWaitGetMessage(int lenght,BufferedReader bufInArg){
        this.bufIn=bufInArg;
    }

    @Override
    public void run() {
        while(Game){
            try {
                char[] data;
                data=new char[1];
                bufIn.read(data, 0, 1);
                if(data[0]=='3'){
                    TETRIS.command2=3;
                }else if(data[0]=='2'){
                    TETRIS.command2=2;
                }else if(data[0]=='1'){
                    TETRIS.command2=1;
                }else if(data[0]=='4'){
                    TETRIS.command2=4;
                }

            } catch (IOException ex) {
                Logger.getLogger(MyWaitGetMessage.class.getName()).log(Level.SEVERE, null, ex);
}
        }
    }
}