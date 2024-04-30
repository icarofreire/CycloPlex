package cycloplex;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


public class CyclomaticComplexity {

	public int check(final String fileName) {
		int complexity = 0;
        final String[] blocks = {"{", "}"};
		final String[] keywords = {"if","else","while","case","for","switch","do","continue","break","&&","||","?",":","catch","finally","throw","throws","default","return"};
		String line = null;
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			line = br.readLine();

            int iniLine = -1;
            int fimLine = -1;
            int indB = 0;

            int conLine = 0;
			while (line != null)
			{
                conLine++;
				StringTokenizer stTokenizer = new StringTokenizer(line);
				while (stTokenizer.hasMoreTokens())
				{
					String words = stTokenizer.nextToken();
                    if(words.equals(blocks[0])){
                        if(iniLine == -1) iniLine = conLine;
                        indB++;
                    }

                    for(int i=0; i<keywords.length; i++)
                    {
                        if (keywords[i].equals(words))
                        {
                            complexity++;
                        }
                    }

                    if(words.equals(blocks[1])){
                        indB--;
                        
                        if(indB == 0){
                            fimLine = conLine;
                            System.out.println(fileName + ": " + iniLine + "L - " + fimLine + "L: " + complexity + ";");

                            iniLine = -1;
                            fimLine = -1;
                            complexity = 0;
                        }
                    }
				}
				line = br.readLine();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return (complexity);
	}

	public void showCyclomaticComplexity(String fileName, int ccValue) {
		// System.out.println("\nFile '"+ fileName +"' : The Cyclomatic Complexity is : "+ccValue);
		System.out.print("\nResult : ");
		if (ccValue> 50){
			System.out.print("Most complex and highly unstable method ");
		}
		else if(ccValue>= 21 && ccValue<=50)
			System.out.print("High risk");
		else if(ccValue>= 11 && ccValue<=20)
			System.out.print("Moderate risk");
		else
			System.out.print("Low risk program");
	}
	
	public static void main(String ss[])
	{
		CyclomaticComplexity cc = new CyclomaticComplexity();
        String fileName = "teste.java";
		cc.showCyclomaticComplexity(fileName, cc.check(fileName));
	}

    public static void checkFile(){
        CyclomaticComplexity cc = new CyclomaticComplexity();
        String fileName = "teste.java";
		cc.showCyclomaticComplexity(fileName, cc.check(fileName));
    }
}