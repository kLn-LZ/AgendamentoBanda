package br.edu.fateczl.agendamentobanda;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.edu.fateczl.agendamentobanda.controller.LocalController;
import br.edu.fateczl.agendamentobanda.model.Local;
import br.edu.fateczl.agendamentobanda.persistence.LocalDao;

public class LocalFragment extends Fragment {

    private View view;

    private EditText etCodigoLocal, etNomeLocal, etEnderecoLocal, etDescricaoLocal;
    private Button btnBuscarLocal, btnInserirLocal, btnModificarLocal, btnExcluirLocal, btnListarLocal;
    private TextView tvListarLocal;

    private LocalController lCont;

    public LocalFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_local, container, false);

        etCodigoLocal = view.findViewById(R.id.etCodigoLocal);
        etNomeLocal = view.findViewById(R.id.etNomeLocal);
        etEnderecoLocal = view.findViewById(R.id.etEnderecoLocal);
        etDescricaoLocal = view.findViewById(R.id.etDescricaoLocal);
        tvListarLocal = view.findViewById(R.id.tvListarLocal);
        btnBuscarLocal = view.findViewById(R.id.btnBuscarLocal);
        btnInserirLocal = view.findViewById(R.id.btnInserirLocal);
        btnModificarLocal = view.findViewById(R.id.btnModificarLocal);
        btnExcluirLocal = view.findViewById(R.id.btnExcluirLocal);
        btnListarLocal = view.findViewById(R.id.btnListarLocal);

        lCont = new LocalController(new LocalDao(view.getContext()));

        btnInserirLocal.setOnClickListener(op -> acaoInserir());
        btnModificarLocal.setOnClickListener(op -> acaoModificar());
        btnExcluirLocal.setOnClickListener(op -> acaoExcluir());
        btnBuscarLocal.setOnClickListener(op -> acaoBuscar());
        btnListarLocal.setOnClickListener(op -> acaoListar());

        return view;
    }

    private void acaoInserir() {
        try {
            Local local = montaLocal();

            lCont.inserir(local);
            Toast.makeText(view.getContext(), "Local Inserido com Sucesso", Toast.LENGTH_SHORT).show();

            limpaCampos();
        }
        catch (Exception e){
            Toast.makeText(view.getContext(), "Ocorreu um erro ao inserir o local", Toast.LENGTH_SHORT).show();
        }
    }

    private void acaoModificar() {
        try {
            Local local = montaLocal();

            lCont.modificar(local);
            Toast.makeText(view.getContext(), "Local Atualizado com Sucesso", Toast.LENGTH_SHORT).show();

             limpaCampos();
        } catch (Exception e) {
            Toast.makeText(view.getContext(), "Ocorreu um erro ao modificar o local", Toast.LENGTH_SHORT).show();
        }
    }

    private void acaoExcluir() {
        try {
            Local local = montaLocal();

            int res = lCont.deletar(local);

            if (res > 0) {
                Toast.makeText(view.getContext(), "Local Excluído com Sucesso.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(view.getContext(), "Não há Locais Cadastrados para Excluir..", Toast.LENGTH_SHORT).show();
            }
            limpaCampos();

        } catch (Exception e) {
            Toast.makeText(view.getContext(), "Ocorreu um erro ao Excluir o Local", Toast.LENGTH_SHORT).show();
        }
    }

    private void acaoBuscar() {
        try {
            Local local = montaLocal();

            local = lCont.buscar(local);

            if (local.getNome() != null) {
                preencheCampos(local);
            } else {
                Toast.makeText(view.getContext(), "Local Não encontrado", Toast.LENGTH_SHORT).show();
                limpaCampos();
            }
        } catch (Exception e){
            Toast.makeText(view.getContext(), "Ocorreu um erro ao buscar o local", Toast.LENGTH_SHORT).show();
        }
    }

    private void acaoListar() {
        try {
            limpaCampos();
            List<Local> locals = lCont.listar();
            StringBuffer buffer = new StringBuffer();

            for (Local l: locals) {
                buffer.append(l.toString() + "\n");
            }

            if (buffer.toString().isEmpty()) {
                Toast.makeText(getContext(), "Não locais para mostrar.", Toast.LENGTH_SHORT).show();
            } else {
                tvListarLocal.setText(buffer.toString());
            }
        } catch (Exception e) {
            Toast.makeText(view.getContext(), "Ocorreu um erro ao listar os locais", Toast.LENGTH_SHORT).show();
        }
    }

    private Local montaLocal() throws Exception {
        Local l = new Local();

        l.setId(Integer.parseInt(etCodigoLocal.getText().toString()));
        l.setNome(etNomeLocal.getText().toString());
        l.setEndereco(etEnderecoLocal.getText().toString());
        l.setDescricao(etDescricaoLocal.getText().toString());

        return l;
    }

    private void limpaCampos() {
        etCodigoLocal.setText("");
        etNomeLocal.setText("");
        etEnderecoLocal.setText("");
        etDescricaoLocal.setText("");
        tvListarLocal.setText("");
    }

    private void preencheCampos(Local l) throws Exception {
        etCodigoLocal.setText(String.valueOf(l.getId()));
        etNomeLocal.setText(String.valueOf(l.getNome()));
        etEnderecoLocal.setText(String.valueOf(l.getEndereco()));
        etDescricaoLocal.setText(String.valueOf(l.getDescricao()));
    }
}