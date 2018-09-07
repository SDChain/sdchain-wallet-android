package com.io.sdchain.utils;

import android.graphics.Color;

import com.io.sdchain.R;
import com.io.sdchain.base.BaseApplication;

import cn.bertsir.zbar.QrConfig;
import cn.bertsir.zbar.QrManager;

/**
 * @author : xiey
 * @project name : sdchian.
 * @package name  : com.io.sdchian.utils.
 * @date : 2018/6/26.
 * @signature : do my best.
 * @explain :
 */
public class QrUtils {
    private static QrConfig qrConfig() {
        QrConfig qrConfig = new QrConfig.Builder()
                .setDesText(ResStringUtils.getString(R.string.info423))//Scan the text under the frame
                .setShowDes(false)//Displays the text below the scan box
                .setShowLight(true)//Display the flashlight button
                .setShowTitle(true)//show Title
                .setShowAlbum(true)//Displays the select button from the album
                .setCornerColor(BaseApplication.getApplicationInstance().getResources().getColor(R.color.theme))//Set scan frame color
                .setLineColor(BaseApplication.getApplicationInstance().getResources().getColor(R.color.theme))//Set the scan line color
                .setLineSpeed(QrConfig.LINE_MEDIUM)//Set the scan line speed
                .setScanType(QrConfig.TYPE_QRCODE)//Set the scan type (qr code, barcode, all, custom, default is qr code)
                .setScanViewType(QrConfig.SCANVIEW_TYPE_QRCODE)//Set scan frame type (qr code or barcode, by default is qr code)
                .setCustombarcodeformat(QrConfig.BARCODE_I25)//This is only valid if the scan type is TYPE_CUSTOM
                .setPlaySound(true)//Whether to scan the bi~ sound successfully
//                .setDingPath(R.raw.test)//Set prompt tone (not set as the default Ding~)
                .setIsOnlyCenter(false)//Whether only the contents in the box are recognized (the default is full-screen recognition)
                .setTitleText(ResStringUtils.getString(R.string.info424))//set Tilte text
                .setTitleBackgroudColor(BaseApplication.getApplicationInstance().getResources().getColor(R.color.translation))//Set the status bar color
                .setTitleTextColor(Color.WHITE)//set Title text color
                .create();
        return qrConfig;
    }

    public static QrManager getInstance() {
        return QrManager.getInstance().init(qrConfig());
    }
}
