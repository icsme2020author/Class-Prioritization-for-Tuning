# Class-Prioritization-for-Hyper-parameter-Tuning
This is a replication package provided as an online appendix for the paper submitted in ICSME 2020.

# How to reproduce the experiments?
In short, in order to reproduce the results, one can run EvoSuite on the classes that we experimented configuring the hyperparameters. The way the hyperparameters are configured are discribed in the EvoSuite Documentation available at http://www.evosuite.org/documentation/tutorial-part-1/.

So, we run the following command over and over around 12 million times with different classes, different projects and different configurations:

> java -jar evosuite-1.0.6.jar -target A.jar -class B -Dcrossover_rate=X -Dpopulation=X -Dselection_function=X    
>      -Dmutation_rate=X -Delite=X -Dparent_check=X

however, in our case, as the experiment size is huge, and it takes around 10 years to rerun these experiments, we had to write some scripts to help us speed up the experiments using parallel computing.

# How did we run these experiments in parallel?

We used 3 clusters each provided us with many instances of up to 48 CPU nodes. We defined each task as indicated in the task folder. But this wasn't enough. We had to made the tasks in parralel more. So we used GNU parallel tool to help us both make tiny tasks parallel and keep track of them easily. GNU Parallel tool is a bash tool and more information about it is available at https://www.gnu.org/software/parallel/.

The script that we wrote for our experiment using parallel is also available in the folder.Therefore, if you want to replicate our experiments you can simply run **`sbatch task.sh $config`**.

# What is included in the Results folder?
The results are all orgainzed in two excel files: AllData and AllResults.
AllData includes the median number of covered branches of all studied classes.
AllResults includes the summary of data like some statistical numbers.

# How to tune?
First, we have to rank the classes, then we use Meta-GA to tune the classes. We use the Meta-GA method here in two ways globally and in class-level. Both cases are included in the code folders, and you can see the differences in implementations there.

# How did we calculate the top features?
For all 100 train-test splits, we used RFE method to find the top X features, then we reported the two most repeated features in all test splits.

# Top features
- Top 2 featues: ['rfc' 'else']
- Top 5 featues: ['else' 'rfc' 'Abstract Class Count' 'Difficulty' 'comparisonsQty']
- Top 10 featues: ['Difficulty' 'rfc' 'Abstract Class Count' 'else' 'anonymousClassesQty'
 'comparisonsQty' 'Effort' 'class' 'do' 'stringLiteralsQty']
- Top 20 featues: ['Abstract Class Count' 'rfc' 'Difficulty' 'else' 'comparisonsQty'
 'Effort' 'anonymousClassesQty' 'variablesQty' 'mathOperationsQty'
 'stringLiteralsQty' 'class' 'parenthesizedExpsQty' 'Vocabulary'
 'assignmentsQty' 'do' 'if' 'maxNestedBlocks' 'throws' 'import'
 'numbersQty']
- Top 40 featues: ['rfc' 'Effort' 'else' 'Abstract Class Count' 'variablesQty'
 'mathOperationsQty' 'Difficulty' 'comparisonsQty' 'anonymousClassesQty'
 'parenthesizedExpsQty' 'stringLiteralsQty' 'assignmentsQty' 'if'
 'Vocabulary' 'nosi' 'maxNestedBlocks' 'numbersQty' 'Ce' 'Volume' 'class'
 'loc' 'wmc' 'interface' nan 'do' 'import' 'void' 'Program Length'
 'instanceof' 'throws' 'static' 'break' 'float' 'case' 'byte' 'double'
 'switch' 'new' 'short' 'totalMethods']
