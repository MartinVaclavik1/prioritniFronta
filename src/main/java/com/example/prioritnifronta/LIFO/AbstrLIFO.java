package com.example.prioritnifronta.LIFO;

import com.example.prioritnifronta.abstrDoubleList.AbstrDoubleList;
import com.example.prioritnifronta.abstrDoubleList.AbstrDoubleListException;
import com.example.prioritnifronta.abstrDoubleList.IAbstrDoubleList;

import java.util.Iterator;

public class AbstrLIFO<T> implements IAbstrLIFO<T> {
    IAbstrDoubleList<T> list;

    public AbstrLIFO() {
        this.list = new AbstrDoubleList<>();
    }


    @Override
    public void zrus() {
        list.zrus();
    }

    @Override
    public boolean jePrazdny() {
        return list.jePrazdny();
    }

    @Override
    public void vloz(T data) {
        try {
            list.vlozPrvni(data);
        } catch (AbstrDoubleListException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T odeber() {
        try {
            return list.odeberPrvni();
        } catch (AbstrDoubleListException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterator<T> vytvorIterator() {
        return list.iterator();
    }
}
