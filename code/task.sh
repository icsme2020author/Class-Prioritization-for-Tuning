#!/bin/bash

#SBATCH --nodes=1
#SBATCH --ntasks-per-node=32
#SBATCH --time=3:00:00
#SBATCH --mem=125G

# Set up the Java environment
module load java/1.8.0_192

echo "Starting run at: `date`"

let "start = $1 *20"
let "end = $1 *20 +19"
parallel --jobs 8 --delay 5 java -jar GLOST.jar {1} 0 {2} {3} ::: $(seq 0 3) ::$

echo "Program glost_launch finished with exit code $? at: `date`"
