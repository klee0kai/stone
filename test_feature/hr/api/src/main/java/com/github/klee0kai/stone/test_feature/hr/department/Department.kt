package com.github.klee0kai.stone.test_feature.hr.department;

import com.github.klee0kai.stone.test_feature.hr.model.DepartmentInfo;
import com.github.klee0kai.stone.test_feature.hr.model.EmployeeInfo;

import java.util.List;

public interface Department {

    String getId();

    DepartmentInfo getInfo();

    List<EmployeeInfo> employers();

    void hireEmployee(EmployeeInfo employeeInfo);

    void fireEmployee(EmployeeInfo employeeInfo);

}
