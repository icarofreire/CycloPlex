/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package cycloplex;

import cycloplex.CyclomaticComplexity;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class App {

    public static void main(String[] args) {

        final CyclomaticComplexity comp = new CyclomaticComplexity();

        // /** \/ casos de testes; */
        comp.setTipoParaAnalise(CyclomaticComplexity.tiposAnalise.LINHAS.ordinal());
        // // comp.checkFile("teste.java");
        comp.setMinComplex(4);
        comp.checkFilesDir("/home/icaro/Documentos/johan");
        // /** /\ casos de testes; */

        /*\/ opções de argumentos de linha de comando; */
        if(args.length > 0 && args[0] != null){
            final File dir = new File(args[0]);
            if(dir.exists() && dir.isDirectory()){
                List<String> argumentos = Arrays.asList(args);
                if(argumentos.contains("-b")){
                    comp.setTipoParaAnalise(CyclomaticComplexity.tiposAnalise.BLOCOS.ordinal());
                }else if(argumentos.contains("-l")){
                    comp.setTipoParaAnalise(CyclomaticComplexity.tiposAnalise.LINHAS.ordinal());
                }
                comp.checkFilesDir(dir.getAbsolutePath());
            }else{
                System.out.println("O caminho informado não é um diretório;");
            }
        }

    }
}
