package model;

public class Tester extends Employee {

    public Tester(
            int id,
            String name,
            String department,
            double salary) {

        super(id, name, department, salary);
    }

    @Override
    public void work() {
        System.out.println(
                getName() + " is testing the application."
        );
    }
}