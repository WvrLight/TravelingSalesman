public class GeneticTSPSolver {
    final int MAX_GENERATIONS = 10000;
    final int VECTOR_SIZE = 15;
    static final int MUTATION_CHANCE = 100;

    Path fittest_parent = null;

    /*
     * Variables for crossing site to be used in crossover
     * CROSS_SIZE_MIN = the start index for cross site bounds
     * CROSS_SIZE_MAX = the end index for cross site bounds
     */
    final int CROSS_SIZE_MIN = 5;
    final int CROSS_SIZE_MAX = 9;

    double[] costPerGen = new double[MAX_GENERATIONS];

    GeneticTSPSolver() {
        // Create parent variables and generate initial parents
        Path parent1 = new Path("gen0parent1");
        Path parent2 = new Path("gen0parent2"); 
        int fitterParent = compareFitness(parent1, parent2);

        // Perform GA
        for (int gen = 0; gen < MAX_GENERATIONS; gen++) {
            // DEBUG - Print order
            // System.out.print("\n\n" + parent1.getName() + " - ");
            // for (int i = 0; i < VECTOR_SIZE; i++) {
            //     System.out.print(parent1.getPoint(i).getName() + " ");
            // }

            // System.out.print("\n" + parent2.getName() + " - ");
            // for (int i = 0; i < VECTOR_SIZE; i++) {
            //     System.out.print(parent2.getPoint(i).getName() + " ");
            // }

            // Calculate and display generation fitness
            parent1.calculateFitness();
            parent2.calculateFitness();

            // System.out.println("\n\nGeneration " + gen + " fitness:");
            // System.out.println(parent1.getName() + " - " + parent1.getTotalFitness());
            // System.out.println(parent2.getName() + " - " + parent2.getTotalFitness());

            /* 
            * Crossover
            * Method: PMX 
            * Copy the parents, then perform the crossover function
            */
            Path child1 = new Path(parent1, "gen" + gen + "child1");
            Path child2 = new Path(parent2, "gen" + gen + "child2");
            crossoverPMX(child1, child2, VECTOR_SIZE, CROSS_SIZE_MIN, CROSS_SIZE_MAX);

            // DEBUG - Print order
            // System.out.print("\n\n" + child1.getName() + " (Crossover): ");
            // for (int i = 0; i < VECTOR_SIZE; i++) {
            // System.out.print(child1.getPoint(i).getName() + " ");
            // }

            // System.out.print("\n" + child2.getName() + " (Crossover): ");
            // for (int i = 0; i < VECTOR_SIZE; i++) {
            // System.out.print(child2.getPoint(i).getName() + " ");
            // }

            /*
             * Mutation
             * Chosen Mutation Mode: SWAP MUTATION
             */
            if (swapMutation(child1, VECTOR_SIZE)) {
                System.out.print("\n" + child1.getName() + "mutated ");
                // for (int i = 0; i < VECTOR_SIZE; i++) {
                // System.out.print(child1.getPoint(i).getName() + " ");
                // }
            }
            if (swapMutation(child2, VECTOR_SIZE)) {
                System.out.print("\n" + child2.getName() + "mutated ");
                // for (int i = 0; i < VECTOR_SIZE; i++) {
                // System.out.print(child2.getPoint(i).getName() + " ");
                // }
            }

            // Re-calculate child fitness
            child1.calculateFitness();
            child2.calculateFitness();
            // System.out.println("\n" + child1.getName() + " - " + child1.getTotalFitness());
            // System.out.println(child2.getName() + " - " + child2.getTotalFitness());

            // Parent selection
            // If parents are somehow equal, parent1 will be considered the fitter parent
            fitterParent = compareFitness(parent1, parent2);
            if (fitterParent == 2) {
                if (compareFitness(child1, child2) == 2) parent1 = new Path(child2, child2.getName());
                else parent1 = new Path(child1, child1.getName());

            }
            else {
                if (compareFitness(child1, child2) == 2) parent2 = new Path(child2, child2.getName());
                else parent2 = new Path(child1, child1.getName());
            }

            // DEBUG - Display new parents
            // System.out.println("Selected parents:");
            // System.out.println(parent1.getName());
            // System.out.println(parent2.getName());

            parent1.setName("gen" + (gen + 1) + "parent1");
            parent2.setName("gen" + (gen + 1) + "parent2");

            // Get cost of generation by getting the fittest parent
            // System.out.print("gen" + gen + " Cost: ");
            if (compareFitness(parent1, parent2) == 2) {
                costPerGen[gen] = parent2.getTotalFitness();
                // System.out.println("\n" + parent2.getName()); 
                // for (int i = 0; i < VECTOR_SIZE; i++) {
                //     System.out.print(parent2.getPoint(i).getName() + " ");
                // }
            }
            else {
                costPerGen[gen] = parent1.getTotalFitness();
                // System.out.println("\n" + parent1.getName());
                // for (int i = 0; i < VECTOR_SIZE; i++) {
                //     System.out.print(parent1.getPoint(i).getName() + " ");
                // }
            }
            System.out.println("\nGen " + gen + " total fitness: " + costPerGen[gen]);
        }

        // Display fittest parent
        System.out.println("\nMost fit parent: ");
        if (compareFitness(parent1, parent2) == 2) {
            fittest_parent = parent2;
        }
        else {
            fittest_parent = parent1;
        }

        System.out.println("\n" + fittest_parent.getName());
        for (int i = 0; i < VECTOR_SIZE; i++) {
            System.out.print(fittest_parent.getPoint(i).getName() + " ");
        }
        System.out.println("\nTotal fitness: " + fittest_parent.getTotalFitness());
    }

    /*
     * Get the PMX crossover values for 2 TSP paths where:
     * first_parent - One of 2 parents
     * second_parent - One of 2 parents
     * vectorMax - Size of the vector
     * crossMin - First value in crossing site
     * crossMax - Last value in crossing site
     */

    static void crossoverPMX(Path first_parent, Path second_parent, int vectorMax, int crossMin, int crossMax) {
        Point temp = new Point("", 0, 0);

        // Swap the values in the crossover sites
        for (int i = crossMin; i <= crossMax; i++) {
            temp = first_parent.getPoint(i);
            first_parent.getList().set(i, second_parent.getPoint(i));
            second_parent.getList().set(i, temp);
        }

        // Check values in the new child
        boolean has_error = true;
        while (has_error) {
            has_error = false;

            for (int i = 0; i < vectorMax; i++) {
                // Skip crossing site
                if (i == crossMin)
                    i = crossMax;

                // Check each value if it already exists in the swapped crossing site
                else
                    for (int j = crossMin; j <= crossMax; j++) {
                        // Change value to the same index in the other child
                        // If value was changed, repeat until no more values can be changed
                        if (first_parent.getPoint(i).getName().equals(first_parent.getPoint(j).getName())) {
                            first_parent.getList().set(i, second_parent.getPoint(j));
                            has_error = true;
                        }
                        if (second_parent.getPoint(i).getName().equals(second_parent.getPoint(j).getName())) {
                            second_parent.getList().set(i, first_parent.getPoint(j));
                            has_error = true;
                        }
                    }
            }
        }
    }

    static boolean swapMutation(Path parent, int vectorMax) {
        // Roll for mutation chance
        int mutate_roll = getRandomNumber(1, 100);

        if (mutate_roll <= MUTATION_CHANCE) {
            // DEBUG - Show mutation success
            // System.out.println("\nA child has mutated!");

            Point temp = new Point("", 0, 0);

            // Generate random number in vector range
            int id1 = getRandomNumber(0, vectorMax - 1);
            int id2 = getRandomNumber(0, vectorMax - 1);

            // Swap the values
            temp = parent.getPoint(id1);
            parent.getList().set(id1, parent.getPoint(id2));
            parent.getList().set(id2, temp);

            return true;
        }
        else return false;
    }

    // Get the parent with the better fitness
    // 0 - equal
    // 1 - parent 1 is fitter
    // 2 - parent 2 is fitter
    int compareFitness(Path x, Path y) {
        double x_fit = x.getTotalFitness();
        double y_fit = y.getTotalFitness();

        if (x_fit == y_fit) return 0;
        else if (x_fit < y_fit) return 1;
        else return 2;
    }

    /* This is a general function that returns a random number in a given range 
     * The range specified by MIN and MAX is INCLUSIVE
    */
    static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min + 1)) + min);
    }

    Path getFittestParent() {
        return this.fittest_parent;
    }

    // public static void main(String[] args) {
    //     new GeneticTSPSolver();
    // }
}
