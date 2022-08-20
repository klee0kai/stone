package com.klee0kai.stone.interfaces;

public interface IComponent {

    void init(Object... modules);

    /**
     * retutn unic prefix. Generated after relateTo
     *
     * @return
     */
    String prefix();


    /**
     * relate to parent DI
     */
    void relateTo(IComponent... components);


}
