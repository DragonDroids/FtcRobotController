package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;

public class SaveJSON {
    public SaveJSON(String filename, String toSave) {
//        String filename = "AdafruitIMUCalibration.json";
        File file = AppUtil.getInstance().getSettingsFile(filename);
        ReadWriteFile.writeFile(file, toSave);
    }
}

