package com.shpp.p2p.cs.vfranchuk.assignment12;

public class Assignment12Part1 {

    private static Programm programm;

    public static void main(String[] args) {

        programm = new Programm();
        if (args.length > 0) {
            programm.findSilhouettes(args[0]);
        } else {
            programm.findSilhouettes("assets/test.jpg");
        }
//        programm.initFrame();

    }

    public int getSilhouettesCount(){
        return programm.getSilhouettes().getSilhouettesCount();
    }
}