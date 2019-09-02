/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @authors Victor Calazans e William Mello
 */
public class RegraCompilador {

    ArrayList<Saida> arrSaida;
    Saida objSaida;
 
    // Várivel para guardar os estados processados.
    String reconhecimento = "";
    String resultado = "";
    //Váriaveis para controle das transições.
    boolean retTransicao = false;
    boolean palavraAceita = false;

    
    /**
     * Retorna as saidas das palavras processadas no método Analisar().
     * @return 
     */
    public ArrayList<Saida> getSaida(){
        return this.arrSaida;
    }

    /**
     * Contrutor da classe RegraCompilador, afim de inicializar os objetos
     * necessários
     */
    public RegraCompilador() {
        //Implementar continuação, se necessário
        arrSaida = new ArrayList<>();
    }

    /**
     * Método responsável por analisar o contexto enviado como parâmetro
     *
     * @param contexto Contexto no qual se deseja analisar
     */
    public void Analisar(List<Palavra> contexto) {
 
        Limpar();
        
        for (Palavra palavra : contexto) {
            
            
            reconhecimento = "";
            //Sempre começa no estado Q0.
            reconhecimento += "Q0, ";
            
            estadoQ0(palavra.getPalavra());
            objSaida = new Saida();

            //Recupera a saída da palavra processada.
            objSaida.setLinha(palavra.getLinha());
            objSaida.setReconhecimento(reconhecimento);
            objSaida.setResultado(resultado);
            objSaida.setSequencia(palavra.getPalavra());
            
            //Adiciona na lista para ser realizado tratamento em tela!
            arrSaida.add(objSaida);
        }
        
        limpaBuffers();

        

    }
    
    private void limpaBuffers(){
                
        reconhecimento = "";
        resultado = "";
        retTransicao = false;
        palavraAceita = false;

    }

    /**
     * Método responsável por limpar todas as variáveis de manipulação da regra
     * do compilador
     */
    public void Limpar() {
       
        arrSaida.clear();

    }

    private boolean estadoQ0(String seqString) {

        if (seqString.length() == 0) {
            resultado = "erro : palavra inválida";
            return retTransicao;
        }

        if (!validaCaracteresMaquina(seqString)) {
            return retTransicao;
        }

        String bckpReconhecimento = reconhecimento;

        if (seqString.charAt(0) == '1') {

            bckpReconhecimento = reconhecimento;

            reconhecimento += "Q0, ";
            retTransicao = estadoQ0(seqString.substring(1));

            if (!retTransicao) {
                reconhecimento = bckpReconhecimento;
            }

        } else if (seqString.charAt(0) == '0') {

            bckpReconhecimento = reconhecimento;

            reconhecimento += "Q1, ";
            retTransicao = estadoQ1(seqString.substring(1));

        } else {
            retTransicao = false;
        }

        //Caso a transição retorne e não tenha encontrado erro e a palavra não foi aceita, seguimos com a rota alternativa!
        if (!retTransicao && seqString.charAt(0) == '1' && !palavraAceita && !reconhecimento.contains("Qerro")) {

            reconhecimento = bckpReconhecimento;

            reconhecimento += "Q6, ";
            retTransicao = estadoQ6(seqString.substring(1));
        }

        return retTransicao;
    }

    private boolean estadoQ1(String seqString) {

        if (seqString.length() == 0) {
            resultado = "erro : palavra inválida";
            return retTransicao;
        }

        if (!validaCaracteresMaquina(seqString)) {
            return retTransicao;
        }

        String bckpReconhecimento;

        if (seqString.charAt(0) == '1') {

            reconhecimento += "Q1, ";
            retTransicao = estadoQ1(seqString.substring(1));

        } else if (seqString.charAt(0) == '0') {

            bckpReconhecimento = reconhecimento;
            reconhecimento += "Q2, ";
            retTransicao = estadoQ2(seqString.substring(1));

            if (retTransicao && !palavraAceita) {
                retTransicao = estadoQ1(seqString.substring(1));
            }

            if (!retTransicao && !reconhecimento.contains("Qerro")) {
                reconhecimento = bckpReconhecimento;
                reconhecimento += "Q6, ";
                retTransicao = estadoQ6(seqString.substring(1));
            }
        } else if (seqString.charAt(0) == '#') {

            reconhecimento += "Q3, ";
            retTransicao = estadoQ3(seqString.substring(1));
        }

        return retTransicao;

    }

    private boolean estadoQ2(String seqString) {

        if (seqString.length() == 0) {
            resultado = "erro : palavra inválida";
            return retTransicao;
        }

        if (!validaCaracteresMaquina(seqString)) {
            return retTransicao;
        }

        if (seqString.charAt(0) == '1') {

            reconhecimento += "Q2, ";
            retTransicao = estadoQ2(seqString.substring(1));

        } else if (seqString.charAt(0) == '0') {

            reconhecimento += "Q1, ";
            retTransicao = estadoQ1(seqString.substring(1));
            retTransicao = true;

        } else {
            retTransicao = false;
        }

        return retTransicao;
    }

    private boolean estadoQ3(String seqString) {

        if (seqString.length() == 0) {
            resultado = "erro : palavra inválida";
            return retTransicao;
        }

        if (!validaCaracteresMaquina(seqString)) {
            return retTransicao;
        }

        if (seqString.charAt(0) == '0') {

            reconhecimento += "Q4, ";
            retTransicao = estadoQ4(seqString.substring(1));
            
        } else {
            retTransicao = false;
        }

        return retTransicao;

    }

    private boolean estadoQ4(String seqString) {

        if (seqString.length() == 0) {
            resultado = "erro : palavra inválida";
            return retTransicao;
        }

        if (!validaCaracteresMaquina(seqString)) {
            return retTransicao;
        }

        if (seqString.charAt(0) == '0') {

            reconhecimento += "Q5, ";
            retTransicao = estadoQ5(seqString.substring(1));

        } else {
            retTransicao = false;
        }

        return retTransicao;
    }

    private boolean estadoQ5(String seqString) {

        if (seqString.length() == 0) {
            resultado = "palavra válida";
            palavraAceita = true;
            retTransicao = true;
            return retTransicao;
        }

        if (!validaCaracteresMaquina(seqString)) {
            return retTransicao;
        }

        if (seqString.charAt(0) == '0') {

            reconhecimento += "Q4, ";
            retTransicao = estadoQ4(seqString.substring(1));

        } else if (seqString.charAt(0) == 0) {

            palavraAceita = true;

        }

        return retTransicao;
    }

    private boolean estadoQ6(String seqString) {

        if (seqString.length() == 0) {
            resultado = "erro : palavra inválida";
            return retTransicao;
        }

        if (!validaCaracteresMaquina(seqString)) {
            return retTransicao;
        }

        if (seqString.charAt(0) == '1') {

            reconhecimento += "Q6, ";
            retTransicao = estadoQ6(seqString.substring(1));

        } else if (seqString.charAt(0) == '#') {
            reconhecimento += "Q7, ";
            retTransicao = estadoQ7(seqString.substring(1));

        }

        return retTransicao;
    }

    private boolean estadoQ7(String seqString) {

        if (seqString.length() == 0) {
            resultado = "erro : palavra inválida";
            return retTransicao;
        }

        if (!validaCaracteresMaquina(seqString)) {
            return retTransicao;
        }

        if (seqString.charAt(0) == '0') {

            reconhecimento += "Q8, ";
            retTransicao = estadoQ8(seqString.substring(1));

        }

        return retTransicao;

    }

    private boolean estadoQ8(String seqString) {

        if (seqString.length() == 0) {
            resultado = "palavra válida";
            palavraAceita = true;
            retTransicao = true;
            return retTransicao;
        }

        if (!validaCaracteresMaquina(seqString)) {
            return retTransicao;
        }

        if (seqString.charAt(0) == '0') {

            reconhecimento += "Q9, ";
            retTransicao = estadoQ9(seqString.substring(1));

        }

        return retTransicao;
    }

    private boolean estadoQ9(String seqString) {

        if (seqString.length() == 0) {
            resultado = "erro : palavra inválida";
            return retTransicao;
        }

        if (!validaCaracteresMaquina(seqString)) {
            return retTransicao;
        }

        if (seqString.charAt(0) == '0') {

            reconhecimento += "Q8, ";
            retTransicao = estadoQ8(seqString.substring(1));

        }

        return retTransicao;
    }

    /**
     * Válida os caracteres de sequencia da análise, verificando se há simbolos inválidos.
     * @param seqString
     * @return 
     */
    boolean validaCaracteresMaquina(String seqString) {

        if ((seqString.charAt(0) != '1' && seqString.charAt(0) != '0' && seqString.charAt(0) != '#')) {
           
            if(seqString.charAt(0) == ';' || seqString.charAt(0) == '.'){
                resultado = "símbolo especial.";
                reconhecimento += "Q999 - Analisar situação";
                return false;
            }else if(seqString.charAt(0) == '+'){
                reconhecimento += "Qerro";
                resultado = "erro: símbolos(s) inválido(s)";
                return false;
            }
            
            reconhecimento += "Qerro";
            return false;
        }

        return true;
    }

    public static void main(String[] args) {

        RegraCompilador regra = new RegraCompilador();

        List<Palavra> letras = new ArrayList<>();
        
        Palavra p = new Palavra();
        p.setLinha(1);
        p.setPalavra("0#0");
        letras.add(p);
      
        p = new Palavra();
        p.setLinha(1);
        p.setPalavra("1#000");
        letras.add(p);
        
        p = new Palavra();
        p.setLinha(2);
        p.setPalavra("0100#00");
        letras.add(p);
        
        p = new Palavra();
        p.setLinha(3);
        p.setPalavra("00%11");
        letras.add(p);
        
        p = new Palavra();
        p.setLinha(3);
        p.setPalavra("+");
        letras.add(p);
        
        p = new Palavra();
        p.setLinha(3);
        p.setPalavra(".");
        letras.add(p);

        p = new Palavra();
        p.setLinha(3);
        p.setPalavra(";");
        letras.add(p);
        
        p = new Palavra();
        p.setLinha(5);
        p.setPalavra("teste001#");
        letras.add(p);
        
        p = new Palavra();
        p.setLinha(6);
        p.setPalavra("10110#000");
        letras.add(p);
        
        
        regra.Analisar(letras);

    }

}
