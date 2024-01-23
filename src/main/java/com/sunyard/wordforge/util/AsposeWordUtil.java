package com.sunyard.wordforge.util;

import com.aspose.words.License;
import com.sunyard.wordforge.complex.constant.LicenseConstant;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;

/**
 * Aspose.Words工具类
 *
 * @author Archer
 */
@Slf4j
public class AsposeWordUtil {
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
                license.setLicense(
                    Files.newInputStream(new File(String.valueOf(Paths.get(LicenseConstant.LIC))).toPath())
                );
                log.info("License set successfully.");
            } catch (Exception e) {
                log.error("registerLicense error", e);
            }
            loaded = true;
        }
    }
}
