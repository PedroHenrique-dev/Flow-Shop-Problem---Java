package FSP;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Leitor {
    private final String pathArquivo;
    int tempoGasto, quantidadeMaquinas;
    int linhas = 20, colunas = 10, aux;
    int colunasDobro = colunas * 2;
    private int[][] matriz = new int[linhas][colunas];
    private int[][] matrizAux = new int[linhas][colunasDobro];
    private int[][] matrizTarefas = new int[colunas][linhas];
    private int[][] tempo = new int[colunas][linhas];
    private int[] solucao = new int[linhas];
    private int[] maquinaTempo = new int[linhas];
    private int[] maquinaPosicaoDecrescente = new int[linhas];
    private int[] vetorAux = new int[linhas];
    private int[] vetorMake = new int[linhas];
    
    public void heuristicaNEH() {
        for (int i = 0; i < this.linhas; i++)
            for (int j = 0; j < this.colunas; j++)
                this.maquinaTempo[i] += this.matriz[i][j];
        for (int i = 0; i < this.maquinaPosicaoDecrescente.length; i++)
            this.maquinaPosicaoDecrescente[i] = i;
        insertionSort();
        for (int i = 2; i <= linhas; i++)
            this.tempoGasto = tempoMinimo(i);
        System.out.println("Ordem das tarefas:");
        for (int i = 0; i < vetorAux.length; i++)
            System.out.print((this.solucao[i]+1)+ " ");
        System.out.println("\n\nTempo gasto: "+this.tempoGasto);
    }
    public int tempoMinimo(int quantidade){
        int minimo = 999999999;
        for (int i = 0; i < vetorAux.length; i++)
            this.vetorAux[i] = -1;
        if (quantidade == 2)
            this.solucao[0] = this.maquinaPosicaoDecrescente[0];
        for (int i = 0; i < quantidade-1; i++)
            this.vetorAux[i] = this.solucao[i];
        this.vetorAux[quantidade-1] = this.maquinaPosicaoDecrescente[quantidade-1];
        for (int k = quantidade; k > 0; k--) {
            for (int i = 0; i < quantidade; i++)
                for (int j = 0; j < colunas; j++)
                    this.matrizTarefas[j][i] = this.matriz[vetorAux[i]][j];
            this.vetorMake[quantidade-k] = makespan();
            if (minimo > this.vetorMake[quantidade-k]) {
                minimo = this.vetorMake[quantidade-k];
                for (int i = 0; i < solucao.length; i++)
                    this.solucao[i] = this.vetorAux[i];
            }
            if (k == 1) break;
            aux = this.vetorAux[k-1];
            this.vetorAux[k-1] = this.vetorAux[k-2];
            this.vetorAux[k-2] = aux;
        }
        for (int i = 0; i < vetorMake.length; i++)
            if (this.vetorMake[i] == minimo)
                aux = i;
        for (int i = 0; i < quantidade; i++)
            this.vetorAux[i] = this.maquinaPosicaoDecrescente[i];
        if (quantidade > 2){
            for (int k = aux; k < quantidade-1; k++) {
                aux = this.vetorAux[quantidade-1];
                this.vetorAux[quantidade-1] = this.vetorAux[quantidade-2];
                this.vetorAux[quantidade-2] = aux;
            }
        } else{
            for (int k = aux; k <= quantidade-1; k++) {
                aux = this.vetorAux[quantidade-1];
                this.vetorAux[quantidade-1] = this.vetorAux[quantidade-2];
                this.vetorAux[quantidade-2] = aux;
            }
        }
        return minimo;
    }
    public int makespan(){
        this.tempo[0][0] = this.matrizTarefas[0][0];
        for (int i = 1; i < colunas; i++) {
            for (int j = 1; j < linhas; j++) {
                this.tempo[0][j] = this.tempo[0][j-1] + this.matrizTarefas[0][j];
                this.tempo[i][0] = this.tempo[i-1][0] + this.matrizTarefas[i][0];
            }
        }
        for (int i = 1; i < colunas; i++) {
            for (int j = 1; j < linhas; j++) {
                if (this.tempo[i][j-1] > this.tempo[i-1][j])
                    this.tempo[i][j] = this.tempo[i][j-1] + this.matrizTarefas[i][j];
                else
                this.tempo[i][j] = this.tempo[i-1][j] + this.matrizTarefas[i][j];
            }
        }
        return tempo[colunas-1][linhas-1];
    }
    public void insertionSort(){
        int j, cartaLocal, cartaTempo;
        for (int i = 0; i < this.maquinaTempo.length; i++) {
            cartaTempo = this.maquinaTempo[i];
            cartaLocal = this.maquinaPosicaoDecrescente[i];
            j = i - 1;
            while(j >= 0 && this.maquinaTempo[j] < cartaTempo){
                this.maquinaTempo[j+1] = this.maquinaTempo[j];
                this.maquinaPosicaoDecrescente[j+1] = this.maquinaPosicaoDecrescente[j];
                j-= 1;
            }
            this.maquinaTempo[j+1] = cartaTempo;
            this.maquinaPosicaoDecrescente[j+1] = cartaLocal;
        }
    }
    public Leitor(String pathDirectory, String Archive){
        this.pathArquivo = pathDirectory + "/" + Archive;
    }
    public void ler(String Archive){
        int c1=0, c2=0;
        construcao(Archive);
        File arquivo = new File(getArquivo());
        if (arquivo.isFile() && arquivo.canRead()) {
            try {
                FileInputStream leitor = new FileInputStream(arquivo);
                byte[] arquivoByte = new byte[(int) arquivo.length()];
                leitor.read(arquivoByte);
                leitor.close();
                for (int i = 0; i < arquivoByte.length; i++) {
                    if(arquivoByte[i] > 47 && arquivoByte[i] < 58)
                        this.matrizAux[c1][c2] = (this.matrizAux[c1][c2]*10)+(arquivoByte[i] - 48);
                    if(arquivoByte[i] == 32 && i != 0 && arquivoByte[i-1] != 32 && arquivoByte[i-1] != 10)
                        c2++;
                    if(arquivoByte[i] == 10){
                      c1++; c2=0;
                    }
                }
                gerarMatriz();
                mostrarMatriz();
                System.out.println();
                heuristicaNEH();
            } catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
    }
    public void gerarMatriz(){
        int k = 0;
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunasDobro; j++) {
                if(j%2 != 0){
                    this.matriz[i][k] = this.matrizAux[i][j];
                    k++;
                }
            }
            k=0;
        }
    }
    public void mostrarMatriz(){
        System.out.println("\nMatriz[" + this.linhas + "][" + this.colunas + "]");
        for (int i = 0; i < this.linhas; i++) {
            for (int j = 0; j < this.colunas; j++)
                System.out.print(this.matriz[i][j] + " ");
            System.out.println("");
        }
    }
    public void mostrarMatrizTempo(){
        System.out.println();
        for (int i = 0; i < colunas; i++) {
            for (int j = 0; j < linhas; j++) {
                System.out.print(this.tempo[i][j] + " ");
            }
            System.out.println();
        }
    }
    public void mostrarMatrizTarefas(){
        System.out.println("\nMatrizTarefas[" + colunas + "][" + linhas + "]");
        for (int i = 0; i < colunas; i++) {
            for (int j = 0; j < linhas; j++)
                System.out.print(this.matrizTarefas[i][j] + " ");
            System.out.println("");
        }
    }
    public String getArquivo(){
        return this.pathArquivo;
    }
    public void zerarTempo(){
        for (int i = 0; i < matriz.length; i++)
            for (int j = 0; j < matriz.length; j++)
                this.tempo[i][j] = 0;
    }
    public void zerarVetorAux(){
        for (int i = 0; i < matriz.length; i++)
            this.vetorAux[i] = 0;
    }
    public void construcao(String Archive){
        System.out.println("------------------ Heurística NEH --------------------");
        System.out.println("As máquinas são as colunas e as tarefas são as linhas.\n");
        System.out.println("Nome do arquivo: " + Archive);
        System.out.println("Quantidade de máquinas: " + colunas);
        System.out.println("Quantidade de tarefas: " + linhas);
    }
}
