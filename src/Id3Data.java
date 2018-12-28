import java.io.File;
import java.io.FileNotFoundException;

public class Id3Data {
    PrepereID3Data prepData = new PrepereID3Data();//all the data is prepered and implemented in the Main Table
    int[][] NewTable;
    int SpamExist=0;
    int HamExist=0;
    int MailExist=0;
    int MailNotExist=0;
    int SpamNotExist=0;
    int HamNotExist=0;
    int TotalMails=0;
    /**this function inputs all the data from the txt
     * into a HashMap adds the to a table shorts them and prints them
     * **/
    public void Initializer() throws FileNotFoundException {
        prepData.initializeData();
        Ckeck2DTableSH();
        AddIgToTable();
        SortIgTable();
        ID3Result(prepData.getPath());
       // PrintIgTable();
    }

    private void PrintIgTable() {
        for (int i = 0; i < prepData.words.length; i++) {
            System.out.print("word lexi: " + prepData.words[i][0] );
            System.out.print(" word id: " + prepData.words[i][1]);
            System.out.print(" word Ig: " + prepData.words[i][2]+ "\n");
        }
    }

    /**in order to take the number of rows
     * MainTable.length =3304 = number of emails
     * MainTable[0].length =16693 = number of words
     * MainTable[i][MainTable[0].length-1] = 0 or 1**/
    /**checks the 2D table last column if its spam or ham**/
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

    private double WordProbapility(int lexi, int Exist, int Category) {
        double WordExists = 0.0;
        double CategoryPerWord = 0.0;
        for (int i = 0; i < prepData.MainTable.length; i++) {
            if (prepData.MainTable[i][lexi] == Exist && prepData.MainTable[i][prepData.MainTable[0].length - 1] == Category) {
                CategoryPerWord++;
            }
            if (prepData.MainTable[i][lexi] == Exist) {
                WordExists++;
            }
        }
        return CategoryPerWord / WordExists;
    }
    private double IG(int lexi) {
        Calculate cal = new Calculate();
        cal.setSpamCounter(prepData.getSpamCounter());
        cal.setMailCounter(prepData.getMailCounter());
        double p1 = WordProbapility(lexi, 0, 0) * cal.log2(1 + WordProbapility(lexi, 0, 0));
        double p2 = WordProbapility(lexi, 0, 1) * cal.log2(1 + WordProbapility(lexi, 0, 1));
        double p3 = WordProbapility(lexi, 1, 0) * cal.log2(1 + WordProbapility(lexi, 1, 0));
        double p4 = WordProbapility(lexi, 1, 1) * cal.log2(1 + WordProbapility(lexi, 1, 1));
        return cal.TotalEntropy() + (p1 + p2 + p3 + p4);
    }


    private void AddIgToTable() {
        for (int i = 0; i < prepData.MainTable[0].length; i++) {
            prepData.words[i][2] = IG(i);
        }
    }
    private void SortIgTable() {
        Sort2Table sort = new Sort2Table();
        sort.bubbleSort(prepData.words);
    }
    private void ID3Result(String path) throws FileNotFoundException {
        String localPath = path + Integer.toString(10);
        File dir = new File(localPath);
        double ProbabilityTable[] = new double[2];
        int counter=0;
        int Mails=0;
        for (File file : dir.listFiles()) {
            Mails++;
            NewTable=prepData.MainTable;
            prepData.ReadMail(file);
            System.out.println("word: " + prepData.words[0][0]+" thesi: " + prepData.words[0][1] + " ig: "+ prepData.words[0][2]);
            if(prepData.checkSpam(file))
            {
                counter++;
            }
            if(prepData.tempSet.contains((int)prepData.words[2][0]))
            {
                ProbabilityTable = CalculateSpam(prepData.words[0][1],1,NewTable);
                System.out.println("contains SpamPro: "+ ProbabilityTable[0]+" HamPro: "+ProbabilityTable[1]);
                if(ProbabilityTable[0]>0.1)
                {
                    System.out.println("Spam: ");
                }
                else if(ProbabilityTable[1]>0.1)
                {
                    System.out.println("Ham: ");
                }
                else
                {
                    NewTable= FillNewTable(MailExist,1,NewTable);
                    AddIgToTable(NewTable);
                    SortIgTable();
                    System.out.println("word: " + prepData.words[0][0]+" thesi: " + prepData.words[0][1] + " ig: "+ prepData.words[0][2]);
                }
            }
            else
            {
                ProbabilityTable = CalculateSpam(prepData.words[0][1],0,NewTable);
                System.out.println("SpamPro: "+ ProbabilityTable[0]+" HamPro: "+ProbabilityTable[1]);
                if(ProbabilityTable[0]>0.1)
                {
                    System.out.println("Spam: ");
                }
                else if(ProbabilityTable[1]>0.1)
                {
                    System.out.println("Ham: ");
                }
                else
                {
                    NewTable= FillNewTable(TotalMails-MailExist,0,NewTable);
                    AddIgToTable(NewTable);
                    SortIgTable();
                    System.out.println("word: " + prepData.words[0][0]+" thesi: " + prepData.words[0][1] + " ig: "+ prepData.words[0][2]);
                }

            }

        }//end for
        System.out.println("Mails: "+ Mails );
        System.out.println("Spam: "+ counter);
    }//end class
    private  int [][] FillNewTable(int newNumber, int k, int table[][])
    {
        NewTable = new int[newNumber][prepData.words.length];
        for(int i=0; i<table.length; i++)
        {
            if(table[i][(int)prepData.words[0][1]]==k)
            {
                for (int j=0; j<table[0].length; j++)
                {
                    NewTable[i][j]=table[i][j];
                }
            }
        }
        return NewTable;
    }
    private double[] CalculateSpam (double word , int WordExist,int table[][])
    {
        MailExist=0;
        HamExist=0;
        SpamExist=0;
        TotalMails=0;
        double tempTable[] = new double[2];
        for(int i= 0; i<table.length; i++)
        {
            if(table[i][(int)word]==WordExist)
            {
                MailExist++;
                if(table[i][table.length-1]==1)
                {
                    HamExist++;
                }
                else if(table[i][table.length-1]==0)
                {
                    SpamExist++;
                }
            }
            TotalMails++;
        }
        double SpamProbability= (double) SpamExist/(double)MailExist;
        double HamProbability= 1- SpamProbability;
        tempTable[0] = SpamProbability;
        tempTable[1]= HamProbability;
        return  tempTable;
    }//end CalculateSpam
    /**compare to values and returns the biggest**/
    private double compare(double value1, double value2)
    {
        double max=value1;
        if(max<value2)
        {
            max=value2;
        }
        return max;
    }
    private double WordProbapility(int lexi, int Exist, int Category,int NewTable[][]) {
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
        return CategoryPerWord / WordExists;
    }
    private double IG(int lexi, int NewTable[][]) {
        Calculate cal = new Calculate();
        cal.setSpamCounter(getNewTableSpamCounter(NewTable));
        cal.setMailCounter(getNewTableMailCounter(NewTable));
        double p1 = WordProbapility(lexi, 0, 0,NewTable) * cal.log2(1 + WordProbapility(lexi, 0, 0,NewTable));
        double p2 = WordProbapility(lexi, 0, 1,NewTable) * cal.log2(1 + WordProbapility(lexi, 0, 1,NewTable));
        double p3 = WordProbapility(lexi, 1, 0,NewTable) * cal.log2(1 + WordProbapility(lexi, 1, 0,NewTable));
        double p4 = WordProbapility(lexi, 1, 1,NewTable) * cal.log2(1 + WordProbapility(lexi, 1, 1,NewTable));
        return cal.TotalEntropy() + (p1 + p2 + p3 + p4);
    }
    private double getNewTableSpamCounter(int NewTable[][])
    {
        int Counter=0;
        for(int i=0; i<NewTable.length; i++)
        {
            if(NewTable[i][NewTable[0].length-1]==0)
            {
                Counter++;
            }
        }
        return Counter;
    }
    private double getNewTableMailCounter(int NewTable[][])
    {
        int Counter=0;
        for(int i=0; i<NewTable.length; i++)
        {
                Counter++;
        }
        return Counter;
    }

    private void AddIgToTable(int NewTable[][]) {
        for (int i = 0; i < NewTable[0].length; i++) {
            prepData.words[i][2] = IG(i,NewTable);
        }
    }
}//end class
