import DTO.SettingsDTO;
import com.google.gson.Gson;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static javax.swing.JOptionPane.showMessageDialog;

public class JGameCMSUpdater extends JFrame {
    private static final ENVIRONMENT_MODE mode = ENVIRONMENT_MODE.PRODUCTION;
    private static final String developmentDir = "C:\\Users\\Eliezer\\Desktop\\WYDTESTE\\";
    private JPanel panelConteudo;
    private JProgressBar progressBar1;
    private JLabel label;
    private static final String gameExe = "WYD.exe";
    public SettingsDTO settingsDTO;
    private static JGameCMSUpdater instance;
    public static JGameCMSUpdater getInstance() {
        if (instance == null) {
            instance = new JGameCMSUpdater();
        }
        return instance;
    }
    public JGameCMSUpdater() {
        initComponents();
    }

    private void initComponents() {
        configuraTamanhoEFechar();
        centralizaTela();
        loadSettings();
        setContentPane(panelConteudo);
        setVisible(true);
        ClassLoader classLoader = this.getClass().getClassLoader();
        URL imageUrl = classLoader.getResource("logo.png");
        ImageIcon iconLogo = new ImageIcon(imageUrl);
        label.setIcon(iconLogo);
        progressBar1.setSize(500, 300);
    }

    private void loadSettings() {
        Gson gson = new Gson();
        String settingsFile = null;
        try {
            settingsFile = Files.readString(Path.of(getRootFolder() + "/GAMECMS.SETTINGS"));
            this.settingsDTO = gson.fromJson(settingsFile, SettingsDTO.class);
        } catch (IOException e) {
            settingsDTO = null;
        }

        this.settingsDTO = gson.fromJson(settingsFile, SettingsDTO.class);
    }

    private void fechar() {
        setVisible(false);
        dispose();
    }

    private void centralizaTela() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int frameWidth = getWidth();
        int frameHeight = getHeight();
        int x = (screenWidth - frameWidth) / 2;
        int y = (screenHeight - frameHeight) / 2;
        setLocation(x, y);
    }

    private void configuraTamanhoEFechar() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setSize(500, 535);
    }

    private void iniciarJogo() {
        progressBar1.setString("Atualizacao completa - Iniciando jogo...");
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(new File(getRootFolder() + "\\" + gameExe));
            fechar();
        } catch (Exception e) {
            progressBar1.setString(e.getMessage());
            fechar();
        }
    }

    static String getRootFolder() {
        String thisClassPath = JGameCMSUpdater.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        File thisJarFile = new File(thisClassPath);
        String jarDir = thisJarFile.getParentFile().getAbsolutePath().replaceAll("%20", " ");

        return mode == ENVIRONMENT_MODE.DEVELOPMENT ? developmentDir : jarDir;
    }

    public String getServerRootFolder(String rootFolder) {
        String restanteDaString = (rootFolder.contains("ClientAtualizado")) ? rootFolder.split("ClientAtualizado", 2)[0] : "";
        return restanteDaString;
    }

    private List<ArquivoUpdateResponseDTO> getArquivosLocaisComDiretoriosRaizTratados() {
        List<ArquivoUpdateResponseDTO> arquivosLocais = UpdaterUtils.getListaDeArquivosLocais(getRootFolder());
        arquivosLocais.forEach(
                n -> {
                    n.setDiretorio(n.getDiretorio().replace(getRootFolder(), "\\"));
                }
        );

        progressBar1.setString("Arquivos locais escaneados com sucesso.");
        return arquivosLocais;
    }

    private List<ArquivoUpdateResponseDTO> getArquivosServidor() throws IOException {
        progressBar1.setString("Buscando atualizacoes...");
        List<ArquivoUpdateResponseDTO> arquivosServidor = UpdaterUtils.getListaArquivosServidor();
        if (arquivosServidor == null) {
            showMessageDialog(this, "Não foi possivel conectar ao servidor de atualizacoes.");
            fechar();
        }
        progressBar1.setString("Atualizando... Iniciando comparacao de checksum.");
        progressBar1.setValue(0);
        return arquivosServidor;
    }

    public static void main(String[] args) {
        JGameCMSUpdater tela = getInstance();

        Thread t = new Thread(() -> {
            String directory = getRootFolder();
            List<ArquivoUpdateResponseDTO> arquivosLocais = tela.getArquivosLocaisComDiretoriosRaizTratados();
            List<ArquivoUpdateResponseDTO> arquivosServidor = null;
            try {
                arquivosServidor = tela.getArquivosServidor();
            } catch (IOException e) {
                LogUtils.write(LogUtils.LogType.ERROR, "Nao foi chamar o metodo getArquivosServidor. Detalhes: " + e.getMessage());
            }

            List<String> arquivosAtualizadosComSucesso = new ArrayList<>();

            AtomicInteger progress = new AtomicInteger();
            tela.progressBar1.setMaximum(arquivosServidor.size());
            arquivosServidor.forEach(arquivoNoServidor -> {
                progress.getAndIncrement();
                tela.progressBar1.setValue(progress.intValue());

                String dirArquivoFinal = arquivoNoServidor.getDiretorio().replace(tela.getServerRootFolder(arquivoNoServidor.getDiretorio()) + "ClientAtualizado", "");

                Optional<ArquivoUpdateResponseDTO> arquivoLocal = arquivosLocais.stream()
                        .filter(n -> n.getDiretorio().contains(dirArquivoFinal))
                        .findFirst();

                Long acceptableDif = 200_000_000L;
                if ((arquivoLocal.isPresent() && arquivoNoServidor.getDataModificao() > arquivoLocal.get().getDataModificao() + acceptableDif || arquivoLocal.isEmpty())
                ) {
                    try {
                        byte[] arquivoAtualizado = UpdaterUtils.getUpdatedFile(new String(Base64.getEncoder().encode(arquivoNoServidor.getDiretorio().getBytes())));
                        tela.progressBar1.setString("Baixando " + arquivoNoServidor.getNome() + "...");

                        String baseDirArquivoAtt = directory + dirArquivoFinal;
                        Path basePathArquivo = Paths.get(baseDirArquivoAtt);

                        if (arquivoLocal.isPresent()) {
                            tela.progressBar1.setString("Deletando: " + arquivoNoServidor.getNome());
                            Files.delete(basePathArquivo);
                            LogUtils.write(LogUtils.LogType.SUCCESS, "Updater - Deletando arquivo local para substituicao: " + arquivoLocal.get().getNome());
                        }

                        String destDir =  baseDirArquivoAtt.replace(arquivoNoServidor.getNome(), "");
                        boolean isNotRootFolder = !destDir.trim().equals("/");
                        boolean dirNotExists = !Files.isDirectory(Paths.get(destDir));
                        if (isNotRootFolder && dirNotExists) {
                            Files.createDirectories(Paths.get(destDir, ""));
                            LogUtils.write(LogUtils.LogType.SUCCESS, "Updater - Diretorio novo criado: " + destDir + " - Para o arquivo: " + arquivoNoServidor.getNome());
                        }

                        Files.write(basePathArquivo, arquivoAtualizado);

                        if(arquivoLocal.isPresent()) {
                            tela.progressBar1.setString(arquivoNoServidor.getNome() + "( L: " + arquivoLocal.get().getDataModificao() + "SV: " + arquivoNoServidor.getDataModificao() + " ) - Completo.");
                        }else{
                            tela.progressBar1.setString(arquivoNoServidor.getNome() + "- Completo.");

                        }
                        arquivosAtualizadosComSucesso.add(arquivoNoServidor.getNome());
                    } catch (IOException e) {
                        LogUtils.write(LogUtils.LogType.ERROR, "Nao foi possível baixar:" + arquivoNoServidor.getNome() + " - Reporte para a adm. Detalhe: " + e.getMessage());
                    }
                }

            });

            LogUtils.write(LogUtils.LogType.SUCCESS, "Verificacao / Atualizacao concluida. Arquivos atualizados: " + new Gson().toJson(arquivosAtualizadosComSucesso));
            tela.iniciarJogo();

        }, "Atualizador");
        t.start();
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelConteudo = new JPanel();
        panelConteudo.setLayout(new FormLayout("fill:d:grow", "center:d:noGrow"));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setBackground(new Color(-14941696));
        panel1.setForeground(new Color(-16777216));
        CellConstraints cc = new CellConstraints();
        panelConteudo.add(panel1, cc.xy(1, 1));
        label = new JLabel();
        label.setForeground(new Color(-16777216));
        label.setText("");
        panel1.add(label, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FormLayout("fill:d:grow", "center:max(d;4px):noGrow"));
        panel2.setBackground(new Color(-16777216));
        panel2.setForeground(new Color(-16777216));
        panel1.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(450, 80), null, null, 0, false));
        progressBar1 = new JProgressBar();
        progressBar1.setForeground(new Color(-6880768));
        progressBar1.setStringPainted(true);
        panel2.add(progressBar1, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelConteudo;
    }

}
