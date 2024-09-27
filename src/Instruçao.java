public class Instruçao {
     private String completa;
    private String mnem;
    private String r1;
    private String r2;
    private String r3;

    public Instruçao(String mnem, String r1, String r3, String r2, String completa) {
         this.mnem = mnem;
         this.r1 = r1;
         this.r2 = r2;
         this.r3 = r3;
         this.completa = completa;
    }

    public void setr1(String r1){
         this.r1 = r1;
    }

    public void setr3(String r3){
         this.r3 = r3;
    }

    public String getmnem() {
         return mnem;
    }

    public String getr1() {
         return r1;
    }

    public String getr2() {
         return r2;
    }

    public String getr3() {
         return r3;
    }

    public String getAllValues() {
         return completa;
    }
}
