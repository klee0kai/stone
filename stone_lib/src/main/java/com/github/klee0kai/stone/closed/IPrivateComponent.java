package com.github.klee0kai.stone.closed;

import com.github.klee0kai.stone.closed.types.StoneCallback;

/**
 * hidden components interface
 */
public interface IPrivateComponent extends IComponent {

    /**
     * hidden module
     */
    IModule __hidden();

    void __eachModule(StoneCallback<IModule> callback);

}
