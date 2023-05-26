package com.github.klee0kai.stone.model.annotations;

import java.lang.annotation.Annotation;

public interface IAnnotation {

    Class<? extends Annotation> originalAnn();

}
