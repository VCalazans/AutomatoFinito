/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import model.NumeredBorder;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import model.Palavra;
import model.RegraCompilador;
import model.Saida;

/**
 *
 * @authors Victor Calazans e William Mello
 */
public class FrameCompilador extends javax.swing.JFrame {
        
    private static JTextArea jtaContexto;
    private List<Palavra> palavras = new ArrayList<>();
    
    public static JPanel getPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        jtaContexto = new JTextArea();        
        jtaContexto.setBorder(new NumeredBorder());       
        
        JScrollPane scroll = new JScrollPane(jtaContexto);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scroll);                        
        return panel;
    }
    
    private RegraCompilador regraCompilador;
    
    /**
     * Método principal de invocação da janela do FrameCompilador
     */
    public static void ExecutarFrameCompilador(){
        FrameCompilador frameCompilador = new FrameCompilador();        
        frameCompilador.initComponents();
        frameCompilador.InicializaObjetos();
        frameCompilador.DefineExigencias();        
        frameCompilador.show();
    }
    
    /**
     * Método responsável por instaciar objetos básicos
     */    
    private void InicializaObjetos(){
        regraCompilador = new RegraCompilador();
    }
    
    /**
     * Método para definir exigênias do cliente
     */    
    private void DefineExigencias(){
        this.setLocationRelativeTo(null);
        this.setResizable(false);                                       
        DefineTamanhoColunasResultado();                
        JPanel panel = getPanel();
        panel.setBounds(0, 0, 860, 187);
        this.getContentPane().add(panel);
    }        

    /**
     * Método responsável por definir o tamanho das colunas
     */   
    private void DefineTamanhoColunasResultado(){
        jtResultados.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);        
        //Linha
        this.jtResultados.getColumnModel().getColumn(0).setMinWidth(50);
        this.jtResultados.getColumnModel().getColumn(0).setMaxWidth(50);
        //Resultado
        this.jtResultados.getColumnModel().getColumn(1).setMinWidth(200);        
        //Sequência
        this.jtResultados.getColumnModel().getColumn(2).setMinWidth(175);        
        //Reconhecimento
        this.jtResultados.getColumnModel().getColumn(3).setMinWidth(430);
        if(this.jtResultados.getModel().getRowCount() < 8){
            ((DefaultTableModel) this.jtResultados.getModel()).setRowCount(this.jtResultados.getModel().getRowCount()+ (8 - this.jtResultados.getModel().getRowCount()));
        }
    }        
    
    /**
     * Método responsável por analisar o contexto
     */
    public void Analisar(){
        processaPalavras();
        if(!palavras.isEmpty()){
            regraCompilador.Analisar( palavras );
            exibeResultados();
        }    
    }
    
    /**
     * Processa e adiciona as palavras em uma lista para análise futura
     */
    private void processaPalavras(){
        palavras.clear();
        
        String contexto          = jtaContexto.getText();               
        String contextoSemEspaco = tiraExcessoEspacos( contexto );
        StringTokenizer st       = new StringTokenizer(contexto,"\n");        
    
        int contLinhaAtual = ConsideraEntersIniciais(contexto);
        while(st.hasMoreTokens()){
            contLinhaAtual++;                            
            
            String strLinhaAtual = tiraExcessoEspacos( st.nextToken() );            
            contextoSemEspaco = contextoSemEspaco.substring(contextoSemEspaco.indexOf(strLinhaAtual),
                                                            contextoSemEspaco.length());
            
            int quantPalavras = strLinhaAtual.length() - strLinhaAtual.replaceAll(" ", "").length();
            String[] param = new String[ quantPalavras ];
            param = strLinhaAtual.split(" ");
            for(int x = 0; x < param.length; x++){
                Palavra palavra = new Palavra(contLinhaAtual,
                                              param[x]);
                palavras.add(palavra);
            }                        
            contLinhaAtual += verificaQuebraLinhasEmBranco( strLinhaAtual,
                                                            contextoSemEspaco);
        } 
    }
    
    /**
     * Função responsável por considerar as primeiras quebras de linhas das palavras
     * @param contexto Contexto a ser analisado 
     * @return retorna a quantidade de quebra de linhas
     */
    private int ConsideraEntersIniciais(String contexto){
        int linhasConsideradas = 0;        
        while(contexto.toString().startsWith("\n")){
            linhasConsideradas++;
            contexto = contexto.substring(1, contexto.length());
        }
        return linhasConsideradas;
    }
    
    /**
     * Método responsável por incrementar as linhas que estão em branco
     * @param text Texto da linha em que está sendo analisada
     * @param contextoAtual Texto completo
     * @return Retorna a quandidade de linhas que estão com espaços em branco e pula elas para ficar correto a contagem de linhas
     */
    private int verificaQuebraLinhasEmBranco(String text, String contextoAtual){
        int linesBreak = 0;                            
        StringBuffer strLineBreak = new StringBuffer( text );
        strLineBreak.append("\n"); strLineBreak.append("\n");                        
        while(contextoAtual.contains( strLineBreak.toString() )){
            strLineBreak.append("\n");
            linesBreak++;
        }
        return linesBreak;
    }
    
    /**
     * Método responsável por retirar espaços antes e depois da linha analisada, bem como excesso de espaços no meio da linha
     * @param text Texto da linha que está sendo analisada
     * @return Retorna o texto da linha em espaços no começo e no final da linha, bem como sem excesso de espaços em branco no meio da linha
     */
    private String tiraExcessoEspacos(String text){
        String result = text.trim();
        while(result.contains("  ")){
            result = result.replaceAll("  ", " ");
        }
        return result;
    }
    
    /**
     * Método responsável por exibir os resultados do compilador
     */
    private void exibeResultados(){        
        limparResultados();
        if(regraCompilador.getSaida().size() > 0){            
            List<Saida> resultados = regraCompilador.getSaida();
            String colunas[] = {"Linha", // Colunas
                                "Resultado", 
                                "Sequência", 
                                "Reconhecimento"};
            DefaultTableModel modelo = new DefaultTableModel(colunas, 0);            
            for(int i = 0; i < resultados.size(); i++){
                Saida saida = resultados.get(i);                
                modelo.addRow(new String[]{String.valueOf(saida.getLinha()), //AdicionaLinhas
                                           saida.getResultado(), 
                                           saida.getSequencia(),
                                           saida.getReconhecimento()});            
            }
            jtResultados.setModel(modelo);            
            DefineTamanhoColunasResultado();
        }        
        regraCompilador.Limpar();
    }                
    
    /**
     * Método para limpar a tabela de resultados
     */
    private void limparResultados(){                                
        ((DefaultTableModel) jtResultados.getModel()).setRowCount(0);
        ((DefaultTableModel) jtResultados.getModel()).setRowCount(8);        
    }
    
    /**
     * Método responsável por limpar o contexto
     */
    public void Limpar(){                
        limparEntrada();        
        limparResultados();                                
    }
    
    /**
     * Método utilizado para limpar as variáveis/componentes de entrada
     */
    private void limparEntrada(){
        palavras.clear();
        jtaContexto.setText("");
    }
    
    /**
     * Método responsável por exibir a equipe
     */
    public void Equipe(){
       DialogEquipe.ExecutarDialogEquipe(this, true);       
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jbAnalisar = new javax.swing.JButton();
        jbLimpar = new javax.swing.JButton();
        jbEquipe = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtResultados = new javax.swing.JTable();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Reconhecedor de Lingaguem Formal");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMaximumSize(new java.awt.Dimension(865, 410));
        setMinimumSize(new java.awt.Dimension(865, 410));
        setPreferredSize(new java.awt.Dimension(865, 410));
        getContentPane().setLayout(null);

        jbAnalisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/Analisar.png"))); // NOI18N
        jbAnalisar.setText("Analisar");
        jbAnalisar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jbAnalisarMouseClicked(evt);
            }
        });
        getContentPane().add(jbAnalisar);
        jbAnalisar.setBounds(0, 190, 270, 32);

        jbLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/Limpar.png"))); // NOI18N
        jbLimpar.setText("Limpar");
        jbLimpar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jbLimparMouseClicked(evt);
            }
        });
        getContentPane().add(jbLimpar);
        jbLimpar.setBounds(273, 190, 305, 32);

        jbEquipe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/Equipe.png"))); // NOI18N
        jbEquipe.setText("Equipe");
        jbEquipe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jbEquipeMouseClicked(evt);
            }
        });
        getContentPane().add(jbEquipe);
        jbEquipe.setBounds(580, 190, 278, 32);

        jtResultados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Linha", "Resultado", "Sequência", "Reconhecimento"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jtResultados.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jtResultados.setColumnSelectionAllowed(true);
        jScrollPane2.setViewportView(jtResultados);
        jtResultados.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(0, 230, 860, 150);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbEquipeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEquipeMouseClicked
        Equipe();
    }//GEN-LAST:event_jbEquipeMouseClicked

    private void jbLimparMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbLimparMouseClicked
        Limpar();
    }//GEN-LAST:event_jbLimparMouseClicked

    private void jbAnalisarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbAnalisarMouseClicked
        Analisar();
    }//GEN-LAST:event_jbAnalisarMouseClicked
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrameCompilador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameCompilador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameCompilador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameCompilador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FrameCompilador.ExecutarFrameCompilador();
            }
        });
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton jbAnalisar;
    private javax.swing.JButton jbEquipe;
    private javax.swing.JButton jbLimpar;
    private javax.swing.JTable jtResultados;
    // End of variables declaration//GEN-END:variables
}
