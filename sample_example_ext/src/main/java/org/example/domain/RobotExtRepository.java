package org.example.domain;

public class RobotExtRepository extends RobotRepository {

    @Override
    public String getVer() {
        return "ext";
    }

    public boolean isExt() {
        return true;
    }

}
