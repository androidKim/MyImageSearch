package com.midas.myimagesearch;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;

import com.midas.myimagesearch.core.APIClient;
import com.midas.myimagesearch.core.APIInterface;
import com.midas.myimagesearch.util.NetworkCtrl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MyApp extends Application
{
    /******************** Define ********************/

    /******************** Member ********************/
    public APIInterface m_APIInterface = null;
    public NetworkCtrl m_NetworkCtrl = null;
    public boolean m_bInit = false;
    /******************** Cotroller ********************/

    /******************** User function ********************/
    //------------------------------------------------
    //
    public MyApp()
    {

    }
    //------------------------------------------------
    //
    public MyApp(Context pContext)
    {
        if(m_bInit == false)
        {
            if(pContext == null)
                return;

            //getKeyHash(pContext);

            m_APIInterface = APIClient.getClient().create(APIInterface.class);//web request ctrl
            m_NetworkCtrl = new NetworkCtrl(pContext);
            m_bInit = true;
        }
    }

    //------------------------------------------------
    //
    public void getKeyHash(final Context context)
    {
        String keyHash = "";
        try
        {
            PackageInfo info = context.getPackageManager().getPackageInfo("com.midas.myimagesearch", PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("keyHash",keyHash);
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
    }
    //------------------------------------------------
    //
    public void showMessageDlg(Context pContext, String title, String message)
    {
        if(pContext == null || title == null || message == null)
            return;

        AlertDialog.Builder builder = new AlertDialog.Builder(pContext);
        builder.setTitle(title);
        builder.setMessage(message);
        // Add the buttons
        builder.setPositiveButton(pContext.getResources().getString(R.string.msg_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
