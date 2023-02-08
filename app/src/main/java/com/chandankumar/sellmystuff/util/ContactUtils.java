package com.chandankumar.sellmystuff.util;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.net.URLEncoder;

public class ContactUtils {

    public static void makeCall(Context context, String phoneNumber){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("tel:"+phoneNumber));
        context.startActivity(intent);
    }

    public static void sendEmail(Context context, String email, String subject){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri data = Uri.parse("mailto:" + email + "?subject=" + Uri.encode(subject));
        intent.setData(data);
        context.startActivity(intent);
    }


    public static void sendWhatsAppMessage(Context context, String whatsappNumber, String message){
        PackageManager packageManager = context.getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            String url = "https://api.whatsapp.com/send?phone=+91"+ whatsappNumber +"&text=" + URLEncoder.encode(message, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                context.startActivity(i);
            }
        } catch (Exception e){
            Utils.showLongMessage(context, "Error: " + e.getMessage());
        }
    }

}
