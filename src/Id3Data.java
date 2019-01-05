import java.io.File;
import java.io.FileNotFoundException;

public class Id3Data {
    PrepereID3Data prepData = new PrepereID3Data();//all the data is prepered and implemented in the Main Table
    int[][] NewTable;
    int[][] LocalTable;
    int SpamExist = 0;
    int HamExist = 0;
    int MailExist = 0;
    int MailNotExist = 0;
    int SpamNotExist = 0;
    int HamNotExist = 0;
    int TotalMails = 0;
    int SpamCounter=0;
    int HammCounter=0;
    /**
     * this function inputs all the data from the txt
     * into a HashMap adds the to a table shorts them and prints them
     **/
    public void Initializer() throws FileNotFoundException {
        prepData.initializeData();
        Ckeck2DTableSH();
        AddIgToTable(prepData.MainTable);
        SortIgTable(2);
        ID3Result(prepData.getPath());
        //  PrintIgTable();

    }

    public void PrintIgTable() {
        for (int i = 0; i < prepData.words.length; i++) {
            System.out.print("word lexi: " + prepData.words[i][0]);
            System.out.print(" word id: " + prepData.words[i][1]);
            System.out.print(" word Ig: " + prepData.words[i][2] + "\n");
        }
        System.out.println(prepData.words.length);
    }

    /**in order to take the number of rows
     * MainTable.length =3304 = number of emails
     * MainTable[0].length =16693 = number of words
     * MainTable[i][MainTable[0].length-1] = 0 or 1**/
    /**
     * checks the 2D table last column if its spam or ham
     **/
    private int Ckeck2DTableSH() {
        int Spam = 0;
        int Ham = 0;
        for (int i = 0; i < prepData.MainTable.length; i++) {
            if (prepData.MainTable[i][prepData.MainTable[0].length - 1] == 1) {
                Ham++;
            } else {
                Spam++;
            }
        }
        return Spam;
    }//end Check2DTable

    /**
     * the actual implementation of the id3 algorithm
     **/
    private void ID3Result(String path) throws FileNotFoundException {
        String localPath = path + Integer.toString(10);
        File dir = new File(localPath);

        int counter = 0;
        int Mails = 0;
        for (File file : dir.listFiles()) {
            System.out.println("mail: " + file);
            Mails++;
            NewTable=new int [prepData.MainTable.length][prepData.MainTable[0].length];
            CopyTable(NewTable,prepData.MainTable);
            AddIgToTable(prepData.MainTable);
            SortIgTable(2);
            prepData.ReadMail(file);//ftiaxnw to hash set gia kathe mail
            double DecisionTable[] =new double[]{0.0,0.0};
            if (prepData.checkSpam(file)) {
                counter++;
            }
            while (Decision(DecisionTable,file))
            {
               // System.out.println("word: " + prepData.words[0][0] + " thesi: " + prepData.words[0][1] + " ig: " + prepData.words[0][2]); //leksi me megalitero ig

                if (prepData.tempSet.contains((int) prepData.words[0][0])) {
                    LocalTable = new int[CountNewTableBounds(prepData.words[0][1], NewTable, 1)][prepData.words.length];
                    CopyTable(LocalTable, NewTable, prepData.words[0][1], 1);
                    NewTable = new int[LocalTable.length][LocalTable[0].length];
                    CopyTable(NewTable, LocalTable);
                    DecisionTable=CalculateSpam(prepData.words[0][1],1,NewTable);

                    AddIgToTable(NewTable);
                    SortIgTable(2);
                } else {
                    LocalTable = new int[CountNewTableBounds(prepData.words[0][1], NewTable, 0)][prepData.words.length];
                    CopyTable(LocalTable, NewTable, prepData.words[0][1], 0);
                    NewTable = new int[LocalTable.length][LocalTable[0].length];
                    CopyTable(NewTable, LocalTable);
                    DecisionTable=CalculateSpam(prepData.words[0][1],0,NewTable);

                    AddIgToTable(NewTable);
                    SortIgTable(2);
                }
            }

        }//end for
        System.out.println("Mails: " + Mails);
        System.out.println("Spam: " + counter);
        System.out.println("Ham: " + (Mails-counter));
        System.out.println("PredictedSpa: " + SpamCounter);
        System.out.println("PredictedHam: "+ HammCounter);
        System.out.println("Accuracy: " +((double)(SpamCounter+HammCounter)/(double)Mails*100.0));

    }//end class

    private void CopyTable(int LTable[][], int NTable[][]) {
        for (int i = 0; i < LTable.length; i++) {
            for (int j = 0; j < LTable[0].length; j++) {

                LTable[i][j] = NTable[i][j];
            }
        }
    }//end CopyTable

    private void CopyTable(int LTable[][], int NTable[][], double wordId, int WordExistsOrNot) {
        int k = 0;

        for (int i = 0; i < NTable.length; i++) {
            if (NTable[i][(int)wordId] == WordExistsOrNot) {

                for (int j = 0; j < NTable[0].length; j++) {
                    LTable[k][j] = NTable[i][j];

                }
                k++;
            }
        }
    }//end CopyTable

    /**
     * Calculates the spamProbability and ham  of the word with the most ig and takes a decision baste on the probabilities
     **/
    private boolean Decision(double tempTable [],File dir) throws FileNotFoundException {
        if (tempTable[0] > 0.7) {
            System.out.println(">>>>>>>>>>>>>Spam: " + "\n");
            if(prepData.checkSpam(dir))
            {
                SpamCounter++;
            }
            return false;
        } else if (tempTable[1] > 0.7) {
            System.out.println(">>>>>>>>>>>>>Ham: " + "\n");
            if(!prepData.checkSpam(dir))
            {
                HammCounter++;
            }
            return false;
        }
        return true;
    }// end regration

    /**
     * fills the new table depending on if the word exist or not int the table
     **/
    private int[][] FillNewTable(int newNumber, int k, int table[][]) {
        NewTable = new int[newNumber][prepData.words.length];
        for (int i = 0; i < table.length; i++) {
            if (table[i][(int) prepData.words[0][1]] == k) {
                for (int j = 0; j < table[0].length; j++) {
                    NewTable[i][j] = table[i][j];
                }
            }
        }
        return NewTable;
    }//end FillNewTable

    /**
     * fills the new table with Main Tab;e
     **/
    private int[][] FillInitialTable() {
        int[][] NewTable = new int[prepData.MainTable.length][prepData.MainTable[0].length];
        for (int i = 0; i < prepData.MainTable.length; i++) {
            for (int j = 0; j < prepData.MainTable[0].length; j++) {

                NewTable[i][j] = prepData.MainTable[i][j];
            }
        }
        return NewTable;
    }//end FillNewTable

    private int CountNewTableBounds(double wordId, int table[][], int WordExistOrNot) {
        int loCounter = 0;

        for (int i = 0; i < table.length; i++) {
            if (table[i][(int) wordId] == WordExistOrNot) {
                loCounter++;
            }
        }
        return loCounter;
    }

    /**
     * calculates the spamProbability and ham of the given word based on the training data
     **/
    private double[] CalculateSpam(double word, int WordExist, int table[][]) {
        MailExist = 0;
        HamExist = 0;
        SpamExist = 0;
        TotalMails = 0;
        double tempTable[] = new double[2];
        for (int i = 0; i < table.length; i++) {
            if (table[i][(int) word] == WordExist) {
                MailExist++;
                if (table[i][table[0].length - 1] == 1) {
                    HamExist++;
                } else if (table[i][table[0].length - 1] == 0) {
                    SpamExist++;
                }
            }
            TotalMails++;
        }
        double SpamProbability = (double) SpamExist / (double) MailExist;
        double HamProbability = 1 - SpamProbability;
        tempTable[0] = SpamProbability;
        tempTable[1] = HamProbability;
      /*  System.out.println("(0 the word does not exist 1 it exists)");
        System.out.println("WordExist: " + WordExist);
        System.out.println("TotalMails: " + TotalMails);
        System.out.println("MailExist: " + MailExist);
        System.out.println("SpamExist: " + SpamExist);
        System.out.println("HamExidst: " + HamExist);
        System.out.println("SpamPro: "+ tempTable[0]);
        System.out.println("HamPro: "+ tempTable[1]);*/
        return tempTable;
    }//end CalculateSpam

    /**
     * compare to values and returns the biggest
     **/
    private double compare(double value1, double value2) {
        double max = value1;
        if (max < value2) {
            max = value2;
        }
        return max;
    }//end compare

    /**
     * lexi = the words id || Exist = 1 or 0 if the word exist or not || Category 1 or 0 for spam or ham || NewTable the table
     * counts for each mail if the mail exist and if exists is its spam or ham
     **/
    private double WordProbapility(int lexi, int Exist, int Category, int NewTable[][]) {
        double WordExists = 0.0;
        double CategoryPerWord = 0.0;
        for (int i = 0; i < NewTable.length; i++) {
            if (NewTable[i][lexi] == Exist && NewTable[i][NewTable[0].length - 1] == Category) {
                CategoryPerWord++;
            }
            if (NewTable[i][lexi] == Exist) {
                WordExists++;
            }
        }
        if(WordExists==0.0)
        {
            WordExists=1;
        }
        return CategoryPerWord / WordExists;//P(C=1/x1)
    }//end WordProbability

    /**
     * calculates the information gain for the given table
     **/
    private double IG(int lexi, int NewTable[][]) {
        Calculate cal = new Calculate();
        cal.setSpamCounter(getNewTableSpamCounter(NewTable));
        cal.setMailCounter(getNewTableMailCounter(NewTable));
        double p1 = WordProbapility(lexi, 0, 0, NewTable) * cal.log2(1 + WordProbapility(lexi, 0, 0, NewTable));
        double p2 = WordProbapility(lexi, 0, 1, NewTable) * cal.log2(1 + WordProbapility(lexi, 0, 1, NewTable));
        double p3 = WordProbapility(lexi, 1, 0, NewTable) * cal.log2(1 + WordProbapility(lexi, 1, 0, NewTable));
        double p4 = WordProbapility(lexi, 1, 1, NewTable) * cal.log2(1 + WordProbapility(lexi, 1, 1, NewTable));
        return cal.TotalEntropy() + (p1 + p2 + p3 + p4);
    }// end IG

    /**
     * Adds the calculated Ig to the words table
     **/
    private void AddIgToTable(int NewTable[][]) {
        SortIgTable(1);
        for (int i = 0; i < NewTable[0].length - 1; i++) {
            prepData.words[i][2] = IG(i, NewTable);
        }
    }//end AddIgToTable

    /**
     * Sorts the table words
     **/
    private void SortIgTable(int column) {
        Sort2Table sort = new Sort2Table();
        sort.bubbleSort(prepData.words,column);
    }

    /**
     * returns the number of spam in the given table
     **/
    private double getNewTableSpamCounter(int NewTable[][]) {
        int Counter = 0;
        for (int i = 0; i < NewTable.length; i++) {
            if (NewTable[i][NewTable[0].length - 1] == 0) {
                Counter++;
            }
        }
        return Counter;
    }//end getNewTableSpamCounter

    /**
     * return  the total amount of mails in the table
     **/
    private double getNewTableMailCounter(int NewTable[][]) {
        int Counter = 0;
        for (int i = 0; i < NewTable.length; i++) {
            Counter++;
        }
        return Counter;
    }//end getNewTableMailCounter



}//end class
