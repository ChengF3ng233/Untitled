import cn.feng.untitled.music.api.MusicAPI;
import cn.feng.untitled.music.api.QRCode;
import cn.feng.untitled.music.api.QRCodeState;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * @author ChengFeng
 * @since 2024/8/11
 **/
public class Test {
    public static void main(String[] args) throws IOException {
        QRCode qrCode = MusicAPI.genQRCode();
        File f = new File("QRCode.png");
        f.createNewFile();
        ImageIO.write(qrCode.image(), "png", f);

        QRCodeState result;

        do {
            result = MusicAPI.getScanResult(qrCode.key());
            System.out.println("Response: State: " + result.name());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } while (result == QRCodeState.WAITING_SCAN || result == QRCodeState.WAITING_CONFIRM);

        System.out.println("Succeeded");
        MusicAPI.getUserInfo();
    }
}
