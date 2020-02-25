import java.io.*;
import java.net.InetAddress;
import java.sql.SQLOutput;
import java.util.Scanner;
import java.lang.*;
import java.net.Socket;

/**
 * A command line client for the date server. Requires the IP address of the
 * server as the sole argument. Exits after printing the response.
 */
public class client {

    public static void main(String[] args) throws IOException
    {

        boolean flag = false;

        Scanner sc  = new Scanner(System.in);
        while(!flag)
        {
            System.out.println("Welcome to Jeff. Please enter 1 to upload a file and 2 to download a file. Enter 0 to quit.");
            int input = sc.nextInt();
            if(input == 1)
            {

            }
            else if(input == 2)
            {

            }
            else
            {
                flag = false;
            }
        }

        var socket = new Socket(InetAddress.getLocalHost(), 59090);
        var in = new Scanner(socket.getInputStream());
        System.out.println("Server response: " + in.nextLine());
        BufferedInputStream bufferedIS = null;

        //Declaration with try catch for the input and output streams of the socket called socket.
        DataOutputStream output = null;
        try
        {
            output = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        DataInputStream input = null;
        try
        {
            input = new DataInputStream(socket.getInputStream());
        }
        catch (IOException e)
        {
            System.out.println(e);
        }

        File file = new File("DISGOSTANG.jpg");
        int count;
        byte[] buffer = new byte[(int)file.length()]; // or 4096, or more

        bufferedIS = new BufferedInputStream(input);
        bufferedIS.read(buffer,0,buffer.length);

        System.out.println("Sending File:");
        output.write(buffer,0,buffer.length);
        output.flush();


    }

}