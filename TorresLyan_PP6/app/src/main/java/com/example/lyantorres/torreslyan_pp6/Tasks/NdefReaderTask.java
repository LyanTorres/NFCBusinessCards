package com.example.lyantorres.torreslyan_pp6.Tasks;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

// Lyan Torres Labiosa

// 1802

// com.example.lyantorres.torreslyan_pp6.Tasks

public class NdefReaderTask extends AsyncTask<Tag, Void, String> {

    public NDEFAsyncTaskInterface mInterface = null;

    public interface NDEFAsyncTaskInterface {
        void finishedReading(String _result);
    }

    @Override
    protected String doInBackground(Tag... params) {
        Tag tag = params[0];

        Ndef ndef = Ndef.get(tag);
        if (ndef == null) {
            // NDEF is not supported by this Tag.
            return null;
        }

        NdefMessage ndefMessage = ndef.getCachedNdefMessage();

        NdefRecord[] records = ndefMessage.getRecords();
        for (NdefRecord ndefRecord : records) {
            if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                try {
                    return readText(ndefRecord);
                } catch (UnsupportedEncodingException e) {
                    Log.i("=== LYAN ===", "Unsupported Encoding", e);
                }
            }
        }

        return null;
    }

    private String readText(NdefRecord record) throws UnsupportedEncodingException {


        byte[] payload = record.getPayload();

        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

        int languageCodeLength = payload[0] & 0063;

        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            mInterface.finishedReading(result);
        }
    }

}
