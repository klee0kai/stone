package com.github.klee0kai.stone.closed.types.single;

/**
 * Stone Private class
 */
public class StrongItemHolder<T> extends SingleItemHolder<T>{

    public T set(T ob){
        setStrong(ob);
        return ob;
    }

    public void defRef(){
        strong();
    }

}
