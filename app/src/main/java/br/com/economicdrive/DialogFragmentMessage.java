package br.com.economicdrive;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.List;

public class DialogFragmentMessage extends DialogFragment implements DialogInterface.OnClickListener{
	private Communicator communicator;
	private String title;
	private String message;
	private List<Information> deleteList;
	private MyAdapter myAdapter;
	private String information;
	
	public DialogFragmentMessage(String title, String message){
		this.title = title;
		this.message = message;
	}

	public DialogFragmentMessage(String title, String message, List<Information> deleteList, MyAdapter myAdapter, String information){
		this.title = title;
		this.message = message;
		this.deleteList = deleteList;
		this.myAdapter = myAdapter;
		this.information = information;

	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
			.setIcon(R.drawable.ic_launcher_3)
			.setTitle(title)
			.setMessage(message)
			.setPositiveButton("Sim", this)
			.setNegativeButton("Não", this).create();
	}
	@Override
	public void onAttach(Activity activity) {
		communicator = (Communicator) getActivity();
		super.onAttach(activity);
	}
	@Override
	public void onClick(DialogInterface dialog, int which) {
		if ( which == DialogInterface.BUTTON_POSITIVE){
			if (title.equals("Sair")) {
				communicator.onDialogMessage("Sim");
			}else{
				communicator.OnDeleteMessage(deleteList, myAdapter, information);
			}
		}
		else if ( which == DialogInterface.BUTTON_NEGATIVE){
			communicator.onDialogMessage("Não");
		}
	}
}
