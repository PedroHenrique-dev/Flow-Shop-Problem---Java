package FSP;

import java.time.LocalTime;

class FSPmain{
    public static void main(String[] args) {
        LocalTime horaAgora1 = LocalTime.now();
        String pathDir;
        String Arquivo;
        pathDir = "C:/pastaTeste";
        Arquivo = "ta013";
        
        Leitor arquivo = new Leitor(pathDir, Arquivo);
        arquivo.ler(Arquivo);
        
        System.out.println("");
        System.out.println("Horário de inicio do algoritmo: " + horaAgora1);
        LocalTime horaAgora2 = LocalTime.now();
        System.out.println("Horário de fim do algoritmo: " + horaAgora2);
    }
}