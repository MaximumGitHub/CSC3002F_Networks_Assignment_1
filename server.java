
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class server extends Thread {

    private String[][] docList;
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

    public void readDocList() throws Exception
    {
        File f = new File("docList.txt");
        Scanner sc = new Scanner(f);
        int i = 0;
        //count number of lines for text file to assign array dimensions appropriately
        BufferedReader reader = new BufferedReader(new FileReader("docList.txt"));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();
        //
        docList = new String[lines][2];
        while(sc.hasNextLine())
        {
            String[] tempArr = sc.nextLine().split(",");
            //System.out.println(tempArr[0]);
            docList[i][0] = tempArr[0];
            docList[i][1] = tempArr[1];

            System.out.println("Filename: " + docList[i][0] + " password: " + docList[i][1]);

            i++;
        }
        //testing code to print out the array
        // iterate through 2D array

        /*for(int x = 0; x < docList.length; x++) {
            for(int y = 0; y<docList[x].length; y++) {
                System.out.println("Values at arr["+x+"]["+y+"] is "+docList[x][y]);
            }
        }*/

    }

    public void run() {
        System.out.println("Server is now Running...");
        while (true) {
            try {
                //System.out.printf("test1:  ");
                Socket clientSock = ss.accept();
                //System.out.printf("test2:  ");
                DataInputStream dis = new DataInputStream(clientSock.getInputStream());
                String tempPro = "";
                tempPro = dis.readUTF();
                //tempPro = sc.nextLine();
                //System.out.println("File name and size:  "+tempPro);
                String[] tempArr = tempPro.split(",");
                if(tempArr[2].equals("u"))
                {
                    saveFile(clientSock, tempArr);
                }
                else if(tempArr[2].equals("d"))
                {
                    System.out.println("testDownload");
                    sendFile(clientSock, tempArr);
                }
                //Socket clientSock = ss.accept();
                //saveFile(clientSock);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveFile(Socket clientSock,String[] tempArr) throws IOException {
        /*DataInputStream dis = new DataInputStream(clientSock.getInputStream());
        String tempPro = "";
        tempPro = dis.readUTF();
        //tempPro = sc.nextLine();
        System.out.println("File name and size:  "+tempPro);
        String[] tempArr = tempPro.split(",");*/
        DataInputStream dis = new DataInputStream(clientSock.getInputStream());
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

    private void sendFile(Socket clientSock, String[] tempArr) throws IOException {
        DataOutputStream dos = new DataOutputStream(clientSock.getOutputStream());
        DataInputStream dis = new DataInputStream(clientSock.getInputStream());

       // tempfname = dis.readUTF();
        System.out.println("readUTF read 2");
        File f = new File(tempArr[0]);
        FileInputStream FinputS = new FileInputStream(tempArr[0]);
        long fileSize;
        fileSize = f.length();

        String tempPro = "";
        tempPro = tempArr[0]+ "," + fileSize;

        System.out.println("tempPro: "+tempPro);

        dos.writeUTF(tempPro);
        dos.flush();
        System.out.println("Sent protocol");

        dos.writeUTF(tempfname + "," + fileSize);

        byte[] buffer = new byte[(int)f.length()]; // was 4096
        System.out.println("buffer created");
        while (dis.read(buffer) > 0){
            dos.write(buffer);
        }

        dos.close();
        dis.close();


        System.out.println("File sent. Check Local Directory\n");
        clientSock.close();
    }

    /*private void sendFile(Socket clientSock) throws IOException {
        DataOutputStream dos = new DataOutputStream(clientSock.getOutputStream());
        FileInputStream fis = new FileInputStream;
    }*/

    public static void main(String[] args) {
        server fs = new server();
        fs.start();
        try {
            fs.readDocList();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
