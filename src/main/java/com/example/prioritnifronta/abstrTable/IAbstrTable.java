package com.example.prioritnifronta.abstrTable;

import com.example.prioritnifronta.enumy.eTypProhl;

import java.util.Iterator;

public interface IAbstrTable<K extends Comparable<K> ,V>  {
    void zrus();
    boolean jePrazdny();
    V najdi(K key) throws AbstrTableException;
    void vloz(K key, V value) throws AbstrTableException;
    V odeber(K key) throws AbstrTableException;
    Iterator<V> vytvorIterator(eTypProhl typ);
    int getPocet();
}
