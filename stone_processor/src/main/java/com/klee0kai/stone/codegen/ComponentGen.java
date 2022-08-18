package com.klee0kai.stone.codegen;

import com.klee0kai.stone.AnnotationProcessor;
import com.klee0kai.stone.container.ItemsWeakContainer;
import com.klee0kai.stone.interfaces.IModule;
import com.klee0kai.stone.model.ClassDetail;
import com.klee0kai.stone.utils.ClassNameUtils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Generated;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class ComponentGen {

    private ProcessingEnvironment env;
    private Messager messager;

    private final List<ClassDetail> classes = new LinkedList<>();

    public void init(ProcessingEnvironment env) {
        this.env = env;
        messager = env.getMessager();
    }

    public void addComponent(ClassDetail... classDetails) {
        this.classes.addAll(Arrays.asList(classDetails));

    }

    public void genCode() {

    }

}
