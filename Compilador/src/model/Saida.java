/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Iterator;


/**
 *
 * @author vcalazans
 */
public class Saida{
    
    private int linha;
    private String resultado;
    private String sequencia;
    private String reconhecimento;
    
    public void setLinha(int linha){
        this.linha = linha;
    }
    
    public void setResultado(String resultado){
        this.resultado = resultado;
    }
    
    public void setSequencia(String sequencia){
        this.sequencia = sequencia;
    }
    
    public void setReconhecimento(String reconhecimento){
        this.reconhecimento = reconhecimento;
    }
    
    public int getLinha(){
        return linha;
    }
    
    public String getResultado(){
        return resultado;
    }
    
    public String getSequencia(){
        return sequencia;
    }
    
    public String getReconhecimento(){
        return reconhecimento;
    }
}
