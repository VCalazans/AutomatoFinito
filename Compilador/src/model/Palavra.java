/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @authors Victor Calazans e William Mello
 */
public class Palavra{
    private int linha;
    private String palavra;
    
    /**
     * Construtor padrão da classe Palavra
     */
    public Palavra(){
        this.linha   = 0;
        this.palavra = "";
    }
    
    /**
     * Construtor personaliado da classe Palavra
     * @param linha linha da palavra
     * @param palavra palavra na qual quer se armazenar
     */
    public Palavra(int linha,
                   String palavra){
        setLinha(linha);
        setPalavra(palavra);
    }
    
    /**
     * Setter da variável linha
     * @param linha variável linha 
     */
    public void setLinha(int linha){
        this.linha = linha;
    }
    
    /**
     * Getter da variável linha
     * @return Retorna a varável linha
     */
    public int getLinha(){
        return this.linha;
    }
            
    /**
     * Setter da variável Palavra
     * @param palavra variável palavra
     */
    public void setPalavra(String palavra){
        this.palavra = palavra;
    }
    
    /**
     * Getter da variável palavra
     * @return Retorna a variável palavra
     */
    public String getPalavra(){
        return this.palavra;
    }   
     
    /**
    * Método sobrecarregado para fins de comparação justa, indo além da igualdade de objeto 
    * @param obj Objeto que irá ser comparado
    * @return Retorna verdadeiro para a igualdade e falso para a não igualdade
    */
    @Override
    public boolean equals(Object obj){
        if(obj == this){
            return true;
        }
        if(obj == null){
            return false;
        }
        if(obj.getClass() != this.getClass()){
            return false;
        }
        Palavra other = (Palavra) obj;
        return other.linha == this.linha && 
               other.palavra.equals(this.palavra);
    }
   
}
