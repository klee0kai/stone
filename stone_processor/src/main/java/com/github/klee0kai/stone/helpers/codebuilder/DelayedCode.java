package com.github.klee0kai.stone.helpers.codebuilder;

public interface DelayedCode extends ISmartCode {

    SmartCode apply(SmartCode builder);

}
