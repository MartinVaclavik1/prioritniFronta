package com.example.prioritnifronta.abstrHeap;

import com.example.prioritnifronta.Obec;

import java.util.Iterator;

public interface IAbstrHeap {
    void vybuduj(Obec[] Obce); //-vybuduje požadovanou prioritní frontu, vstupní parametr pole obcí

    void reorganizace(); // - přebuduje prioritní frontu dle požadované priority

    void zrus();//: - zrušení prioritní fronty,

    boolean jePrazdny();//: - test naplněnosti prioritní fronty,

    void vloz(int pocetObcanu, String nazev, Obec prvek); //: - vložení prvku do prioritní fronty,

    Obec odeberMax();//: - odebraní prvku z prioritní fronty s maximální prioritou,

    Obec zpristupniMax();//: - zpřístupnění prvku z prioritní fronty s maximální prioritou,

    Iterator<Obec> vytvorIterator();//: - vypíše prvky prioritní fronty (využívá iterátor do šířky i do hloubky).
}
