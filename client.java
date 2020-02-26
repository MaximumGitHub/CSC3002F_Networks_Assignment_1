
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
        try{
            socked = new Socket("localhost", 59090);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public void uploadFile(String file) throws IOException {

        in = new Scanner(socked.getInputStream());
        System.out.println("Enter the filename you wish to upload:");
        fname = sc.nextLine();
        File f = new File(fname);

        DataOutputStream DoutputS = new DataOutputStream(socked.getOutputStream());
        FileInputStream FinputS = new FileInputStream(fname);

        long fileSize = 0;
        fileSize = f.length();

        String tempPro = "";
        tempPro = fname + "," + fileSize;

        DoutputS.writeUTF(fname + "," + Long.toString(fileSize));
        /*PrintWriter pw = new PrintWriter(
                new BufferedWriter(new OutputStreamWriter(socked.getOutputStream(), "UTF-8")),true);
        pw.write(tempPro);*/

        byte[] buffer = new byte[(int)f.length()]; // was 4096

        while (FinputS.read(buffer) > 0){
            DoutputS.write(buffer);
        }
        System.out.println("File sent. Check Directory\n");
        //pw.flush();
        //FinputS.close();
        //DoutputS.close();
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