#!/bin/bash

#SBATCH --nodes=1
#SBATCH --ntasks-per-node=32
#SBATCH --time=12:00:00
#SBATCH --mem=125G

# Set up the Java environment
module load java/1.8.0_192

echo "Starting run at: `date`"

javac AddMoreBudget.java
parallel --jobs 8 --delay 5 java AddMoreBudget {1} 0 {2} $1 ::: $(seq 0 11) ::: $(seq 1 24)

echo "Program glost_launch finished with exit code $? at: `date`"
