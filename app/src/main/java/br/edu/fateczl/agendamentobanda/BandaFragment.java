package br.edu.fateczl.agendamentobanda;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.edu.fateczl.agendamentobanda.controller.BandaController;
import br.edu.fateczl.agendamentobanda.model.Banda;
import br.edu.fateczl.agendamentobanda.persistence.BandaDao;

public class BandaFragment extends Fragment {

    private View view;

    private EditText etCodigoBanda, etNomeBanda;
    private Button btnBuscarBanda, btnInserirBanda, btnModificarBanda, btnExcluirBanda, btnListarBanda;
    private TextView tvListarBanda;

    BandaController bCont;

    public BandaFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_banda, container, false);

        etCodigoBanda = view.findViewById(R.id.etCodigoBanda);
        etNomeBanda = view.findViewById(R.id.etNomeBanda);
        tvListarBanda = view.findViewById(R.id.tvListarBanda);
        btnBuscarBanda = view.findViewById(R.id.btnBuscarBanda);
        btnInserirBanda = view.findViewById(R.id.btnInserirBanda);
        btnModificarBanda = view.findViewById(R.id.btnModificarBanda);
        btnExcluirBanda = view.findViewById(R.id.btnExcluirBanda);
        btnListarBanda = view.findViewById(R.id.btnListarBanda);

        bCont = new BandaController(new BandaDao(view.getContext()));

        btnInserirBanda.setOnClickListener(op -> acaoInserir());
        btnModificarBanda.setOnClickListener(op -> acaoModificar());
        btnExcluirBanda.setOnClickListener(op -> acaoExcluir());
        btnBuscarBanda.setOnClickListener(op -> acaoBuscar());
        btnListarBanda.setOnClickListener(op -> acaoListar());

        return view;
    }

    private void acaoInserir() {
        try {
            Banda banda = montaBanda();

            bCont.inserir(banda);
            Toast.makeText(view.getContext(), "Banda Inserida com Sucesso", Toast.LENGTH_SHORT).show();

            limpaCampos();
        }
         catch (Exception e){
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Generic Error", e.getMessage());
        }
    }

    private void acaoModificar() {
        try {
            Banda banda = montaBanda();

            bCont.modificar(banda);
            Toast.makeText(view.getContext(), "Banda Atualizada com Sucesso", Toast.LENGTH_SHORT).show();

            limpaCampos();
        } catch (Exception e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void acaoExcluir() {
        try {
            Banda banda = montaBanda();

            int res = bCont.deletar(banda);

            if (res > 0) {
                Toast.makeText(view.getContext(), "Banda Excluída com Sucesso.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(view.getContext(), "Não há Bandas Cadastradas para Excluir..", Toast.LENGTH_SHORT).show();
            }
            limpaCampos();


        } catch (Exception e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void acaoBuscar() {
        try {
            Banda banda = montaBanda();

            banda = bCont.buscar(banda);

            if (banda.getNome() != null) {
                preencheCampos(banda);
            } else {
                Toast.makeText(view.getContext(), "Banda Não encontrada", Toast.LENGTH_SHORT).show();
                limpaCampos();
            }
        } catch (Exception e){
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void acaoListar() {
        try {
            limpaCampos();
            List<Banda> bandas = bCont.listar();
            StringBuffer buffer = new StringBuffer();

            for (Banda b: bandas) {
                buffer.append(b.toString() + "\n");
            }
            tvListarBanda.setText(buffer.toString());
        } catch (Exception e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private Banda montaBanda() throws Exception {
        Banda b = new Banda();

            b.setCodigo(Integer.parseInt(etCodigoBanda.getText().toString()));
            b.setNome(etNomeBanda.getText().toString());

        return b;
    }

    private void limpaCampos() {
        etCodigoBanda.setText("");
        etNomeBanda.setText("");
        tvListarBanda.setText("");
    }

    private void preencheCampos(Banda b) throws Exception {
        etCodigoBanda.setText(String.valueOf(b.getCodigo()));
        etNomeBanda.setText(String.valueOf(b.getNome()));
    }
}