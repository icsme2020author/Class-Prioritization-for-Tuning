import java.io.*;
import java.util.*;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.evosuite.Properties;
import org.evosuite.ga.Chromosome;
import org.evosuite.ga.ChromosomeFactory;
import org.evosuite.ga.FitnessFunction;
import org.evosuite.ga.metaheuristics.StandardGA;

public class MetaGA {

    public static int[] classList;

    public MetaGA() {
    }

    public static void main(String[] args) throws IOException, InterruptedException {


        // for class-level tuning
        /*
        1. get the budget and classes
        2. find the max iteration per class and set that for the stopping condition
        3. initiate the initial population with fixed values and 6 ones
        4. get fitness for all of the configs:
        4.1. search the excel file and create an arraylist of possible values
        4.2. if it is empty for that configuration go with the median value
        4.3. if it is non-empty pick a random one for that configuration
        5. evolve till getting to the max iteration
        6. get the highest fitness for each given class
        7. Do the same for each class and leave the rest with of classes with their default value
         */

        // getID not working
        double[] defaults = new double[250];
        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream("/Users/shayan/Desktop/AllData.xlsx"));

        ChromosomeFactory factory = new ChromosomeFactory() {
            public Solution getChromosome() {
                int rnd1 = new Random().nextInt(Solution.cross.length);

                int rnd2 = new Random().nextInt(Solution.pop.length);

                int rnd3 = new Random().nextInt(Solution.elite.length);

                int rnd4 = new Random().nextInt(Solution.selection_set.length);

                int rnd5 = new Random().nextInt(Solution.parent_check.length);
                int[] p = {rnd1, rnd2, rnd3, rnd4, rnd5};

                return new Solution(p);

            }


        };

        Properties.POPULATION = 6;
        Properties.ALGORITHM = org.evosuite.Properties.Algorithm.NSGAII;
        Properties.CROSSOVER_RATE = 0.5D;
        Properties.MUTATION_RATE = 0.1D;
        Properties.WRITE_INDIVIDUALS = true;
        Properties.CHROMOSOME_LENGTH = 5;
        Properties.TEST_ARCHIVE = false;
        Properties.ELITE = 2;
//
        Properties.CROSSOVER_FUNCTION = org.evosuite.Properties.CrossoverFunction.UNIFORM;
        Properties.STOPPING_CONDITION = Properties.StoppingCondition.MAXGENERATIONS;
//
//
        System.out.println(Properties.POPULATION);
        System.out.println(Properties.CROSSOVER_RATE);
        System.out.println(Properties.MUTATION_RATE);
        System.out.println(Properties.CROSSOVER_FUNCTION);
        System.out.println(Properties.STOPPING_CONDITION);
        System.out.println("write_individuals:  " + Properties.WRITE_INDIVIDUALS);


//        classList = new int[]{67,30,59,6,8,25,16,14,70,245,81,49,48,4,47,78,66,246,43,46,63,41,18,161,57,56,62,20,247,50,12,230,72,213,237,113,9,239,35,0,26,120,61,217,15,21,28,10,29,150}; //ids of classes in AllData.xlsx

        Integer[] arr = new Integer[250];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }

        File tasks = new File("./50randomclasses_classlevel.csv");
        FileWriter outputfile = new FileWriter(tasks);



        int[] BUDGET = new int[]{300,600,900,1200,1500,3000,4500};

        HashMap<Integer,ArrayList<Double>> map = new HashMap<>();

        for (int b :BUDGET){
            map.put(b,new ArrayList<>());
        }

        for (int shuffle = 0; shuffle< 25;shuffle++) {
            System.out.println("shuffle " + (shuffle + 1));
            outputfile.write("\n-----------------------\n");
            outputfile.write("shuffle " + (shuffle + 1) + ":\n");
            Collections.shuffle(Arrays.asList(arr));
            classList = new int[50];
            for (int j = 0; j < 50; j++) {
                classList[j] = arr[j];
            }

            for (int budget : BUDGET) {
//                if (budget < 1500) {
//                    //for case 2,3: randomly selection
//                    int number_of_classes = budget / 6;
//                    classList = new int[number_of_classes];
//                    for (int j = 0; j < number_of_classes; j++) {
//                        classList[j] = arr[j];
//                    }
//                } else {
//                    classList = new int[250];
//                    for (int j = 0; j < 250; j++) {
//                        classList[j] = arr[j];
//                    }
//                }
                int max_iteration_per_class = budget / classList.length / 6;
                XSSFSheet sheet = wb.getSheet("Median");
                XSSFRow row;
                XSSFCell cell;

                row = sheet.getRow(696);
                for (int cc = 0; cc < 250; cc++) {
                    cell = row.getCell(cc);
                    defaults[cc]=cell.getNumericCellValue();
                }

                double[] tuned_coverage = new double[25];
                for (int repeat = 0; repeat < 25; repeat++) {
                    for (int class_id = 0; class_id < classList.length; class_id++) {
                        StandardGA ga = new StandardGA(factory);
                        int finalClass_id = class_id;

                        FitnessFunction function = new FitnessFunction() {

                            public double getFitness(Chromosome solution) {
                                Solution s = (Solution) solution;
                                ArrayList<Double> vals = new ArrayList<>();
                                try {
                                    for (int shit = 0; shit < 10; shit++) {
                                        XSSFSheet sheet = wb.getSheetAt(shit);
                                        XSSFRow row;
                                        XSSFCell cell;

                                        row = sheet.getRow(s.getID());
                                        cell = row.getCell(classList[finalClass_id]);
                                        try {
                                            vals.add(cell.getNumericCellValue());
                                        } catch (Exception e) {

                                        }
                                    }
                                    if (vals.isEmpty()) {
                                        XSSFSheet sheet2 = wb.getSheet("Median");
                                        XSSFRow row;
                                        XSSFCell cell;
                                        row = sheet2.getRow(1201);
                                        cell = row.getCell(classList[finalClass_id]);
                                        vals.add(cell.getNumericCellValue());
                                    }


                                } catch (Exception ioe) {
                                    ioe.printStackTrace();
                                }
                                double fitness = vals.get(new Random().nextInt(vals.size()));
                                solution.addFitness(this, fitness);
                                return fitness;
                            }


                            public boolean isMaximizationFunction() {
                                return true;
                            }


                        };
                        ga.addFitnessFunction(function);


                        ga.setCrossOverFunction(new org.evosuite.ga.operators.crossover.UniformCrossOver());
                        MaxGenerationStpngCndtn s1 = new MaxGenerationStpngCndtn();
                        s1.setMaxIterations(max_iteration_per_class);

                        ga.addStoppingCondition(s1);
                        ga.initializePopulation();


                        ga.generateSolution();
                        defaults[classList[finalClass_id]] = ga.getBestIndividual().getFitness();


                        System.out.println("for " + classList[finalClass_id] + " fitness is : " + ga.getBestIndividual().getFitness());
                    }
                    double sum2 = 0;
                    for (int i = 0; i < 250; i++)
                        sum2 += defaults[i];


                    tuned_coverage[repeat] = sum2;
                    wb.close();


                }
                Arrays.sort(tuned_coverage);
                System.out.println("budget " + budget);
                outputfile.write("budget " + budget + ":\n");
                System.out.println(Arrays.toString(tuned_coverage));
                outputfile.write(Arrays.toString(tuned_coverage) + "\n");
                System.out.println(tuned_coverage[12]);
                outputfile.write("median: " + (int) tuned_coverage[12] + "\n");

                ArrayList<Double> update = map.get(budget);
                update.add(tuned_coverage[12]);
                map.put(budget, update);
            }
        }
        outputfile.write("\n-----------------------\n");
        outputfile.write("\n-----------------------\n");
        outputfile.write("\n-----------------------\n");

        for (int bb : BUDGET){
            ArrayList<Double> arrayList = map.get(bb);
            Collections.sort(arrayList);
            outputfile.write("budget " + bb + ":\n");
            outputfile.write(arrayList.toString() + "\n");
            outputfile.write("median:"+arrayList.get(12)+"\n");
            outputfile.write("\n-----------------------\n");

        }
        outputfile.close();
        }
        

    }
