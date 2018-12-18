import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class BiData {
    public HashMap<Integer, Double[]> MailHash;
    public HashSet<Integer> tempSet;
    public int SpamCounter;
    public int HammCounter;
    public double TrueFalse[] = {0.0, 0.0, 0.0, 0.0};
    /**
     * this function inputs all the data from the txt
     * into a HashMap
     **/
    public void inputToHashMap(String path, int posost) throws FileNotFoundException {
        MailHash = new HashMap<Integer, Double[]>();
        SpamCounter = 0;
        HammCounter = 0;
        /**loops all the directories **/
        for (int i = 1; i <= posost; i++) {
            String localPath = path + Integer.toString(i);
            File dir = new File(localPath);
            /**lops all the files**/
            for (File file : dir.listFiles()) {
                /**counts the number of spam and ham email**/
                if (checkSpam(file)) {
                    SpamCounter++;
                } else {
                    HammCounter++;
                }
                /**create an new HaSet for every email
                 * in order to store each unique word
                 * for the specific email**/
                tempSet = new HashSet<Integer>();
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    String word = scanner.next();//inputs every word in a local variable
                    if (!word.equals("Subject:")) {//checks if its not Subject otherwise sketchy things may happen
                        /**parses the sting that it receives into an integer
                         * for later calculations**/
                        tempSet.add(Integer.parseInt(word));//keep in mind add checks if the cell is unique
                    }//end if
                }//end while
                if (checkSpam(file)) {//if its spam
                    /**increases the SpamCounter if the word is found in a Spam mail **/
                    UpdateMailHash(0);
                } else {
                    /**increases the HamCounter if the word is found in a Ham mail **/
                    UpdateMailHash(1);
                }//end if
                scanner.close();
                // tempSet.clear();
            }//end for2
        }//end for1
        Laplace();
        Probability();
        System.out.println("SpamCounter: " + SpamCounter + " HammCounter: " + HammCounter);
        System.out.println("Counter: " + (SpamCounter + HammCounter));
    }//end input To HashMap

    /**
     * this function implements the laplace algorithm
     * which in order to escape from having 0 ass a probability
     * ads 1/2 in all
     **/
    private void Laplace() {
        for (int lexi : MailHash.keySet()) {
            MailHash.get(lexi)[0]++;
            MailHash.get(lexi)[1]++;
        }
        SpamCounter += 2;
        HammCounter += 2;
    }//end Laplace
    /**
     * calculates the probability of each mail to be spam or ham
     * based on the bayes algorithm
     **/
    private void Probability() {
        for (int lexi : MailHash.keySet()) {
            MailHash.get(lexi)[0] = Math.log(1.0 + (MailHash.get(lexi)[0] / (double) SpamCounter));
            MailHash.get(lexi)[1] = Math.log(1.0 + (MailHash.get(lexi)[1] / (double) HammCounter));
        }
    }// end Propability
    /**this function Updates the MailHash HashMap
     * and increases the counter when an word is found in a email **/
    private void UpdateMailHash(int i)
    {
        /**loops all the tempSet which contains
         * unique word **/
        for (int k : tempSet) {
            /**checks if the (word)=k that is contained in the mail
             * exists in the known words or dictionary
             * and ads an counter which means that the word is found
             * at least 1 time**/
            if (MailHash.containsKey(k)) {

                MailHash.get(k)[i] += 1;

            } else {
                /**if (word)=k does not exists it create on
                 * which contains 1 table 1 column for the number of spam
                 * 1 for the number of ham**/
                MailHash.put(k, new Double[]{0.0, 0.0});
                MailHash.get(k)[i] += 1;
            }
        }//end for
    }//end UpdateMailHash
    /**checks if the given file is spam or ham based on the Title**/
    private boolean checkSpam(File dir) throws FileNotFoundException {
        if (dir.getName().contains("spmsg")) {
            return true;
        }
        return false;
    }// end checkSpam
    /**this function returns probability the mail to be Spam**/
    public double getSpamPropability() {
        return (double) SpamCounter / ((double) SpamCounter + (double) HammCounter);
    }
    /**this function returns probability the mail to be Ham**/
    public double getHamPropability() {
        return 1.0 - getSpamPropability();
    }
    public double Accuracy() {
        return ((TrueFalse[0] + TrueFalse[1]) / (TrueFalse[0] + TrueFalse[1] + TrueFalse[2] + TrueFalse[3])) * 100;
    }
    public double HamPrecision() {
        return (TrueFalse[0] / (TrueFalse[0] + TrueFalse[2])) * 100;
    }

    public double SpamPrecision() {
        return (TrueFalse[1] / (TrueFalse[1] + TrueFalse[3])) * 100;
    }

    public double HamRecall() {
        return (TrueFalse[0] / (TrueFalse[0] + TrueFalse[3])) * 100;
    }

    public double SpamRecall() {
        return (TrueFalse[1] / (TrueFalse[1] + TrueFalse[2])) * 100;
    }
}//endBayesAddData
