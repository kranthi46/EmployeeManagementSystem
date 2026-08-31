package model;

public class Manager extends Employee {

    public Manager(
            int id,
            String name,
            String department,
            double salary) {

        super(id, name, department, salary);
    }

    @Override
    public void work() {
        System.out.println(
                getName() + " is managing the team."
        );
    }
}