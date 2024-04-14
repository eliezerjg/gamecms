import DTO.SettingsDTO;
import DTO.UpdaterResponseDTO;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UpdaterUtils {
    private static final Gson gson;
    private static final String BACKEND_GAME_CMS_URL = "https://back.gamecms.com.br";

    static{
        gson = new Gson();
    }

    public static boolean versionMatches(){
        String url = BACKEND_GAME_CMS_URL + "/updater/customer/:customerId/version/:version";
        url = url.replace(":customerId", JSelfUpdater.getInstance().settingsDTO.id.toString())
                .replace(":version", JSelfUpdater.getInstance().settingsDTO.version.toString());

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            String responseBody =  response.body().string();
            UpdaterResponseDTO responseDTO = gson.fromJson(responseBody, UpdaterResponseDTO.class);
            return JSelfUpdater.getInstance().settingsDTO.version.equals(responseDTO.getVersion());
        } catch (IOException e) {
                return false;
        }
    }

    public static Long getServerVersion(){
        String url = BACKEND_GAME_CMS_URL + "/updater/customer/:customerId/version/:version";
        url = url.replace(":customerId", JSelfUpdater.getInstance().settingsDTO.id.toString())
                .replace(":version", JSelfUpdater.getInstance().settingsDTO.version.toString());

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            String responseBody =  response.body().string();
            UpdaterResponseDTO responseDTO = gson.fromJson(responseBody, UpdaterResponseDTO.class);
            return responseDTO.getVersion();
        } catch (IOException e) {
            return null;
        }
    }

    public static void updateAndReplaceUpdaterFile() {
        String url = BACKEND_GAME_CMS_URL + "/updater/customer/:customerId";
        url = url.replace(":customerId", JSelfUpdater.getInstance().settingsDTO.id.toString());

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            byte[] responseBodyBytes =  response.body().bytes();
            Path updaterPath = Paths.get(JSelfUpdater.getRootFolder() + "/UPD.exe");
            if(Files.exists(updaterPath)){
                Files.delete(updaterPath);
            }

            Files.write(updaterPath, responseBodyBytes);
            LogUtils.write(LogUtils.LogType.SUCCESS, "Self Updater - Atualizado.");

            Path path = Paths.get(JSelfUpdater.getRootFolder() + "/GAMECMS.SETTINGS");
            SettingsDTO settingsDTO = JSelfUpdater.getInstance().settingsDTO;
            settingsDTO.version = getServerVersion();
            Files.writeString(path, gson.toJson(settingsDTO, SettingsDTO.class));
            LogUtils.write(LogUtils.LogType.SUCCESS, "Self Updater - Settings Atualizado, versão alterada para: " + settingsDTO.version);
        } catch (IOException e) {
            LogUtils.write(LogUtils.LogType.ERROR, "Self Updater - Não foi possível atualizar o updater. Detalhes: " + e.getMessage());
        }
    }


}
