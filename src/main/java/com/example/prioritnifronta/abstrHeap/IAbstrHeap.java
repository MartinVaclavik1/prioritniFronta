package com.example.prioritnifronta.abstrHeap;

import com.example.prioritnifronta.Obec;

import java.util.Iterator;
import java.util.List;

public interface IAbstrHeap<O extends Comparable<O>> {
    void vybuduj(List <O> obce) throws AbstrHeapException; //-vybuduje požadovanou prioritní frontu, vstupní parametr pole obcí

    void reorganizace() throws AbstrHeapException; // - přebuduje prioritní frontu dle požadované priority

    void zrus();//: - zrušení prioritní fronty,

    boolean jePrazdny();//: - test naplněnosti prioritní fronty,

    void vloz(O obec) throws AbstrHeapException; //: - vložení prvku do prioritní fronty,

    O odeberMax() throws AbstrHeapException;//: - odebraní prvku z prioritní fronty s maximální prioritou,

    O zpristupniMax() throws AbstrHeapException;//: - zpřístupnění prvku z prioritní fronty s maximální prioritou,

    Iterator<O> vytvorIterator(eTypProhl typ);//: - vypíše prvky prioritní fronty (využívá iterátor do šířky i do hloubky).
}
