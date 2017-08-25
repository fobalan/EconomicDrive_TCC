package br.com.economicdrive.communicator;

import java.util.List;

import br.com.economicdrive.Information;
import br.com.economicdrive.adapter.MyAdapter;

public interface Communicator {
	void onDialogMessage(String data);
	void OnDeleteMessage(List<Information> deleteList, MyAdapter myAdapter, String information);
}
