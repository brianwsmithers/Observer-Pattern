package com.example.observationpattern.nopattern;


import java.util.Scanner;

/**
 * Author: Brian Smithers <br>
 * Class: Main <br>
 * Description: This class is used to demonstrate the hard coded version of
 * the observer design pattern. The class instantiates two Subscriber objects
 * on creation of a Channel object. The user is then able to enter new uploads
 * into a scanner that are sent to the subscriber objects.
 */
public class Main {
    public static void main(String[] args) {
        Subscriber brian = new Subscriber("Brian");
        Subscriber tom = new Subscriber("Tom");

        Channel channel = new Channel("Coding Class 101");

        Scanner scanner = new Scanner(System.in);

        boolean flag = true;
        while (flag) {
            System.out.print("Enter a command: ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("-1")) {
                flag = false;
            }
            else if (input.equalsIgnoreCase("brian subscribe")) {
                channel.subscribe(brian);
            }
            else if (input.equalsIgnoreCase("brian unsubscribe")) {
                channel.unSubscribe(brian);
            }
            else if (input.equalsIgnoreCase("tom subscribe")) {
                channel.subscribe(tom);
            }
            else if (input.equalsIgnoreCase("tom unsubscribe")) {
                channel.unSubscribe(tom);
            }
            else if (input.equalsIgnoreCase("help")) {
                System.out.println("brian subscribe");
                System.out.println("brian unsubscribe");
                System.out.println("tom subscribe");
                System.out.println("tom unsubscribe");
                System.out.println("upload");
            }
            else if (input.equalsIgnoreCase("upload")) {
                System.out.print("Enter an upload: ");
                channel.upload(scanner.nextLine());
            }
        }
    }
}
