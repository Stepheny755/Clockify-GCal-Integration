package com.example.timetracker

import java.io.*
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.view.View

class FileStorage(){
    companion object {
        fun write(fileName: String, fileData: String, context: Context) {
            val fileOStream: FileOutputStream;
            try {
                fileOStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                fileOStream.write(fileData.toByteArray());
            } catch (e: Exception) {
                e.printStackTrace();
            }
        }

        fun read(fileName: String, fileData: String, context: Context): String {
            var fileIStream: FileInputStream? = null;
            fileIStream = context.openFileInput(fileName);
            var IStreamReader: InputStreamReader = InputStreamReader(fileIStream);
            val bufferedReader: BufferedReader = BufferedReader(IStreamReader);
            val stringBuilder: StringBuilder = StringBuilder();
            var text: String? = null;
            while ({ text = bufferedReader.readLine(); text }() != null) {
                stringBuilder.append(text)
            }
            return stringBuilder.toString();
        }
    }
}