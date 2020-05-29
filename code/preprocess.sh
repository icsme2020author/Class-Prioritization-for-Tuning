for r in {1..10};
 do
        for p in freemind db-everywhere saxpath;
                 do



                                if [ -f ./1/$p/1/rep1/statistics.csv ];
                                 then awk -F "\"*,\"*" 'FNR == 2 {print $1}' ./$
                                elif [ -f ./results2/1/$p/1/rep1/statistics.csv$
                                 then awk -F "\"*,\"*" 'FNR == 2 {print $1}' ./$
                                else printf ' ';
                                fi | awk -F, 'ORS=FS' >> file$r.csv;

                 done
                printf '\n' >> file$r.csv
        for j in {1..1200};
         do
                for p in freemind db-everywhere saxpath;
                    do
                                if [ -f ./$j/$p/1/rep$r/statistics.csv ];
                                 then awk -F "\"*,\"*" 'FNR == 2 {print $5}' ./$
                                elif [ -f ./results2/$j/$p/1/rep$r/statistics.c$
                                 then awk -F "\"*,\"*" 'FNR == 2 {print $5}' ./$
                                else printf ' ';
                                fi | awk -F, 'ORS=FS' >> file$r.csv;
                    done
                printf '\n' >> file$r.csv
         done
 done
