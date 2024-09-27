
import java.util.ArrayList;
import java.util.List;

public class Excecoes {
private List<String> regDest;
    private List<String> regSource;
    private List<String> memAcess;

    public Excecoes() {
        regDest = new ArrayList<>();
        regSource = new ArrayList<>();
        memAcess = new ArrayList<>();

        regDest.add("lb");
        regDest.add("lh");
        regDest.add("lwl");
        regDest.add("lw");
        regDest.add("lbu");
        regDest.add("lhu");
        regDest.add("lwr");
        regDest.add("add");
        regDest.add("addu");
        regDest.add("sub");
        regDest.add("subu");
        regDest.add("and");
        regDest.add("or");
        regDest.add("xor");
        regDest.add("nor");
        regDest.add("slt");
        regDest.add("sltu");
        regDest.add("addi");
        regDest.add("addiu");
        regDest.add("slti");
        regDest.add("sltiu");
        regDest.add("andi");
        regDest.add("ori");
        regDest.add("xori");
        regDest.add("lui");
        regDest.add("sll");
        regDest.add("srl");
        regDest.add("sra");
        regDest.add("sllv");
        regDest.add("srlv");
        regDest.add("srav");
        regDest.add("mfhi");
        regDest.add("mflo");
        regDest.add("jalr");

        regSource.add("sw");
        regSource.add("sb");
        regSource.add("sh");
        regSource.add("swl");
        regSource.add("swr");
        regSource.add("mthi");
        regSource.add("mtlo");
        regSource.add("mult");
        regSource.add("multu");
        regSource.add("div");
        regSource.add("divu");
        regSource.add("jr");
        regSource.add("bltz");
        regSource.add("bgez");
        regSource.add("bltzal");
        regSource.add("bgezal");
        regSource.add("j");
        regSource.add("jal");
        regSource.add("beq");
        regSource.add("bne");
        regSource.add("blez");
        regSource.add("btgz");

        memAcess.add("lb");
        memAcess.add("lh");
        memAcess.add("lwl");
        memAcess.add("lw");
        memAcess.add("lbu");
        memAcess.add("lhu");
        memAcess.add("lwr");
        memAcess.add("sb");
        memAcess.add("sh");
        memAcess.add("swl");
        memAcess.add("sw");
        memAcess.add("swr");
        memAcess.add("j");
        memAcess.add("jal");
        memAcess.add("jr");
        memAcess.add("jalr");
        memAcess.add("sll");
        memAcess.add("srl");
        memAcess.add("sra");
        memAcess.add("sllv");
        memAcess.add("srlv");
        memAcess.add("srav");
        
    }

    
    /** 
     * verifica se a instrução usa um registrador de destino primeiro na ordem de registradores
     * @param instru
     * @return boolean
     */
    public boolean isDest(String instru) {
        if (regDest.contains(instru)) {
            return true;
        }
        return false;

    }

    public boolean isOri(String instru) {
        if (regSource.contains(instru)) {
            return true;
        }
        return false;

    }

    public boolean isMem(String instru) {
        if (memAcess.contains(instru)) {
            return true;
        }
        return false;
    }
}
