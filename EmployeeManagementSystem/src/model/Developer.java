package model;

public class Developer extends Employee {

    public Developer(
            int id,
            String name,
            String department,
            double salary) {

        super(id, name, department, salary);
    }

    @Override
    public void work() {
        System.out.println(
                getName() + " is writing code."
        );
    }
}