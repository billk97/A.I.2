import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

public class Id3Data {
    PrepereID3Data prepData = new PrepereID3Data();//all the data is prepered and implemented in the Main Table
    int[][] NewTable;
    int SpamExist=0;
    int HamExist=0;
    int MailExist=0;
    int MailNotExist=0;
    int SpamNotExist=0;
    int HamNotExist=0;
    /**this function inputs all the data from the txt
     * into a HashMap adds the to a table shorts them and prints them
     * **/
    public void Initializer() throws FileNotFoundException {
        prepData.initializeData();
        Ckeck2DTableSH();
        AddIgToTable();
        SortIgTable();
        PrintIgTable();
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
    private void ID3CheckWords(String path) throws FileNotFoundException {
        String localPath = path + Integer.toString(10);
        File dir = new File(localPath);
        double WordExist = 0.0;
        double WordNotExist = 0.0;
        for (File file : dir.listFiles()) {
            prepData.ReadMail(file);
            if(prepData.tempSet.contains(prepData.words[0][0]))
            {
                WordExist = CalculateSpam(prepData.words[0][1],1);
            }
            else
            {
                WordNotExist = CalculateSpam(prepData.words[0][1],1);
            }
            if(WordExist>WordNotExist)
            {
                NewTable(MailExist,1);
            }
            else if(WordExist>WordNotExist)
            {
                NewTable(MailNotExist,0);
            }
        }
    }
    private  void NewTable(int newNumber,int k)
    {
        NewTable = new int[newNumber][prepData.words.length];
        for(int i=0; i<prepData.MainTable.length; i++)
        {
            if(prepData.MainTable[i][(int)prepData.words[0][0]]==k)
            {
                for (int j=0; j<prepData.MainTable[0].length; j++)
                {
                    NewTable[i][j]=prepData.MainTable[i][j];
                }
            }
        }
    }
    private double CalculateSpam (double word , int SpamOrHam)
    {

        for(int i= 0; i<prepData.MainTable.length; i++)
        {
            if(prepData.MainTable[i][(int)word]==SpamOrHam)
            {
                MailExist++;
            }
            if(prepData.MainTable[i][(int)word]==SpamOrHam&&prepData.MainTable[i][prepData.MainTable.length-1]==1)
            {
                HamExist++;
            }
            else if(prepData.MainTable[i][(int)word]==SpamOrHam&&prepData.MainTable[i][prepData.MainTable.length-1]==0)
            {
                SpamExist++;
            }
        }
        double SpamProbability= SpamExist/MailExist;
        double HamProbability= 1- SpamProbability;
        return  compaire(SpamProbability,HamProbability);
    }


    private double compaire(double value1, double value2)
    {
        double max=value1;
        if(max<value2)
        {
            max=value2;
        }
        return max;
    }

    private void ID3Algorithm(String path) throws FileNotFoundException {
        String localPath = path + Integer.toString(10);
        File dir = new File(localPath);
        for (File file : dir.listFiles()) {
            prepData.ReadMail(file);
            for (int i = 0; i < prepData.words.length; i++) {
                int foundcounter = 0;
                int nonfoundcounter = 0;
                int spamcounter = 0;
                int hamcounter = 0;
                if (prepData.tempSet.contains(prepData.words[i][0])) {
                    for (int j = 0; j < prepData.MainTable.length; j++) {
                        if (prepData.MainTable[j][i] == 1) {
                            foundcounter++;
                            if (prepData.MainTable[j][prepData.MainTable[0].length - 1] == 0) {
                                spamcounter++;
                            } else {
                                hamcounter++;
                            }//end if
                        }//end //if
                    }//end for
                } else {
                    for (int j = 0; j < prepData.MainTable.length; j++) {
                        if (prepData.MainTable[j][i] == 0) {
                            nonfoundcounter++;
                            if (prepData.MainTable[j][prepData.MainTable[0].length - 1] == 0) {
                                spamcounter++;
                            } else {
                                hamcounter++;
                            }
                        }//end if 1
                    }//end for3
                }// end else1
            }//end for 2
        }//end for1
    }// end ID3Algorithm
}//end class
