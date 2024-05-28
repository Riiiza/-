/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tetris;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import mycompany.tetris.TETRIS;

/**
 *
 * @author kekos
 */
public class ClientClass extends Thread{


    @Override
   public void run() {

       BufferedWriter ServerOut = null; // потокзаписивсокет
       Socket clientSocket=null;
       InputStream InputStream=null;
       boolean GameOver=false;
       BufferedReader br=null;
       char[] field;
       field=new char[401];
       int portInt;
       portInt=Integer.parseInt(TETRIS.port.getText());

       try {
            clientSocket = new Socket (TETRIS.ip.getText(),portInt);
            InputStream = clientSocket.getInputStream();
            br = new BufferedReader(new InputStreamReader(InputStream));
            ServerOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            //String receivedData = br.readLine();
           // System.out.println("Received Data: "+receivedData);
    }
    catch (IOException e) {
        TETRIS.labl.setText("");
        TETRIS.StartServerBtn.setEnabled(true);
        TETRIS.StartClientBtn.setEnabled(true);
        TETRIS.ip.setEnabled(true);
        TETRIS.port.setEnabled(true);
        TETRIS.StopBtn.setEnabled(false);
      e.printStackTrace();
    }

       while(true){

           try {
               br.read(field, 0, 401);
           } catch (IOException ex) {
               ex.printStackTrace();
           }
           int counter=0;
           for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 20; j++) {
                    if (field[counter]=='0'){
                        TETRIS.arrJPanel[j][i].setBackground(Color.gray);
                    }else{
                        TETRIS.arrJPanel[j][i].setBackground(Color.red);
                    }
                    counter=counter+1;
                }
            }
           if(field[400]=='5'){
               GameOver=true;

           }
           boolean flag=false;
           char kekos='0';

           if(TETRIS.command==3){//rotate
               TETRIS.command=0;
               kekos='3';
               flag=true;
           }else if(TETRIS.command==2){//right
                TETRIS.command=0;
                kekos='2';
                flag=true;
           }else if(TETRIS.command==1){//left
                TETRIS.command=0;
                kekos='1';
                flag=true;
           }
           else if(TETRIS.command==4){//left
                TETRIS.command=0;
                kekos='4';
                flag=true;
           }

           if(flag){
                try {
                        ServerOut.write(kekos);
                        ServerOut.flush();
                        flag=false;
                } catch (IOException ex) {
                    Logger.getLogger(ServerClass.class.getName()).log(Level.SEVERE, null, ex);
                }

           }
           if(GameOver){
                TETRIS.labl.setText("");
                TETRIS.StartServerBtn.setEnabled(true);
                TETRIS.StartClientBtn.setEnabled(true);
                TETRIS.ip.setEnabled(true);
                TETRIS.port.setEnabled(true);
                TETRIS.StopBtn.setEnabled(false);
                try {
                    clientSocket.close();
                }
                catch (IOException e) {
                  e.printStackTrace();
                }
                break;
           }
       }




   }
}