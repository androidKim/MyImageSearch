package com.midas.myimagesearch.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Map;
import java.util.Set;

public class SharedPrefCtrl {

    /******************************* Define *******************************/
    public static final String SP_KEY_IMG_STORAGE = "SP_KEY_IMG_STORAGE";

    /******************************* Member *******************************/
    private SharedPreferences m_Preference = null;

    //------------------------------------------------------------------------
    //
    public SharedPrefCtrl(Context pContext) {
        Init(pContext);
    }

    //------------------------------------------------------------------------
    //
    public void Uninit() {
        if (m_Preference != null) {
            m_Preference = null;

        }
    }

    //------------------------------------------------------------------------
    //
    public boolean Init(Context context) {
        m_Preference = PreferenceManager.getDefaultSharedPreferences(context);
        if (m_Preference == null) {
            return false;
        }

        return true;
    }

    //------------------------------------------------------------------------
    //
    public void clearData(Context context) {
        m_Preference = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = m_Preference.edit();
        editor.clear();
        editor.commit();
    }

    /*********************************  Function *********************************/
    //------------------------------------------------------------------------
    //
    private void SavePreferenceStr(String strKey, String strValue) {
        SharedPreferences.Editor editor = m_Preference.edit();
        editor.putString(strKey, strValue);
        editor.commit();
    }

    private String LoadPreferenceStr(String strKey, String strDefault) {
        if (m_Preference == null)
            return null;

        return m_Preference.getString(strKey, strDefault);
    }

    //------------------------------------------------------------------------
    //
    private void SavePreferenceInt(String strKey, int nValue) {
        SharedPreferences.Editor editor = m_Preference.edit();
        editor.putInt(strKey, nValue);
        editor.commit();
    }

    private int LoadPreferenceInt(String strKey, int nDefault) {
        if (m_Preference == null)
            return nDefault;

        return m_Preference.getInt(strKey, nDefault);
    }

    //------------------------------------------------------------------------
    //
    private void SavePreferenceLong(String strKey, long nValue) {
        SharedPreferences.Editor editor = m_Preference.edit();
        editor.putLong(strKey, nValue);
        editor.commit();
    }

    private long LoadPreferenceLong(String strKey, long nDefault) {
        if (m_Preference == null)
            return nDefault;

        return m_Preference.getLong(strKey, nDefault);
    }

    //------------------------------------------------------------------------
    //
    private void SavePreferenceFloat(String strKey, float nValue) {
        SharedPreferences.Editor editor = m_Preference.edit();
        editor.putFloat(strKey, nValue);
        editor.commit();
    }

    private float LoadPreferenceFolat(String strKey, float nDefault) {
        if (m_Preference == null)
            return nDefault;

        return m_Preference.getFloat(strKey, nDefault);
    }

    //------------------------------------------------------------------------
    //
    private void SavePreferenceBoolean(String strKey, boolean bDefault) {
        SharedPreferences.Editor editor = m_Preference.edit();
        editor.putBoolean(strKey, bDefault);
        editor.commit();
    }

    private boolean LoadPreferenceBoolean(String strKey, boolean bDefault) {
        if (m_Preference == null)
            return bDefault;

        return m_Preference.getBoolean(strKey, bDefault);
    }

    //------------------------------------------------------------------------
    //
    private void SavePreferenceSet(String strKey, Set<String> setDefault) {
        SharedPreferences.Editor editor = m_Preference.edit();
        editor.remove(strKey);//
        editor.commit();

        editor.putStringSet(strKey, setDefault);//
        editor.commit();
    }

    private Set<String> LoadPreferenceSet(String strKey, Set<String> setDefault) {
        if (m_Preference == null)
            return setDefault;

        return m_Preference.getStringSet(strKey, setDefault);
    }

    /************************** User Function **************************/
    //------------------------------------------------------------------------
    //
    public void setImageUrlJsonData(String strValue)
    {
        SavePreferenceStr(SP_KEY_IMG_STORAGE, strValue);
    }
    //------------------------------------------------------------------------
    //
    public String getImageUrlJsonData()
    {
        String strResult = LoadPreferenceStr(SP_KEY_IMG_STORAGE, "");
        return strResult;
    }
}
