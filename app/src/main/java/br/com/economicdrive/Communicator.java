package br.com.economicdrive;

import java.util.List;

public interface Communicator {
	void onDialogMessage(String data);
	void OnDeleteMessage(List<Information> deleteList, MyAdapter myAdapter, String information);
}
