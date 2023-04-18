package com.github.klee0kai.stone.test_feature.hr.department;

import com.github.klee0kai.stone.test_feature.hr.model.DepartmentInfo;
import com.github.klee0kai.stone.test_feature.hr.model.EmployeeInfo;

import java.util.List;
import java.util.UUID;

public class DepartmentImpl implements Department {

    public String uuid = UUID.randomUUID().toString();

    @Override
    public String getId() {
        return uuid;
    }

    @Override
    public DepartmentInfo getInfo() {
        return null;
    }

    @Override
    public List<EmployeeInfo> employers() {
        return null;
    }

    @Override
    public void hireEmployee(EmployeeInfo employeeInfo) {

    }

    @Override
    public void fireEmployee(EmployeeInfo employeeInfo) {

    }
}
