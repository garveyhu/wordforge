package com.sunyard.wordforge.util;

import com.aspose.words.License;
import java.io.File;
import java.nio.file.Files;
import lombok.extern.slf4j.Slf4j;

/**
 * Aspose.Words工具类
 *
 * @author Archer
 */
@Slf4j
public class AsposeWordUtil {

    private static final String LIC = "C:\\Users\\lenovo\\Downloads\\aspose\\license\\Aspose.TotalProductFamily(20240125).lic";

    private static final AsposeWordUtil wordStyleUtil = new AsposeWordUtil();

    private static boolean loaded = false;

    public static AsposeWordUtil getInstance() {
        return wordStyleUtil;
    }

    /**
     * license认证
     */
    public void registerLicense() {
        if (!loaded) {
            License license = new License();
            try {
                license.setLicense(Files.newInputStream(new File(LIC).toPath()));
                log.info("License set successfully.");
            } catch (Exception e) {
                log.error("registerLicense error", e);
            }
            loaded = true;
        }
    }
}


