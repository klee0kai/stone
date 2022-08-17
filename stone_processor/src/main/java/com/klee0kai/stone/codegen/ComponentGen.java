package com.klee0kai.stone.codegen;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;

public class ComponentGen {

    public ProcessingEnvironment env;
    public Messager messager;

    public void init(ProcessingEnvironment env) {
        this.env = env;
        messager = env.getMessager();
    }

    public void genCode() {

    }

}
