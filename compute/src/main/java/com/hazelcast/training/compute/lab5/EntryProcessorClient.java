package com.hazelcast.training.compute.lab5;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.map.AbstractEntryProcessor;

import java.util.Map;

public class EntryProcessorClient {
    public static void main(String[] args) {
        HazelcastInstance hz = HazelcastClient.newHazelcastClient();
        IMap<String, Employee> employees = hz.getMap("employees");
        employees.put("John", new Employee(1000));
        employees.put("Mark", new Employee(1000));
        employees.put("Spencer", new Employee(1000));

        Map<String, Object> stringObjectMap = employees.executeOnEntries(new EmployeeRaiseEntryProcessor());

        for (Map.Entry<String, Employee> entry : employees.entrySet()) {
            System.out.println(entry.getKey() + " salary: " + entry.getValue().getSalary());
        }
        System.exit(0);
    }

    static class EmployeeRaiseEntryProcessor extends AbstractEntryProcessor<String, Employee> {
        @Override
        public Object process(Map.Entry<String, Employee> entry) {
            Employee value = entry.getValue();
            value.incSalary(10);
            entry.setValue(value);
            System.out.println("Processing : " + entry.getKey() + " salary: " + entry.getValue().getSalary());
            return null;
        }
    }

}
