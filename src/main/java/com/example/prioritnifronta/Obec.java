package com.example.prioritnifronta;


import java.io.Serializable;

public class Obec implements Serializable, Comparable<Obec>{
    private final int psc;
    private final String obec;
    private int pocetMuzu;
    private int pocetZen;
    private int pocetCelkem;
    private static boolean jePrioritaNaPocet = true;

    public Obec(int psc, String obec, int pocetMuzu, int pocetZen) {
        this.psc = psc;
        this.obec = obec;
        this.pocetMuzu = pocetMuzu;
        this.pocetZen = pocetZen;
        this.pocetCelkem = pocetMuzu + pocetZen;
    }

    public int getPsc() {
        return psc;
    }

    public String getObec() {
        return obec;
    }


    public int getPocetMuzu() {
        return pocetMuzu;
    }

    public int getPocetZen() {
        return pocetZen;
    }

    public int getPocetCelkem() {
        return pocetCelkem;
    }

    public void setPocetMuzu(int pocetMuzu) {
        this.pocetMuzu = pocetMuzu;
        pocetCelkem = pocetMuzu + pocetZen;
    }

    public void setPocetZen(int pocetZen) {
        this.pocetZen = pocetZen;
        pocetCelkem = pocetMuzu + pocetZen;
    }

    @Override
    public String toString() {
        return "Obec{" + "PSČ = " + psc +
                ", obec = " + obec +
                ", počet mužů = " + pocetMuzu +
                ", počet žen = " + pocetZen +
                ", počet celkem = " + pocetCelkem +
                '}';
    }

    public static void zmenPrioritu(){
        jePrioritaNaPocet = !jePrioritaNaPocet;
    }

    @Override
    public int compareTo(Obec o) {
        if(jePrioritaNaPocet){
        return Integer.compare(this.getPocetCelkem(), o.getPocetCelkem());
        }else {
            return this.obec.compareTo(o.obec);
        }
    }
}
