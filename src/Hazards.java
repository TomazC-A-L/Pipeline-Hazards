import java.util.*;

public class Hazards {

    private static Excecoes exe = new Excecoes();

    
    /** 
     * Este método identifica qual arquivo esta sendo lido para decidir qual solução de hazard usar.
     * @param instruçoes recebe a lista de instruções específicadas
     * @param fileNum recebe o nome do arquivo para comparação
     * @return ArrayList<Instruçao>
     */
    public static ArrayList<Instruçao> pipeline (ArrayList<Instruçao> instruçoes, String fileNum){

        ArrayList<Instruçao> resposta = new ArrayList<>();
        int tam = instruçoes.size()-1;

        for (int i = 0; i < tam; i++){
            Instruçao um = instruçoes.get(i);
            Instruçao dois = instruçoes.get(i+1);
            Instruçao tres = null;

            if (i == instruçoes.size() - 2)
                tres = null;
            else
                tres = instruçoes.get(i + 2);;

            int nops = CalcularNops(um, dois, tres);
            if (
                fileNum.contains("01") ||
                fileNum.contains("02") ||
                fileNum.contains("05") ||
                fileNum.contains("06") ||
                fileNum.contains("07")
            )
                noOperation(um,nops,resposta);

            if (
                fileNum.contains("03") ||
                fileNum.contains("04") ||
                fileNum.contains("08") ||
                fileNum.contains("09") ||
                fileNum.contains("10")
            )
                adiantamento(um,nops,resposta);
        }

        resposta.add(instruçoes.get(tam));

        if(
            fileNum.contains("03") ||
            fileNum.contains("04") ||
            fileNum.contains("08") ||
            fileNum.contains("09") ||
            fileNum.contains("10")
        )
            reordenamento(resposta);

        return resposta;
    }

    
    /** 
     * Verifica se tem o conflito entre as instruções, e adiciona objtos instrução com valores "nop"
     * @param inst
     * @param nops
     * @param resposta
     */
    public static void noOperation(Instruçao inst, int nops,ArrayList<Instruçao> resposta) {

        //caso não houver conflito
        if(nops == 0) {
            resposta.add(inst);
        }

        //caso o conflito for diretamente com a próxima instrução,  adiciona duas bolhas
        if(nops == 2) {
            resposta.add(inst);
            resposta.add(new Instruçao("nop","nop","nop","nop","nop"));
            resposta.add(new Instruçao("nop","nop","nop","nop","nop"));
        }

        //caso o conflito seja com a instrução depois da próxima, adicionando so uma bolha
        if(nops == 1) {
            resposta.add(inst);
            resposta.add(new Instruçao("nop","nop","nop","nop","nop"));
        }
    }


    
    
    /** 
     * Este método faz o mesmo tipo de verificação que o método noOperation(), porém, trata os casos de forma diferente.
     * @param inst
     * @param nops
     * @param resposta
     */
    public static void adiantamento(Instruçao inst, int nops,ArrayList<Instruçao> resposta) {
        
        //caso não houver conflito, ou caso o conflito for com uma instrução que não seja a próxima, apenas adiciona a instrução
        if(nops == 0 || nops == 1) 
            resposta.add(inst);

        //caso houver conflito com a instrução que vem logo após a atual
        if(nops == 2)
            //verificar se a instrução pode ser adiantada pela memória
            if(exe.isMem(inst.getmnem())) {
                resposta.add(inst);
                resposta.add(new Instruçao("nop","nop","nop","nop","nop"));
            }
            else
                resposta.add(inst);
    }

    
    /** 
     * Verifica se a instrução tem alguma dependencia, se tiver, ela fica estatica, se não, procura uma bolha para ela assumir o lugar.
     * @param lista
     */
    public static void reordenamento(ArrayList<Instruçao> lista) {

        for (int i = 0; i < lista.size(); i++) {
            Instruçao aux = lista.get(i);

            //verificar se a instrução é do tipo J ou de Desvio
            char inicio = aux.getmnem().charAt(0);

            //verificar se é um nop
            if(!aux.getmnem().equals("nop")) {
                if(inicio == 'j') {
                    lista.add(0,aux);
                    lista.remove(i+1);
                }
                else {
                    //verifica se a instrução tem dependecias
                    if (!dependencia(i, lista, 15)){
                        //se ela for de Desvio, manda a instrução pro topo da lista
                        if(inicio == 'b'){
                            lista.add(0,aux);
                            lista.remove(i+1);
                        } else {

                            for (int j = 0; j < lista.size();j++) {
                                if(lista.get(j).getmnem().equals("nop")){
                                    Instruçao target = lista.get(j);
                                    if(target.getmnem().equals("nop")) {
                                        //verifica se a instrução vai gerar um conflito caso ela mude de lugar
                                        if(!identificarConflito(j, i, lista, 2)) {
                                            lista.set(j, aux);
                                            lista.remove(i);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Garantia de execução
        try {
            char inicio = lista.get(lista.size()-1).getmnem().charAt(0);
            if(inicio == 'j'){
                lista.add(0,lista.get(lista.size()- 1));
                lista.remove(lista.size()-1);
            }
            if(inicio == 'b') {
                lista.add(0,lista.get(lista.size()-1));
                lista.remove(lista.size()-1);
            }
        }catch (Error error) {
            System.err.println("");
        }
    }

    
    
    /** 
     * Verifica se a instrução tem alguma dependencia com registradores acima e abaixo
     * @param index
     * @param lista
     * @param quant
     * @return boolean
     */
    public static boolean dependencia (int index, ArrayList<Instruçao> lista, int quant){
        //instrução atual
        Instruçao principal = lista.get(index);

        //delimitar o tamanho da busca
        int inicio = Math.max(0,index - quant);
        int fim = Math.min(lista.size() - 1, index + quant);

        for(int i = inicio; i <= fim; i++) {
            //pula caso seja a posição original da instrução
            if(i == index) {
                continue;
            }

            Instruçao target = lista.get(i); // instruçao sendo comparada

            if(target.getmnem() == null)
                continue;

            if(i < index)
                if (validarConflito(target,principal))
                    return true;
            else
                if (validarConflito(principal, target))
                    return true;
        }
        return false;
    }

    
    
    /** Verifica se tem registradores conflitando acima e abaixo da instrução
     * @param index
     * @param index2
     * @param lista
     * @param nops
     * @return boolean
     */
    public static boolean identificarConflito(int index, int index2, ArrayList<Instruçao> lista, int nops) {
        //instrução atual
        Instruçao principal = lista.get(index2);

        //delimitar o tamanho da busca
        int inicio = Math.max(0,index - nops);
        int fim = Math.min(lista.size() - 1, index + nops);

        for(int i = inicio; i <= fim; i++){
            //pula caso seja a posição original da instrução
            if(i == index)
                continue;

            Instruçao target = lista.get(i);

            //caso a lista seja vazia
            if (target.getmnem() == null)
                continue;

            if(i < index2){
                if(validarConflito(target, principal));
                    return true;
            } else {
                if(validarConflito(principal, target))
                    return true;
            } 
        }
        return false;
    }


    
    
    /** 
     * Calcula a quantidade de nops a ser adicionada em uma operação de Stall ou Bypassing
     * @param um
     * @param dois
     * @param tres
     * @return int
     */
    public static int CalcularNops(Instruçao um,Instruçao dois, Instruçao tres){

        if(um.getmnem() != "nop")
            if(dois.getmnem() != "nop")
                //verifica se há conflito com a instrução logo após a atual, indicando duas bolhas
                if (validarConflito(um,dois))
                    return 2;

        if(tres != null) 
            if(tres.getmnem() != "nop")
                //verfica se há conflito com a instrução que vem após a próxima, indicando uma bolha
                if(validarConflito(um,tres))
                    return 1;
        return 0;
    }

    
    
    /** 
     * método para verificar conflitos de acesso aos registradores
     * @param um
     * @param target
     * @return boolean
     */
    public static boolean validarConflito(Instruçao um, Instruçao target) {

        if(exe.isDest(um.getmnem()) && exe.isDest(target.getmnem()))
            if(
                um.getr1().equals(target.getr2()) ||
                um.getr1().equals(target.getr3())
                )
                return true;

        if (exe.isDest(um.getmnem()) && !exe.isDest(target.getmnem())) {
            if (
                    um.getr1().equals(target.getr1()) ||
                    um.getr1().equals(target.getr2()) ||
                    um.getr1().equals(target.getr3())
            )
                    return true;
        }

        if(!exe.isDest(um.getmnem()) && exe.isDest(target.getmnem()))
            if (
                um.getr2().equals(target.getr2()) ||
                um.getr3().equals(target.getr3())
            )
                return true;

        return false;
    }
}
