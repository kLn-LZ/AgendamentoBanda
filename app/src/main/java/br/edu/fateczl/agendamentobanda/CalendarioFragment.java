package br.edu.fateczl.agendamentobanda;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import br.edu.fateczl.agendamentobanda.controller.EnsaioController;
import br.edu.fateczl.agendamentobanda.model.Ensaio;
import br.edu.fateczl.agendamentobanda.persistence.EnsaioDao;
import br.edu.fateczl.agendamentobanda.util.EventDecorator;

public class CalendarioFragment extends Fragment {

    private View view;

    private MaterialCalendarView cvCalendario;

    private Button btnAgendarCalendario, btnConsultarCalendario, btnExcluirAgendamento;
    private Spinner spTipoAgendamento;

    private EnsaioController eCont;

    private long timestamp;

    private String[] ensaioArray;

    public CalendarioFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_calendario, container, false);

        eCont = new EnsaioController(new EnsaioDao(getContext()));
        cvCalendario = view.findViewById(R.id.cvCalendario);
        btnExcluirAgendamento = view.findViewById(R.id.btnExcluirEnsaio);

        cvCalendario.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                carregarEventos();
                cvCalendario.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    carregarEventos();
                }
            }
        });

        cvCalendario.setOnDateChangedListener(new OnDateSelectedListener() {
            public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
                timestamp = date.getDate().getTime();
            }
        });

        cvCalendario.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    carregarEventos();
                }
            }
        });
        btnAgendarCalendario = view.findViewById(R.id.btnAgendarCalendario);
        btnAgendarCalendario.setOnClickListener(op -> {
            LocalDate data = (new java.util.Date(timestamp)).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (!LocalDate.now().isAfter(data) || LocalDate.now().isEqual(data)) {
                openCadastroAgendamentoFragment(view, new Ensaio());
            } else {
                Toast.makeText(getContext(), "Data inválida para agendamento", Toast.LENGTH_SHORT).show();
            }
        });

        btnConsultarCalendario = view.findViewById(R.id.btnConsultarCalendario);
        btnConsultarCalendario.setOnClickListener(op -> {
            Ensaio ensaio = new Ensaio();
            ensaio.setData(timestamp);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Escolha um agendamento");

            ensaioArray = consultarAgendamento(ensaio).toArray(new String[0]);
            if (!(ensaioArray.length < 1)) {
                builder.setItems(ensaioArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            ensaio.setId(Integer.valueOf(ensaioArray[i].substring(0, ensaioArray[i].indexOf("-") - 1)));
                            Ensaio ensaioConsulta = eCont.buscar(ensaio);
                            openCadastroAgendamentoFragment(view, ensaioConsulta);
                        } catch (SQLException e) {
                            Toast.makeText(getContext(), "Erro ao consultar agendamento", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                Toast.makeText(getContext(), "Não há nada agendado para esse dia", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private List<String> consultarAgendamento(Ensaio ensaio) {
        try {
            SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");

            List<String> ensaiosAgendados = new ArrayList<>();
            List<Ensaio> ensaios = eCont.listar();

            Date data = new java.util.Date(timestamp);

            for(Ensaio ensaiar : ensaios) {
                if (data.equals(new java.util.Date(ensaiar.getData()))) {
                    ensaiosAgendados.add(String.valueOf(ensaiar.getId()) + " - " + ensaiar.getNome() + " - " + dt.format(new java.util.Date(ensaiar.getData())) + " - " + String.valueOf(ensaiar.getHora()));
                }
            }
            return ensaiosAgendados;
        } catch (SQLException e) {
            Toast.makeText(getContext(), "Erro ao consultar agendamentos", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }
    }

    private void openCadastroAgendamentoFragment(View view, Ensaio ensaio){
        try {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            CadastroAgendamentoFragment caf = new CadastroAgendamentoFragment();

            Bundle bundle = new Bundle();
            if (ensaio.getId() > 0) {
                bundle.putString("id", String.valueOf(ensaio.getId()));
                bundle.putString("nome", ensaio.getNome());
                bundle.putString("hora", String.valueOf(ensaio.getHora()));
                bundle.putString("banda_id", String.valueOf(ensaio.getBanda().getCodigo()));
                bundle.putString("local_id", String.valueOf(ensaio.getLocal().getId()));
                bundle.putString("cor", String.valueOf(ensaio.getCor()));
            }
            bundle.putString("data", String.valueOf(timestamp));
            caf.setArguments(bundle);

            caf.setOnDialogInteractionListener(() -> {
                carregarEventos();
            });

            caf.show(fragmentTransaction, "dialog");


        } catch (Exception e) {
            Toast.makeText(getContext(), "Ocorreu um erro ao abrir o menu de agendamento", Toast.LENGTH_SHORT).show();
        }
    }


    private void turnOffDialogFragment() {
        try {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            CadastroAgendamentoFragment caf = (CadastroAgendamentoFragment) getParentFragmentManager().findFragmentByTag("dialog");
            if(caf != null) {
                caf.dismiss();
                fragmentTransaction.remove(caf);
                carregarEventos();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Ocorreu um erro ao fechar o menu.", Toast.LENGTH_SHORT).show();
        }
    }

    private void carregarEventos() {
        try {
            List<Ensaio> agendamentos  = eCont.listar();

            for (Ensaio agendamento : agendamentos) {
                CalendarDay dia = CalendarDay.from(new java.util.Date(agendamento.getData()));
                cvCalendario.addDecorator(new EventDecorator((Color.parseColor(agendamento.getCor())), Collections.singleton(dia)));
            }

        } catch (SQLException e) {
           Toast.makeText(getContext(), "Ocorreu um erro ao buscar agendamentos cadastrados.", Toast.LENGTH_SHORT).show();
        }
    }

    private void preencheSpinner(){
        try {
            List<String> tipoAgendamento = new ArrayList<>();

            tipoAgendamento.add(0, "Escolha um agendamento");
            tipoAgendamento.add(1, "Ensaio");
            tipoAgendamento.add(2, "Show");

            ArrayAdapter ad = new ArrayAdapter(view.getContext(),
                        android.R.layout.simple_spinner_item,
                        tipoAgendamento);
            ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spTipoAgendamento.setAdapter(ad);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Ocorreu um erro ao carregar os tipos de agendamento.", Toast.LENGTH_SHORT).show();
        }
    }
}

