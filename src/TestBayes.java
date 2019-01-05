import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Scanner;
public class TestBayes {
    private BiData biData = new BiData();

    /**checks if the given file is spam or ham based on the Title**/
    private boolean checkSpam(File dir) throws FileNotFoundException {
        if (dir.getName().contains("spmsg")) {
            return true;
        }
        return false;
    }// end checkSpam
    /**
     * this function loads the Training data
     * (loads the propabilites in  a hashMap type double[]
     * called MailHash) this function loads the Testing data
     **/
    public void WriteTxt() throws FileNotFoundException {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("BayesResult.txt"), "utf-8"));
            writer.write("MailCounter  Accuracy  HamPrecision  HamRecall  SpamPrecision  SpamRecall");
            ((BufferedWriter) writer).newLine();
            String path = "src\\pu_corpora_public\\pu3\\part";
            for(int i=1; i<10; i++)
            {
                biData.inputToHashMap(path, i);
                CalculateTest(path);
                writer.write((biData.SpamCounter + biData.HammCounter)
                        + " " + Double.toString(biData.Accuracy()).replace(".",",")
                        + " " + Double.toString(biData.HamPrecision()).replace(".",",")
                        + " " + Double.toString(biData.HamRecall()).replace(".",",")
                        + " " + Double.toString(biData.SpamPrecision()).replace(".",",")
                        + " " + Double.toString(biData.SpamRecall()).replace(".",","));
                ((BufferedWriter) writer).newLine();
                System.out.println("==================");
            }
        } catch (IOException ex) {
            // Report
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {/*ignore*/}
        }
    }

    /**this function loads and checks how the good the algorithm does in unknown data**/
    public void CalculateTest(String path) throws FileNotFoundException {
        biData.TrueFalse[0] = 0.0;
        biData.TrueFalse[1] = 0.0;
        biData.TrueFalse[2] = 0.0;
        biData.TrueFalse[3] = 0.0;
        String localPath = path + Integer.toString(10);
        File dir = new File(localPath);
        for (File file : dir.listFiles()) {
            double SpamProbability = 0.0;
            double HamProbability = 0.0;
            biData.tempSet = new HashSet<Integer>();
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String word = scanner.next();
                if (!word.equals("Subject:")) {
                    int cell = Integer.parseInt(word);
                    biData.tempSet.add(cell);
                }
            }//end while
            /**for every unique word in the mail**/
            for (int word : biData.tempSet) {
                /**if the word exists then probaWord1+probaWord2+probaWord3....
                 * to get the total probability adding the probability because
                 * log(a*b)=log(a)+log(b) for spam and ham**/
                if (biData.MailHash.containsKey(word)) {
                    SpamProbability = SpamProbability + biData.MailHash.get(word)[0];
                    HamProbability = HamProbability + biData.MailHash.get(word)[1];
                } else {
                    SpamProbability = SpamProbability + Math.log(1.0 + (1.0 / (double) biData.SpamCounter));
                    HamProbability = HamProbability + Math.log(1.0 + (1.0 / (double) biData.HammCounter));
                }
            }
            /**takes a decision **/
            if (SpamProbability * biData.getSpamPropability() < HamProbability * biData.getHamPropability()) {
                //System.out.println("Its a Ham eimai sto file " + file.toString());
                /**TrueFalse==TP,TN,FP,FN**/
                if (!checkSpam(file)) {
                    biData.TrueFalse[0]++;
                } else {
                    biData.TrueFalse[2]++;
                }
            } else {
                //System.out.println("Its a Spam eimai sto file " + file.toString());
                if (checkSpam(file)) {
                    biData.TrueFalse[1]++;
                } else {
                    biData.TrueFalse[3]++;
                }
            }//end else
        }//end for
        System.out.println("Accuracy: " + biData.Accuracy());
        System.out.println("Spam Probability: "+ biData.getSpamPropability());
        System.out.println("Ham Probability: "+ biData.getHamPropability());
        System.out.println("HamPRecision: " + biData.HamPrecision());
        System.out.println("SpamPRecision: " + biData.SpamPrecision());
        System.out.println("HamRecall: " + biData.HamRecall());
        System.out.println("SpamRecall: " + biData.SpamRecall());
    }
}//end TestBayes

