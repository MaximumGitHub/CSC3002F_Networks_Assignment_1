
import java.io.*;
import java.net.InetAddress;
import java.sql.SQLOutput;
import java.util.Scanner;
import java.lang.*;
import java.net.Socket;
import java.util.concurrent.ForkJoinPool;

/**
 * A command line client for the date server. Requires the IP address of the
 * server as the sole argument. Exits after printing the response.
 */
public class client {

    private Socket socked;
    //private static String FILE_NAME;
    private static String fname;
    private static boolean flag;
    static Scanner sc  = new Scanner(System.in);
    Scanner in;

    public client(){
    //    try{
    //        socked = new Socket("localhost",59090);
        //    //       // socked = new Socket("196.47.201.237", 59090);
        //    //    } catch (Exception e){
        //    //        e.printStackTrace();
        //    //    }
    }


    public void uploadFile(String file) throws IOException {

        try{
            socked = new Socket("localhost",59090);
            //socked = new Socket("196.47.201.237", 59090);
        } catch (Exception e){
            e.printStackTrace();
        }

        in = new Scanner(socked.getInputStream());
        System.out.println("Enter the filename you wish to upload:");
        fname = sc.nextLine();
        File f = new File(fname);

        DataOutputStream DoutputS = new DataOutputStream(socked.getOutputStream());
        FileInputStream FinputS = new FileInputStream(fname);

        //building the protocol
        long fileSize = 0;
        fileSize = f.length();

        String tempPro;
        tempPro = fname + "," + fileSize +","+"u";

        DoutputS.writeUTF(tempPro);//Sends the protocol
        /*PrintWriter pw = new PrintWriter(
                new BufferedWriter(new OutputStreamWriter(socked.getOutputStream(), "UTF-8")),true);
        pw.write(tempPro);*/

        //Read the file into the input stream
        byte[] buffer = new byte[(int)f.length()]; // was 4096
        while (FinputS.read(buffer) > 0){
            DoutputS.write(buffer);
        }


        System.out.println("File sent. Check Directory\n");
        socked.close();
        //pw.flush();
        //FinputS.close();
        DoutputS.flush();
    }

    public void downloadFile() throws IOException {

        DataOutputStream dos = null;
        DataInputStream dis = null;
        try{
            socked = new Socket("localhost",59090);
            //socked = new Socket("196.47.201.237", 59090);
        } catch (Exception e){
            e.printStackTrace();
        }
        in = new Scanner(socked.getInputStream());
        System.out.println("Enter the filename you wish to download:");
        fname = sc.nextLine();
        System.out.println("Enter the password:");
        String tempPass = sc.nextLine();
        String tempPro = fname+","+tempPass+","+"d";

        dos = new DataOutputStream(socked.getOutputStream());
        dos.writeUTF(tempPro); //sends through file name


        dis = new DataInputStream(socked.getInputStream());
        byte[] buffer = new byte[4096]; // need to send number of bytes from client via UTF



        String sfile = dis.readUTF();
        System.out.println("sfile: "+sfile);
        String[] tempArr = sfile.split(",");
        FileOutputStream fos = new FileOutputStream(tempArr[0]);

        System.out.println("post fos");
        File f = new File(tempArr[0]); // attain from client using UTF

        int read = 0;
        int totalRead = 0;
        int remaining = Integer.parseInt(tempArr[1]);
        while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
            totalRead += read;
            remaining -= read;
            System.out.println("read " + totalRead + " bytes.");
            fos.write(buffer, 0, read);
        }
    }

    public static void main (String[] args){

        flag = true;
        client c = new client();
        while (flag){
            System.out.println("Welcome to Jeff's Files. Choose an appropriate option:\nList Available Files [q]\nUpload: [u]\nDownload [d]\nQuit [q]");
            String in = sc.nextLine();

            switch(in){
                case "l":
                    flag = false;
                    break;

                case "u":
                    try {
                        c.uploadFile(fname);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "d":
                    try{
                        c.downloadFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("You've selected the download function, unfortunately, we have to code that part first :(\n");
                    break;
                case "q":
                    flag = false;
                    System.out.println("Thank you for using Jeff's Files!");
                    break;
            }

        }
    }

}