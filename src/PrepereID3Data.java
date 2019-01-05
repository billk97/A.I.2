import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

public class PrepereID3Data {
    public HashMap<Integer, Integer[]> MailHash = new HashMap<Integer, Integer[]>();//contains the vocabulary
    public HashSet<Integer> tempSet; //contains the vocabulary of each mail at the time
    public double[][] words; //[lexi,thesi,ig] just like a pointer table
    public int[][] MainTable;//contains all the mails in a vector form <0,1,0,1,0,1,0,1,0,0>
    public String path = "src\\pu_corpora_public\\pu1\\part";
    private int MailCounter;
    public int TrainingDataNumber = 8;
    public double SpamCounter ;
    public double HamCounter ;

    public String getPath() {
        return path;
    }

    public int getMailCounter() {
        return MailCounter;
    }

    public void setMailCounter(int mailCounter) {
        MailCounter = mailCounter;
    }

    public int getTrainingDataNumber() {
        return TrainingDataNumber;
    }

    public void setTrainingDataNumber(int trainingDataNumber) {
        TrainingDataNumber = trainingDataNumber;
    }

    public double getSpamCounter() {
        return SpamCounter;
    }

    public void setSpamCounter(double spamCounter) {
        SpamCounter = spamCounter;
    }

    public double getHamCounter() {
        return HamCounter;
    }

    public void setHamCounter(double hamCounter) {
        HamCounter = hamCounter;
    }

    public void initializeData() throws FileNotFoundException {
        inputToHashMap(path, TrainingDataNumber);
        Pruning();
        HashWordToTable();

    }

    /**
     * this function creates the vocabulary
     **/
    public void inputToHashMap(String path, int TrainingDataNumber) throws FileNotFoundException {
        MailCounter = 0;
        /**loops all the directories until  8**/
        for (int i = 1; i <= TrainingDataNumber; i++) {
            String localPath = path + Integer.toString(i);
            File dir = new File(localPath);
            /**lops all the files**/
            for (File file : dir.listFiles()) {
                MailCounter++;
                ReadMail(file);
                UpdateMailHash();
            }//end for2
        }//end for1

    }//end input To HashMap

    /**
     * reduces the vocabulary keeps the most important words
     **/
    private void Pruning() {
        Iterator<Integer> it = MailHash.keySet().iterator();
        while (it.hasNext()) {
            int onoma = it.next();
            if (MailHash.get(onoma)[0] < 30 || MailHash.get(onoma)[0] > 2000) {
                it.remove();
            }
        }//end while
    }

    /**
     * reads a single mail and saves it to a temporary hash set
     **/
    public void ReadMail(File file) throws FileNotFoundException {
        tempSet = new HashSet<Integer>();
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            String word = scanner.next();
            if (!word.equals("Subject:")) {
                int cell = Integer.parseInt(word);
                tempSet.add(cell);
            }
        }//end while
        scanner.close();
    }//end ReadMail

    /**this function Updates the MailHash HashMap
     * and increases the counter when an word is found in a email
     * loops all the tempSet which contains
     * unique word
     **/
    /**checks if the (word)=k that is contained in the mail
     * exists in the known words or dictionary
     * and ads an counter which means that the word is found
     * at least 1 time**/
    /**
     * if (word)=k does not exists it create on
     * which contains 1 table 1 column for the number of spam
     * 1 for the number of ham
     **/
    private void UpdateMailHash() {
        for (int k : tempSet) {
            if (MailHash.containsKey(k)) {
                MailHash.get(k)[0] += 1;
            } else {
                MailHash.put(k, new Integer[]{1});
            }
        }//end for
    }//end UpdateMailHash

    /**
     * this function keeps track of the words in the main table each
     * word has a unique number
     **/
    public void HashWordToTable() {
        int count = 0;
        words = new double[MailHash.size()][3];
        for (int lexi : MailHash.keySet()) {
            words[count][0] = lexi;
            words[count][1] = count;
            count++;
        }
        System.out.println("Mailhash: " + MailHash.size() + " count: " + count + " size of table " + words.length + " size of column " + words[0].length);

    }//end HashWordToTable


    /**
     * this function is responsible for inputting the all the mails in a single table
     * example
     * <0,0,0,0,1,1,1,0>
     * <0,0,0,0,1,1,1,0>
     **/
    public void Read(int TotalMails, int posost, String path) throws FileNotFoundException {
        System.out.println("Total Mails: " + TotalMails + " Words.length " + words.length);
        SpamCounter=0.0;
        HamCounter=0.0;
        MainTable = new int[TotalMails][words.length];
        HashSet<Integer> MailWords;
        int MailCounter = 0;
        /**loops all the directories until  8**/
        for (int i = 1; i <= posost; i++) {
            String localPath = path + Integer.toString(i);
            File dir = new File(localPath);
            /**lops all the files**/
            for (File file : dir.listFiles()) {
                MailWords = new HashSet<Integer>();
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    String word = scanner.next();//inputs every word in a local variable
                    if (!word.equals("Subject:")) {//checks if its not Subject otherwise sketchy things may happen
                        int lexi = Integer.parseInt(word);
                        MailWords.add(lexi);
                    }//end if
                }//end while
                scanner.close();
                if (checkSpam(file)) {
                    SpamCounter++;
                    InputToMainTable(MailCounter, MailWords, 0);
                } else {
                    HamCounter++;
                    InputToMainTable(MailCounter, MailWords, 1);
                }
                MailCounter++;
            }//end for2
        }//end for1
    }

    /**
     * this function inputs the data from the Hash Table to the 2 dimensional table
     **/
    public void InputToMainTable(int Mails, HashSet<Integer> TempMailWords, int SpamORham) {
        for (int i = 0; i < words.length; i++) {
            if (TempMailWords.contains((int) words[i][0])) {
                MainTable[Mails][i] = 1;
            }
        }
        MainTable[Mails][words.length - 1] = SpamORham;

    }

    /**
     * checks if the given file is spam or ham based on the Title
     **/
    public boolean checkSpam(File dir) throws FileNotFoundException {
        if (dir.getName().contains("spmsg")) {
            return true;
        }
        return false;
    }// end checkSpam

    /**
     * this function prints the main table
     **/
    private void PrintMainTable() {
        int spam=0;
        int ham=0;

        for (int i = 0; i < MainTable.length; i++) {
            for (int j = 0; j < MainTable[0].length; j++) {
                System.out.print(MainTable[i][j] + " ");
            }
            System.out.println("");

        }
    }
}// end class
