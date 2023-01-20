package com.github.klee0kai.test.mowgli;

import com.github.klee0kai.test.di.bindinstance.simple_inject.SevenPlanetComponent;
import com.github.klee0kai.test.di.bindinstance.singlemethod_inject.StarSkyComponent;
import com.github.klee0kai.test.mowgli.galaxy.*;

import javax.inject.Inject;

public class MoonSky {

    @Inject
    public IStar star;

    @Inject
    public Sun sun;


    @Inject
    public IPlanet planet;

    @Inject
    public Mercury mercury;

    @Inject
    public Earth earth;


}
