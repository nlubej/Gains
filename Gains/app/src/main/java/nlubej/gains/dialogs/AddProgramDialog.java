package nlubej.gains.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.BreakIterator;
import java.util.ArrayList;

import nlubej.gains.DataTransferObjects.ProgramDto;
import nlubej.gains.DataTransferObjects.RoutineDto;
import nlubej.gains.Database.QueryFactory;
import nlubej.gains.Enums.AddDialogType;
import nlubej.gains.R;
import nlubej.gains.Views.Program;
import nlubej.gains.Views.Routine;
import nlubej.gains.interfaces.OnItemAdded;
import nlubej.gains.interfaces.onActionSubmit;

/**
 * Created by nlubej on 22.10.2015.
 */
public class AddProgramDialog extends DialogFragment implements View.OnClickListener
{

    private Object fragmentClass;
    private QueryFactory db;
    private OnItemAdded callback;
    private Context context;
    private int programId;
    private MaterialEditText program;
    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            callback = (OnItemAdded<ProgramDto>) fragmentClass;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException("Calling Fragment must implement OnSubmit");
        }
    }

    public void SetData(Program fragment, QueryFactory database)
    {
        this.fragmentClass = fragment;
        db = database;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        context = getActivity();



        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_edit, null);
        program = (MaterialEditText) view.findViewById(R.id.programName);

        Button yes = (Button)view.findViewById(R.id.btn_yes);
        Button no = (Button)view.findViewById(R.id.btn_no);

        yes.setOnClickListener(this);
        no.setOnClickListener(this);


        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();

        return alertDialog;
    }


    @Override
    public void onClick(View v)
    {
        int programId = 0;

        Boolean wantToCloseDialog = true;
        switch (v.getId())
        {

            case R.id.btn_yes:
                if (program.getText().toString().compareTo("") != 0)
                {
                    db.Open();
                    programId = db.InsertProgram(program.getText().toString());
                    db.Close();
                }
                else
                {
                    program.setError("Name must not be empty.");
                    wantToCloseDialog = false;
                }

                if (wantToCloseDialog)
                {
                    callback.OnAdded(new ProgramDto(programId, program.getText().toString()));
                    alertDialog.dismiss();
                }
                break;
            case R.id.btn_no:
                alertDialog.dismiss();
                break;
        }
    }
}
