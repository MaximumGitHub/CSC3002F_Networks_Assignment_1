
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class server extends Thread {

    private String[][] docList;
    private ServerSocket ss;
    private server server;

    public server() {
        try {
            ss = new ServerSocket(59090);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readDocList() throws Exception
    {
        File f = new File("docList.txt");
        Scanner sc = new Scanner(f);
        int i = 0;
        //count number of lines for text file to assign array dimensions appropriately
        BufferedReader reader = new BufferedReader(new FileReader("docList.txt"));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();
        docList = new String[lines][2];
        while(sc.hasNextLine())
        {
            String[] tempArr = sc.nextLine().split(",");
            docList[i][0] = tempArr[0];
            docList[i][1] = tempArr[1];
            i++;
        }

    }

    private void saveFile(Socket clientSock,String[] tempArr) throws Exception {
        DataInputStream dis = new DataInputStream(clientSock.getInputStream());
        FileOutputStream fos = new FileOutputStream(tempArr[0]);

        File file = new File(tempArr[0]); // attain from client using UTF
        byte[] buffer = new byte[4096]; // need to send number of bytes from client via UTF

        int read = 0;
        int totalRead = 0;
        int remaining = Integer.parseInt(tempArr[1]);
        while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
            totalRead += read;
            remaining -= read;
            System.out.println("read " + totalRead + " bytes.");
            fos.write(buffer, 0, read);
        }
        //server.readDocList();
        //System.out.println("the thing"+docList[0][0]);
        FileWriter fr = new FileWriter("docList.txt", true);
        String tempDoc;
        tempDoc = tempArr[0] + "," + tempArr[3];
        //System.out.println("tempDoc: " + tempDoc);
        fr.write(tempDoc + "\n");
        fr.close();


    }

    private void sendFile(Socket clientSock, String[] tempArr) throws IOException {
        DataOutputStream dos = new DataOutputStream(clientSock.getOutputStream());
        //DataInputStream dis = new DataInputStream(clientSock.getInputStream());


        File f = new File(tempArr[0]);
        try{
            FileInputStream FinputS = new FileInputStream(tempArr[0]);
            long fileSize;
            fileSize = f.length();

            String tempPro = "";
            tempPro = tempArr[0]+ "," + fileSize;
            String incorrectPass = "incorrectPass,"+0;
            String incorrectFile = "incorrectFile,"+0;
            System.out.println(docList.length);
            for(int x = 0; x < docList.length; x++)
            {
                System.out.println("loop starts");
                if(docList[x][1].equals(tempArr[1])&& docList[x][0].equals(tempArr[0])||docList[x][1].equals(" ")&& docList[x][0].equals(tempArr[0])) {
                    dos.writeUTF(tempPro);
                    dos.flush();
                    System.out.println("Sent protocol");

                }
                else if(docList[x][0].equals(tempArr[0])){
                    System.out.println("Incorrect password.");
                    dos.writeUTF(incorrectPass);

                }
                else {
                    System.out.println("file not exist");
                    dos.writeUTF(incorrectFile);
                }


            }

            byte[] buffer = new byte[(int)f.length()]; // was 4096
            while (FinputS.read(buffer) > 0){
                dos.write(buffer);
            }
            dos.close();
            System.out.println("File sent. Check Local Directory\n");
            clientSock.close();
        }
        catch(Exception e){
            //send error message
            String errMess = "incorrectFile,"+0;
            dos.writeUTF(errMess);
        }


    }

    public void sendList(Socket clientSock, String[] tempArr) throws IOException
    {
        DataOutputStream dos = new DataOutputStream(clientSock.getOutputStream());
        //DataInputStream dis = new DataInputStream(clientSock.getInputStream());

        //System.out.println("readUTF read 2");
        File f = new File("docList.txt");

        BufferedReader reader = new BufferedReader(new FileReader("docList.txt"));
        int lines = 0;
        while (reader.readLine() != null) {
            lines++;
            String[] temp = new String[lines];
        }
        reader.close();

        String row;
        String doc = "";
        BufferedReader txtReader = new BufferedReader(new FileReader("docList.txt"));
        while ((row = txtReader.readLine()) != null) {
            String[] data = row.split(",");
            if(data[1].equals(tempArr[3])||data[1].equals(" "))
            {doc += data[0] +"\n";}
        }
        txtReader.close();

        System.out.println(doc);
        //System.out.println("just before writeUTF");
        dos.writeUTF(doc);
        dos.close();
        System.out.println("List sent. Check Local Directory\n");
        clientSock.close();

    }


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

    public void run() {
        System.out.println("Server is now Running...\n");
        while (true) {
            try {
                Socket clientSock = ss.accept();
                DataInputStream dis = new DataInputStream(clientSock.getInputStream());
                String tempPro = "";
                tempPro = dis.readUTF();
                //tempPro = sc.nextLine();
                //System.out.println("File name and size:  "+tempPro);
                String[] tempArr = tempPro.split(",");
                switch(tempArr[2]){
                    case "u":
                        saveFile(clientSock, tempArr);
                        break;
                    case "d":
                        try{
                            sendFile(clientSock, tempArr);
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case "v":
                        sendList(clientSock, tempArr);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
