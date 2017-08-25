package br.com.economicdrive.fragment;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import br.com.economicdrive.R;

public class DatePickerFragment extends DialogFragment implements
        DatePickerDialog.OnDateSetListener {
    private TextView dataAbastecimentoTextView;
    private TextView dataDespesaTextView;
    private TextView datainicioTextView;
    private TextView datafimTextView;
    private TextView datainicio2TextView;
    private TextView datafim2TextView;
    private int operacao = 0;

    public DatePickerFragment(int teste){
        operacao = teste;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        String data = (String.format("%02d", day) + "/" + String.format("%02d", month + 1) + "/" + Integer.toString(year));
        if (getActivity().getTitle().equals("Abastecimento")){
            dataAbastecimentoTextView = (TextView) getActivity().findViewById(
                    R.id.dataAbastecimentoTextView);
            dataAbastecimentoTextView.setText(data);
        }
        else if (getActivity().getTitle().equals("Despesas")){
            dataDespesaTextView = (TextView) getActivity().findViewById(
                    R.id.dataDespesaTextView);
            dataDespesaTextView.setText(data);
        }
        else if (getActivity().getTitle().equals("Opções do relatório") && operacao == 0){
            datainicioTextView = (TextView) getActivity().findViewById(
                    R.id.datainicioTextView);
            datainicioTextView.setText(data);
        }
        else if (getActivity().getTitle().equals("Opções do relatório") && operacao == 1){
            datafimTextView = (TextView) getActivity().findViewById(
                    R.id.datafimTextView);
            datafimTextView.setText(data);
        }
        else if (getActivity().getTitle().equals("Relatório de gastos") && operacao == 0){
            datainicio2TextView = (TextView) getActivity().findViewById(
                    R.id.datainicio2TextView);
            datainicio2TextView.setText(data);
        }
        else if (getActivity().getTitle().equals("Relatório de gastos") && operacao == 1){
            datafim2TextView = (TextView) getActivity().findViewById(
                    R.id.datafim2TextView);
            datafim2TextView.setText(data);
        }
    }
}