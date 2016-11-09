import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.lang.System;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.RenderingHints;
import java.net.URL;
import javax.swing.BorderFactory;
import java.lang.Math;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import javax.swing.border.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;

// REF: https://material.google.com/
// http://danilotl.com.br/blog/reconhecendo-caracteres-em-imagens-com-java-e-tess4j/
// https://en.wikipedia.org/wiki/Optical_character_recognition
// https://github.com/tesseract-ocr
// http://www.fatecsp.br/dti/tcc/tcc00068.pdf
// http://docs.opencv.org/2.4/doc/tutorials/introduction/desktop_java/java_dev_intro.html
// pixelCoordinate = worldCoordinate * 2zoomLevel

class Window implements ActionListener{
  // Atributos
  private BufferedImage image;
  private JFrame window;
  private JPanel buttonPanel;
  private JLabel mainPanel;
  private JFileChooser fileChooser;
  private JPanel legenda;

  CustonTextField t1, t2, t3;
  JSeparator sep5, sep6, sep7;

  //----------------------------------------------------------------------------
  // Actions: enum contendo todas as acoes possiveis dentro da janela
  //----------------------------------------------------------------------------
  protected enum Actions {
    GET_IMAGE, ROTATE, OPEN_MAP, GET_VALUE, NULL_ACTION, CLOSE
  }

  //----------------------------------------------------------------------------
  // Construtor Padrao
  //----------------------------------------------------------------------------
  public Window() {
    window = null;
    buttonPanel = null;
    mainPanel = null;
    fileChooser = null;
    image = null;
    legenda = null;
  }

  //----------------------------------------------------------------------------
  // showIt/hideIt: metodos para "esconder" e "aparecer" a janela
  //----------------------------------------------------------------------------
  public void showIt() { if(window != null) window.setVisible(true); }
  public void hideIt() { if(window != null) window.setVisible(false); }

  //----------------------------------------------------------------------------
  // custonSeparator: cria um separador customizado
  //----------------------------------------------------------------------------
  private JSeparator custonSeparator() {
    JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
    sep.setMaximumSize( new Dimension(Integer.MAX_VALUE, 1) );
    sep.setBackground(MyConstants.COLOR_4.brighter());
    sep.setForeground(MyConstants.COLOR_4.brighter());

    return sep;
  }

  //----------------------------------------------------------------------------
  // imgDataBoxesVisibility:
  //----------------------------------------------------------------------------
  private void imgDataBoxesVisibility(boolean b) {
    t1.setVisible(b);
    t2.setVisible(b);
    t3.setVisible(b);
    sep5.setVisible(b);
    sep6.setVisible(b);
    sep7.setVisible(b);
  }

  //----------------------------------------------------------------------------
  // createSubBox: cria a 'caixa' de legenda
  //----------------------------------------------------------------------------
  private void createSubBox() {
    legenda = new JPanel();
    legenda.setOpaque(false);
    TitledBorder b = BorderFactory.createTitledBorder(
      new LineBorder(MyConstants.COLOR_4.brighter()), "Legenda");
    b.setTitleColor(Color.WHITE);
    legenda.setBorder(b);
  }

  //----------------------------------------------------------------------------
  // createButtonPanel: cria o painel que contem os botoes e os mesmos.
  //----------------------------------------------------------------------------
  private void createButtonPanel() {
    buttonPanel = new JPanel();
    GroupLayout layout = new GroupLayout(buttonPanel);
    buttonPanel.setLayout(layout);
    buttonPanel.setBackground(MyConstants.COLOR_4);

    // Componentes do GroupLayout
    createSubBox();
    CustonButton b1 = new CustonButton("Select Image", Actions.GET_IMAGE.name(), this);
    CustonButton b2 = new CustonButton("Identify Value", Actions.GET_VALUE.name(), this);
    CustonButton b3 = new CustonButton("Open Map", Actions.OPEN_MAP.name(), this);
    CustonButton b4 = new CustonButton("Close", Actions.CLOSE.name(), this);
    t1 = new CustonTextField("Latitude");
    t2 = new CustonTextField("Longitude");
    t3 = new CustonTextField("Angle");
    JSeparator sep1 = custonSeparator();
    JSeparator sep2 = custonSeparator();
    JSeparator sep3 = custonSeparator();
    JSeparator sep4 = custonSeparator();
    sep5 = custonSeparator();
    sep6 = custonSeparator();
    sep7 = custonSeparator();

    imgDataBoxesVisibility(false);

    BufferedImage myPicture = null;
    try {
      myPicture = ImageIO.read(new File("src/resources/icon.png"));
    } catch(Exception e) { e.printStackTrace(); }

    JLabel picLabel = new JLabel(new ImageIcon(myPicture));

    // Layout horizontal
    layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
      //.addComponent(picLabel, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(b1, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(sep1, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(t1, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(sep5, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(t2, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(sep6, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(t3, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(sep7, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(b2, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(sep2, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(b3, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(sep3, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(b4, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(sep4, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(legenda, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    // Layout vertical
    layout.setVerticalGroup(layout.createSequentialGroup()
      .addGap(40)
      .addComponent(b1)
      .addComponent(sep1)
      .addComponent(t1)
      .addComponent(sep5)
      .addComponent(t2)
      .addComponent(sep6)
      .addComponent(t3)
      .addComponent(sep7)
      .addComponent(b2)
      .addComponent(sep2)
      .addComponent(b3)
      .addComponent(sep3)
      .addComponent(b4)
      .addComponent(sep4)
      .addGap(20)
      .addComponent(legenda)
    );
  }

  //----------------------------------------------------------------------------
  // createMainPanel: cria o painel principal e o BufferedImage que contem a
  // imagem a ser trabalhada
  //----------------------------------------------------------------------------
  private void createMainPanel() {
    image = new BufferedImage(650, 650, BufferedImage.TYPE_INT_RGB);

    // criando o painel para "ajustar" a imagem
    mainPanel = new JLabel(new ImageIcon(image)) {
      // sobrescrevendo o metodo de ajustar o tamanho
      @Override
      public Dimension getPreferredSize() {
         return new Dimension(image.getWidth(), image.getHeight());
      }
    };

    // inicializa o painel todo branco
    Graphics g = image.getGraphics();
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, 640, 640);
    g.dispose();

    // criando borda por questoes esteticas
    mainPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 10));
  }

  //----------------------------------------------------------------------------
  // createFileChooser: cria a janela de selecao de arquivo
  //----------------------------------------------------------------------------
  private void createFileChooser() {
    fileChooser = new JFileChooser();

    // filtro da janela de aquivos
    FileNameExtensionFilter filter =
      new FileNameExtensionFilter("IMAGE FILES", "png", "jpeg");
    fileChooser.setFileFilter(filter);

    // pasta default
    String path = System.getProperty("user.home") +
      System.getProperty("file.separator") + "Pictures";
    fileChooser.setCurrentDirectory(new File(path));
  }

  //----------------------------------------------------------------------------
  // openFileChooser: abre a janela de selecao de arquivo
  //----------------------------------------------------------------------------
  private void openFileChooser() {
    int fileVal = fileChooser.showOpenDialog(window);

    // acao de selecionar imagem
    if (fileVal == JFileChooser.APPROVE_OPTION) {
      File file = fileChooser.getSelectedFile();

      //tenta ler a imagem
      try {
        image = ImageIO.read(file);
        mainPanel.setIcon(new ImageIcon(image));
      } catch(Exception e) { System.out.println("ERRO: Arquivo Invalido"); }


      t1.setText("");
      t2.setText("");
      t3.setText("");
      imgDataBoxesVisibility(true);
      buttonPanel.repaint();
      mainPanel.repaint();
    }
  }

  //----------------------------------------------------------------------------
  // loadingAnimation: cria uma animacao de carregamento e anima o frame usando
  // uma thread secundaria
  //----------------------------------------------------------------------------
  public void loadingAnimation(boolean load) {
    // Criando outra thread
    SwingUtilities.invokeLater(new Runnable() {
        // Metodo principal da thread
        public void run() {
          if(!load) {
            ImageIcon loading = new ImageIcon("src/resources/load.gif");
            mainPanel.setIcon(loading);
            mainPanel.repaint();
          }
        }
    });
  }

  //----------------------------------------------------------------------------
  // loadMap: carrega o mapa
  //----------------------------------------------------------------------------
  public void loadMap() {
    imgDataBoxesVisibility(false);
    buttonPanel.repaint();


    MapView map = new MapView(image, mainPanel, "-20.0113562", "-44.0912681");
    map.marker("-20.0113562", "-44.0912681", 'A');
    map.marker("-19.971031", "-44.034286", 'B');
    map.marker("-19.946022", "-44.111534", 'C');
    map.execute();
    loadingAnimation(map.getLoad());
    mainPanel.repaint();
  }

  //----------------------------------------------------------------------------
  // writeCoordenates: escreve as coordenadas no arquivo
  //----------------------------------------------------------------------------
  public void writeCoordenates(String str) {
    if(str == null) return;

    try {
      BufferedWriter bw = new BufferedWriter(
        new FileWriter("src/resources/coor.txt", true));
      bw.write(str);
      bw.close();
    }catch (Exception e) { e.printStackTrace(); }
  }

  //----------------------------------------------------------------------------
  // getTextValues: recebe os valores do texto e os checka
  //----------------------------------------------------------------------------
  public String getTextValues() {
    if(!t1.checkValue() || !t2.checkValue()) return null;
    return t1.getText() + t2.getText() + "\n";
  }

  //----------------------------------------------------------------------------
  // getValueAction: metodo do botao Identify Value
  //----------------------------------------------------------------------------
  public void getValueAction() {
    imgDataBoxesVisibility(false);
    buttonPanel.repaint();

    writeCoordenates(getTextValues());
  }

  //----------------------------------------------------------------------------
  // create: cria a janela e os elementos dentro da mesma.
  //----------------------------------------------------------------------------
  public void create() {
    window = new JFrame();

    // criando componentes
    createFileChooser();
    createMainPanel();
    createButtonPanel();

    // propriedades da janela
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setLayout(new BorderLayout());
    window.getContentPane().setBackground(Color.WHITE);
    window.add(buttonPanel, BorderLayout.LINE_START);
    window.add(mainPanel, BorderLayout.CENTER);
    window.pack();
    window.setMinimumSize(new Dimension(window.getWidth(), window.getHeight()));
  }

  //----------------------------------------------------------------------------
  // actionPerformed: metodo obrigatorio da interface ActionListener que
  // responde a alguma acao ocorrida na janela (Acoes contidas no Enum)
  //----------------------------------------------------------------------------
  @Override
  public void actionPerformed(ActionEvent event) {
    if (event.getActionCommand() == Actions.GET_IMAGE.name()) { openFileChooser(); }
    if (event.getActionCommand() == Actions.GET_VALUE.name()) { getValueAction(); }
    if (event.getActionCommand() == Actions.OPEN_MAP.name()) { loadMap(); }
    if (event.getActionCommand() == Actions.CLOSE.name()) { window.dispose(); }
  }
}
