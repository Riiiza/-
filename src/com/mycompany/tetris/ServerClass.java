/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tetris;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import mycompany.tetris.TETRIS;

/**
 *
 * @author kekos
 */
public class ServerClass extends Thread{


   public static boolean check(Point[] Player,boolean[][] field,int left, int right){

       for (int i = 0; i < 4; i++)
		if (Player[i].x < left || Player[i].x >= right || Player[i].y >= 20)
			return false;
		else if ((Player[i].y >= 0) && (field[Player[i].x][Player[i].y]))
			return false;

	return true;

   } 

   @Override
   public void run() {

       //Сервер
        int port = 40000;
       ServerSocket server = null;
       Socket clientSocket = null;
       BufferedReader ServerIn = null; // потокчтенияизсокета
       BufferedWriter ServerOut = null; // потокзаписивсокет

        try {
            server = new ServerSocket(port);
            clientSocket = server.accept();
            ServerIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // писатьтудаже
            ServerOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            //serverOutput.writeBytes("Java revisited\n");
            //
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        MyWaitGetMessage waitgetmessage;
        waitgetmessage=new MyWaitGetMessage(1,ServerIn);
        waitgetmessage.start();


        boolean GameOver =false;
        Random rand = new Random();
        int Width=20;
        int Height=20;
	Point p = new Point();
        Point[] Player1;
        Player1 = new Point[4];
        Point Player1Buff[]; 
        Player1Buff=new Point[4]; 
        Point Player2[]; 
        Player2=new Point[4];
        Point Player2Buff[];
        Player2Buff=new Point[4];
        long time;
	int LeftPlayer1 = 0, RightPlayer1 = 10, LeftPlayer2 = 10, RightPlayer2 = 20, dx = 0, dy = 0, n = 0, count = 0;
	boolean rotate = false, CheckUpdate = false;
	long delay = 500;

	int[][] figures;
        figures= new int[][]{
       {0,2,4,6}, // I
       {2,4,5,7}, // s
       {3,4,5,6}, // z
       {2,4,5,6}, // T
       {2,4,6,7}, // L
       {3,5,6,7}, // J
       {4,5,6,7}, // O
	};

	boolean[][] field;
        field =new boolean[20][20]; // игровоеполе
        for (int i = 0; i < Width; i++) {
            for (int j = 0; j < Height; j++) {
                        field[i][j]=false;
                    }
		}

	char player2Command;
	char player2CommandSwap;
	char[] FieldMsg;
        FieldMsg=new char[401];// 0-399 ->поле |||||   400 состояниеигры: 0-Играпродолжается, 1-Играокончена,2-Игроквышел
	FieldMsg[400] = '0';



	//Первичное генерирование фигур
	n = rand.nextInt(100) % 7;
	for (int i = 0; i < 4; i++)
	{
                Player1[i]=new Point((figures[n][i] % 2 + 4),(figures[n][i] / 2));
//		Player1[i].x = figures[n][i] % 2 + 4;
//		Player1[i].y = figures[n][i] / 2 - 3;
	}
        n = rand.nextInt(100) % 7;
	for (int i = 0; i < 4; i++)
	{
                Player2[i]=new Point((figures[n][i] % 2 + 16),(figures[n][i] / 2));
//		Player1[i].x = figures[n][i] % 2 + 4;
//		Player1[i].y = figures[n][i] / 2 - 3;
	}

        time = System.currentTimeMillis();
       while(true){

//ПРОВЕРКА ДЕЙСТВИЙ 1 ИГРОКА
if(TETRIS.command==3){//rotate
rotate=true;
               CheckUpdate=true;
               TETRIS.command=0;
           }else if(TETRIS.command==2){//right
                dx = 1;
		CheckUpdate = true;
                TETRIS.command=0;
           }else if(TETRIS.command==1){//left
               dx = -1;
                CheckUpdate = true;
                TETRIS.command=0;
           }else if(TETRIS.command==4){    
               TETRIS.command=0;
               GameOver=true;
           }

           //РЕАЛИЗАЦИЯДЕЙСТВИЙ 1 ИГРОКА
            if (CheckUpdate) {
// Горизонтальное перемещение и бэкап фигуры
if (dx != 0) {
for (int i = 0; i < 4; i++)
                        {
                                Player1Buff[i] = new Point(Player1[i].x,Player1[i].y);
                                Player1[i].x += dx;
                        }
                        if (!check(Player1, field, LeftPlayer1, RightPlayer1))
                                for (int i = 0; i < 4; i++)
                                        Player1[i] = Player1Buff[i];
                        dx = 0;
                }
                // Вращение //
                if (rotate)
                {
                        for (int i = 0; i < 4; i++)
                        {
                                Player1Buff[i] = new Point(Player1[i].x,Player1[i].y);
                                Player1[i].x += dx;
                        }
                        p = Player1[1]; // задаемцентрвращения
                        for (int i = 0; i < 4; i++)
                        {
                                int x = Player1[i].y - p.y; //y-y0
                                int y = Player1[i].x - p.x; //x-x0
                                Player1[i].x = p.x - x;
                                Player1[i].y = p.y + y;
}
                        // Вышли за пределы поля после поворота?
if (!check(Player1, field, LeftPlayer1, RightPlayer1)) {
                                for (int i = 0; i < 4; i++)
                                        Player1[i] = Player1Buff[i];
                        }
                        rotate = false;
                }

                CheckUpdate = false;
        }



            // 2 ИГРОК
            //ПРОВЕРКАДЕЙСТВИЙ 2 ИГРОКА

           if(TETRIS.command2==3){//rotate
               rotate=true;
               CheckUpdate=true;
               TETRIS.command2=0;
           }else if(TETRIS.command2==2){//right
                dx = 1;
		CheckUpdate = true;
                TETRIS.command2=0;
           }else if(TETRIS.command2==1){//left
               dx = -1;
                CheckUpdate = true;
                TETRIS.command2=0;
           }else if(TETRIS.command2==4){//left
               GameOver=true;
                TETRIS.command2=0;
           }

           //РЕАЛИЗАЦИЯДЕЙСТВИЙ 2 ИГРОКА
            if (CheckUpdate) {
        // Горизонтальное перемещение и бэкап фигуры
if (dx != 0) {
                        for (int i = 0; i < 4; i++)
                        {
                                Player2Buff[i] = new Point(Player2[i].x,Player2[i].y);
                                Player2[i].x += dx;
                        }
                        if (!check(Player2, field, LeftPlayer2, RightPlayer2))
                                for (int i = 0; i < 4; i++)
                                        Player2[i] = Player2Buff[i];
                        dx = 0;
                }
                // Вращение //
                if (rotate)
                {
                        for (int i = 0; i < 4; i++)
                        {
                                Player2Buff[i] = new Point(Player2[i].x,Player2[i].y);
                                Player2[i].x += dx;
                        }
                        p = Player2[1]; // задаемцентрвращения
                        for (int i = 0; i < 4; i++)
                        {
                                int x = Player2[i].y - p.y; //y-y0
                                int y = Player2[i].x - p.x; //x-x0
                                Player2[i].x = p.x - x;
                                Player2[i].y = p.y + y;
}
                        // Вышли за пределы поля после поворота?
if (!check(Player2, field, LeftPlayer2, RightPlayer2)) {
                                for (int i = 0; i < 4; i++)
                                        Player2[i] = Player2Buff[i];
                        }
                        rotate = false;
                }

                CheckUpdate = false;
        }




        //// ШагТаймера
		if (System.currentTimeMillis()-time>delay)
		{
			time=System.currentTimeMillis();

			//проверка ИГРОК 1, достиг ли он низа
			for (int i = 0; i < 4; i++) {
				Player1Buff[i] = new Point(Player1[i].x,Player1[i].y);
				Player1[i].y += 1;
			}
			if (!check(Player1, field, LeftPlayer1, RightPlayer1))
			{
				for (int i = 0; i < 4; i++){
					if (Player1[i].y >= 0) {
                                            field[Player1Buff[i].x][Player1Buff[i].y] = true;
					}

                                }
				n = rand.nextInt(100) % 7;
				for (int i = 0; i < 4; i++)
				{
					//проверка на поражение
					if (Player1[i].y< 2) {
GameOver=true;
                                            System.out.print("LOOOOOOSE");
					}
					Player1[i].x = figures[n][i] % 2 + 4;
					Player1[i].y = figures[n][i] / 2;
				}

			}
                        //проверка ИГРОК 2, достиг ли он низа
			for (int i = 0; i < 4; i++) {
				Player2Buff[i] = new Point(Player2[i].x,Player2[i].y);
				Player2[i].y += 1;
			}
			if (!check(Player2, field, LeftPlayer2, RightPlayer2))
			{
				for (int i = 0; i < 4; i++){
					if (Player2[i].y >= 0) {
                                            field[Player2Buff[i].x][Player2Buff[i].y] = true;
					}

                                }
				n = rand.nextInt(100) % 7;
				for (int i = 0; i < 4; i++)
				{
					//проверка на поражение
					if (Player2[i].y< 2) {
System.out.print("LOOOOOOSE");
                                            GameOver=true;
					}
					Player2[i].x = figures[n][i] % 2 + 16;
					Player2[i].y = figures[n][i] / 2;
				}

			}



			//ПРОВЕРКАЛИНИИ
			for (int i = Height - 1; i >= 0; i--) {
				count = 0;
				for (int j = 0; j <= Width - 1; j++) {
					if (field[j][i]) {
						count++;
					}
				}
				if (count == Width) {
					for (int i1 = i; i1 >= 1; i1--) {
						for (int j1 = Width - 1; j1 >= 0; j1--) {
							field[j1][i1] = field[j1][i1 - 1];
						}
					}


				}
			}

		
		}

        //----ОТРИСОВКА/ОТПРАВКА----//
		//поле		
            for (int i = 0; i < 4; i++)
		{
			//отрисовка только тех частей игрока что находятся в видимом поле
			if (Player1[i].y >= 0) {
				field[Player1[i].x][Player1[i].y]=true;
			}
                        if (Player2[i].y >= 0) {
				field[Player2[i].x][Player2[i].y]=true;
			}

		}
            for (int i = 0; i < Width; i++) {
                for (int j = 0; j < Height; j++) {
                    if (field[i][j]){
                            TETRIS.arrJPanel[j][i].setBackground(Color.red);
                    }else{
                        TETRIS.arrJPanel[j][i].setBackground(Color.gray);
                    }
                }
            }


                //ОТПРАВКА
                int countField=0;
                for(int i=0;i<20;i++){
                    for( int j=0;j<20;j++){

                        if(field[i][j]){
                            FieldMsg[countField]='1';
                        }else{
                            FieldMsg[countField]='0';
                        }
                        countField=countField+1;

                    }
                }
                if(GameOver){
                    FieldMsg[400]='5';
                }

            try {
                ServerOut.write(FieldMsg, 0, 401);
                ServerOut.flush();
            } catch (IOException ex) {
                Logger.getLogger(ServerClass.class.getName()).log(Level.SEVERE, null, ex);
            }


		// Игроки
            for (int i = 0; i < 4; i++)
		{
			//отрисовка только тех чайстей игрока что находятся в видимом поле
			if (Player1[i].y >= 0) {
				field[Player1[i].x][Player1[i].y]=false;
			}
                        if (Player2[i].y >= 0) {
				field[Player2[i].x][Player2[i].y]=false;
			}

		}





        if (GameOver){

                waitgetmessage.Game=false;
               try {
                   waitgetmessage.join();
               } catch (InterruptedException ex) {
                   Logger.getLogger(ServerClass.class.getName()).log(Level.SEVERE, null, ex);
               }
                TETRIS.labl.setText("");
                TETRIS.StartServerBtn.setEnabled(true);
                TETRIS.StartClientBtn.setEnabled(true);
                TETRIS.ip.setEnabled(true);
                TETRIS.port.setEnabled(true);
                TETRIS.StopBtn.setEnabled(false);
                try {
                    clientSocket.close();
                    server.close();
                }
                catch (IOException e) {
                  e.printStackTrace();
                }
                break;
        }

       }




    }


}