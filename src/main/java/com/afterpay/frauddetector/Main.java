package com.afterpay.frauddetector;

public class Main {
    public static void main(String [] args){
        if (args.length != 2) {
            System.err.println("Invalid number of arguments. usage java -jar fraud-detector-0.1.0.jar threshold filename.csv ");                
            System.exit(1);
        }
    }    
}
