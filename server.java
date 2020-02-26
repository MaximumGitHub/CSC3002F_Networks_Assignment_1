package com.company;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class server extends Thread {

    private ServerSocket ss;
    private String fname; /// need to have this change depending on client side input
    private File f;

    public server() {
        try {
            ss = new ServerSocket(59090);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        System.out.println("Server is now Running...");
        while (true) {
            try {
                Socket clientSock = ss.accept();
                saveFile(clientSock);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveFile(Socket clientSock) throws IOException {
        DataInputStream dis = new DataInputStream(clientSock.getInputStream());
        //Scanner sc = new Scanner(new BufferedReader(new InputStreamReader(clientSock.getInputStream(),"UTF-8")));
        String tempPro = "";
        tempPro = dis.readUTF();
        //tempPro = sc.nextLine();
        System.out.println("File name and size:  "+tempPro);
        String[] tempArr = tempPro.split(",");
        FileOutputStream fos = new FileOutputStream(tempArr[0]);

        File f = new File(tempArr[0]); // attain from client using UTF
        byte[] buffer = new byte[4096]; // need to send number of bytes from client via UTF
        //
        //int filesize = 15123; // Send file size in separate message using UTF
        //
        int read = 0;
        int totalRead = 0;
        int remaining = Integer.parseInt(tempArr[1]);
        while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
            totalRead += read;
            remaining -= read;
            System.out.println("read " + totalRead + " bytes.");
            fos.write(buffer, 0, read);
        }

        //fos.close();
        //dis.close();
    }

    /*private void sendFile(Socket clientSock) throws IOException {
        DataOutputStream dos = new DataOutputStream(clientSock.getOutputStream());
        FileInputStream fis = new FileInputStream;
    }*/

    public static void main(String[] args) {
        server fs = new server();
        fs.start();
    }

}
