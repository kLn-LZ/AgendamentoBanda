package br.edu.fateczl.agendamentobanda;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.edu.fateczl.agendamentobanda.controller.BandaController;
import br.edu.fateczl.agendamentobanda.controller.EnsaioController;
import br.edu.fateczl.agendamentobanda.controller.LocalController;
import br.edu.fateczl.agendamentobanda.model.Banda;
import br.edu.fateczl.agendamentobanda.model.Cor;
import br.edu.fateczl.agendamentobanda.model.Ensaio;
import br.edu.fateczl.agendamentobanda.model.ListaCor;
import br.edu.fateczl.agendamentobanda.model.Local;
import br.edu.fateczl.agendamentobanda.persistence.BandaDao;
import br.edu.fateczl.agendamentobanda.persistence.EnsaioDao;
import br.edu.fateczl.agendamentobanda.persistence.LocalDao;
import br.edu.fateczl.agendamentobanda.view.ColorSpinnerAdapter;

public class CadastroAgendamentoFragment extends DialogFragment {

    private View view;


    private TextView tvColor,tvColorHex, colorNameBG, colorBlob, tvDataEnsaio;

    private EditText etCodigoAgendamento, etNomeAgendamento, etHoraAgendamento;

    private Button btnConfirmarAgendamento, btnExcluirAgendamento;
    private Spinner spBandaAgendamento, spLocalAgendamento, spColor;

    private BandaController bCont;
    private List<Banda> bandas;
    private Banda bandaSelecionada;

    private LocalController lCont;
    private List<Local> locals;
    private Local localSelecionado;

    private EnsaioController eCont;

    private long data;

    private int hora, minuto;

    private Cor corSelecionada;

    private String ensaio_id, ensaio_nome, ensaio_hora, ensaio_banda_id, ensaio_local_id, ensaio_cor;

    private OnDialogInteractionListener  listener;

    public interface OnDialogInteractionListener {
        void onDialogClosed();
    }

    public CadastroAgendamentoFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.cadastro_agendamento_ensaio, container);

        Bundle args = getArguments();
        if (args != null) {
            data = Long.parseLong(args.getString("data"));
            ensaio_id = args.getString("id");
            ensaio_nome = args.getString("nome");
            ensaio_hora = args.getString("hora");
            ensaio_banda_id = args.getString("banda_id");
            ensaio_local_id = args.getString("local_id");
            ensaio_cor = args.getString("cor");
        }

        etCodigoAgendamento = view.findViewById(R.id.etCodigoEnsaio);
        etNomeAgendamento = view.findViewById(R.id.etNomeEnsaio);
        etHoraAgendamento = view.findViewById(R.id.etHoraEnsaio);
        tvDataEnsaio = view.findViewById(R.id.tvDataEnsaio);

        SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
        tvDataEnsaio.setText(dt.format(new java.util.Date(data)));
        bCont = new BandaController(new BandaDao(view.getContext()));
        lCont = new LocalController(new LocalDao(view.getContext()));
        eCont = new EnsaioController(new EnsaioDao(view.getContext()));


        etHoraAgendamento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    popTimePicker(view);
                }
            }
        });

        etHoraAgendamento.setOnClickListener(op -> {
            popTimePicker(view);
        });

        spBandaAgendamento = view.findViewById(R.id.spBandaEnsaio);
        spBandaAgendamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i <= 0) {
                    Toast.makeText(view.getContext(),"Selecione uma banda", Toast.LENGTH_SHORT).show();
                } else {
                    bandaSelecionada = bandas.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        preencheSpinnerBanda();

        spLocalAgendamento = view.findViewById(R.id.spLocalEnsaio);
        spLocalAgendamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i <= 0) {
                    Toast.makeText(view.getContext(),"Selecione um local de agendamento", Toast.LENGTH_SHORT).show();
                } else {
                    localSelecionado = locals.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        preencheSpinnerLocal();

        spColor = view.findViewById(R.id.spColorEnsaio);
        preencheSpinnerCor();

        btnConfirmarAgendamento = view.findViewById(R.id.btnConfirmarEnsaio);
        btnConfirmarAgendamento.setOnClickListener(op -> {
            try {
                Ensaio ensaio = new Ensaio();
                ensaio.setId(Integer.parseInt(etCodigoAgendamento.getText().toString()));
                ensaio.setData(data);
                ensaio = eCont.buscar(ensaio);
                if (!(ensaio.getNome() != null)) {
                    acaoInserir();
                } else {
                    acaoModificar();
                }
                Toast.makeText(view.getContext(),"Agendamento Concluído", Toast.LENGTH_SHORT).show();
                dismiss();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Erro ao salvar, verifique as informações inseridas", Toast.LENGTH_SHORT).show();
            }
        });

        btnExcluirAgendamento = view.findViewById(R.id.btnExcluirEnsaio);
        btnExcluirAgendamento.setOnClickListener(op -> {
            try {
                if(!etCodigoAgendamento.getText().toString().isEmpty()) {
                    acaoExcluir();
                    Toast.makeText(view.getContext(),"Agendamento Excluído", Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Não foi possível excluir o agendamento", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Toast.makeText(getContext(), "Erro ao excluir, verifique as informações inseridas", Toast.LENGTH_SHORT).show();
            }
        });


        if (!(ensaio_id == null)) {
            preencherCampos();
        }

        return view;
    }

    public void setOnDialogInteractionListener(OnDialogInteractionListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        // Notify the listener
        if (listener != null) {
            listener.onDialogClosed();
        }
    }

    private void preencherCampos() {
        ListaCor listaCor = new ListaCor();

        for(Cor cor: listaCor.basicColors()) {
            if (cor.getColorHash().contains(ensaio_cor)) {
                spColor.setSelection(listaCor.colorPosition(cor), false);
            }
        }

        int i = 0;
        for (Local local: locals) {
            if (local.getId() == Integer.parseInt(ensaio_local_id)) {
                spLocalAgendamento.setSelection(i);
            }
            i++;
        }

        i = 0;
        for (Banda banda: bandas) {
            if (banda.getCodigo() == Integer.parseInt(ensaio_banda_id)) {
                spBandaAgendamento.setSelection(i);
            }
            i++;
        }

        etCodigoAgendamento.setText(ensaio_id);
        etNomeAgendamento.setText(ensaio_nome);
        etHoraAgendamento.setText(ensaio_hora);

    }

    private void acaoInserir() throws Exception {
            Ensaio ensaio = montaAgendamento();

            eCont.inserir(ensaio);
            Toast.makeText(view.getContext(), "Agendamento Concluído", Toast.LENGTH_SHORT).show();
    }

    private void acaoExcluir() throws Exception {
        try {
            Ensaio ensaio = montaAgendamento();

            int res = eCont.deletar(ensaio);

            if (res > 0) {
                Toast.makeText(view.getContext(), "Agendamento Excluído com Sucesso.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(view.getContext(), "Não há Agendamentos Cadastrados para Excluir..", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void acaoModificar() throws Exception {
        try {
            Ensaio ensaio = montaAgendamento();

            eCont.modificar(ensaio);
            Toast.makeText(view.getContext(), "Agendamento Atualizado com Sucesso", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int horaSelecionada, int minutoSelecionado) {
                hora = horaSelecionada;
                minuto = minutoSelecionado;
                etHoraAgendamento.setText(String.format(Locale.getDefault(), "%02d:%02d:00", hora, minuto));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(), onTimeSetListener, hora, minuto, true);
        timePickerDialog.setTitle("Selecione o Horário");
        timePickerDialog.show();
    }

    private void preencheSpinnerCor() {
        ListaCor listaCor = new ListaCor();

        corSelecionada = listaCor.getDefaultColor();

        spColor.setAdapter(new ColorSpinnerAdapter(requireContext(), listaCor.basicColors()));
        spColor.setSelection(listaCor.colorPosition(corSelecionada), false);

        spColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                corSelecionada = listaCor.basicColors().get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void preencheSpinnerLocal() {
        Local l0 = new Local();
        l0.setId(0);
        l0.setNome("Selecione um local");

        try {
            locals = lCont.listar();
            locals.add(0, l0);

            List<String> nomesLocal = new ArrayList<>();
            for(Local local : locals){
                nomesLocal.add(local.getNome());
            }

            ArrayAdapter ad = new ArrayAdapter(view.getContext(),
                    android.R.layout.simple_spinner_item,
                    nomesLocal);
            ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spLocalAgendamento.setAdapter(ad);

        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void preencheSpinnerBanda() {
        Banda b0 = new Banda();
        b0.setCodigo(0);
        b0.setNome("Selecione uma banda");

        try {
            bandas = bCont.listar();
            bandas.add(0, b0);

            List<String> nomesBanda = new ArrayList<>();
            for(Banda banda : bandas){
                nomesBanda.add(banda.getNome());
            }

            ArrayAdapter ad = new ArrayAdapter(view.getContext(),
                    android.R.layout.simple_spinner_item,
                    nomesBanda);
            ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spBandaAgendamento.setAdapter(ad);

        } catch (SQLiteException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private Ensaio montaAgendamento() throws Exception {
        Ensaio agendamentoEnsaio = new Ensaio();

        agendamentoEnsaio.setId(Integer.parseInt(etCodigoAgendamento.getText().toString()));
        agendamentoEnsaio.setNome(etNomeAgendamento.getText().toString());
        agendamentoEnsaio.setCor(corSelecionada.getColorHash());
        agendamentoEnsaio.setData(data);

        Time t = Time.valueOf(etHoraAgendamento.getText().toString());
        agendamentoEnsaio.setHora(t);
        agendamentoEnsaio.setBanda(bandaSelecionada);
        agendamentoEnsaio.setLocal(localSelecionado);

        return agendamentoEnsaio;
    }
}
