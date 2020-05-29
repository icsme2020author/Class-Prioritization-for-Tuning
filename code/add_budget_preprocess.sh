num_of_classes="$(awk 'END {print NR}' ./classlist2_old.csv)";
for c in $(seq 1 $num_of_classes);
        do
                project="$(awk -v var="${c}"  -F "\"*,\"*" 'FNR == var {print $1}' ./classlist2_old.csv)";
                class="$(awk -v var="${c}"  -F "\"*,\"*" 'FNR == var {print $6}' ./classlist2_old.csv)";
                for r in {1..3};
                 do
                         for j in {1..24};
                                 do
                                        num_of_lines="$(awk 'END {print NR}' ./more_budget/$j/$project/rep$r/statistics.csv)";

                                         for l in $(seq 2 $num_of_lines);
                                         do

                                                 awk -v var="${l}" -v var2="${class}" -v var3="${j}"  -F "\"*,\"*" 'BEGIN{OFS=",";} FNR == var && $1 == var2  {printf  "%d, %d\n", var3, $5}' ./more_budget$

                                         done


                                done
                 done

         done
