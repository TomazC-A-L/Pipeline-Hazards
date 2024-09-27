import java.io.*;
import java.nio.file.*;
import java.util.*;
public class Leitor {

    /*
     * Itera pela pasta de arquivos extraindo o nome de cada arquivo para enviar para o metodo de leitura.
     */
    public static void pegarArquivo() {
        Path dir = Paths.get("arquivos");

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path file : stream) {
                if (Files.isRegularFile(file)) {
                    Path fileName = file.getFileName();
                    String fileNameStr = file.getFileName().toString();
                    System.out.println("========================================");
                    System.out.printf("Open file: %s\n", fileNameStr);

                    ler("" + fileName);
                }
            }
        } catch (IOException | DirectoryIteratorException e) {
            System.err.printf("Error opening directory: %s\n", e);
        }
    }
    
    /** 
     * Recebe o nome do arquivo da iteração atual do método pegarArquivo().
     * Destes arquivos, o método extrai cada linha e tokeniza elas, estes tokens serão usados posteriormente para criar um objeto instrução.
     * Após isso tudo, o método chama a função escrever() para criar os arquivos de respostas.
     * @param arquivo
     * @throws IOException
     */
    public static void ler (String arquivo) throws IOException {
        BufferedReader read = new BufferedReader(new FileReader(arquivo));
        String linha;

        //String para salvar o conteudo da arraylist inteira
        String conteudo = " ";
        ArrayList<Instruçao> instruções = new ArrayList<Instruçao>();

        Instruçao instruçao;

        while ((linha = read.readLine())!=null){

            StringTokenizer registradores = new StringTokenizer(linha, ",$() ");
            //auxiliar para jogar os tokens em um objeto instrução
            String[] aux = new String[4];
            int cont = 0;
            
            while (registradores.hasMoreTokens()){
                aux[cont] = registradores.nextToken().toLowerCase();
                cont++;
            }
            for (int i = 0; i < 4; i++){
                if(aux[i] == null)
                    aux[i] = " ";
            }

            if(aux[0].equals("nop"))
                instruçao = new Instruçao("nop","nop","nop","nop","nop");
            else
                instruçao = new Instruçao(aux[0],aux[1],aux[3],aux[2],linha);

            instruções.add(instruçao);
        }

        //Implementa as funções de solução de hazard no arraylist para enviar para a string conteúdo
        for(Instruçao instru : Hazards.pipeline(instruções,arquivo)) {
            System.out.println(instru.getAllValues());
            conteudo += instru.getAllValues();
            conteudo += "\n";
        }
        read.close();
        escrever(conteudo, arquivo);
    }

    /*
     * Cria os arquivos de resultado com o nome correto e número requisitado
     */
    public static void escrever(String conteudo, String titulo) throws IOException{

        titulo = titulo.split(".txt")[0];
        titulo += "-RESULTADO.txt";
        BufferedWriter out = new BufferedWriter(new FileWriter(titulo));
        out.write(conteudo);
        out.flush();
        out.close();

    }
}
