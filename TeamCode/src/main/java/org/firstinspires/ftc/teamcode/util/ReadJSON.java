package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;

public class ReadJSON {
    public String value = "";
    public ReadJSON(String filename) {
        File file = AppUtil.getInstance().getSettingsFile(filename);
        value = ReadWriteFile.readFile(file);
    }
}
