package daniellyomori.utfpr.edu.controledemissoesdojogozelda.utils;

public class UtilsString {
    public static boolean verificaStringVazia(String texto){
        return texto == null || texto.trim().length() == 0;
    }

}
