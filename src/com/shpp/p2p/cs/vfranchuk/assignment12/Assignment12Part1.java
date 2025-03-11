package com.shpp.p2p.cs.vfranchuk.assignment12;

public class Assignment12Part1 {

    public static void main(String[] args) {

        Programm programm = new Programm();
        if (args.length > 0) {
            programm.findSilhouettes(args[0]);
        } else {
            programm.findSilhouettes("assets/test.jpg");
        }
        programm.initFrame();

    }
}