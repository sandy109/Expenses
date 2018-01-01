package me.nijraj.expenses.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import me.nijraj.expenses.R;
import me.nijraj.expenses.utils.Database;

import static android.app.Activity.RESULT_OK;

/**
 * Created by buddha on 12/15/17.
 */

public class FragmentSettings extends Fragment {

    private static final int PERMISSION_CONST = 519;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        setHasOptionsMenu(false);
        getActivity().setTitle(R.string.settings);
        Button buttonBackup = view.findViewById(R.id.button_backup);
        Button buttonRestore = view.findViewById(R.id.button_restore);
        Button buttonReset = view.findViewById(R.id.button_reset);

        buttonBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CONST);
                else
                    performBackup();
            }
        });

        buttonRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 1);
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearDatabase();
            }
        });
        return view;
    }

    private void clearDatabase() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.warning));
        builder.setMessage(getResources().getString(R.string.clear_warning));
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.do_continue, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Database.clear();
                Snackbar.make(getView(), R.string.database_cleared, Snackbar.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CONST){
            if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                performBackup();
            }else{
                Snackbar.make(getView(), getResources().getString(R.string.permission_denied), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void performBackup() {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File outputFile = Database.export(getContext(), dir);
            if(outputFile != null)
                Snackbar.make(getView(), String.format("Saved as %s", outputFile.getName()), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            Uri uri = data.getData();
            try {
                InputStream reader = getActivity().getContentResolver().openInputStream(uri);
                StringBuilder builder = new StringBuilder();
                while (reader.available() != 0)
                    builder.append((char)reader.read());
                JSONObject json = new JSONObject(builder.toString());
                promptConfirmation(json);
            } catch (IOException e){
                Snackbar.make(getView(), "Can not read the file", Snackbar.LENGTH_SHORT).show();
            } catch (JSONException e){
                Snackbar.make(getView(), "Invalid backup file", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void restoreBackup(JSONObject json){
        if(Database.fromJSON(json)){
            Snackbar.make(getView(), getResources().getString(R.string.restore_success), Snackbar.LENGTH_LONG).show();
        }else{
            Snackbar.make(getView(), getResources().getString(R.string.restore_error), Snackbar.LENGTH_LONG).show();
        }
    }

    private void promptConfirmation(final JSONObject json) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setMessage(getResources().getString(R.string.restore_confirmation));
        dialog.setTitle(getResources().getString(R.string.warning));
        dialog.setCancelable(false);

        dialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                restoreBackup(json);
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        dialog.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }
}
