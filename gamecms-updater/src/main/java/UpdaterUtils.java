import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static javax.swing.JOptionPane.showMessageDialog;

public class UpdaterUtils {
    private static final Gson gson;
    private static final String BACKEND_GAME_CMS_URL = "https://back.gamecms.com.br";

    static{
        gson = new Gson();
    }

    public static java.util.List<ArquivoUpdateResponseDTO> getListaDeArquivosLocais(String dir){
        try (Stream<Path> paths = Files.walk(Paths.get(dir))) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(file -> new ArquivoUpdateResponseDTO(
                            file.toFile().getName(),
                            file.toFile().getPath(),
                            file.toFile().lastModified(),
                            file.toFile().length()
                    ))
                    .toList();
        }
        catch (Exception e){
            return java.util.List.of();
        }
    }

    public static List<ArquivoUpdateResponseDTO> getListaArquivosServidor() {
        String url = BACKEND_GAME_CMS_URL + "/updater/customer/:customerId/getfiles";
        url = url.replace(":customerId", JGameCMSUpdater.getInstance().settingsDTO.id.toString());
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            String responseBody = Objects.requireNonNull(response.body()).string();
            if(responseBody.contains("getUpdaterVersion - Customer Inativo")){
                throw new Exception("Servidor Inativo, regularize as pendencias financeiras.");
            }
            return gson.fromJson(responseBody, new TypeToken<ArrayList<ArquivoUpdateResponseDTO>>(){}.getType());
        } catch (Exception e) {
            showMessageDialog(JGameCMSUpdater.getInstance(), "Nao foi possivel conectar ao servidor. Detalhes: " + e.getMessage());
            return null;
        }
    }

    public static byte[] getUpdatedFile(String pathNameEncoded) {
        String url = BACKEND_GAME_CMS_URL + "/updater/customer/:customerId/getfile/:filename";
        url = url
                .replace(":customerId", JGameCMSUpdater.getInstance().settingsDTO.id.toString())
                .replace(":filename", pathNameEncoded);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            return  response.body().bytes();
        } catch (Exception e) {
                showMessageDialog(JGameCMSUpdater.getInstance(), "Nao foi possivel conectar ao servidor. Detalhes: " + e.getMessage());
                return null;
        }
    }


}
